package com.gameplat.admin.util;

import com.alibaba.csp.sentinel.util.StringUtil;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 导出Excel表格工具类
 *
 * @author Armin
 * @date 2021-01-05
 */
public class ExportPoiExcelUtils {

  /**
   * 导出excel表格 在excel表中，无论是行还是列都是从0开始
   *
   * @return
   */
  public static Map<String, Object> handlerData(String sheetObj, List<String> cellKeys)
      throws Exception {
    // 创建HSSFWorkbook对象(excel的文档对象)
    HSSFWorkbook wb = new HSSFWorkbook();
    // 建立新的sheet对象（excel的表单）
    HSSFSheet sheet = wb.createSheet(sheetObj);

    // 1、获取样式
    CellStyle cellStyle = wb.createCellStyle();
    // 2、 给样式设置对齐方式（水平对齐）
    cellStyle.setAlignment(HorizontalAlignment.CENTER);

    // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
    /*HSSFRow row1 = sheet.createRow(0);
    //创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
    HSSFCell cell = row1.createCell(0);
    //设置单元格内容
    cell.setCellValue(cellContent);
    //3.设置单元格样式居中
    cell.setCellStyle(cellStyle);
    //合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
    sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));*/
    // 在sheet里创建第二行
    HSSFRow row2 = sheet.createRow(0);
    // 创建单元格并设置单元格内容
    for (int i = 0; i < cellKeys.size(); i++) {
      row2.createCell(i).setCellValue(cellKeys.get(i));
    }
    HashMap<String, Object> map = new HashMap<>();
    map.put("wb", wb);
    map.put("sheet", sheet);
    return map;
  }

  /**
   * 导出excel表格 在excel表中，无论是行还是列都是从0开始
   *
   * @return
   */
  public static Map<String, Object> handlerData(
      String sheetName, String title, int lastCol, List<String> cellKeys) throws Exception {
    // 创建HSSFWorkbook对象(excel的文档对象)
    HSSFWorkbook wb = new HSSFWorkbook();
    // 建立新的sheet对象（excel的表单）
    HSSFSheet sheet = wb.createSheet(sheetName);

    // 1、获取样式
    CellStyle cellStyle = wb.createCellStyle();
    // 2、 给样式设置对齐方式（水平对齐）
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    // 设置字体
    HSSFFont font = wb.createFont();
    font.setBold(true);
    font.setFontHeightInPoints((short) 20);
    cellStyle.setFont(font);
    // 判断是否设置标题
    int index = 0;
    if (!StringUtil.isEmpty(title)) {
      String[] split = title.split(",");
      for (String content : split) {
        // 在sheet里创建第一行，参数为行索引(excel的行)，可以是0～65535之间的任何一个
        HSSFRow row1 = sheet.createRow(index);
        // 创建单元格（excel的单元格，参数为列索引，可以是0～255之间的任何一个
        HSSFCell cell = row1.createCell(0);
        // 合并单元格CellRangeAddress构造参数依次表示起始行，截至行，起始列， 截至列
        sheet.addMergedRegion(new CellRangeAddress(index, index, 0, lastCol));
        if (index == 0) {
          row1.setHeightInPoints(40); // 目的是想把行高设置成20px
          cellStyle.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
          cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        } else {
          cellStyle = wb.createCellStyle();
          row1.setHeightInPoints(30); // 目的是想把行高设置成20px
          cellStyle.setFillForegroundColor(IndexedColors.LEMON_CHIFFON.getIndex());
          cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }
        // 设置单元格内容
        cell.setCellValue(content);
        // 3.设置单元格样式居中
        cell.setCellStyle(cellStyle);
        index++;
      }
    }
    // 在sheet里创建第二行
    HSSFRow row2 = sheet.createRow(index);
    row2.setHeightInPoints(20); // 目的是想把行高设置成20px

    // 创建单元格并设置单元格内容
    for (int i = 0; i < cellKeys.size(); i++) {
      row2.createCell(i).setCellValue(cellKeys.get(i));
    }
    cellStyle = wb.createCellStyle();
    HashMap<String, Object> map = new HashMap<>();
    map.put("wb", wb);
    map.put("sheet", sheet);
    map.put("cellStyle", cellStyle);
    return map;
  }

