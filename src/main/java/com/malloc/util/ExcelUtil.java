package com.malloc.util;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tuyh3(tuyh3 @ asiainfo.com)
 * @desc	excel工具类
 * @date 2021/1/29 17:31
 * @Version
 */
public final class ExcelUtil {

	private static Logger logger = Logger.getLogger(ExcelUtil.class);
	private static String CLASSNAME = ExcelUtil.class.getName();
	private static final String XLS = "xls";
	private static final String XLSX = "xlsx";
	private static final String POINT_XLS = ".xls";
	private static final String POINT_XLSX = ".xlsx";
	private static final String POINT = ".";

	private ExcelUtil(){}
	/**
	 * 读取excel
	 *
	 * @param filePath
	 * @return
	 */
	public static Workbook readExcel(String filePath) {
		logger.debug(">>>>>" + CLASSNAME + ".readExcel().....");
		logger.debug("filePath:" + filePath);
		Workbook workbook = null;
		try {
			if(StringUtil.isTrimNull(filePath) || !(new File(filePath)).exists()) {
				return null;
			}

			if(FileUtil.isFileNameEndsWith(filePath, POINT_XLS) || FileUtil.isFileNameEndsWith(filePath, POINT_XLSX)) {
				return null;
			}

			String extString = filePath.substring(filePath.lastIndexOf(POINT));
			InputStream is = new FileInputStream(filePath);
			if (POINT_XLS.equals(extString)) {
				workbook = new HSSFWorkbook(is);
			} else if (POINT_XLSX.equals(extString)) {
				workbook = new XSSFWorkbook(is);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".readExcel().");
			return workbook;
		}
	}

