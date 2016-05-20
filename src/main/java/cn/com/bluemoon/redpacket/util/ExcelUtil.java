package cn.com.bluemoon.redpacket.util;

import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtil {
	/**
	 * 从Excel中获取数据，注意：只读取第一个sheet,指定列数，默认数据从第二行开始，第一行为表头
	 * 
	 * @param fileName
	 *            文件名字
	 * @param columnNum
	 *            列数
	 * @return 1=xx 2=xx 创建时间:2015年5月13日 下午12:46:00 dakou
	 */
	public static List<Map<Integer, String>> getDataFromExcel(String fileName,int columnNum) {
		fileName = StringUtils.trim(fileName);
		boolean isE2007 = false; // 判断是否是excel2007格式
		if (fileName.endsWith("xlsx")) {
			isE2007 = true;
		}
		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
		Workbook Workbook = null;
		try {
			if (isE2007) {
				Workbook = new XSSFWorkbook(new FileInputStream(fileName));
			} else {
				Workbook = new HSSFWorkbook(new FileInputStream(fileName));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			Sheet sheet = Workbook.getSheetAt(numSheet);
			if (sheet == null) {
				continue;
			}
			for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) {
					continue;
				}
				Map<Integer, String> cellMap = new HashMap<Integer, String>();
				for (int i = 0; i < columnNum; i++) {
					String cellValue = getStringCellValue(row.getCell(i));
					cellMap.put(i, cellValue);
				}
				list.add(cellMap);
			}
		}
		return list;
	}

	public static List<Map<String, String>> getDataFromExcel(String fileName) {
		fileName = StringUtils.trim(fileName);
		boolean isE2007 = false; // 判断是否是excel2007格式
		if (fileName.endsWith("xlsx")) {
			isE2007 = true;
		}
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		Workbook Workbook = null;
		try {
			// Resource resource = new FileSystemResource(fileName);
			if (isE2007) {
				// Workbook = new XSSFWorkbook(resource.getInputStream());
				Workbook = new XSSFWorkbook(new FileInputStream(fileName));
			} else {
				Workbook = new HSSFWorkbook(new FileInputStream(fileName));
				// Workbook = new HSSFWorkbook(resource.getInputStream());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Workbook.getNumberOfSheets()
		for (int numSheet = 0; numSheet < 1; numSheet++) {
			Sheet sheet = Workbook.getSheetAt(numSheet);
			if (sheet == null) {
				continue;
			}
			Map<Short, String> headerCellMap = new HashMap<Short, String>();
			Integer totalCellNum = null;
			for (Integer rowNum = 0; rowNum <= sheet.getLastRowNum(); rowNum++) {
				Row row = sheet.getRow(rowNum);
				if (row == null) {
					continue;
				}
				if (rowNum.equals(0)) {
					totalCellNum = new Integer(row.getPhysicalNumberOfCells());
					for (short i = row.getFirstCellNum(); i < totalCellNum; i++) {
						String cellValue = getStringCellValue(row.getCell(i));
						cellValue = StringUtils.trim(cellValue);
						headerCellMap.put(i, cellValue);
					}
					continue;
				}
				Map<String, String> cellMap = new HashMap<String, String>();
				for (Short i = row.getFirstCellNum(); i < totalCellNum; i++) {
					String cellValue = getStringCellValue(row.getCell(i));
					String fieldName = headerCellMap.get(i);
					cellMap.put(fieldName, cellValue);
				}
				list.add(cellMap);
			}
		}
		return list;
	}

	private static String getStringCellValue(Cell cell) {// 获取单元格数据内容为字符串类型的数据
		String strCell = "";
		if (cell == null) {
			return "";
		}
		switch (cell.getCellType()) {
			case Cell.CELL_TYPE_FORMULA:
			// cell.getCellFormula();
			try {
				/*
				 * 此处判断使用公式生成的字符串有问题，因为HSSFDateUtil.isCellDateFormatted(cell)
				 * 判断过程中cell
				 * .getNumericCellValue();方法会抛出java.lang.NumberFormatException异常
				 */
				if (HSSFDateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					strCell = (date.getYear() + 1900) + "-"
							+ (date.getMonth() + 1) + "-" + date.getDate();
					break;
				} else {
					/*double ss=cell.getNumericCellValue();
					System.out.println(ss);
					 strCell = String.valueOf(Long.valueOf(ss));*/
					BigDecimal bigDecimal = new BigDecimal(cell.getNumericCellValue());
					strCell = bigDecimal.toString();
					// System.out.println(strCell+"~~"+bigDecimal);
				}
			} catch (IllegalStateException e) {
				strCell = String.valueOf(cell.getRichStringCellValue());
			}
			break;
		case Cell.CELL_TYPE_STRING:
			strCell = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				strCell = cell.getDateCellValue().toString();
				break;
			} else {
				BigDecimal bigDecimal = new BigDecimal(cell.getNumericCellValue());
				strCell = bigDecimal.toString();
				//	BigDecimal bigDecimal = new BigDecimal(cell.getNumericCellValue());
				/*DecimalFormat df = new DecimalFormat("2");
				strCell = df.format(cell.getNumericCellValue());*/
				// 2443452.0~~2443452
				// 1.0000072E7~~10000072
				// System.out.println(strCell+"~~"+bigDecimal);
				// strCell=bigDecimal.toString();
				break;
			}
		case Cell.CELL_TYPE_BOOLEAN:
			strCell = String.valueOf(cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_BLANK:
			strCell = "";
			break;
		default:
			strCell = "";
			break;
		}
		return strCell.trim();
	}
}
