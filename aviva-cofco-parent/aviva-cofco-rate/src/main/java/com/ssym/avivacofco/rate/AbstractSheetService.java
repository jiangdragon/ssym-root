package com.ssym.avivacofco.rate;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ssym.avivacofco.rate.dto.RateInfo;
import com.ssym.avivacofco.util.ExcelUtil;
import com.ssym.avivacofco.util.FileUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author 三傻云梦
 * @Description 费率计算抽象服务, Sheet与保单字段对应关系
 * <br/> RISKCODE:险种编码
 * <br/> DUTYCODE:责任编码
 * <br/> MINAGE:最小年龄
 * <br/> MAXAGE:最大年龄
 * <br/> GENDER:性别[男0 女1]
 * <br/> PAYUNIT:缴费期间单位[年Y 周岁A 趸交Y]
 * <br/> PAYPERIOD:缴费期间[趸交1]
 * <br/> PERIODFLAG:周期标识[默认L]
 * <br/> PERIOD:周期[默认0]
 * <br/> POLICYYEAR:保单年度
 * <br/> RATE:费率[注意一般sql需要在此基础上/1000]
 * <br/> AMNT:保额
 * <br/> SECURITYFLAG:社保标识[无社保0 有社保1]
 * <br/> PLANCODE:计划编码
 * <br/> RETIREAGE:约定年龄
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2026/1/4
 */
