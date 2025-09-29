package com.lgcns.svcp.prod.util.excel;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.MethodInvoker;

import com.lgcns.svcp.prod.exception.BusinessException;
import com.lgcns.svcp.prod.util.FileUtil;
import com.lgcns.svcp.prod.util.StringUtilCustom;

import jakarta.servlet.http.Part;

public class ExcelReader {

	private Workbook workBook;

	public static final String XLS = "xls";
	public static final String XLSX = "xlsx";

	public ExcelReader() {
	}

	public ExcelReader(Part filePart) {
		try {
			String type = FileUtil.getExtension(filePart.getSubmittedFileName());
			if (XLS.equalsIgnoreCase(type)) {
				this.workBook = new HSSFWorkbook(filePart.getInputStream());
			} else if (XLSX.equalsIgnoreCase(type)) {
				this.workBook = new XSSFWorkbook(filePart.getInputStream());
			}
		} catch (IOException e) {
			throw new BusinessException("Read file fail!");
		}
	}

	public ExcelReader(InputStream fileInputStream, String type) {
		try {
			if (XLS.equalsIgnoreCase(type)) {
				this.workBook = new HSSFWorkbook(fileInputStream);
			} else if (XLSX.equalsIgnoreCase(type)) {
				this.workBook = new XSSFWorkbook(fileInputStream);
			}
		} catch (IOException e) {
			throw new RuntimeException("Error: "+e.getMessage());
		}
	}

	public Workbook getWorkBook() {
		return workBook;
	}

	public <T> List<T> getList(Class<T> dataClass, String[] fieldNames, String sheetName) {
		Sheet sheet = workBook.getSheet(sheetName);
		return readDataList(dataClass, sheet, fieldNames);
	}

	protected <T> List<T> readDataList(Class<T> dataClass, Sheet sheet, String[] fieldNames) {
		List<T> objList = new ArrayList<T>();
		Map<String, Class<?>> cacheFieldMap = getFieldSet(dataClass);

		int rows = sheet.getPhysicalNumberOfRows();
		if (rows == 0) {
			return objList;
		}

		List<String> fieldNameList = (fieldNames == null || fieldNames.length == 0) ? readFieldNames(sheet.getRow(0))
				: Arrays.asList(fieldNames);

		MethodInvoker[] invoker = null;
		for (int rowIndex = 1; rowIndex < rows; rowIndex++) {
			Row row = sheet.getRow(rowIndex);
			T object = getInstance(dataClass);

			if (row == null) {
				objList.add(object);
				continue;
			}

			for (int cellIndex = 0, length = fieldNameList.size(); cellIndex < length; cellIndex++) {
				Cell cell = row.getCell(cellIndex);

				if (cell != null) {
					String fieldName = fieldNameList.get(cellIndex);

					if (cacheFieldMap.containsKey(fieldName)) {
						Object value = readValue(cell, cacheFieldMap.get(fieldName));

						try {
							if (invoker == null) {
								invoker = new MethodInvoker[length];
							}
							if (invoker[cellIndex] == null) {
								invoker[cellIndex] = new MethodInvoker();
								invoker[cellIndex]
										.setTargetMethod("set" + StringUtilCustom.toUpperCaseFirstLetter(fieldName));
								invoker[cellIndex].setTargetObject(object);
								invoker[cellIndex].setArguments(value);
								invoker[cellIndex].prepare();
								invoker[cellIndex].invoke();
							} else {
								invoker[cellIndex].setTargetObject(object);
								invoker[cellIndex].setArguments(value);
								invoker[cellIndex].invoke();
							}
						} catch (NoSuchMethodException e) {
							throw new RuntimeException(
									"Excel cell type is different from VO property type.(" + fieldName + ")", e);
						} catch (IllegalArgumentException e) {
							throw new RuntimeException(
									"Excel cell type is different from VO property type.(" + fieldName + ")", e);
						} catch (Exception e) {
							throw new RuntimeException("Exception while converting excel to " + dataClass, e);
						}
					}
				}
			}

			objList.add(object);
		}

		return objList;
	}

	protected Map<String, Class<?>> getFieldSet(Class<?> dataClass) {
		Map<String, Class<?>> cacheFieldMap = new HashMap<String, Class<?>>();
		Field[] fields = dataClass.getDeclaredFields();
		for (Field field : fields) {
			if (!field.isSynthetic()) {
				cacheFieldMap.put(field.getName(), field.getType());
			}
		}
		return cacheFieldMap;
	}

	protected Object readValue(Cell cell, Class<?> type) {
		Object value = null;

		switch (cell.getCellType()) {
		case FORMULA:
			value = cell.getCellFormula();
			break;
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				value = getDateValue(cell, type);
			} else if (type.isAssignableFrom(String.class)) {
				DataFormatter dataFormatter = new DataFormatter();
				value = dataFormatter.formatCellValue(cell);
			} else {
				double valueDouble = cell.getNumericCellValue();
				if (type == int.class || type == Integer.class) {
					value = (int) valueDouble;
				} else if (type == float.class || type == Float.class) {
					value = (float) valueDouble;
				} else {
					value = valueDouble;
				}
			}
			break;
		case BOOLEAN:
			value = cell.getBooleanCellValue();
			break;
		case STRING:
			value = cell.getStringCellValue();
			break;
		case BLANK:
			if (type == String.class) {
				value = "";
			}
			break;
		case ERROR:
			value = Byte.valueOf(cell.getErrorCellValue());
			break;
		default:
			if (type == String.class) {
				value = cell.getStringCellValue();
			} else {
				value = "";
			}
		}

		return value;
	}

	protected Object getDateValue(Cell cell, Class<?> type) {

		if (type == Timestamp.class) {
			return new Timestamp(cell.getDateCellValue().getTime());
		}
		if (type == Date.class) {
			return cell.getDateCellValue();
		}
		if (type == LocalDateTime.class) {
			return cell.getLocalDateTimeCellValue();
		}
		if (type == LocalDate.class) {
			return cell.getLocalDateTimeCellValue().toLocalDate();
		} else if (type == Calendar.class) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(cell.getDateCellValue());
			return calendar;
		}

		return cell.getDateCellValue();
	}

	private <T> T getInstance(Class<?> dataClass) {
		T object = null;
		try {
			object = (T) dataClass.getDeclaredConstructor().newInstance();
		} catch (InstantiationException e) {
			throw new IllegalStateException("The " + dataClass.getName() + " class object cannot be instantiated.", e);
		} catch (IllegalAccessException e) {
			throw new IllegalStateException("The currently executing method does not have access to the constructor of "
					+ dataClass.getName() + ".", e);
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException("The illegal argument to the constructor of " + dataClass.getName() + ".",
					e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(
					"The invocation target exception to the constructor of " + dataClass.getName() + ".", e);
		} catch (NoSuchMethodException e) {
			throw new IllegalStateException(
					"The no such method exception to the constructor of " + dataClass.getName() + ".", e);
		} catch (SecurityException e) {
			throw new IllegalStateException("The security exception to the constructor of " + dataClass.getName() + ".",
					e);
		}
		return object;
	}

	protected List<String> readFieldNames(Row row) {
		List<String> fieldNameList = new ArrayList<String>();

		for (Cell cell : row) {

			if (cell.getCellType() != CellType.STRING) {
				throw new RuntimeException("First column in excel should be String type.");
			}
			String firstColumn = cell.getStringCellValue();
			if (firstColumn != null && !"".equals(firstColumn)) {
				fieldNameList.add(firstColumn);
			}
		}
		return fieldNameList;
	}
}
