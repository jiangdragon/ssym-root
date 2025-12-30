package com.ssym.avivacofco.rate.impl;

import cn.hutool.json.JSONUtil;
import com.ssym.avivacofco.rate.RowSheetRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WB20200724005
 * @Description 减额缴清保费表-两个表格
 * 表头:保险期间 费期间	性别	约定年龄 保单年度/年龄
 * sheet:有基本医疗保险 无基本医疗保险
 * <br/> RISKCODE:产品编码 DUTYCODE:责任编码 MINAGE:最小年龄 MAXAGE:最大年龄
 * <br/> GENDER:性别 PAYUNIT:缴费期间单位 PAYPERIOD:缴费期间 PERIODFLAG:周期标识L
 * <br/> PERIOD:周期0 POLICYYEAR:保单年度 RATE:费率 SECURITYFLAG:有/无社保
 * <br/> PLANCODE:计划编码 RETIREAGE:约定年龄
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/12/17
 */
public class ReducedPrem extends RowSheetRateService {
    private static final Logger logger = LoggerFactory.getLogger(ReducedPrem.class);
    /**
     * 社保状态[有社保:1 无社保:0]
     */
    private String securityFlag = "0";

    public ReducedPrem(String riskCode, String dutyCode, String securityFlag) {
        this.riskCode = riskCode;
        this.dutyCode = dutyCode;
        this.securityFlag = securityFlag;
        this.insertSqlLine = 20000;
    }

    /**
     * 初始化表格及SQL设置
     */
    @Override
    protected void init() {
        // 设置SQL语句
//        this.sqlText = "INSERT INTO RATE_DERATING_PREM (RISKCODE, DUTYCODE, MINAGE, MAXAGE, GENDER, PAYUNIT, PAYPERIOD, PERIODFLAG,"
//                + "PERIOD, POLICYYEAR, RATE, SECURITYFLAG, PLANCODE, RETIREAGE) VALUES "
//                + "('" + this.riskCode + "', '" + this.dutyCode + "', ${MINAGE}, ${MAXAGE}, '${GENDER}', '${PAYUNIT}', ${PAYPERIOD},"
//                + " 'L', 0, ${POLICYYEAR},  ${RATE},'" + this.securityFlag + "', null, ${RETIREAGE});";
        this.sqlInsert = "INSERT INTO RATE_DERATING_PREM (RISKCODE, DUTYCODE, MINAGE, MAXAGE, GENDER, PAYUNIT, PAYPERIOD, PERIODFLAG,"
                + "PERIOD, POLICYYEAR, RATE, SECURITYFLAG, PLANCODE, RETIREAGE) VALUES ";
        this.sqlValues = "('" + this.riskCode + "', '" + this.dutyCode + "', ${MINAGE}, ${MAXAGE}, '${GENDER}', '${PAYUNIT}', ${PAYPERIOD},"
                + " 'L', 0, ${POLICYYEAR},  ${RATE},'" + this.securityFlag + "', null, ${RETIREAGE})";

        // 表格设置
        this.headerRowIndex = 5;// 数据表头行号[以0开始]
        this.headerRowCount = 1;// 数据表头行数量
        this.contentRowIndex = 6;// 费率数据开始行号[以0开始]
        this.spaceRowCount = 1; // 数据表头与费率数据的空行数
        this.headerIndexs = Arrays.asList("1", "2", "3", "4");// header列序号
        // 设置费率属性对应列号[以0开始]
        this.props.put("5_1", "缴费期间");
        this.props.put("5_2", "性别");
        this.props.put("5_3", "约定年龄");
        this.props.put("5_4", "年龄");
        this.props.put("5_5", "保单年度");
        this.extProps = "保单年度"; // 费率额外对应属性
    }

    @Override
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
}
