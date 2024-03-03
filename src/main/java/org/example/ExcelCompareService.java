package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;

@Service
public class ExcelCompareService {
    static public
        LinkedHashMap<String, String> convertKey = new LinkedHashMap<>() {{
                put("식별자", "type");
                put("type", "식별자");
                put("순번", "num");
                put("num", "순번");
                put("명칭", "name");
                put("name", "명칭");
                put("거리","distance");
                put("distance","거리");
                put("시간","time");
                put("time","시간");
                put("속도", "speed");
                put("speed", "속도");
                put("기본", "basic");
                put("basic", "기본");
                put("계산", "calc");
                put("calc", "계산");
        }};

    public XSSFSheet getSheet(String path, Integer index) throws Exception {
        Path source = Path.of(path).toAbsolutePath();
        FileInputStream fileInputStream = new FileInputStream(source.toString());
        XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
        XSSFSheet sheet = workbook.getSheetAt(index);
        return sheet;
    }

    public LinkedHashMap<Integer, String> getHeaderMap(XSSFSheet sheet){
        LinkedHashMap<Integer, String> keyNumber = new LinkedHashMap<>();
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

    public LinkedHashMap<String, LinkedHashMap<String, Object>> getParsingResultMap(XSSFSheet sheet,
                                                                                    LinkedHashMap<Integer, String> headerKeyNumberMap) {
        int rowsSize = sheet.getPhysicalNumberOfRows();
        LinkedHashMap<String, LinkedHashMap<String, Object>> result = new LinkedHashMap<>();
        for (int r = 1; r < rowsSize; r++) {
            LinkedHashMap<String, Object> rowData = new LinkedHashMap<>();
            for(String key : ExcelCompareService.convertKey.keySet()) {
                rowData.put(key, "");
            }
            XSSFRow row = sheet.getRow(r); // 0 ~ rows

            if (row != null) { // 행이 비어있지 않을 때
                int cells = row.getPhysicalNumberOfCells(); // 열의 수

                for (int c = 0; c <= cells; c++) {
                    XSSFCell cell = row.getCell(c); // 0 ~ cell
                    if(cell!=null) {
                        String value = cell.toString();
                        System.out.println(r + "번 행 : " + c + "번 열 값은: " + value);
                        switch (cell.getCellType()) {
                            case FORMULA:
                                rowData.put(headerKeyNumberMap.get(c), cell.getCellFormula());
                                break;
                            case NUMERIC:
                                rowData.put(headerKeyNumberMap.get(c), cell.getNumericCellValue());
                                break;
                            case STRING:
                                rowData.put(headerKeyNumberMap.get(c), cell.getStringCellValue());
                                break;
                            case BLANK:
                                rowData.put(headerKeyNumberMap.get(c), cell.getBooleanCellValue());
                                break;
                            case ERROR:
                                rowData.put(headerKeyNumberMap.get(c), cell.getErrorCellValue());
                                break;
                        }
                    }
                }

            }

            result.put(rowData.get("식별자").toString(), rowData);
        }

        return result;
    }


    public XSSFWorkbook makeExcel(Map<Integer, String> headerKeyNumberMap,
                                  LinkedHashMap<String, LinkedHashMap<String, String>> parsingResultMap) {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("parsing");
        Row titleRow = sheet.createRow(0);
        Iterator<Map.Entry<Integer, String>> iteratorValues = headerKeyNumberMap.entrySet().iterator();
        for(int i = 0; i < headerKeyNumberMap.size(); i++){
            Map.Entry<Integer, String> next = iteratorValues.next();
            String key = next.getValue();

            Cell cell = titleRow.createCell(i);
            cell.setCellValue(key);
        }

        Iterator<Map.Entry<String, LinkedHashMap<String, String>>> iterator = parsingResultMap.entrySet().iterator();
        for(int i = 0; i < parsingResultMap.size(); i++) {
            Row excelRow = sheet.createRow(1+i);

            Map.Entry<String, LinkedHashMap<String, String>> next = iterator.next();

            HashMap<String, String> row = next.getValue();
            Iterator<Map.Entry<String, String>> cells = row.entrySet().iterator();

            for(int j = 0; j < row.size(); j++) {
                Map.Entry<String, String> cell = cells.next();
                String value = cell.getValue();

                Cell dataCell = excelRow.createCell(j);
                dataCell.setCellValue(value);
            }

        }


        return workbook;
    }

    public void printWorkbook(Workbook workbook) throws Exception {
        XSSFSheet sheet = (XSSFSheet) workbook.getSheetAt(0); // 엑셀 파일의 첫번째 (0) 시트지
        int rows = sheet.getPhysicalNumberOfRows(); // 행의 수

        for (int r = 0; r < rows; r++) {
            XSSFRow row = sheet.getRow(r); // 0 ~ rows

            if (row != null) { // 행이 비어있지 않을 때
                int cells = row.getPhysicalNumberOfCells(); // 열의 수

                for (int c = 0; c < cells; c++) {
                    XSSFCell cell = row.getCell(c); // 0 ~ cell
                    String value = "";

                    if (cell == null) { // r열 c행의 cell이 비어있을 때
                        continue;
                    } else { // 타입별로 내용 읽기
                        switch (cell.getCellType()) {
                            case FORMULA:
                                value = cell.getCellFormula();
                                break;
                            case NUMERIC:
                                value = cell.getNumericCellValue() + "";
                                break;
                            case STRING:
                                value = cell.getStringCellValue() + "";
                                break;
                            case BLANK:
                                value = cell.getBooleanCellValue() + "";
                                break;
                            case ERROR:
                                value = cell.getErrorCellValue() + "";
                                break;
                        }

                    }

                    System.out.println(r + "번 행 : " + c + "번 열 값은: " + value);

                }
            }
        }
    }

    public List<BaseData> compare(List<BaseData> convertData, List<BaseData> dbAllData) {
        ArrayList<BaseData> result = new ArrayList<>();
        for(BaseData excel : convertData) {
            String type = excel.getType();
            BaseData baseData = dbAllData.stream().filter(data -> Objects.nonNull(data.getType()) && data.getType().equals(type)).findFirst().orElse(null);
            if(Objects.nonNull(baseData)) {
                BaseData compare = excel.compareData(baseData);
                if(compare.isChange) result.add(compare);
            }
        }
        return result;
    }
}
