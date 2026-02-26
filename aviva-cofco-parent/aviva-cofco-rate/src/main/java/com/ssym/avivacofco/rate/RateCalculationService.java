package com.ssym.avivacofco.rate;

import com.ssym.avivacofco.rate.dto.RateInfo;

/**
 * @author WB20200724005
 * @Description 费率计算接口服务, 目前只支持读取Excel.xlsx格式
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2026/1/4
 */
public interface RateCalculationService {
    /**
     * 生成SQL文件,默认读取文件第1个Sheet
     *
     * @param sourcePath
     * @param targetPath
     * @param sheetIndex sheet序号,以0开始
     */
    void createSqlFile(String sourcePath, String targetPath, int sheetIndex) throws Exception;

    /**
     * 读取文件指定Sheet生成SQL文件
     *
     * @param sourcePath 费率文件源路径
     * @param targetPath SQL目标路径
     * @param sheetIndex sheet序号,以0开始
     * @param rateInfo   动态参数
     * @throws Exception
     */
    void createSqlFile(String sourcePath, String targetPath, int sheetIndex, RateInfo rateInfo) throws Exception;
}
