package com.ssym.avivacofco.rate.impl;

import com.ssym.avivacofco.rate.AbstractSheetService;

/**
 * @author 三傻云梦
 * @Description 减额缴清保额A款, Sheet与保单字段对应关系
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
 * <br/> AMNT:保额[默认null]
 * <br/> SECURITYFLAG:社保标识[无社保0 有社保1]
 * <br/> PLANCODE:计划编码[默认null]
 * <br/> RETIREAGE:约定年龄[默认null]
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2026/2/25
 */
public class ReducedAmntA extends AbstractSheetService {
    /**
     * 构造函数
     *
     * @param riskCode 险种编码
     * @param dutyCode 责任编码
     */
    public ReducedAmntA(String riskCode, String dutyCode) {
        this.riskCode = riskCode;
        this.dutyCode = dutyCode;
    }

    /**
     * 初始化表格及SQL设置
     * <br/>设置费率属性[行列都以0开始,格式:行_列_属性 N代表变化]
     * <br/>设置表格表头属性[表头开始行数 表头行数量 表头空行数量]
     * <br/>设置SQL语句[每条SQL中Val的行数 INSERT VALUES]
     */
    @Override
    protected void init() {
        // 设置费率属性对应序号
        this.props.add("N_0_保险期间");
        this.props.add("N_1_社保状态");
        this.props.add("N_2_缴费期间");
        this.props.add("N_3_性别");
        this.props.add("N_4_年龄");
        this.props.add("5_N_保单年度");
        // 设置表格表头属性
        this.headerStartRowIndex = 5;
        this.headerRowCount = 1;
        this.spaceRowCount = 1;
        // 设置SQL插入语句
        this.plSQLValCount = 20000;
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("INSERT INTO NEWLIS.RATE_DERATING (RISKCODE, DUTYCODE, MINAGE, MAXAGE, GENDER, PAYUNIT, ");
        stringBuffer.append("PAYPERIOD, PERIODFLAG, PERIOD, POLICYYEAR, RATE, SECURITYFLAG, PLANCODE, RETIREAGE) VALUES ");
        this.plSQLInsert = stringBuffer.toString();
        // 设置SQL值语句
        stringBuffer = new StringBuffer();
        stringBuffer.append("('" + this.riskCode + "', '" + this.dutyCode + "', ");
        stringBuffer.append("${MINAGE}, ${MAXAGE}, '${GENDER}', '${PAYUNIT}', ${PAYPERIOD}, ");
        stringBuffer.append("'L', 0, ${POLICYYEAR}, ${RATE}, null, '${SECURITYFLAG}', null, null)");
        this.plSQLVal = stringBuffer.toString();
    }

}