	/**
	 * 通过文件路径，获取xls、xlsx的第一个sheet内容
	 *
	 * @param filePath
	 * @return
	 */
	public static List<List<String>> getExcelContent(String filePath) {
		logger.debug(">>>>>" + CLASSNAME + ".getExcelContent().....");
		logger.debug("filePath:" + filePath);

		List<List<String>> list = new ArrayList<List<String>>();
		try {
			if(StringUtil.isTrimNull(filePath)) {
				return null;
			}

			Workbook workbook = readExcel(filePath);
			if(null == workbook) {
				return null;
			}

			list = getExcelContent(workbook);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".getExcelContent().");
			return list;
		}
	}

	/**
	 * 通过 excel，获取 xls、xlsx 的第一个 sheet 内容
	 *
	 * @param workbook
	 * @return
	 */
	public static List<List<String>> getExcelContent(Workbook workbook) {
		logger.debug(">>>>>" + CLASSNAME + ".getExcelContent().....");
		logger.debug("workbook:" + workbook);

		List<List<String>> list = new ArrayList<List<String>>();
		try {
			if (null == workbook) {
				return null;
			}

			Sheet sheet = workbook.getSheetAt(0);
			if(null == sheet) {
				return null;
			}

			for (int i = 0; i <= sheet.getLastRowNum(); i++) {
				List<String> list2 = getCellRow(sheet, i);
				if(null != list2 && !list2.isEmpty()) {
					list.add(list2);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".getExcelContent().");
			return list;
		}

	}

	/**
	 * 获取 excel的 sheet 中第 iRowIndex 索引行第 iColumIndex 索引列的内容
	 *
	 * @param sheet
	 * @param iRowIndex
	 * @param iColumIndex
	 * @return
	 */
	public static String getCellStr(Sheet sheet, int iRowIndex, int iColumIndex) {
		logger.debug(">>>>>" + CLASSNAME + ".getCell().....");
		logger.debug("sheet:" + sheet);
		logger.debug("iRowIndex:" + iRowIndex);
		logger.debug("iColumIndex:" + iColumIndex);

		String stringForRow = "";
		try {
			if(null == sheet) {
				return stringForRow;
			}

			List<String> list = getCellRow(sheet, iRowIndex);
			if(null == list || list.isEmpty()) {
				return stringForRow;
			}

			stringForRow = list.get(iColumIndex);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".getCell().");
			return stringForRow;
		}
	}

	/**
	 * 获取excel的sheet的row中第iColumIndex索引列的内容
	 *
	 * @param row
	 * @param iColumIndex
	 * @return
	 */
	public static String getStringForRow(Row row, int iColumIndex) {
		logger.debug(">>>>>" + CLASSNAME + ".getStringForRow().....");
		logger.debug("row:" + row);
		logger.debug("iColumIndex:" + iColumIndex);

		String str = "";
		try {
			if (row == null) {
				return null;
			}

			Cell cell = row.getCell((short) iColumIndex);
			if (cell == null) {
				return str;
			}

			str = cell.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".getStringForRow().");
			return str;
		}
	}

	/**
	 * 获取excel中第iSheet索引sheet第iRowIndex索引行第iColumIndex索引列的内容
	 *
	 * @param workbook
	 * @param iSheet
	 * @param iRowIndex
	 * @param iColumIndex
	 * @return
	 */
	public static String getCellStr(Workbook workbook, int iSheet, int iRowIndex, int iColumIndex) {
		if(workbook == null) {
			return null;
		}
		return getCellStr(workbook.getSheetAt(iSheet), iRowIndex, iColumIndex);
	}

	/**
	 * 获取excel的sheet中iRowIndex行数据
	 *
	 * @param sheet
	 * @param iRowIndex
	 * @return
	 */
	public static List<String> getCellRow(Sheet sheet, int iRowIndex) {
		logger.debug(">>>>>" + CLASSNAME + ".getCellRow().....");
		logger.debug("sheet:" + sheet);
		logger.debug("iRowIndex:" + iRowIndex);

		List<String> list = new ArrayList<String>();

		try {
			if(sheet == null) {
				return null;
			}

			Row row = sheet.getRow(iRowIndex);
			if(row == null) {
				return null;
			}

			String str = "";
			for (int i = 0; i < row.getLastCellNum(); i++) {
				str = getStringForRow(row, i).trim();
				if(str != null) {
					list.add(str);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".getCellRow().");
			return list;
		}
	}

	/**
	 * 获取excel中第iSheet索引sheet中iRowIndex行数据
	 *
	 * @param workbook
	 * @param iSheet
	 * @param iRowIndex
	 * @return
	 */
	public static List<String> getCellRow(Workbook workbook, int iSheet, int iRowIndex) {
		if(workbook == null) {
			return null;
		}
		return getCellRow(workbook.getSheetAt(iSheet), iRowIndex);
	}

	/**
	 * 获取excel的sheet中iColumIndex列数据
	 *
	 * @param sheet
	 * @param iColumIndex
	 * @return
	 */
	public static List<String> getCellCol(Sheet sheet, int iColumIndex) {
		logger.debug(">>>>>" + CLASSNAME + ".getCellCol().....");
		logger.debug("sheet:" + sheet);
		logger.debug("iColumIndex:" + iColumIndex);

		List<String> list = new ArrayList<String>();

		try {
			if(sheet == null) {
				return null;
			}

			String str = "";
			for(int i = 0; i <= sheet.getLastRowNum(); i++) {
				str = getCellStr(sheet, i, iColumIndex);
				list.add(str);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".getCellCol().");
			return list;
		}
	}

	/**
	 * 获取excel中第iSheet索引sheet中iColumIndex列数据
	 *
	 * @param workbook
	 * @param iSheet
	 * @param iColumIndex
	 * @return
	 */
	public static List<String> getCellCol(Workbook workbook, int iSheet, int iColumIndex) {
		if(workbook == null) {
			return null;
		}
		return getCellCol(workbook.getSheetAt(iSheet), iColumIndex);
	}

	/**
	 * 设置excel中第iSheet索引sheet中第iRowIndex索引行第iColumIndex索引列的内容为strValue
	 *
	 * @param sheet
	 * @param iRowIndex
	 * @param iColumIndex
	 * @param strValue
	 * @return
	 */
	public static Sheet setSheetCellValue(Sheet sheet, int iRowIndex, int iColumIndex, String strValue) {
		logger.debug(">>>>>" + CLASSNAME + ".setSheetCellValue()...");
		logger.debug("sheet: " + sheet);
		logger.debug("iRowIndex: " + iRowIndex);
		logger.debug("iColumIndex: " + iColumIndex);
		logger.debug("strValue: " + strValue);

		try {
			if(sheet == null) {
				return null;
			}

			Row row = sheet.getRow(iRowIndex);
			row = setRowColumCellValue(row, iColumIndex, strValue);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".setSheetCellValue().");
			return sheet;
		}
	}

	/**
	 * 设置excel的row行中iColumIndex索引列的值为strValue
	 *
	 * @param row
	 * @param iColumIndex
	 * @param strValue
	 * @return
	 */
	public static Row setRowColumCellValue(Row row, int iColumIndex, String strValue) {
		logger.debug(">>>>>" + CLASSNAME + ".setRowColumCellValue()...");
		logger.debug("row: " + row);
		logger.debug("iColumIndex: " + iColumIndex);
		logger.debug("strValue: " + strValue);

		try {
			if(row == null) {
				return row;
			}

			Cell cell = row.getCell((short) iColumIndex);
			if (cell == null) {
				cell = row.createCell((short) iColumIndex);
			}

			if (cell != null) {
				cell.setCellValue(strValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".setRowColumCellValue().");
			return row;
		}
	}

	/**
	 * 设置excel的row行的内容为list
	 *
	 * @param row
	 * @param list
	 * @return
	 */
	public static <T> Row setRowCellValue(Row row, List<T> list) {
		logger.debug(">>>>>" + CLASSNAME + ".setRowCellValue()...");
		logger.debug("row: " + row);
		logger.debug("list: " + list);

		try {
			if(row == null || list == null || list.size() <= 0) {
				return row;
			}

			for(int i = 0; i < list.size(); i++) {
				row = setRowColumCellValue(row, i, list.get(i).toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".setRowCellValue().");
			return row;
		}
	}

	/**
	 * 设置excel的sheet中第iRowIndex索引行的内容为list
	 *
	 * @param sheet
	 * @param iRowIndex
	 * @param list
	 * @return
	 */
	public static <T> Sheet setSheetRowCellValue(Sheet sheet, int iRowIndex, List<T> list) {
		logger.debug(">>>>>" + CLASSNAME + ".setSheetRowCellValue()...");
		logger.debug("sheet: " + sheet);
		logger.debug("iRowIndex: " + iRowIndex);
		logger.debug("list: " + list);

		try {
			if(sheet == null) {
				return sheet;
			}
			Row row = sheet.getRow(iRowIndex);
			if(row == null) {
				row = sheet.createRow(iRowIndex);
			}
			row = setRowCellValue(row, list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".setSheetRowCellValue().");
			return sheet;
		}
	}

	/**
	 * 设置excel的sheet的内容为list
	 *
	 * @param sheet
	 * @param list
	 * @return
	 */
	public static <T> Sheet setSheetValue(Sheet sheet, List<List<T>> list) {
		logger.debug(">>>>>" + CLASSNAME + ".setSheetValue()...");
		logger.debug("sheet: " + sheet);
		logger.debug("list: " + list);

		try {
			if(sheet == null || list == null || list.size() <= 0) {
				return sheet;
			}

			for(int i = 0; i < list.size(); i++) {
				sheet = setSheetRowCellValue(sheet, i, list.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".setSheetValue().");
			return sheet;
		}
	}

	/**
	 * 设置excel的workbook中iSheet索引的Sheet的内容为list，如果Sheet对象不存在，则设置失败
	 *
	 * @param workbook
	 * @param iSheet
	 * @param list
	 * @return
	 */
	public static Workbook setWorkbookSheetValue(Workbook workbook, int iSheet, List<List<String>> list) {
		logger.debug(">>>>>" + CLASSNAME + ".setWorkbookSheetValue()...");
		logger.debug("workbook: " + workbook);
		logger.debug("iSheet: " + iSheet);
		logger.debug("list: " + list);

		try {
			if(workbook == null || list == null || list.size() <= 0) {
				return workbook;
			}

			Sheet sheet = workbook.getSheetAt(iSheet);
			sheet = setSheetValue(sheet, list);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".setWorkbookSheetValue().");
			return workbook;
		}
	}

	/**
	 * 新建fileName文件的excel，然后把list内容写进去。.xls或者是.xlsx
	 *
	 * @param fileName
	 * @param list
	 * @return
	 */
	public static <T> Boolean createExcelFile(String fileName, List<List<T>> list) {
		logger.debug(">>>>>" + CLASSNAME + ".createExcelFile()...");
		logger.debug("fileName: " + fileName);
		logger.debug("list: " + list);

		FileOutputStream fileOut = null;
		try {
			if(StringUtil.isTrimNull(fileName)) {
				return false;
			}

			File file = new File(fileName);
			if(file.exists()) {
				System.out.println("文件已存在！");
				logger.debug("文件已存在！");
				return false;
			}

			Workbook workbook = null;
			String excelType = null;
			if(FileUtil.isFileNameEndsWith(fileName, ".xls")) {
				excelType = "xls";
				workbook = new HSSFWorkbook();
			} else if(FileUtil.isFileNameEndsWith(fileName, ".xlsx")) {
				excelType = "xlsx";
				workbook = new XSSFWorkbook();
			} else {
				return false;
			}

			fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);
			fileOut.close();

			FileInputStream is = new FileInputStream(file);
			if("xls".equals(excelType)) {
				POIFSFileSystem fs = new POIFSFileSystem(is);
				workbook = new HSSFWorkbook(fs);
			} else if("xlsx".equals(excelType)) {
				workbook = new XSSFWorkbook(is);
			}

			Sheet sheet = workbook.createSheet();
			sheet = setSheetValue(sheet , list);

			fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			logger.debug("<<<<<" + CLASSNAME + ".createExcelFile().");
			return true;
		}
	}

	public static void updateExcel(String strTemplateFile, String strNewFile) {
		logger.debug(">>>>>" + CLASSNAME + ".updateExcel()...");
		logger.debug("strTemplateFile:" + strTemplateFile);
		logger.debug("strNewFile:" + strNewFile);
		try {
			FileInputStream is = new FileInputStream(new File(strTemplateFile));
			POIFSFileSystem fs = new POIFSFileSystem(is);

			HSSFWorkbook wwb = new HSSFWorkbook(fs);

			HSSFSheet sheet = wwb.getSheetAt(0);

			setSheetCellValue(sheet, 6, 3, "333");

			// 利用已经创建的Excel工作薄创建新的可写入的Excel工作薄
			// int rows = ws.getLastRowNum();

			FileOutputStream fileOut = new FileOutputStream(strNewFile);
			wwb.write(fileOut);
			fileOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			logger.debug("<<<<<" + CLASSNAME + ".updateExcel().");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//System.out.println(getExcelContent("D://test.xls"));
		//System.out.println(getCellCol(readExcel("D://test.xls"), 1, 1));
		//setCellValue(readExcel("D://test.xls").getSheetAt(0), 6, 0, "haha");
		//updateExcel("D://test2.xls", "D://test3.xls");

		List<List<String>> list = new ArrayList<List<String>>();
		for(int i = 0; i < 10; i++) {
			List<String> list2 = new ArrayList<String>();
			for(int j = 0; j < 10; j++) {
				list2.add("i = " + i + "j = " + j);
			}
			list.add(list2);
		}
		createExcelFile("e:/test3.xlsx", list);

		String fileName = "e:/test_test/test3.xls";

		System.out.println(FileUtil.isFileNameEndsWith(fileName, ".xls"));
	}

}
