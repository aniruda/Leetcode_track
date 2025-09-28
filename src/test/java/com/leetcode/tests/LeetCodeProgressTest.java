package com.leetcode.tests;

import com.leetcode.pages.HomePage;
import com.leetcode.pages.ProgressPage;
import com.leetcode.utils.ConfigReader;
import com.leetcode.utils.DriverManager;
import com.leetcode.utils.ExcelUtils;
import org.testng.annotations.Test;

import java.util.Map;

/**
 * Test class for LeetCode Progress tracking
 */
public class LeetCodeProgressTest extends BaseTest {
    
    @Test(priority = 1, description = "Extract LeetCode progress and save to Excel")
    public void testLeetCodeProgressExtraction() throws InterruptedException {
        // Get credentials from config
        String username = ConfigReader.getUsername();
        String password = ConfigReader.getPassword();
        
        if (username.isEmpty() || password.isEmpty()) {
            System.err.println("Please provide username and password in config.properties file");
            return;
        }
        
        // Navigate to home page and login
        HomePage homePage = new HomePage(DriverManager.getDriver());
        ProgressPage progressPage = homePage
                .navigateToHomePage()
                .clickSignIn()
                .login(username, password)
                .navigateToProgress()
                .clickLastSubmittedFilter();
        
        // Extract data for each difficulty level
        String[] difficultyLevels = {"Easy", "Med", "Hard"};
        
        for (String difficulty : difficultyLevels) {
            System.out.println("Processing " + difficulty + " level questions...");
            
            Map<String, Map<String, String>> questionsData = progressPage
                    .extractQuestionsByDifficulty(difficulty);
            
            // Write to Excel
            ExcelUtils.writeToExcel(questionsData, difficulty);
            
            // Print summary
            int totalQuestions = questionsData.values().stream()
                    .mapToInt(innerMap -> innerMap.size())
                    .sum();
            
            System.out.println(difficulty + " level: " + totalQuestions + " questions found");
            System.out.println("Data: " + questionsData);
        }
        
        System.out.println("Data extraction completed. Check file: " + ExcelUtils.getOutputPath());
    }
    
    @Test(priority = 2, description = "Extract only Easy level questions", enabled = false)
    public void testExtractEasyQuestions() throws InterruptedException {
        extractQuestionsForDifficulty("Easy");
    }
    
    @Test(priority = 3, description = "Extract only Medium level questions", enabled = false)
    public void testExtractMediumQuestions() throws InterruptedException {
        extractQuestionsForDifficulty("Med");
    }
    
    @Test(priority = 4, description = "Extract only Hard level questions", enabled = false)
    public void testExtractHardQuestions() throws InterruptedException {
        extractQuestionsForDifficulty("Hard");
    }
    
    /**
     * Helper method to extract questions for specific difficulty
     * @throws InterruptedException 
     */
    private void extractQuestionsForDifficulty(String difficulty) throws InterruptedException {
        String username = ConfigReader.getUsername();
        String password = ConfigReader.getPassword();
        
        if (username.isEmpty() || password.isEmpty()) {
            System.err.println("Please provide username and password in config.properties file");
            return;
        }
        
        HomePage homePage = new HomePage(DriverManager.getDriver());
        ProgressPage progressPage = homePage
                .navigateToHomePage()
                .clickSignIn()
                .login(username, password)
                .navigateToProgress()
                .clickLastSubmittedFilter();
        
        Map<String, Map<String, String>> questionsData = progressPage
                .extractQuestionsByDifficulty(difficulty);
        
        ExcelUtils.writeToExcel(questionsData, difficulty);
        
        int totalQuestions = questionsData.values().stream()
                .mapToInt(innerMap -> innerMap.size())
                .sum();
        
        System.out.println(difficulty + " level: " + totalQuestions + " questions extracted");
    }
}