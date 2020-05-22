package com.g2academy.utilities;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class GetDataFromExcel {
	private FileInputStream fin;
	private XSSFWorkbook workbook;
	private XSSFSheet sheet;
	private XSSFRow row;
	private XSSFCell cell;

	private int getRowCount(String fileName, String sheetName) throws IOException {
		fin = new FileInputStream(fileName);
		workbook = new XSSFWorkbook(fin);
		sheet = workbook.getSheet(sheetName);
		int rowCount = sheet.getLastRowNum();
		workbook.close();
		fin.close();
		return rowCount;
	}

	private int getCellCount(String fileName, String sheetName, int rowNumber) throws IOException {
		fin = new FileInputStream(fileName);
		workbook = new XSSFWorkbook(fin);
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(rowNumber);
		int cellCount = row.getLastCellNum();
		workbook.close();
		fin.close();
		return cellCount;
	}

	private String getCellData(String fileName, String sheetName, int rowNumber, int columnNumber) throws IOException {
		fin = new FileInputStream(fileName);
		workbook = new XSSFWorkbook(fin);
		sheet = workbook.getSheet(sheetName);
		row = sheet.getRow(rowNumber);
		cell = row.getCell(columnNumber);

		String data;
		try {
			DataFormatter formatter = new DataFormatter();
			String cellData = formatter.formatCellValue(cell);
			return cellData;
		} catch(Exception e) {
			data = "";
		}

		workbook.close();
		fin.close();
		return data;
	}

	public Object[][] getDataExcel(String path, String sheetName) throws IOException {
		int rows = getRowCount(path, sheetName);
		int cols = getCellCount(path, sheetName, 1);
		String[][] data = new String[rows][cols];

		for (int i = 1; i <= rows; i++)
			for (int j = 0; j < cols; j++)
				data[i-1][j] = getCellData(path, sheetName, i, j);

		return data;
	}
}
