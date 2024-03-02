package org.example;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ExcelCompareWithDB {
    static public
        Map<String, String> convertKey = Map.of(
                "식별자", "type",
                "순번", "num",
                "명칭", "name",
                "거리","distance",
                "시간","time",
                "속도", "speed",
                "기본", "basic",
                "계산", "calc"
        );

    public XSSFSheet getSheet(String path, Integer index) throws Exception {
        Path source = Path.of(path).toAbsolutePath();
        FileInputStream fileInputStream = new FileInputStream(source.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(index);
        return sheet;
    }

    public Map<Integer, String> getHeaderMap(XSSFSheet sheet){
        Map<Integer, String> keyNumber = new HashMap<>();
        XSSFRow headerRow = sheet.getRow(0);
        int cellNum = headerRow.getPhysicalNumberOfCells();
        for(int i = 0; i < cellNum; i++) {
            String headerName = headerRow.getCell(i).toString();
            if (convertKey.containsKey(headerName)) {
                keyNumber.put(i, headerName);
            }
        }
        return keyNumber;
    }

    public Map<String, HashMap<String, String>> getParsingResultMap(XSSFSheet sheet, Map<Integer, String> headerKeyNumberMap) {
        int rows = sheet.getPhysicalNumberOfRows();
        Map<String, HashMap<String, String>> result = new HashMap<>();
        for (int r = 1; r < rows; r++) {
            HashMap<String, String> rowData = new HashMap<>();
            for(String key : ExcelCompareWithDB.convertKey.keySet()) {
                rowData.put(key, "");
            }
            XSSFRow row = sheet.getRow(r); // 0 ~ rows

            if (row != null) { // 행이 비어있지 않을 때
                int cells = row.getPhysicalNumberOfCells(); // 열의 수

                for (int c = 0; c < cells; c++) {
                    XSSFCell cell = row.getCell(c); // 0 ~ cell
                    if(cell!=null) {
                        String value = cell.toString();
                        rowData.put(headerKeyNumberMap.get(c), value);
                        System.out.println(r + "번 행 : " + c + "번 열 값은: " + value);
                    }
                }

            }

            result.put(rowData.get("식별자"), rowData);
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        ExcelCompareWithDB excelCompareWithDB = new ExcelCompareWithDB();

        XSSFSheet sheet = excelCompareWithDB.getSheet("src/main/resources/excel.xlsx", 0);

        Map<Integer, String> headerKeyNumberMap
                = excelCompareWithDB.getHeaderMap(sheet);

        Map<String, HashMap<String, String>> parsingResultMap
                = excelCompareWithDB.getParsingResultMap(sheet, headerKeyNumberMap);

        System.out.println(parsingResultMap);
    }
}
