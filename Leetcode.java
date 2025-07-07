// XPATH expressions for locating elements by difficulty level
/*
//div[contains(text(),"Med")]/preceding-sibling::div
//div[contains(text(),"Med")]/preceding-sibling::div/child::a
//div[contains(text(),"Easy")]/preceding-sibling::div
//div[contains(text(),"Easy")]/preceding-sibling::div/child::a
//div[contains(text(),"Hard")]/preceding-sibling::div
//div[contains(text(),"Hard")]/preceding-sibling::div/child::a
        
//div[contains(text(),"Easy")]/preceding-sibling::div/parent::div/parent::div/parent::div/parent::div/preceding-sibling::div/child::div
//div[contains(text(),"Hard")]/preceding-sibling::div/parent::div/parent::div/parent::div/parent::div/preceding-sibling::div/child::div
//div[contains(text(),"Med")]/preceding-sibling::div/parent::div/parent::div/parent::div/parent::div/preceding-sibling::div/child::div
*/

package LaunchBrowser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import dev.failsafe.internal.util.Assert;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Leetcode {
    private static WebDriver driver;
    private static WebDriverWait wait;
    
    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        // Setup ChromeDriver with options
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        options.setExperimentalOption("useAutomationExtension", false);
        
        // Map to store question data
        Map<String, Map<String, String>> outermaporig = new LinkedHashMap();

        // Initialize driver and navigate to LeetCode
        driver = new ChromeDriver(options);
        driver.get("https://leetcode.com/");
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, 10);
        
        // Wait for page to fully load
        if (wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"))) {
            System.out.println("Page is fully loaded");
        }
        
        // Click sign in button
        WebElement signinbutton = driver.findElement(By.xpath("//a//span[contains(text(),\"Sign in\")]"));
        wait.until(ExpectedConditions.elementToBeClickable(signinbutton));
        signinbutton.click();
        
        // Wait for login page to load
        if (wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"))) {
            System.out.println("Login page is fully loaded");
        }
        
        Thread.sleep(3000);
        
        // Enter login credentials
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder=\"Username or E-mail\"]")));
        WebElement Username = driver.findElement(By.xpath("//input[@placeholder=\"Username or E-mail\"]"));
        WebElement password = driver.findElement(By.xpath("//input[@placeholder=\"Password\"]"));
        wait.until(ExpectedConditions.elementToBeClickable(Username));
        Username.sendKeys("");
        password.sendKeys("");
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button//div//span[contains(text(),'Sign In')]"))).click();
        Thread.sleep(2000);

        // Navigate to progress page
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@id='headlessui-menu-button-5']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),\"Progress\")]/ancestor::a"))).click();
        
        // Switch to new window
        Object[] windowHandles = driver.getWindowHandles().toArray();
        driver.switchTo().window((String) windowHandles[1]);
        System.out.println("New window title: " + driver.getTitle());
        
        Thread.sleep(1000);
        
        // Click on "Last Submitted" filter
        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//div[contains(text(),'Last Submitted')]")));        
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
        Thread.sleep(1000);
        
        // Process questions for each difficulty level
        String[] Difficultylevel = {"Easy", "Med", "Hard"};
        for (String diff : Difficultylevel) {
            outermaporig = diffquestion(diff);
            excelwrite(outermaporig, diff);
            System.out.println(outermaporig);
        }

        Thread.sleep(10000);
        driver.quit();
    }
    
    /**
     * Extracts questions for a given difficulty level
     * @param difficulty The difficulty level ("Easy", "Med", "Hard")
     * @return Map containing date as key and another map of question names and links as value
     */
    public static Map<String, Map<String, String>> diffquestion(String difficulty) {
        Map<String, Map<String, String>> outermap = new LinkedHashMap();
        Map<String, String> innermap = new LinkedHashMap();
        String date = null;

        // Find all question elements for the given difficulty
        List<WebElement> med = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.xpath("//div[contains(text(),'" + difficulty + "')]/ancestor::div[@role='row']/descendant::div[@role='cell'][position()<=2]")));
        
        int counter = 1;
        for (WebElement me : med) {
            String textn = me.getText();
            String[] parts = textn.split("\n");
            String text = parts[0].trim();
            
            switch (counter) {
                case 1:
                    // First element is the date
                    date = text;
                    counter = 2;
                    break;
                    
                case 2:
                    // Second element is the question name
                    String scenario = text;
                    try {
                        // Try to find the question link
                        String link = driver.findElement(By.xpath("//a[contains(text(),'" + text + "')]")).getAttribute("href");
                        counter = 1;
                        
                        // Add to the map structure
                        if (outermap.containsKey(date)) {
                            innermap = outermap.get(date);
                            innermap.put(scenario, link);
                            outermap.put(date, innermap);
                        } else {
                            Map<String, String> innermaptemp = new LinkedHashMap();
                            innermaptemp.put(scenario, link);
                            outermap.put(date, innermaptemp);
                        }
                    } catch (org.openqa.selenium.InvalidSelectorException e) {
                        // Handle selector exceptions
                    } catch (org.openqa.selenium.NoSuchElementException e) {
                        // Handle missing element exceptions
                    } catch (Exception e) {
                        // Handle other exceptions
                    }
                    break;
            }
        }
        return outermap;
    }
    
    /**
     * Writes question data to an Excel file with separate sheets for each difficulty level
     * @param data The question data to write
     * @param difficultylevel The difficulty level for the sheet name
     * @throws FileNotFoundException
     */
    public static void excelwrite(Map<String, Map<String, String>> data, String difficultylevel) throws FileNotFoundException {
        String outputPath = System.getProperty("user.home") + "/Downloads/LeetCodeProblems.xlsx";
        File excelFile = new File(outputPath);
        
        Workbook workbook;
        
        // Try to open existing workbook if it exists, otherwise create new
        if (excelFile.exists()) {
            try (FileInputStream inputStream = new FileInputStream(excelFile)) {
                workbook = new XSSFWorkbook(inputStream);
            } catch (Exception e) {
                workbook = new XSSFWorkbook(); // Create new if can't read existing
            }
        } else {
            workbook = new XSSFWorkbook();
        }
        
        // Remove existing sheet with same name if it exists
        int sheetIndex = workbook.getSheetIndex(difficultylevel);
        if (sheetIndex != -1) {
            workbook.removeSheetAt(sheetIndex);
        }
        
        // Create new sheet with the difficulty level name
        Sheet sheet = workbook.createSheet(difficultylevel);

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
        try (FileOutputStream outputStream = new FileOutputStream(outputPath)) {
            workbook.write(outputStream);
            System.out.println("Excel file updated successfully with sheet: " + difficultylevel);
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
}