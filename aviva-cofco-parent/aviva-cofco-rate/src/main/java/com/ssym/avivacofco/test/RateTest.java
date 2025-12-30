package com.ssym.avivacofco.test;

import com.ssym.avivacofco.rate.AbstractService;
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
        // 现金价值表
//        RateTest.CashValueTest("现价", "CashValue");
        // 减额缴清
//        RateTest.ReducedTest("减额缴清", "Reduced");
        // 费率表-基本责任
//        RateTest.RateValTest("费率", "RateVal");
        // 费率表-次标体等级
//        RateTest.RateAddFeeTest("RateVal", "RateAddFee");
        // 减额缴清保费费率表
        RateTest.ReducedPremTest("减额缴清保费", "减额缴清保费");
    }

    /**
     * 现金价值表
     *
     * @param excelName
     * @param sqlName
     */
    private static void CashValueTest(String excelName, String sqlName) {
        try {
            String sqlText = "INSERT INTO NEWLIS.RATE_PRESENTPRICE (RISKCODE, DUTYCODE, MINAGE, MAXAGE, GENDER, PAYUNIT, PAYPERIOD, PERIODFLAG,"
                    + " PERIOD, POLICYYEAR, RATE, AMNT, SECURITYFLAG, PLANCODE, RETIREAGE) VALUES "
                    + "('211511', '151102', ${MINAGE}, ${MAXAGE}, '${GENDER}', '${PAYUNIT}', ${PAYPERIOD}, 'L', 0, ${POLICYYEAR},  ${RATE}, null,"
                    + " '${SECURITYFLAG}', null, ${RETIREAGE});";
            String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
            String sqlFilePath = RateTest.RESOURCES_PATH + sqlName + ".txt";

            CashValue cashValue = new CashValue();
            cashValue.setSqlText(sqlText);
            cashValue.createSqlFile(filePath, sqlFilePath, 0);
        } catch (Exception ex) {
            System.out.println("CashValueTest Exception:" + ex.getMessage());
        }
    }

    /**
     * 减额缴清
     *
     * @param excelName
     * @param sqlName
     */
    private static void ReducedTest(String excelName, String sqlName) {
        try {
            String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
            String sqlFilePath = RateTest.RESOURCES_PATH + sqlName + ".txt";
            String sqlText = "INSERT INTO NEWLIS.RATE_DERATING (RISKCODE, DUTYCODE, MINAGE, MAXAGE, GENDER, PAYUNIT, PAYPERIOD, PERIODFLAG,"
                    + "PERIOD, POLICYYEAR, RATE, SECURITYFLAG, PLANCODE, RETIREAGE) VALUES "
                    + "('211511', '151102', ${MINAGE}, ${MAXAGE}, '${GENDER}', '${PAYUNIT}', ${PAYPERIOD}, 'L', 0, ${POLICYYEAR},  ${RATE},"
                    + "'${SECURITYFLAG}', null, ${RETIREAGE});";
            Reduced reduced = new Reduced();
            reduced.setSqlText(sqlText);
            reduced.createSqlFile(filePath, sqlFilePath, 0);
        } catch (Exception ex) {
            System.out.println("ReducedTest Exception:" + ex.getMessage());
        }
    }

    /**
     * 减额缴清保费-两个表格
     * 表头:保险期间 费期间	性别	约定年龄 保单年度/年龄
     * sheet:有基本医疗保险 无基本医疗保险
     *
     * @param excelName
     * @param sqlName
     */
    private static void ReducedPremTest(String excelName, String sqlName) {
        String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
        String sqlFilePath = RateTest.RESOURCES_PATH + sqlName;
        // 社保标识:1-有基本医疗保险 0-无基本医疗保险
        List<String> securityFlags = Arrays.asList("1", "0");
        IntStream.range(0, securityFlags.size()).forEach(index -> {
            try {
                ReducedPrem reducedPrem = new ReducedPrem("211509", "150901", securityFlags.get(index));
                reducedPrem.createSqlFile(filePath, sqlFilePath + "_" + index + ".txt", index);
                logger.info("ReducedPremTest sheet index:{}", index);
            } catch (Exception ex) {
                logger.info("ReducedPremTest Exception:", ex);
            }
        });
    }

    /**
     * 费率表-基本责任
     *
     * @param excelName
     * @param sqlName
     */
    private static void RateValTest(String excelName, String sqlName) {
        try {
            String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
            String sqlFilePath = RateTest.RESOURCES_PATH + sqlName + ".txt";
            String sqlText = "INSERT INTO NEWLIS.RATE_AGE_GENDER_PAYPERIOD (RISKCODE,DUTYCODE,MINAGE,MAXAGE,GENDER,PAYUNIT,PAYPERIOD,PERIODFLAG,"
                    + " PERIOD,RATE,AMNT,SECURITYFLAG,PLANCODE,RETIREAGE) VALUES "
                    + "('211511','151102',${MINAGE},${MAXAGE},'${GENDER}','${PAYUNIT}',${PAYPERIOD},'L',0,${RATE},null,'${SECURITYFLAG}', null, ${RETIREAGE});";
            RateVal rateVal = new RateVal();
            rateVal.setSqlText(sqlText);
            rateVal.createSqlFile(filePath, sqlFilePath, 0);
        } catch (Exception ex) {
            System.out.println("RateValTest Exception:" + ex.getMessage());
        }
    }

    /**
     * 费率表-次标体等级
     *
     * @param excelName
     * @param sqlName
     */
    private static void RateAddFeeTest(String excelName, String sqlName) {
        List<String> temps = Arrays.asList("50", "75", "100", "125", "150");
//        List<String> temps = Arrays.asList("50");
        List<String> levelList = Arrays.asList("0.5", "0.75", "1", "1.25", "1.5");
        String sqlText = "INSERT INTO NEWLIS.RATE_PAYPERIOD_ADDFEE (RISKCODE,DUTYCODE,ADDFEERATE,MINAGE,MAXAGE,GENDER,"
                + "PAYUNIT,PAYPERIOD,PERIODFLAG,PERIOD,RATE,AMNT,SECURITYFLAG) VALUES "
                + "('211510','151002','${ADDFEERATE}',${MINAGE},${MAXAGE},'${GENDER}','${PAYUNIT}',${PAYPERIOD},'L',0,${RATE},null,'${SECURITYFLAG}');";

        temps.stream().forEach(key -> {
            try {
                int level = Integer.parseInt(key);
                String filePath = RateTest.RESOURCES_PATH + excelName + ".xlsx";
                String sqlFilePath = RateTest.RESOURCES_PATH + sqlName + level + ".txt";

                RateAddFee rateAddFee = new RateAddFee();
                rateAddFee.setSqlText(sqlText);
                rateAddFee.setAddFeeRate(levelList.get(level / 25 - 2));
                rateAddFee.createSqlFile(filePath, sqlFilePath, (level / 25 - 1));
            } catch (Exception ex) {
                System.out.println("RateValTest Exception:" + ex.getMessage());
            }
        });
    }

}
