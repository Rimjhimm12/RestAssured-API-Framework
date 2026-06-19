package api.utilities;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelUtil {

    private static final String TEST_DATA_SHEET_PATH = "./src/test/resources/UserData.xlsx";


    public static Object[][] getTestData(String sheetName) {
        FileInputStream ip = null;
        Workbook workbook;
        try {
            ip = new FileInputStream(TEST_DATA_SHEET_PATH);
            workbook = WorkbookFactory.create(ip);
            Sheet sheet = workbook.getSheet(sheetName.trim());
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found in " + TEST_DATA_SHEET_PATH
                        + ". Available sheets: " + getSheetNames(workbook));
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return new Object[0][0];
            }

            int rowCount = sheet.getLastRowNum();
            int columnCount = headerRow.getLastCellNum();
            Object[][] data = new Object[rowCount][columnCount];
            DataFormatter formatter = new DataFormatter();

            for (int i = 0; i < rowCount; i++) {
                Row currentRow = sheet.getRow(i + 1);
                for (int j = 0; j < columnCount; j++) {
                    data[i][j] = currentRow == null ? "" : formatter.formatCellValue(currentRow.getCell(j));
                }
            }

            return data;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Test data file not found: " + TEST_DATA_SHEET_PATH, e);
        } catch (InvalidFormatException e){
            throw new RuntimeException("Invalid Excel format for test data file: " + TEST_DATA_SHEET_PATH, e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read test data file: " + TEST_DATA_SHEET_PATH, e);
        } finally {
            closeInputStream(ip);
        }
    }

    public static Object[][] getColumnData(String sheetName, String columnName) {
        Object[][] allData = getTestData(sheetName);
        FileInputStream ip = null;
        try {
            ip = new FileInputStream(TEST_DATA_SHEET_PATH);
            Workbook workbook = WorkbookFactory.create(ip);
            Sheet sheet = workbook.getSheet(sheetName.trim());
            Row headerRow = sheet.getRow(0);
            int columnIndex = -1;
            for (int i = 0; i < headerRow.getLastCellNum(); i++) {
                if (headerRow.getCell(i).getStringCellValue().equalsIgnoreCase(columnName.trim())) {
                    columnIndex = i;
                    break;
                }
            }
            if (columnIndex == -1) {
                throw new IllegalArgumentException("Column '" + columnName + "' not found in sheet '" + sheetName + "'");
            }
            Object[][] columnData = new Object[allData.length][1];
            for (int i = 0; i < allData.length; i++) {
                columnData[i][0] = allData[i][columnIndex];
            }
            return columnData;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Test data file not found: " + TEST_DATA_SHEET_PATH, e);
        } catch (InvalidFormatException e) {
            throw new RuntimeException("Invalid Excel format: " + TEST_DATA_SHEET_PATH, e);
        } catch (IOException e) {
            throw new RuntimeException("Unable to read test data file: " + TEST_DATA_SHEET_PATH, e);
        } finally {
            closeInputStream(ip);
        }
    }

    private static List<String> getSheetNames(Workbook workbook) {
        List<String> sheetNames = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            sheetNames.add(workbook.getSheetName(i));
        }
        return sheetNames;
    }

    private static void closeInputStream(FileInputStream inputStream) {
        if (inputStream == null) {
            return;
        }

        try {
            inputStream.close();
        } catch (IOException ignored) {
        }
    }
}
