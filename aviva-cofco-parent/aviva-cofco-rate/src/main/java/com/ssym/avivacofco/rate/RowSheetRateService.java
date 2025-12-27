package com.ssym.avivacofco.rate;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ssym.avivacofco.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author WB20200724005
 * @Description EXCEL视图[行属性]
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/9/22
 */
public abstract class RowSheetRateService extends AbstractService {
    private static final Logger logger = LoggerFactory.getLogger(RowSheetRateService.class);
    protected List<String> headerIndexs = new ArrayList<>(1);
    /**
     * 初始化表格及SQL设置
     */
    @Override
    protected void init() {

    }

    /**
     * 单元格元数据转换为单元格费率对象map
     *
     * @param rateData
     * @return
     * @throws Exception
     */
    @Override
    protected List<Map<String, String>> convertRateData(List<Map<String, String>> rateData) {
        return null;
    }

    @Override
    protected List<Map<String, String>> buildRateData(Sheet sheet) throws Exception {
        List<Map<String, String>> rowList = new ArrayList<>();
        // header列序号
        // 1.属性记录到headerDataMap 方便费率时使用
        Map<String, String> headerDataMap = new HashMap<>();
        for (int i = 0; i < this.headerRowCount; i++) {
            int rowIndex = this.headerRowIndex + i;
            Row rowData = sheet.getRow(rowIndex);
            if (ObjectUtil.isEmpty(rowData)) {
                continue;
            }
            int cellCount = rowData.getPhysicalNumberOfCells();
            // 读列内容[由于第1列数据不用所以直接取第1列]
            for (int cellIndex = 1; cellIndex < cellCount; cellIndex++) {
                // 1.1无意义属性不存
                if (this.headerIndexs.contains(String.valueOf(cellIndex))) {
                    continue;
                }
                String cellVal = ExcelUtil.getCellVal(rowData.getCell(cellIndex));
                cellVal = cellVal.replaceAll("0+?$", "").replaceAll("[\\.]$", "");
                String cellName = rowIndex + "_" + cellIndex + "_" + this.extProps;
                headerDataMap.put(cellName, cellVal);
            }
        }
        // 2.内容数据
        BigDecimal premSumBigDecimal = new BigDecimal(0);
        int rowCount = sheet.getPhysicalNumberOfRows() + this.spaceRowCount;
        for (int rowIndex = this.contentRowIndex; rowIndex < rowCount; rowIndex++) {
            Row rowData = sheet.getRow(rowIndex);
            if (ObjectUtil.isEmpty(rowData)) {
                continue;
            }
            // 读列内容[由于第1列数据不用所以直接取第1列]
            int cellCount = rowData.getPhysicalNumberOfCells();
            for (int cellIndex = 1; cellIndex < cellCount; cellIndex++) {
                String cellVal = ExcelUtil.getCellVal(rowData.getCell(cellIndex));
                // 2.1其它属性记录到headerDataMap 方便费率时使用
                String headerKey = rowIndex + "_" + cellIndex + "_" + this.props.get(this.headerRowIndex + "_" + cellIndex);
                if (this.headerIndexs.contains(String.valueOf(cellIndex))) {
                    headerDataMap.put(headerKey, cellVal.replaceAll("0+?$", "").replaceAll("[\\.]$", ""));
                    continue;
                }
                // 2.2去空
                if (StrUtil.isEmpty(cellVal)) {
                    continue;
                }
                premSumBigDecimal = premSumBigDecimal.add(new BigDecimal(cellVal));
                // 2.3费率开始处理
                Map<String, String> contentDataMap = new HashMap<>();
                // 2.3.1费率属性
                contentDataMap.put(rowIndex + "_" + cellIndex + "_费率", cellVal);
                // 2.3.2添加其它属性
                for (Map.Entry<String, String> entry : this.props.entrySet()) {
                    // 属性所在的列序号
                    String headerCountIndex = entry.getKey().split("_", 2)[1];
                    // 属性名称
                    String propsVal = entry.getValue();
                    String cellName = rowIndex + "_" + cellIndex + "_" + propsVal;
                    if (this.extProps.equals(propsVal)) {
                        // 2.3.3 额外属性特殊处理
                        contentDataMap.put(cellName, headerDataMap.get(this.headerRowIndex + "_" + cellIndex + "_" + propsVal));
                    } else {
                        contentDataMap.put(cellName, headerDataMap.get(rowIndex + "_" + headerCountIndex + "_" + propsVal));
                    }
                }
                rowList.add(contentDataMap);
            }
        }
        rowList.stream().forEach(cellMap -> logger.debug(JSONUtil.toJsonStr(cellMap)));
        return rowList;
    }
}