  /**
   * 数据导出
   *
   * @param response
   * @param wb
   * @throws Exception
   */
  public static void exportData(HttpServletResponse response, XSSFWorkbook wb) throws Exception {
    // 输出Excel文件
    OutputStream output = null;
    try {
      output = response.getOutputStream();
      response.reset();
      response.setHeader("Content-disposition", "attachment; filename=details.xls");
      response.setContentType("application/msexcel");
      wb.write(output);
      output.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      output.close();
      wb.close();
    }
  }

  /**
   * 数据导出
   *
   * @param response
   * @param wb
   * @throws Exception
   */
  public static void exportData(HttpServletResponse response, Workbook wb) throws Exception {
    // 输出Excel文件
    OutputStream output = null;
    try {
      output = response.getOutputStream();
      response.reset();
      response.setHeader("Content-disposition", "attachment; filename=details.xls");
      response.setContentType("application/msexcel");
      wb.write(output);
      output.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      output.close();
      wb.close();
    }
  }

  /**
   * 数据导出
   *
   * @param response
   * @param wb
   * @throws Exception
   */
  public static void exportData(HttpServletResponse response, HSSFWorkbook wb) throws Exception {
    // 输出Excel文件
    OutputStream output = null;
    try {
      output = response.getOutputStream();
      response.reset();
      response.setHeader("Content-disposition", "attachment; filename=details.xls");
      response.setContentType("application/msexcel");
      wb.write(output);
      output.close();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      output.close();
      wb.close();
    }
  }

  public static void main(String[] args) throws Exception {
    ExportPoiExcelUtils export = new ExportPoiExcelUtils();
    export.exportExcel(null);
  }

  // 根据指定的excel模板导出数据
  public void exportExcel(HttpServletResponse response) throws Exception {

    // 创建Excel文件的输入流对象
    FileInputStream fis = new FileInputStream("d:/导出交收报表格式.xlsx");
    // 根据模板创建excel工作簿
    XSSFWorkbook workBook = new XSSFWorkbook(fis);
    // 创建Excel文件输出流对象
    FileOutputStream fos =
        new FileOutputStream("d:/" + "财务报表_" + System.currentTimeMillis() + ".xlsx");
    // 获取创建的工作簿第一页
    XSSFSheet sheet = workBook.getSheetAt(0);
    // 给指定的sheet命名
    workBook.setSheetName(0, "财务报表");

    // 修改标题
    XSSFRow row = sheet.getRow(0);
    XSSFCell cell = row.getCell(0);
    // 获取指定单元格值
    String s = cell.getStringCellValue();
    cell.setCellValue("修改后的标题为:" + s);

    // 获取当前sheet最后一行数据对应的行索引
    int currentLastRowIndex = sheet.getLastRowNum();
    int newRowIndex = currentLastRowIndex + 1;
    XSSFRow newRow = sheet.createRow(newRowIndex);
    // 开始创建并设置该行每一单元格的信息，该行单元格的索引从 0 开始
    int cellIndex = 0;

    // 创建一个单元格，设置其内的数据格式为字符串，并填充内容，其余单元格类同
    XSSFCell newNameCell = newRow.createCell(cellIndex++, CellType.STRING);
    newNameCell.setCellValue("乔玉宝");
    XSSFCell newGenderCell = newRow.createCell(cellIndex++, CellType.STRING);
    newGenderCell.setCellValue("男");
    XSSFCell newAgeCell = newRow.createCell(cellIndex++, CellType.NUMERIC);
    newAgeCell.setCellValue(25);
    XSSFCell newAddressCell = newRow.createCell(cellIndex++, CellType.NUMERIC);
    newAddressCell.setCellValue("重庆市渝北区");

    // 输出Excel文件
    OutputStream output = response.getOutputStream();
    response.reset();
    response.setHeader("Content-disposition", "attachment; filename=details.xls");
    response.setContentType("application/msexcel");
    workBook.write(output);
    output.close();
  }
}
