package com.leetcode.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Configuration reader utility class
 */
public class ConfigReader {
    private static Properties properties;
    private static final String CONFIG_FILE_PATH = "src/test/resources/config.properties";
    
    static {
        loadProperties();
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream(CONFIG_FILE_PATH)) {
            properties.load(fis);
        } catch (IOException e) {
            System.err.println("Error loading configuration file: " + e.getMessage());
            // Set default values
            properties.setProperty("browser", "chrome");
            properties.setProperty("username", "");
            properties.setProperty("password", "");
            properties.setProperty("baseUrl", "https://leetcode.com/");
            properties.setProperty("timeout", "10");
        }
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public static String getBrowser() {
        return getProperty("browser");
    }
    
    public static String getUsername() {
        return getProperty("username");
    }
    
    public static String getPassword() {
        return getProperty("password");
    }
    
    public static String getBaseUrl() {
        return getProperty("baseUrl");
    }
    
    public static int getTimeout() {
        return Integer.parseInt(getProperty("timeout"));
    }
}