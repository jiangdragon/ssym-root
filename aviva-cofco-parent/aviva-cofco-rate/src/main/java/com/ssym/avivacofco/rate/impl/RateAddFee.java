package com.ssym.avivacofco.rate.impl;

import cn.hutool.json.JSONUtil;
import com.ssym.avivacofco.rate.ColSheetRateService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author WB20200724005
 * @Description 费率表-次标体等级
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/9/17
 */
public class RateAddFee extends ColSheetRateService {
    private String addFeeRate = "";

    public RateAddFee() {
        this.sqlText = "INSERT INTO NEWLIS.RATE_PAYPERIOD_ADDFEE (RISKCODE, DUTYCODE, ADDFEERATE, MINAGE, MAXAGE, GENDER, PAYUNIT, PAYPERIOD, PERIODFLAG, PERIOD, RATE, AMNT, SECURITYFLAG) VALUES ('211510', '151001', '${ADDFEERATE}', ${MINAGE}, ${MAXAGE}, '${GENDER}', '${PAYUNIT}', ${PAYPERIOD}, 'L', 0, ${RATE}, null, '${SECURITYFLAG}');";
        this.headerRowIndex = 7;
        this.headerRowCount = 3;
        this.contentRowIndex = 10;
        this.spaceRowCount = 2;

        this.props.put("7_0", "社保状态");
        this.props.put("8_0", "缴费期间");
        this.props.put("9_0", "性别");
        this.props.put("10_0", "年龄");
    }

    @Override
    protected List<Map<String, String>> convertRateData(List<Map<String, String>> rateData) {
        System.out.println("=======================");
        // 整理下数据  {"79_64_费率":"","79_64_缴费期间":"缴至60周岁","79_64_性别":"女","79_64_社保状态":"无基本医疗保险"}
        List<Map<String, String>> newRateData = rateData.stream().map(map -> {
            HashMap<String, String> newMap = new HashMap<>();
            newMap.put("ADDFEERATE", this.addFeeRate);// 此标准体等级
            map.forEach((key, val) -> {
                if (key.contains("费率")) {
                    newMap.put("RATE", String.format("%.2f", Double.parseDouble(val)));
                } else if (key.contains("缴费期间")) {
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
                } else if (key.contains("性别")) {
                    newMap.put("GENDER", val.equals("男") ? "0" : "1");
                } else if (key.contains("社保状态")) {
                    newMap.put("SECURITYFLAG", (val.contains("有基本医疗保险") ? "1" : "0"));
                } else if (key.contains("年龄")) {
                    String age = val.replaceAll("0+?$", "");
                    age = age.replaceAll("[\\.]$", "");
                    newMap.put("MINAGE", age);
                    newMap.put("MAXAGE", age);
                }
            });
            return newMap;
        }).collect(Collectors.toList());
        System.out.println(JSONUtil.toJsonStr(newRateData));
        return newRateData;
    }

    public String getAddFeeRate() {
        return addFeeRate;
    }

    public void setAddFeeRate(String addFeeRate) {
        this.addFeeRate = addFeeRate;
    }
}
