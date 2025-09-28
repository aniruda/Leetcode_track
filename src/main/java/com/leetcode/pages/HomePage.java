package com.leetcode.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.WebElement;

/**
 * Page Object for LeetCode Home Page
 */
public class HomePage extends BasePage {
    
    // Locators
    private final By signInButton = By.xpath("//a//span[contains(text(),\"Sign in\")]");
    
    public HomePage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Navigate to LeetCode home page
     */
    public HomePage navigateToHomePage() {
        driver.get("https://leetcode.com/");
        driver.manage().window().maximize();
        waitForPageLoad();
        return this;
    }
    
    /**
     * Click sign in button
     */
    public LoginPage clickSignIn() {
        clickElement(signInButton);
        return new LoginPage(driver);
    }
}