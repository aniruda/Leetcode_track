package com.leetcode.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Page Object for LeetCode Progress Page
 */
public class ProgressPage extends BasePage {
    
    // Locators
    private final By lastSubmittedFilter = By.xpath("//div[contains(text(),'Last Submitted')]");
    
    public ProgressPage(WebDriver driver) {
        super(driver);
        System.out.println("Progress page title: " + driver.getTitle());
    }
    
    /**
     * Click on Last Submitted filter
     */
    public ProgressPage clickLastSubmittedFilter() {
        clickElementJS(lastSubmittedFilter);
        waitFor(1);
        return this;
    }
    
    /**
     * Extract questions for a given difficulty level
     */
    public Map<String, Map<String, String>> extractQuestionsByDifficulty(String difficulty) {
        Map<String, Map<String, String>> outerMap = new LinkedHashMap<>();
        String date = null;
        
        // Find all question elements for the given difficulty
        List<WebElement> questionElements = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(
            By.xpath("//div[contains(text(),'" + difficulty + "')]/ancestor::div[@role='row']/descendant::div[@role='cell'][position()<=2]")));
        
        int counter = 1;
        for (WebElement element : questionElements) {
            String text = element.getText().split("\n")[0].trim();
            
            switch (counter) {
                case 1:
                    // First element is the date
                    date = text;
                    counter = 2;
                    break;
                    
                case 2:
                    // Second element is the question name
                    String questionName = text;
                    try {
                        String link = driver.findElement(By.xpath("//a[contains(text(),'" + text + "')]"))
                                           .getAttribute("href");
                        counter = 1;
                        
                        // Add to the map structure
                        if (outerMap.containsKey(date)) {
                            Map<String, String> innerMap = outerMap.get(date);
                            innerMap.put(questionName, link);
                            outerMap.put(date, innerMap);
                        } else {
                            Map<String, String> newInnerMap = new LinkedHashMap<>();
                            newInnerMap.put(questionName, link);
                            outerMap.put(date, newInnerMap);
                        }
                    } catch (Exception e) {
                        System.out.println("Error extracting link for question: " + questionName);
                        counter = 1;
                    }
                    break;
            }
        }
        return outerMap;
    }
}