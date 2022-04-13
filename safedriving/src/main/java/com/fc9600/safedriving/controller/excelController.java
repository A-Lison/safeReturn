package com.fc9600.safedriving.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/excel")

class excelController {
    @Autowired
    JdbcTemplate jdbcTemplate;
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sheetNamedf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    HSSFWorkbook workbook;

    public void createSheet(
            List<Map<String, Object>> list,
            String num, String id) throws IOException, ParseException {
        Date date = df.parse(num);
        String time = sheetNamedf.format(date);
        HSSFSheet sheet = workbook.createSheet(time);
        sheet.setColumnWidth(0, 20 * 256);

        HSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle style1 = workbook.createCellStyle();
        style1.setAlignment(HorizontalAlignment.CENTER);
        style1.setFillForegroundColor(IndexedColors.LIGHT_TURQUOISE.getIndex());
        style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle style2 = workbook.createCellStyle();
        style2.setAlignment(HorizontalAlignment.CENTER);
        style2.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        HSSFCellStyle Hstyle = workbook.createCellStyle();
        Hstyle.setFillForegroundColor(IndexedColors.GOLD.getIndex());
        Hstyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Hstyle.setAlignment(HorizontalAlignment.CENTER);

        int rowNum = 5;
        String[] headers = { "时间", "心跳", "血压", "酒精", "体温" };
        HSSFRow row = sheet.createRow(4);
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            cell.setCellStyle(style);
        }
        int cnt[] = { 0, 0, 0, 0 };
        int size = list.size();
        for (int i = 0; i < size; i++) {

            HSSFRow row1 = sheet.createRow(rowNum);

            int heart = Integer.parseInt(list.get(i).get("heart") + "");
            double press = Double.parseDouble(list.get(i).get("press") + "");
            double alcohol = Double.parseDouble(list.get(i).get("alcohol") + "");
            double heat = Double.parseDouble(list.get(i).get("heat") + "");
            row1.createCell(0).setCellValue(list.get(i).get("time") + "");
            row1.createCell(1).setCellValue(heart + "");
            row1.createCell(2).setCellValue(press + "");
            row1.createCell(3).setCellValue(alcohol + "");
            row1.createCell(4).setCellValue(heat + "");
            if (rowNum % 2 == 0) {
                for (int j = 0; j < 5; j++) {
                    row1.getCell(j).setCellStyle(style1);
                }
            } else {
                for (int j = 0; j < 5; j++) {
                    row1.getCell(j).setCellStyle(style2);
                }
            }
            if (heart < 40 || heart > 120) {
                cnt[0]++;
                row1.getCell(1).setCellStyle(Hstyle);
            }
            if (press < 40 || press > 120) {
                cnt[1]++;
                row1.getCell(2).setCellStyle(Hstyle);
            }
            if (alcohol < 40 || alcohol > 120) {
                cnt[2]++;
                row1.getCell(3).setCellStyle(Hstyle);
            }
            if (heat < 40 || heat > 120) {
                cnt[3]++;
                row1.getCell(4).setCellStyle(Hstyle);
            }
            rowNum++;

        }
        // 第一行的统计数据
        row = sheet.createRow(0);
        headers[0] = "指标异常次数统计";
        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
            cell.setCellStyle(Hstyle);
        }
        row = sheet.createRow(1);
        HSSFCell t = row.createCell(0);
        HSSFRichTextString tx = new HSSFRichTextString("次数");
        t.setCellValue(tx);
        t.setCellStyle(Hstyle);
        for (int i = 0; i < cnt.length; i++) {
            HSSFCell cell = row.createCell(i + 1);
            HSSFRichTextString text = new HSSFRichTextString(cnt[i] + "");
            cell.setCellValue(text);
            cell.setCellStyle(Hstyle);
        }
        // 违规行为统计
        // String actName[] = { "抽烟", "饮食", "打电话", "疲劳驾驶" };
        // int cntAct[] = { 0, 0, 0, 0 };
        // row.createCell(size + 2);
        // 合并第三行
        CellRangeAddress region = new CellRangeAddress(2, 3, 0, 4);
        sheet.addMergedRegion(region);
        // t = sheet.createRow(3).createCell(0);
        // t.setCellValue("详细统计列表");
    }

    // 导出num1 到 num2时间段的健康表
    @GetMapping("/export/{id}/{num1}/{num2}")
    public boolean excel(@PathVariable("id") String id,
            @PathVariable("num1") String num1,
            @PathVariable("num2") String num2,
            HttpServletResponse response) throws IOException, ParseException {
        Date date = df.parse(num1);
        String start = sheetNamedf.format(date);
        date = df.parse(num2);
        String end = sheetNamedf.format(date);
        String formName = "health" + id;
        String fileName = id + "'s health report.xls";
        String sql = "select num from (SELECT * FROM " + formName + " WHERE num BETWEEN '" + start +
                ".000' AND '" + end + ".999')A group by num;";
        List<Map<String, Object>> total = jdbcTemplate.queryForList(sql);
        this.workbook = new HSSFWorkbook();
        // 建立escel中的每一个sheet
        for (int i = 0; i < total.size(); i++) {
            String newN = (String) total.get(i).get("num");
            sql = "select * from " + formName + " where num = '" + newN + "';";
            List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
            createSheet(list, newN, id);
        }
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
        return true;
    }
}