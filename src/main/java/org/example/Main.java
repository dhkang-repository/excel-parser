package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.LinkedHashMap;
import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Autowired
    BaseDataRepository baseDataRepository;
    @Autowired
    ExcelCompareService excelCompareService;
    @Autowired
    LocalFileTransfer localFileTransfer;
    @PostConstruct
    public void compare() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        XSSFSheet sheet = excelCompareService.getSheet("src/main/resources/excel.xlsx", 0);

        LinkedHashMap<Integer, String> headerKeyNumberMap
                = excelCompareService.getHeaderMap(sheet);

        LinkedHashMap<String, LinkedHashMap<String, Object>> parsingResultMap
                = excelCompareService.getParsingResultMap(sheet, headerKeyNumberMap);

        List<BaseData> excelAllData = objectMapper.convertValue(parsingResultMap.values().toArray(),
                new TypeReference<List<BaseData>>() {});

        List<BaseData> dbAllData = baseDataRepository.findAll();

        List<BaseData> compareExcelList = excelCompareService.compare(excelAllData, dbAllData);

        localFileTransfer.save("src/main/resources", "excel", objectMapper.writeValueAsString(excelAllData));
        localFileTransfer.save("src/main/resources", "db", objectMapper.writeValueAsString(dbAllData));
        localFileTransfer.save("src/main/resources", "compare", objectMapper.writeValueAsString(compareExcelList));
    }
}