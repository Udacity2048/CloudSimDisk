/**
 * 
 */
package org.cloudbus.cloudsim.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

/**
 * This class create a trace of all informations concerning "Storage Simulation"(Ex: arrival times, waiting times,
 * execution times, seekTime, rotation latency, internal transfer Time, power consumption, energy consumption,
 * filenames, file sizes, transaction Time).
 * 
 * @author Baptiste Louis
 */
public class WriteToResultFile {

	/** The relative path of the future results file. */
	public static String		file_name	= "";

	public static File			file;

	public static HSSFWorkbook	workbook;

	public static HSSFSheet		sheet;

	public static int			tempRowNum;

	public static void init() {
		// variable to change each log files names.
		java.util.Date d = new java.util.Date();

		// Create Result File
		if (file_name == "") {

			// define the filename
			file_name = "results/cloudSim_Res" + d.getTime() + ".xls";

			// Instantiate a File object
			file = new File(file_name);

			// Get the workbook instance for XLS file
			workbook = new HSSFWorkbook();

			// if file doesn't exists, then create it
			FileOutputStream fileOut;
			try {
				fileOut = new FileOutputStream(file);
				workbook.write(fileOut);
				fileOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		// Get first sheet from the workbook
		sheet = workbook.createSheet("Sample sheet");

		// Create a new row in current sheet
		Row row = sheet.createRow(0);

		// Create a new cell in current row
		Cell cell = row.createCell(0);
		cell.setCellValue("Cloudlet ID");
		cell = row.createCell(1);
		cell.setCellValue("Arrival Time (s)");
		cell = row.createCell(2);
		cell.setCellValue("Waiting Time (s)");
		cell = row.createCell(3);
		cell.setCellValue("Transaction Time (s)");
		cell = row.createCell(4);
		cell.setCellValue("Seek Time (s)");
		cell = row.createCell(5);
		cell.setCellValue("Rotation Latency (s)");
		cell = row.createCell(6);
		cell.setCellValue("Transfer Time (s)");
		cell = row.createCell(7);
		cell.setCellValue("Execusion Time (s)");
		cell = row.createCell(8);
		cell.setCellValue("Filename");
		cell = row.createCell(9);
		cell.setCellValue("File size (MB)");
		cell = row.createCell(10);
		cell.setCellValue("Energy Consumption (J)");

		FileOutputStream out;
		try {
			out = new FileOutputStream(file);
			workbook.write(out);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Add a new value to the result sheet tab.
	 * 
	 * @param input
	 *            the input to add (String, Double or Int).
	 */
	public static void AddValueToSheetTab(Object input, int rownum, int column) {
		try {

			tempRowNum = rownum;

			FileInputStream fileIntStream = new FileInputStream(file);

			HSSFWorkbook workbook = new HSSFWorkbook(fileIntStream);
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Write the value in the good cell
			Row row = sheet.getRow(rownum);
			if (row == null) {
				row = sheet.createRow(rownum);
			}
			Cell cell = row.getCell(column);
			if (cell == null) {
				cell = row.createCell(column);
			}
			if (input instanceof String) {
				cell.setCellValue((String) input);
			} else if (input instanceof Double) {
				cell.setCellValue((Double) input);
			} else if (input instanceof Integer) {
				cell.setCellValue((int) input);
			}

			// close input stream
			fileIntStream.close();

			// update workbook
			FileOutputStream outFile = new FileOutputStream(file);
			workbook.write(outFile);
			outFile.close();
			workbook.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Add a new value to the result sheet tab on the same raw than previous action.
	 * 
	 * @param value
	 *            the value to add on the results file in Double format.
	 */
	public static void AddValueToSheetTabSameRow(double value, int column) {
		try {
			FileInputStream fileIntStream = new FileInputStream(file);

			HSSFWorkbook workbook = new HSSFWorkbook(fileIntStream);
			HSSFSheet sheet = workbook.getSheetAt(0);

			// Write the value in the good cell
			Row row = sheet.getRow(tempRowNum);
			if (row == null) {
				row = sheet.createRow(tempRowNum);
			}
			Cell cell = row.getCell(column);
			if (cell == null) {
				cell = row.createCell(column);
			}
			cell.setCellValue(value);

			// close input stream
			fileIntStream.close();

			// update workbook
			FileOutputStream outFile = new FileOutputStream(file);
			workbook.write(outFile);
			outFile.close();
			workbook.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
