package com.ssym.avivacofco.test;

import com.ssym.avivacofco.rate.dto.RateInfo;
import com.ssym.avivacofco.rate.impl.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

/**
 * @author WB20200724005
 * @Description
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/9/22
 */
public class RateTest {
    private static final Logger logger = LoggerFactory.getLogger(RateTest.class);
    private static final String RESOURCES_PATH = "D:\\workspace\\naga-silly\\aviva-cofco-parent\\aviva-cofco-rate\\src\\main\\resources\\";

    public static void main(String[] args) {
//        // 现金价值表A款模板
//        RateTest.CashValueATest("CashValueA", "CashValueA");
//        // 现金价值表B款模板
//        RateTest.CashValueBTest("CashValueB", "CashValueB");
//        // 减额缴清保额A款模板
//        RateTest.ReducedAmntATest("ReducedAmntA", "ReducedAmntA");
//        // 减额缴清保额B款模板
//        RateTest.ReducedAmntBTest("ReducedAmntB", "ReducedAmntB");
//        // 减额交清保费B款模板
//        RateTest.ReducedPremBTest("ReducedPremB", "ReducedPremB");
        // 费率表A款模板
        RateTest.RateATest("RateA", "RateA");
//        // 费率表B款模板
//        RateTest.RateBTest("RateB", "RateB");
    }

    /**
     * 现金价值表A款模板
     * <br/> RISKCODE：险种编码
     * <br/> DUTYCODE：责任编码
     * <br/> MINAGE, MAXAGE:年龄，这两个值保持一致
     * <br/> GENDER:性别，0：男，1：女（传编码,如：0）
     * <br/> PAYUNIT：缴费期间单位，年：Y，周岁：A，趸交：Y（传编码，如：Y）
     * <br/> PAYPERIOD：缴费期间，传数字，趸交：1
     * <br/> PERIODFLAG：本次固定：L
     * <br/> PERIOD：本次固定：0
     * <br/> POLICYYEAR:保单年度
     * <br/> RATE：费率（在表基础上/1000）
     * <br/> AMNT：本次固定：null
     * <br/> SECURITYFLAG:有无社保，0：无社保，1：有社保
     * <br/> PLANCODE, RETIREAGE:本次固定：null
     *
     * @param excelName
     * @param sqlName
     */
    private static void CashValueATest(String excelName, String sqlName) {
        try {
            String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
            String sqlFilePath = RateTest.RESOURCES_PATH + sqlName + ".txt";

            CashValueA cashValueA = new CashValueA("211510", "151001");
            cashValueA.createSqlFile(filePath, sqlFilePath, 0);
        } catch (Exception ex) {
            System.out.println("CashValueATest Exception:" + ex.getMessage());
        }
    }

    /**
     * 现金价值表B款模板
     * <br/> RISKCODE：险种编码
     * <br/> DUTYCODE：责任编码
     * <br/> MINAGE, MAXAGE:年龄，这两个值保持一致
     * <br/> GENDER:性别，0：男，1：女（传编码,如：0）
     * <br/> PAYUNIT：缴费期间单位，年：Y，周岁：A，趸交：Y（传编码，如：Y）
     * <br/> PAYPERIOD：缴费期间，传数字，趸交：1
     * <br/> PERIODFLAG：本次固定：L
     * <br/> PERIOD：本次固定：0
     * <br/> POLICYYEAR:保单年度
     * <br/> RATE：费率（在表基础上/1000）
     * <br/> AMNT：本次固定：null
     * <br/> SECURITYFLAG:有无社保，0：无社保，1：有社保
     * <br/> PLANCODE:本次固定：null
     * <br/> RETIREAGE：约定年龄
     *
     * @param excelName
     * @param sqlName
     */
    private static void CashValueBTest(String excelName, String sqlName) {
        try {
            String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
            String sqlFilePath = RateTest.RESOURCES_PATH + sqlName;

            CashValueB cashValueB = new CashValueB("211509", "150901");
            // 基本责任+可选责任
            cashValueB.createSqlFile(filePath, sqlFilePath + "_sheet0.txt", 0);
            // 基本责任
            cashValueB.createSqlFile(filePath, sqlFilePath + "_sheet1.txt", 1);
        } catch (Exception ex) {
            System.out.println("CashValueBTest Exception:" + ex.getMessage());
        }
    }

