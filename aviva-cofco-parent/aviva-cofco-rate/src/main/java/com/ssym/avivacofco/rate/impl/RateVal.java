package com.ssym.avivacofco.rate.impl;

import cn.hutool.json.JSONUtil;
import com.ssym.avivacofco.rate.ColSheetRateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WB20200724005
 * @Description 费率表-基本责任
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/9/17
 */
public class RateVal extends ColSheetRateService {
    public RateVal() {
        this.sqlText = "INSERT INTO NEWLIS.RATE_AGE_GENDER_PAYPERIOD (RISKCODE, DUTYCODE, MINAGE, MAXAGE, GENDER, PAYUNIT, PAYPERIOD, PERIODFLAG, PERIOD, RATE, AMNT, SECURITYFLAG, PLANCODE, RETIREAGE) VALUES ('211510', '151001', ${MINAGE}, ${MAXAGE}, '${GENDER}', '${PAYUNIT}', ${PAYPERIOD}, 'L', 0, ${RATE}, null, '${SECURITYFLAG}', null, null);";
        this.headerRowIndex = 6;// 下标以0开始
        this.headerRowCount = 5;
        this.contentRowIndex = 11;// 下标以0开始
        this.spaceRowCount = 2;// getPhysicalNumberOfRows空行不计入总行数

        this.props.put("6_0", "约定年龄");
        this.props.put("7_0", "保险期间");
        this.props.put("8_0", "社保状态");
        this.props.put("9_0", "缴费期间");
        this.props.put("10_0", "性别");
        this.props.put("11_0", "年龄");
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
                    String age = val.replaceAll("0+?$", "");
                    age = age.replaceAll("[\\.]$", "");
                    newMap.put("MINAGE", age);
                    newMap.put("MAXAGE", age);
                } else if (key.contains("_约定年龄")) {
                    newMap.put("RETIREAGE", val.replaceAll("周岁", ""));
                }
            });
            return newMap;
        }).collect(Collectors.toList());
        System.out.println(JSONUtil.toJsonStr(newRateData));
        return newRateData;
    }
}
