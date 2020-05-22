package com.g2academy.utilities;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SetDataToExcel {
    public static void write(Object[][] data, String sheetName) throws IOException {
        InputStream inp = new FileInputStream("test-output/QuotaAppReport.xlsx");
        Workbook wb = WorkbookFactory.create(inp);
        Sheet sheet = wb.getSheet(sheetName);

        int rowCount = 0;
        for (Object[] baris: data) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            for (Object kolom: baris) {
                Cell cell = row.createCell(columnCount++);
                cell.setCellValue((String) kolom);
            }
        }

        try (FileOutputStream outputStream = new FileOutputStream("test-output/QuotaAppReport.xlsx")) {
            wb.write(outputStream);
        }
    }
}
