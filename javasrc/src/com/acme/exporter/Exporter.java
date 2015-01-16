package com.acme.exporter;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.JXLException;
import jxl.Workbook;
import jxl.WorkbookSettings;

import jxl.format.UnderlineStyle;
import jxl.format.Alignment;

import jxl.write.Formula;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/**
 * A buffer between the file system and the program that allows for writing and reading of Excel files.
 */
public class Exporter {
	protected static WritableCellFormat[] FORMATS;
	protected WritableSheet excelSheet;
	protected WritableWorkbook workbook;
	protected WorkbookSettings wbSettings;
	protected File file;
	
	/**
	 * Creates an <code>Exporter</code> object with the given file path.
	 * @param filename - the path to where the file should be exported
	 */

	public Exporter(String filename){ //Will be in the same directory where this is running
		//Initialize static cell formats
		initStaticFormats();
		
		file = new File(filename);
		
		wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("en", "EN"));
		try {
			workbook = Workbook.createWorkbook(file, wbSettings);
		} catch (IOException e) {
			e.printStackTrace();
		}

		workbook.createSheet("Report", 0);
		excelSheet = workbook.getSheet(0);
	}
	
	/**
	 * Sets the export file
	 * @param newExportFile - the new export file to be used
	 */

	public void setExportFile(File newExportFile) {
		file = newExportFile;
	}
	
	/**
	 * Writes a couple of lines to a workbook and then closes it
	 * @throws IOException
	 * @throws WriteException
	 */

	public void write() throws IOException, WriteException {
		createContent();
		workbook.write();
		workbook.close();
	}

	protected void createContent()
			throws WriteException, RowsExceededException {
		// Write a few number
		for (int i = 1; i < 10; i++) {
			// First column
			addNumber(0, i, i + 10, FORMATS[1]);
			// Second column
			addNumber(1, i, i * i, FORMATS[1]);
		}
		// Lets calculate the sum of it
		StringBuffer buf = new StringBuffer();
		buf.append("SUM(A2:A10)");
		Formula f = new Formula(0, 10, buf.toString());
		excelSheet.addCell(f);
		buf = new StringBuffer();
		buf.append("SUM(B2:B10)");
		f = new Formula(1, 10, buf.toString());
		excelSheet.addCell(f);

		// Now a bit of text
		for (int i = 12; i < 20; i++) {
			// First column
			addLabel(0, i, "Boring text " + i, FORMATS[1]);
			// Second column
			addLabel(1, i, "Another text", FORMATS[1]);
		}
	}

	protected void initStaticFormats(){
		FORMATS = new WritableCellFormat[5];

		WritableFont times = new WritableFont(WritableFont.TIMES, 12);
		// Define the cell format
		FORMATS[1] = new WritableCellFormat(times);
		// Lets automatically wrap the cells
		try {
			FORMATS[1].setWrap(true);
		} catch (WriteException e) {
			e.printStackTrace();
		}

		// Create create a bold font with no underlines
		WritableFont timesBold = new WritableFont(
				WritableFont.TIMES, 12, WritableFont.BOLD, false,
				UnderlineStyle.NO_UNDERLINE);
		FORMATS[0] = new WritableCellFormat(timesBold);
		try {
			FORMATS[0].setAlignment(Alignment.CENTRE);
		} catch (WriteException e1) {
			e1.printStackTrace();
		}
		// Set cell wrapping to false
		try {
			FORMATS[0].setWrap(false);
		} catch (WriteException e) {
			e.printStackTrace();
		}
	}

	protected void addNumber(int column, int row,
			Integer integer, WritableCellFormat f) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, integer, f);
		excelSheet.addCell(number);
	}

	protected void addLabel(int column, int row, String s, WritableCellFormat f)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, f);
		excelSheet.addCell(label);
	}
	
	/**
	 * Writes the given object's <code>String</code> representation to the indicated row and column
	 * @param col - the column to write to
	 * @param row - the row to write to
	 * @param o - the object who's <code>String</code> representation will be written
	 */

	public void write(int col, int row, Object o){
		try {
			addLabel(col, row, o.toString(), FORMATS[1]);
			workbook.write();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the given <code>Integer</code> to the designated row and column
	 * @param col - the zero-based column to write
	 * @param row - the zero-based row to write
	 * @param num - the <code>Integer</code> to be written
	 * @param f - the <code>WritableCellFormat</code> to use when writing
	 */

	public void writeNumber(int col, int row, Integer num, WritableCellFormat f){
		try {
			addNumber(col, row, num, f);
			workbook.write();
		} catch (WriteException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes the given <code>String</code> to the designated row and column
	 * @param col - the zero-based column to write
	 * @param row - the zero-based row to write
	 * @param num - the <code>String</code> object to be written
	 * @param f - the <code>WritableCellFormat</code> to use when writing
	 */

	public void writeLabel(int col, int row, String label, WritableCellFormat f){
		try {
			addLabel(col, row, label, f);
			workbook.write();
		} catch (WriteException e) {
			e.printStackTrace();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the file path for the exported spreadsheet
	 * @return the absolute file path where the spreadsheet can be found
	 */
	
	public String getFilePath(){
		return file.getAbsolutePath();
	}

	public void close(){
		try {
			workbook.close();
		} catch (WriteException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Opens the workbook for editing
	 */

	public void open(){
		try {
			Workbook w = Workbook.getWorkbook(file, wbSettings);
			workbook = Workbook.createWorkbook(file, w, wbSettings);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JXLException e){
			e.printStackTrace();
		}
	}

//	public static void main(String[] args) throws Exception {
//		boolean main = true;
//		if(main){
//			String path = "lars.xls";
//			Exporter test = new Exporter(path);
//			//test.setOutputFile(path);
//			//test.write();
//			test.writeLabel(0, 0, "LABELABELABELABEL");
//			test.close();
//			test.open();
//			test.writeLabel(3, 3, "Hello");
//			test.close();
//			//test.addCaption(10, 10, "Hello");
//			System.out.println("Please check the result file under " + path);
//			System.out.println("path = " + test.path);
//		}
//	}
}
