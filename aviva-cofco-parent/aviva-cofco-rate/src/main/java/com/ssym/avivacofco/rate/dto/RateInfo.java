package com.ssym.avivacofco.rate.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

/**
 * @author WB20200724005
 * @Description 费率Bean对象
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2026/2/26
 */
@Data
@ToString
@Builder
public class RateInfo {
    /**
     * 险种编码
     */
    private String riskCode;
    /**
     * 责任编码
     */
    private String dutyCode;
    /**
     * 社保标识[无社保0 有社保1]
     */
    private String securityFlag;
    /**
     * 费率
     */
    private String rate;
    /**
     * 缴费期间[趸交1]
     */
    private String payPeriod;
    /**
     * 缴费单位[年Y 周岁A 趸交Y]
     */
    private String payUnit;
    /**
     * 保单年度
     */
    private String policyYear;
    /**
     * 计划编码
     */
    private String planCode;
    /**
     * 约定年龄
     */
    private String retireAge;
    /**
     * 性别[男0 女1]
     */
    private String gender;
    /**
     * 最小年龄
     */
    private String minAge;
    /**
     * 最大年龄
     */
    private String maxAge;
}
