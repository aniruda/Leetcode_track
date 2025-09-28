package com.leetcode.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

/**
 * WebDriver management utility class
 * Uses system-installed drivers or drivers from PATH
 */
public class DriverManager {
    private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    
    /**
     * Initialize WebDriver based on browser type
     * Assumes browser drivers are available in system PATH or specified via system properties
     */
    public static void initializeDriver(String browser) {
        WebDriver webDriver;
        
        switch (browser.toLowerCase()) {
            case "chrome":
                // Set ChromeDriver path if not in PATH (optional)
                // System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
                
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
                chromeOptions.addArguments("--disable-web-security");
                chromeOptions.addArguments("--disable-features=VizDisplayCompositor");
                chromeOptions.addArguments("--no-sandbox");
                chromeOptions.addArguments("--disable-dev-shm-usage");
                chromeOptions.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
                chromeOptions.setExperimentalOption("useAutomationExtension", false);
                
                // Add user agent to avoid detection
                chromeOptions.addArguments("--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
                
                webDriver = new ChromeDriver(chromeOptions);
                break;
                
            case "firefox":
                // Set GeckoDriver path if not in PATH (optional)
                // System.setProperty("webdriver.gecko.driver", "/path/to/geckodriver");
                
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                firefoxOptions.addPreference("dom.webdriver.enabled", false);
                firefoxOptions.addPreference("useAutomationExtension", false);
                webDriver = new FirefoxDriver(firefoxOptions);
                break;
                
            default:
                throw new IllegalArgumentException("Browser not supported: " + browser);
        }
        
        driver.set(webDriver);
        System.out.println("WebDriver initialized successfully for: " + browser);
    }
    
    /**
     * Get WebDriver instance
     */
    public static WebDriver getDriver() {
        return driver.get();
    }
    
    /**
     * Quit WebDriver and remove from ThreadLocal
     */
    public static void quitDriver() {
        if (driver.get() != null) {
            driver.get().quit();
            driver.remove();
            System.out.println("WebDriver closed successfully");
        }
    }
    
    /**
     * Set driver path manually if needed
     */
    public static void setDriverPath(String browser, String driverPath) {
        switch (browser.toLowerCase()) {
            case "chrome":
                System.setProperty("webdriver.chrome.driver", driverPath);
                break;
            case "firefox":
                System.setProperty("webdriver.gecko.driver", driverPath);
                break;
            default:
                throw new IllegalArgumentException("Browser not supported for path setting: " + browser);
        }
        System.out.println("Driver path set for " + browser + ": " + driverPath);
    }
}