    /**
     * 减额缴清保额A款模板
     * <br/> RISKCODE：险种编码
     * <br/> DUTYCODE：责任编码
     * <br/> MINAGE, MAXAGE:年龄，这两个值保持一致
     * <br/> GENDER:性别，0：男，1：女（传编码,如：0）
     * <br/> PAYUNIT：缴费期间单位，年：Y，周岁：A，趸交：Y（传编码，如：Y）
     * <br/> PAYPERIOD：缴费期间，传数字，趸交：1
     * <br/> PERIODFLAG：本次固定：L
     * <br/> PERIOD：本次固定：0
     * <br/> POLICYYEAR:保单年度
     * <br/> RATE：费率（在表基础上/1000）
     * <br/> AMNT：本次固定：null
     * <br/> SECURITYFLAG:有无社保，0：无社保，1：有社保
     * <br/> PLANCODE, RETIREAGE:本次固定：null
     *
     * @param excelName
     * @param sqlName
     */
    private static void ReducedAmntATest(String excelName, String sqlName) {
        try {
            String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
            String sqlFilePath = RateTest.RESOURCES_PATH + sqlName + ".txt";

            ReducedAmntA reducedAmntA = new ReducedAmntA("211510", "151001");
            reducedAmntA.createSqlFile(filePath, sqlFilePath, 0);
        } catch (Exception ex) {
            System.out.println("ReducedAmntATest Exception:" + ex.getMessage());
        }
    }

    /**
     * 减额缴清保额B款模板
     * <br/> RISKCODE：险种编码
     * <br/> DUTYCODE：责任编码
     * <br/> MINAGE, MAXAGE:年龄，这两个值保持一致
     * <br/> GENDER:性别，0：男，1：女（传编码,如：0）
     * <br/> PAYUNIT：缴费期间单位，年：Y，周岁：A，趸交：Y（传编码，如：Y）
     * <br/> PAYPERIOD：缴费期间，传数字，趸交：1
     * <br/> PERIODFLAG：本次固定：L
     * <br/> PERIOD：本次固定：0
     * <br/> POLICYYEAR:保单年度
     * <br/> RATE：费率（在表基础上/1000）
     * <br/> AMNT：本次固定：null
     * <br/> SECURITYFLAG:有无社保，0：无社保，1：有社保
     * <br/> PLANCODE:本次固定：null
     * <br/> RETIREAGE：约定年龄
     *
     * @param excelName
     * @param sqlName
     */
    private static void ReducedAmntBTest(String excelName, String sqlName) {
        try {
            String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
            String sqlFilePath = RateTest.RESOURCES_PATH + sqlName;

            ReducedAmntB reducedAmntB = new ReducedAmntB("211509", "150901");
            // 基本责任+可选责任
            reducedAmntB.createSqlFile(filePath, sqlFilePath + "_sheet0.txt", 0);
            // 基本责任
            reducedAmntB.createSqlFile(filePath, sqlFilePath + "_sheet1.txt", 1);
        } catch (Exception ex) {
            System.out.println("ReducedAmntBTest Exception:" + ex.getMessage());
        }
    }

    /**
     * 减额缴清保费B款模板
     * <br/> RISKCODE：险种编码
     * <br/> DUTYCODE：责任编码
     * <br/> MINAGE, MAXAGE:年龄，这两个值保持一致
     * <br/> GENDER:性别，0：男，1：女（传编码,如：0）
     * <br/> PAYUNIT：缴费期间单位，年：Y，周岁：A，趸交：Y（传编码，如：Y）
     * <br/> PAYPERIOD：缴费期间，传数字，趸交：1
     * <br/> PERIODFLAG：本次固定：L
     * <br/> PERIOD：本次固定：0
     * <br/> POLICYYEAR:保单年度
     * <br/> RATE：费率（在表基础上/1000）
     * <br/> AMNT：本次固定：null
     * <br/> SECURITYFLAG:有无社保，0：无社保，1：有社保
     * <br/> PLANCODE:本次固定：null
     * <br/> RETIREAGE：约定年龄
     *
     * @param excelName
     * @param sqlName
     */
    private static void ReducedPremBTest(String excelName, String sqlName) {
        try {
            String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
            String sqlFilePath = RateTest.RESOURCES_PATH + sqlName;

            ReducedPremB reducedPremB = new ReducedPremB("211509", "150901");
            // 有基本医疗保险
            reducedPremB.createSqlFile(filePath, sqlFilePath + "_sheet0.txt", 0, RateInfo.builder().securityFlag("1").build());
            // 无基本医疗保险
            reducedPremB.createSqlFile(filePath, sqlFilePath + "_sheet1.txt", 1, RateInfo.builder().securityFlag("0").build());
        } catch (Exception ex) {
            System.out.println("ReducedAmntBTest Exception:" + ex.getMessage());
        }
    }

