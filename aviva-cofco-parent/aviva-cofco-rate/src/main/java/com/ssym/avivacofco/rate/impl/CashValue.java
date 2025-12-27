package com.ssym.avivacofco.rate.impl;

import cn.hutool.json.JSONUtil;
import com.ssym.avivacofco.rate.RowSheetRateService;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WB20200724005
 * @Description 现金价值表
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/9/17
 */
public class CashValue extends RowSheetRateService {
    public CashValue() {
        this.sqlText = "INSERT INTO NEWLIS.RATE_PRESENTPRICE (RISKCODE, DUTYCODE, MINAGE, MAXAGE, GENDER, PAYUNIT, PAYPERIOD, PERIODFLAG, PERIOD, POLICYYEAR, RATE, AMNT, SECURITYFLAG, PLANCODE, RETIREAGE) VALUES ('211510', '151001', ${MINAGE}, ${MAXAGE}, '${GENDER}', '${PAYUNIT}', ${PAYPERIOD}, 'L', 0, ${POLICYYEAR},  ${RATE}, null, '${SECURITYFLAG}', null, null);";
        this.headerRowIndex = 5;
        this.headerRowCount = 1;
        this.contentRowIndex = 6;
        this.spaceRowCount = 1;
        this.headerIndexs = Arrays.asList("1", "2", "3", "4");// header列序号

        this.props.put("5_1", "社保状态");
        this.props.put("5_2", "缴费期间");
        this.props.put("5_3", "性别");
        this.props.put("5_4", "约定年龄");
        this.props.put("5_5", "年龄");
        this.props.put("5_6", "保单年度");
        this.extProps = "保单年度";
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
            System.out.println(JSONUtil.toJsonStr(newMap));
            return newMap;
        }).collect(Collectors.toList());

        return newRateData;
    }
}