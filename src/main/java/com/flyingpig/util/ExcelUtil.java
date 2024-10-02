package com.flyingpig.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class ExcelUtil {

    public static int getExcelSheetNumber(MultipartFile excelFile) {
        int sheetNumber = 0;
        // 读取 MultipartFile
        try {
            // 使用 EasyExcel 读取流
            ExcelReader excelReader = EasyExcel.read(excelFile.getInputStream()).build();
            // 读取Sheet获取数量
            sheetNumber = excelReader.excelExecutor().sheetList().size();
            // 关闭读取器
            excelReader.finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sheetNumber;
    }
}