    /**
     * 费率表A款模板
     * <br/> RISKCODE：险种编码
     * <br/> DUTYCODE：责任编码
     * <br/> MINAGE, MAXAGE:年龄，这两个值保持一致
     * <br/> GENDER:性别，0：男，1：女（传编码,如：0）
     * <br/> PAYUNIT：缴费期间单位，年：Y，周岁：A，趸交：Y（传编码，如：Y）
     * <br/> PAYPERIOD：缴费期间，传数字，趸交：1
     * <br/> PERIODFLAG：本次固定：L
     * <br/> PERIOD：本次固定：0
     * <br/> POLICYYEAR:保单年度
     * <br/> RATE：费率（在表基础上/1000）
     * <br/> AMNT：本次固定：null
     * <br/> SECURITYFLAG:有无社保，0：无社保，1：有社保
     * <br/> PLANCODE:本次固定：null
     * <br/> RETIREAGE：约定年龄 本次固定null
     *
     * @param excelName
     * @param sqlName
     */
    private static void RateATest(String excelName, String sqlName) {
        try {
            String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
            String sqlFilePath = RateTest.RESOURCES_PATH + sqlName;

            RateA rateA = new RateA("211510", "151001");
            // 基本责任
            rateA.createSqlFile(filePath, sqlFilePath + "_sheet0.txt", 0);

            // 基本责任-次标体等级50
            RateAddFeeA rateAddFeeA = new RateAddFeeA("211510", "151001");
            rateAddFeeA.createSqlFile(filePath, sqlFilePath + "_sheet1.txt", 1, RateInfo.builder().securityFlag("0.5").build());
            // 基本责任-次标体等级75
            rateAddFeeA.createSqlFile(filePath, sqlFilePath + "_sheet2.txt", 2, RateInfo.builder().securityFlag("0.75").build());
            // 基本责任-次标体等级100
            rateAddFeeA.createSqlFile(filePath, sqlFilePath + "_sheet3.txt", 3, RateInfo.builder().securityFlag("1").build());
            // 基本责任-次标体等级125
            rateAddFeeA.createSqlFile(filePath, sqlFilePath + "_sheet4.txt", 4, RateInfo.builder().securityFlag("1.25").build());
            // 基本责任-次标体等级150
            rateAddFeeA.createSqlFile(filePath, sqlFilePath + "_sheet5.txt", 5, RateInfo.builder().securityFlag("1.5").build());
        } catch (Exception ex) {
            System.out.println("RateATest Exception:" + ex.getMessage());
        }
    }

    /**
     * 费率表B款模板
     * <br/> RISKCODE：险种编码
     * <br/> DUTYCODE：责任编码
     * <br/> MINAGE, MAXAGE:年龄，这两个值保持一致
     * <br/> GENDER:性别，0：男，1：女（传编码,如：0）
     * <br/> PAYUNIT：缴费期间单位，年：Y，周岁：A，趸交：Y（传编码，如：Y）
     * <br/> PAYPERIOD：缴费期间，传数字，趸交：1
     * <br/> PERIODFLAG：本次固定：L
     * <br/> PERIOD：本次固定：0
     * <br/> POLICYYEAR:保单年度
     * <br/> RATE：费率（在表基础上/1000）
     * <br/> AMNT：本次固定：null
     * <br/> SECURITYFLAG:有无社保，0：无社保，1：有社保
     * <br/> PLANCODE:本次固定：null
     * <br/> RETIREAGE：约定年龄
     *
     * @param excelName
     * @param sqlName
     */
    private static void RateBTest(String excelName, String sqlName) {
        try {
            String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
            String sqlFilePath = RateTest.RESOURCES_PATH + sqlName;

            RateB rateB = new RateB("211509", "150901");
            // 基本责任+可选责任
            rateB.createSqlFile(filePath, sqlFilePath + "_sheet0.txt", 0);
            // 基本责任
            rateB.createSqlFile(filePath, sqlFilePath + "_sheet1.txt", 1);
        } catch (Exception ex) {
            System.out.println("RateATest Exception:" + ex.getMessage());
        }
    }

}
