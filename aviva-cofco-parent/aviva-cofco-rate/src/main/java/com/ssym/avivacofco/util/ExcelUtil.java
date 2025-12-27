package com.ssym.avivacofco.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;

/**
 * @author WB20200724005
 * @Description
 * @Email jiangdragon@126.com | jianglong@sinosoft.com.cn
 * @Time 2025/9/17
 */
public class ExcelUtil {
    public static Sheet getWorkBookSheet(String filePath, int sheetIndex) throws Exception {
        // 获取文件流
        FileInputStream inputStream = new FileInputStream(filePath);
        // 1.创建一个工作簿。使用Excel能操作的，这个类都可以操作
        Workbook workbook = new XSSFWorkbook(inputStream);
        // 2.得到工作表
        Sheet sheet = workbook.getSheetAt(sheetIndex);
        inputStream.close();
        return sheet;
    }

    public static String getCellVal(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        CellType cellType = cell.getCellType();
        switch (cellType) {
            case STRING:  // 字符串
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN:  // 布尔
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case BLANK:  // 空
                break;
            case NUMERIC:  // 数字（日期、普通数字）
                if (DateUtil.isCellDateFormatted(cell)) {
                    cellValue = cell.getDateCellValue().toString(); // 处理日期类型数据
                } else {
                    cellValue = String.valueOf(cell.getNumericCellValue()); // 转换为字符串，避免科学计数法表示的数字问题
                }
                break;
            case ERROR:
                System.out.print("【数据类型错误】");
                break;
        }
        return cellValue;
    }

    /**
     * 判断单元格是否合并
     *
     * @param sheet
     * @param cell
     * @return
     */
    public static boolean isMergedRegion(Sheet sheet, Cell cell) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress merged = sheet.getMergedRegion(i);
            if (merged.getFirstRow() <= cell.getRowIndex() && merged.getLastRow() >= cell.getRowIndex() &&
                    merged.getFirstColumn() <= cell.getColumnIndex() && merged.getLastColumn() >= cell.getColumnIndex()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回合并单元格
     *
     * @param sheet
     * @param cell
     * @return
     */
    public static Cell getMergedRegionCell(Sheet sheet, Cell cell) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress merged = sheet.getMergedRegion(i);
            if (merged.getFirstRow() <= cell.getRowIndex() && merged.getLastRow() >= cell.getRowIndex() &&
                    merged.getFirstColumn() <= cell.getColumnIndex() && merged.getLastColumn() >= cell.getColumnIndex()) {
                // 返回合并区域的左上角单元格
                return sheet.getRow(merged.getFirstRow()).getCell(merged.getFirstColumn());
            }
        }
        // 如果不是合并单元格，返回null或适当的默认值处理方式。
        return null;
    }
}