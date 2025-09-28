package com.leetcode.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.Map;

/**
 * Utility class for Excel operations
 */
public class ExcelUtils {
    
    private static final String OUTPUT_PATH = System.getProperty("user.home") + "/Downloads/LeetCodeProblems.xlsx";
    
    /**
     * Write question data to Excel file with separate sheets for each difficulty level
     */
    public static void writeToExcel(Map<String, Map<String, String>> data, String difficultyLevel) {
        File excelFile = new File(OUTPUT_PATH);
        Workbook workbook;
        
        // Try to open existing workbook or create new one
        if (excelFile.exists()) {
            try (FileInputStream inputStream = new FileInputStream(excelFile)) {
                workbook = new XSSFWorkbook(inputStream);
            } catch (Exception e) {
                workbook = new XSSFWorkbook();
            }
        } else {
            workbook = new XSSFWorkbook();
        }
        
        // Remove existing sheet with same name if it exists
        int sheetIndex = workbook.getSheetIndex(difficultyLevel);
        if (sheetIndex != -1) {
            workbook.removeSheetAt(sheetIndex);
        }
        
        // Create new sheet with the difficulty level name
        Sheet sheet = workbook.createSheet(difficultyLevel);
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Date");
        headerRow.createCell(1).setCellValue("Question");
        headerRow.createCell(2).setCellValue("Link");
        
        // Populate data rows
        int rowNum = 1;
        for (Map.Entry<String, Map<String, String>> dateEntry : data.entrySet()) {
            String date = dateEntry.getKey();
            Map<String, String> questionsMap = dateEntry.getValue();
            
            for (Map.Entry<String, String> questionEntry : questionsMap.entrySet()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(date);
                row.createCell(1).setCellValue(questionEntry.getKey());
                row.createCell(2).setCellValue(questionEntry.getValue());
            }
        }
        
        // Auto-size columns for better readability
        for (int i = 0; i < 3; i++) {
            sheet.autoSizeColumn(i);
        }
        
        // Write to file
        try (FileOutputStream outputStream = new FileOutputStream(OUTPUT_PATH)) {
            workbook.write(outputStream);
            System.out.println("Excel file updated successfully with sheet: " + difficultyLevel);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Get the output file path
     */
    public static String getOutputPath() {
        return OUTPUT_PATH;
    }
}