public abstract class AbstractSheetService implements RateCalculationService {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSheetService.class);
    /**
     * 源excel文件路径
     */
    protected String sourcePath = "";
    /**
     * SQL文件生成路径
     */
    protected String targetPath = "";
    /**
     * 费率所在单元格设置[以0开始]
     */
    protected List<String> props = new ArrayList<>(1);
    /**
     * 险种编码
     */
    protected String riskCode = "";
    /**
     * 责任编码
     */
    protected String dutyCode = "";
    /**
     * 社保标识[无社保0 有社保1]
     */
    protected String securityFlag = "";
    /**
     * 标准体等级["0.5", "0.75", "1", "1.25", "1.5"]
     */
    protected String addFeeRate = "";
    /**
     * 数据表头开始行号[以0开始]
     */
    protected int headerStartRowIndex = 0;
    /**
     * 费率表头行数
     */
    protected int headerRowCount = 1;

    /**
     * 数据表头与费率数据之前的空行数
     */
    protected int spaceRowCount = 1;

    /**
     * SQL的插入部分
     */
    protected String plSQLInsert = "";
    /**
     * SQL的值部分
     */
    protected String plSQLVal = "";
    /**
     * 每条SQL中Val的行数
     */
    protected int plSQLValCount = 1;

    /**
     * 生成SQL文件,默认读取文件第1个Sheet
     *
     * @param sourcePath
     * @param targetPath
     * @param sheetIndex sheet序号,以0开始
     */
    @Override
    public void createSqlFile(String sourcePath, String targetPath, int sheetIndex) throws Exception {
        this.createSqlFile(sourcePath, targetPath, sheetIndex, null);
    }

    /**
     * 读取文件指定Sheet生成SQL文件
     *
     * @param sourcePath 费率文件源路径
     * @param targetPath SQL目标路径
     * @param sheetIndex sheet序号,以0开始
     * @param rateInfo   费率bean
     * @throws Exception
     */
    @Override
    public void createSqlFile(String sourcePath, String targetPath, int sheetIndex, RateInfo rateInfo) throws Exception {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        // 1.初始化参数
        if (rateInfo != null) {
            BeanUtil.copyProperties(rateInfo, this);
        }
        this.init();
        logger.info("初始化Excel准备参数完成");
        // 2.获取工作薄
        Sheet sheet = this.getWorkBookSheet(sheetIndex);
        logger.info("读取Excel数据完成:{}", targetPath);
        // 3.获取费率数据
        List<Map<String, String>> rateData = this.buildRateData(sheet);
        logger.info("构建费率数据完成:{}", sourcePath);
        // 4.转换费率数据(格式化数据)
        rateData = this.convertRateData(rateData);
        logger.info("转换费率数据完成:{}", sourcePath);
        // 5.统计费率
        double count = rateData.stream().count();
        double ageSumTwo = rateData.stream().map(t -> t.get("RATE")).collect(Collectors.toList()).stream().mapToDouble(Double::parseDouble).sum();
        logger.info("保费数量:{};保费总额:{}", count, String.format("%.3f", ageSumTwo));
        // 6.生成SQL文件
        this.writeToSqlFile(rateData);
        logger.info("生成SQL文件完成:{}", sourcePath);
    }

    /**
     * 初始化表格及SQL设置
     * <br/>设置费率属性[行列都以0开始,格式:行_列_属性 N代表变化]
     * <br/>设置表格表头属性[表头开始行数 表头行数量 表头空行数量]
     * <br/>设置SQL语句
     */
    protected abstract void init();

    /**
     * 获取指定sheet
     *
     * @param sheetIndex
     * @return
     * @throws Exception
     */
    protected Sheet getWorkBookSheet(int sheetIndex) throws Exception {
        return ExcelUtil.getWorkBookSheet(this.sourcePath, sheetIndex);
    }

    /**
     * 通过sheet构建单元格元数据
     *
     * @param sheet
     * @return
     * @throws Exception
     */
    protected List<Map<String, String>> buildRateData(Sheet sheet) throws Exception {
        List<Map<String, String>> rowList = new ArrayList<>();
        // 1.属性记录到headerDataMap方便费率时使用
        Map<String, String> headerDataMap = new HashMap<>();
        for (int i = 0; i < this.headerRowCount; i++) {
            int rowIndex = this.headerStartRowIndex + i;
            Row rowData = sheet.getRow(rowIndex);
            if (ObjectUtil.isEmpty(rowData)) {
                continue;
            }
            int cellCount = rowData.getPhysicalNumberOfCells();
            // 读取费率在行中的属性暂存到费率headerDataMap中
            for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
                Cell cell = rowData.getCell(cellIndex);
                // 处理合并单元格
                if (ExcelUtil.isMergedRegion(sheet, cell)) {
                    cell = ExcelUtil.getMergedRegionCell(sheet, cell);
                }
                String cellVal = ExcelUtil.getCellVal(cell);

                if (StrUtil.isEmpty(cellVal)) {
                    continue;
                }
                String prefix = rowIndex + "_N_";
                List<String> cellNames = this.props.stream()
                        .filter(s -> s.startsWith(prefix)).collect(Collectors.toList());
                if (ObjectUtil.isEmpty(cellNames)) {
                    continue;
                }
                // 费率相关属性写入headerDataMap
                String cellName = cellNames.get(0).replaceAll("N", String.valueOf(cellIndex));
                cellVal = cellVal.replaceAll("0+?$", "").replaceAll("[\\.]$", "");
                headerDataMap.put(cellName, cellVal);
            }
        }
        // 2.内容数据
        int rowCount = sheet.getPhysicalNumberOfRows() + this.spaceRowCount;
        int contentRowIndex = this.headerStartRowIndex + this.headerRowCount;
        for (int rowIndex = contentRowIndex; rowIndex < rowCount; rowIndex++) {
            Row rowData = sheet.getRow(rowIndex);
            if (ObjectUtil.isEmpty(rowData)) {
                continue;
            }
            int cellCount = rowData.getPhysicalNumberOfCells();
            for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
                String cellVal = ExcelUtil.getCellVal(rowData.getCell(cellIndex));
                if (StrUtil.isEmpty(cellVal)) {
                    continue;
                }

                String prefix = "N_" + cellIndex + "_";
                List<String> cellNames = this.props.stream()
                        .filter(s -> s.startsWith(prefix)).collect(Collectors.toList());
                if (ObjectUtil.isEmpty(cellNames)) {
                    // 添加费率属性
                    Map<String, String> contentDataMap = new HashMap<>();
                    String contentDataMapKey = rowIndex + "_" + cellIndex + "_";
                    contentDataMap.put(contentDataMapKey + "费率", cellVal);
                    // 添加其它属性
                    for (String key : this.props) {
                        String rePrefix = key.startsWith("N_") ? String.valueOf(rowIndex) : String.valueOf(cellIndex);
                        contentDataMap.put(contentDataMapKey + key.split("_", 3)[2], headerDataMap.get(key.replaceAll("N", rePrefix)));
                    }
                    rowList.add(contentDataMap);
                } else {
                    // 写属性
                    String cellName = cellNames.get(0).replaceAll("N", String.valueOf(rowIndex));
                    cellVal = cellVal.replaceAll("0+?$", "").replaceAll("[\\.]$", "");
                    headerDataMap.put(cellName, cellVal);
                }
            }
        }

        rowList.stream().forEach(cellMap -> logger.debug(JSONUtil.toJsonStr(cellMap)));
        return rowList;
    }

    /**
     * 单元格元数据转换为单元格费率对象map
     *
     * @param rateData
     * @return
     * @throws Exception
     */
    protected List<Map<String, String>> convertRateData(List<Map<String, String>> rateData) {
        List<Map<String, String>> newRateData = rateData.stream().map(map -> {
            HashMap<String, String> newMap = new HashMap<>();
            map.forEach((key, val) -> {
                if (key.contains("_费率")) {
                    newMap.put("RATE", String.format("%.2f", Double.parseDouble(val)));
                } else if (key.contains("_缴费期间")) {
                    String payTempNum = val.replaceAll("\\D", "");
                    if (val.contains("趸缴")) {
                        newMap.put("PAYPERIOD", "1");
                        newMap.put("PAYUNIT", "Y");
                    } else if (val.contains("周岁")) {
                        newMap.put("PAYPERIOD", payTempNum);
                        newMap.put("PAYUNIT", "A");
                    } else {
                        newMap.put("PAYPERIOD", payTempNum);
                        newMap.put("PAYUNIT", "Y");
                    }
                } else if (key.contains("_性别")) {
                    newMap.put("GENDER", val.equals("男") ? "0" : "1");
                } else if (key.contains("_社保状态")) {
                    newMap.put("SECURITYFLAG", (val.contains("有基本医疗保险") ? "1" : "0"));
                } else if (key.contains("_年龄")) {
                    newMap.put("MINAGE", val);
                    newMap.put("MAXAGE", val);
                } else if (key.contains("_保单年度")) {
                    newMap.put("POLICYYEAR", val);
                } else if (key.contains("_约定年龄")) {
                    newMap.put("RETIREAGE", val.replaceAll("周岁", ""));
                }
            });
            logger.debug(JSONUtil.toJsonStr(newMap));
            return newMap;
        }).collect(Collectors.toList());

        return newRateData;
    }

    /**
     * 把数据写入文件
     *
     * @param rateData
     * @throws Exception
     */
    protected void writeToSqlFile(List<Map<String, String>> rateData) throws Exception {
        File file = FileUtil.createFile(this.targetPath);
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        try {
            for (int i = 0, len = rateData.size(); i < len; i++) {
                int remainder = i % this.plSQLValCount;
                // Insert into
                if (remainder == 0) {
                    bw.write(this.plSQLInsert);
                }
                // VALUES
                Map<String, String> rateMap = rateData.get(i);
                bw.write(this.createSql(rateMap, this.plSQLVal));
                bw.write((((remainder + 1) == this.plSQLValCount) || (i + 1 == len)) ? ";" : ",");
                bw.newLine();
            }
        } catch (Exception e) {
            logger.error("Exception:" + e.getMessage());
        }
        bw.close();
    }

    /**
     * 模板中值替换
     *
     * @param requestMap 数据源
     * @param plSQLVal   plSQL模板
     * @return 字符串
     * @throws Exception
     */
    private String createSql(Map<String, String> requestMap, String plSQLVal) throws Exception {
        if (!plSQLVal.contains("$")) {
            return plSQLVal;
        }

        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(plSQLVal);
        String resultText = "";
        while (matcher.find()) {
            String keyVal = matcher.group(1);
            String value = requestMap.get(keyVal);
            resultText = matcher.replaceFirst(value);
            matcher = pattern.matcher(resultText);
        }
        return resultText;
    }

    /**
     * 社保标识get
     * @return
     */
    public String getSecurityFlag() {
        return securityFlag;
    }

    /**
     * 社保标识set
     * @param securityFlag
     */
    public void setSecurityFlag(String securityFlag) {
        this.securityFlag = securityFlag;
    }

    /**
     * 标准体等级get
     * @return
     */
    public String getAddFeeRate() {
        return addFeeRate;
    }

    /**
     * 标准体等级set
     * @param addFeeRate
     */
    public void setAddFeeRate(String addFeeRate) {
        this.addFeeRate = addFeeRate;
    }
}
