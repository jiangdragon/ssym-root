package com.ssym.avivacofco.rate;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.ssym.avivacofco.util.ExcelUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author WB20200724005
 * @Description EXCEL视图[列属性]
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/9/22
 */
public abstract class ColSheetRateService extends AbstractService {
    /**
     * 初始化表格及SQL设置
     */
    @Override
    protected void init() {

    }

    @Override
    protected List<Map<String, String>> buildRateData(Sheet sheet) throws Exception {
        List<Map<String, String>> rowList = new ArrayList<>();
        // 1.头数据
        Map<String, String> headerDataMap = new HashMap<>();
        for (int i = 0; i < this.headerRowCount; i++) {
            int rowIndex = this.headerRowIndex + i;
            Row rowData = sheet.getRow(rowIndex);
            if (ObjectUtil.isEmpty(rowData)) {
                continue;
            }
            int cellCount = rowData.getPhysicalNumberOfCells();
            for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
                if (cellIndex == 0) {
                    continue;
                }
                Cell cell = rowData.getCell(cellIndex);
                // 处理合并单元格
                if (ExcelUtil.isMergedRegion(sheet, cell)) {
                    cell = ExcelUtil.getMergedRegionCell(sheet, cell);
                }
                String cellVal = ExcelUtil.getCellVal(cell);
                String key = rowIndex + "_" + cellIndex + "_" + this.props.get(rowIndex + "_0");
                headerDataMap.put(key, cellVal);
            }
        }
        // 2.内容数据
        int rowCount = sheet.getPhysicalNumberOfRows() + this.spaceRowCount;
        for (int rowIndex = this.contentRowIndex; rowIndex < rowCount; rowIndex++) {
            Row rowData = sheet.getRow(rowIndex);
            if (ObjectUtil.isEmpty(rowData)) {
                continue;
            }
            int cellCount = rowData.getPhysicalNumberOfCells();
            for (int cellIndex = 0; cellIndex < cellCount; cellIndex++) {
                String cellVal = ExcelUtil.getCellVal(rowData.getCell(cellIndex));
                if (cellIndex == 0) {
                    // 补header
                    String key = rowIndex + "_1_" + this.props.get(this.contentRowIndex + "_0");
                    headerDataMap.put(key, cellVal);
                    continue;
                }
                if (StrUtil.isEmpty(cellVal)) {
                    continue;
                }
                // 真正内容
                Map<String, String> contentDataMap = new HashMap<>();
                for (Map.Entry<String, String> entry : this.props.entrySet()) {
                    String headerRowIndex = entry.getKey().split("_", 2)[0];
                    String propsVal = entry.getValue();
                    String cellName = rowIndex + "_" + cellIndex + "_" + propsVal;
                    if ("年龄".equals(propsVal)) {
                        contentDataMap.put(cellName, headerDataMap.get(rowIndex + "_1_" + propsVal));
                    } else {
                        contentDataMap.put(cellName, headerDataMap.get(headerRowIndex + "_" + cellIndex + "_" + propsVal));
                    }
                }
                // 费率
                String newKey = rowIndex + "_" + cellIndex + "_费率";
                contentDataMap.put(newKey, cellVal);
                rowList.add(contentDataMap);
            }
        }
        rowList.stream().forEach(cellMap -> System.out.println(JSONUtil.toJsonStr(cellMap)));
        return rowList;
    }
}
