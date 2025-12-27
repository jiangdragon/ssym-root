package com.ssym.avivacofco.rate;

import com.ssym.avivacofco.util.ExcelUtil;
import com.ssym.avivacofco.util.FileUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author WB20200724005
 * @Description 抽象入口
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/9/22
 */
public abstract class AbstractService {
    private static final Logger logger = LoggerFactory.getLogger(AbstractService.class);
    /**
     * SQL模板
     */
    protected String sqlText = "";
    /**
     * 源excel文件路径
     */
    protected String sourceExcelPath = "";
    /**
     * SQL文件生成路径
     */
    protected String targetSqlPath = "";

    protected int headerRowIndex = 0;
    protected int headerRowCount = 1;

    protected int contentRowIndex = 0;
    protected int spaceRowCount = 0;

    protected String extProps = "";
    /**
     * 产品编码
     */
    protected String riskCode = "";
    /**
     * 责任编码
     */
    protected String dutyCode = "";


    protected Map<String, String> props = new HashMap<>();

    /**
     * 初始化表格及SQL设置
     */
    protected abstract void init();

    /**
     * 入口
     *
     * @param sourceExcelPath
     * @param targetSqlPath
     * @throws Exception
     */
    public void createSqlFile(String sourceExcelPath, String targetSqlPath) throws Exception {
        this.createSqlFile(sourceExcelPath, targetSqlPath, 0);
    }

    /**
     * 入口
     *
     * @param sourceExcelPath
     * @param targetSqlPath
     * @param sheetIndex
     * @throws Exception
     */
    public void createSqlFile(String sourceExcelPath, String targetSqlPath, int sheetIndex) throws Exception {
        this.sourceExcelPath = sourceExcelPath;
        this.targetSqlPath = targetSqlPath;
        // 初始化参数
        this.init();
        // 获取工作薄
        Sheet sheet = this.getWorkBookSheet(sheetIndex);
        logger.info("读取Excel数据完成:{}", sourceExcelPath);
        // 获取费率数据
        List<Map<String, String>> rateData = this.buildRateData(sheet);
        logger.info("构建费率数据完成:{}", sourceExcelPath);
        rateData = this.convertRateData(rateData);
        logger.info("转换费率数据完成:{}", sourceExcelPath);
        // 统计
        double count = rateData.stream().count();
        double ageSumTwo = rateData.stream().map(t -> t.get("RATE"))
                .collect(Collectors.toList()).stream()
                .mapToDouble(Double::parseDouble).sum();
        // 生成SQL文件
        this.createSqlFile(rateData, targetSqlPath);
        logger.info("保费数量:{};保费总额:{}", count, String.format("%.5f", ageSumTwo));
        logger.info("生成SQL文件完成:{}", sourceExcelPath);
    }

    /**
     * 获取指定sheet
     *
     * @param sheetIndex
     * @return
     * @throws Exception
     */
    protected Sheet getWorkBookSheet(int sheetIndex) throws Exception {
        return ExcelUtil.getWorkBookSheet(this.sourceExcelPath, sheetIndex);
    }

    /**
     * 通过sheet构建单元格元数据
     *
     * @param sheet
     * @return
     * @throws Exception
     */
    protected abstract List<Map<String, String>> buildRateData(Sheet sheet) throws Exception;

    /**
     * 单元格元数据转换为单元格费率对象map
     *
     * @param rateData
     * @return
     * @throws Exception
     */
    protected abstract List<Map<String, String>> convertRateData(List<Map<String, String>> rateData);

    /**
     * 把数据写入文件
     *
     * @param rateData
     * @param sqlFilePath
     * @throws Exception
     */
    protected void createSqlFile(List<Map<String, String>> rateData, String sqlFilePath) throws Exception {
        File file = FileUtil.createFile(sqlFilePath);
        FileWriter fileWriter = new FileWriter(file.getAbsoluteFile(), true);
        BufferedWriter bw = new BufferedWriter(fileWriter);
        rateData.stream().forEach(map -> {
            try {
                bw.write(this.createSql(map));
                bw.newLine();
            } catch (Exception e) {
                System.out.println("Exception:" + e.getMessage());
            }
        });
        bw.close();
    }

    /**
     * SQL变量替换
     *
     * @param requestMap
     * @return
     * @throws Exception
     */
    protected String createSql(Map<String, String> requestMap) throws Exception {
        if (!this.sqlText.contains("$")) {
            return this.sqlText;
        }

        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(this.sqlText);
        String resultText = "";
        while (matcher.find()) {
            String keyVal = matcher.group(1);
            String value = requestMap.get(keyVal);
            resultText = matcher.replaceFirst(value);
            matcher = pattern.matcher(resultText);
        }
//        System.out.println("SQL resultText:" + resultText);
        return resultText;
    }

    public String getSqlText() {
        return sqlText;
    }

    public void setSqlText(String sqlText) {
        this.sqlText = sqlText;
    }
}
