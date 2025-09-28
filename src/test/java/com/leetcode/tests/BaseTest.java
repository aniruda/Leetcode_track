package com.leetcode.tests;

import com.leetcode.utils.ConfigReader;
import com.leetcode.utils.DriverManager;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

/**
 * Base test class containing setup and teardown methods
 */
public class BaseTest {
    
    @BeforeMethod
    @Parameters({"browser"})
    public void setUp(String browser) {
        // Use parameter if provided, otherwise use config
        String browserToUse = (browser != null) ? browser : ConfigReader.getBrowser();
        DriverManager.initializeDriver(browserToUse);
        
        System.out.println("Driver initialized for browser: " + browserToUse);
    }
    
    @AfterMethod
    public void tearDown() {
        try {
            Thread.sleep(2000); // Wait before closing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        DriverManager.quitDriver();
        System.out.println("Driver closed successfully");
    }
}