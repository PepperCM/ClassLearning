package FileSolve;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.File;

public class ReadExcel {

	private static File res;

	public static void readXml(String filename) {
		boolean isXlsx = false;
		if (filename.endsWith(".xlsx"))
			isXlsx = true;
		else if (filename.endsWith(".xls")) {

		} else {
			
			return;
		}
		if (filename.indexOf("报告") == -1)
			return;
		res.setWritable(true);
		try {
			InputStream input = new FileInputStream(filename);
			InputStream inputres = new FileInputStream(res);
			Workbook wb = null;
			if (isXlsx)
				wb = new XSSFWorkbook(input);
			else
				wb = new HSSFWorkbook(input);

			Workbook wbres = new HSSFWorkbook(inputres);
			Sheet sheetres = wbres.getSheetAt(0);
			Iterator<Row> rowsres = sheetres.rowIterator();
			Sheet sheet = wb.getSheetAt(0);
			Iterator<Row> rows = sheet.rowIterator();
			while (rows.hasNext()) {
				Row row = rows.next();
				Row rowres = rowsres.next();
				Iterator<Cell> cells = row.cellIterator();
				Iterator<Cell> cellsres = rowres.cellIterator();
				while (cells.hasNext()) {
					Cell cell = cells.next();
					Cell cellres = cellsres.next();

					// System.out.println( "cell x= " + row.getRowNum()+"
					// y="+cell.getColumnIndex());
					// System.out.println("cellres x = "
					// +cellres.getRowIndex()+" y="+cellres.getColumnIndex());

					switch (cell.getCellType()) { // 根据cell中的类型来输出数据
					case HSSFCell.CELL_TYPE_NUMERIC:
						System.out.println(cell.getNumericCellValue());
						cellres.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cellres.setCellValue(cell.getNumericCellValue());
						break;
					case HSSFCell.CELL_TYPE_STRING:
//						System.out.println(cell.getStringCellValue());
						break;
					case HSSFCell.CELL_TYPE_BOOLEAN:
//						System.out.println(cell.getBooleanCellValue());
//						cellres.setCellValue(cell.getBooleanCellValue());
						break;
					case HSSFCell.CELL_TYPE_FORMULA:
//						System.out.println(cell.getCellFormula());
//						cellres.setCellValue(cell.getCellFormula());
						break;
					default:
						break;
					}
				}
			}
			FileOutputStream fos = new FileOutputStream(res);
			wbres.write(fos);
			wb.close();
			fos.close();
			inputres.close();
			wbres.close();
			input.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}

	public static File[] findFile(String path) {
		File file = new File(path);
		File[] files = file.listFiles();
		return files;
	}

	public static void run(String path) {
		System.out.println("sdfe");
		String respath =  ChoosePath.getPath();
		res = new File(respath + "\\Res.xls");
		if (!res.exists())
			try {
				createExcel(res);
			} catch (Exception e) {
				e.printStackTrace();
			}
		File[] files = findFile(path);
		for (File item : files) {
			readXml(item.getAbsolutePath());
		}
		return;
	}

	private static void createExcel(File res) throws Exception {
		
		String modelPath = ChoosePath.getPath();
		File ex = new File(modelPath);
		FileInputStream fis = new FileInputStream(ex);
		FileOutputStream fos = new FileOutputStream(res);
		int k = 0;
		while ((k = fis.read()) != -1)
			fos.write(k);
		fis.close();
		fos.close();
	}

	public static void main(String[] args) throws Exception {
		// File[] files = findFile("C:\\Users\\Pepper\\Desktop\\sdf");
		// for(File i :files)
		// System.out.println(i.getName());
		res = new File("C:\\Users\\Pepper\\Desktop\\233\\Res.xls");
		createExcel(res);
		run("C:\\Users\\Pepper\\Desktop\\233");
	}
}
