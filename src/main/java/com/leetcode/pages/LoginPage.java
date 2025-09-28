package com.leetcode.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for LeetCode Login Page
 */
public class LoginPage extends BasePage {
    
    // Locators
    private final By usernameField = By.xpath("//input[@placeholder=\"Username or E-mail\"]");
    private final By passwordField = By.xpath("//input[@placeholder=\"Password\"]");
    private final By signInButton = By.xpath("//button//div//span[contains(text(),'Sign In')]");
    
    public LoginPage(WebDriver driver) {
        super(driver);
        waitForPageLoad();
    }
    
    /**
     * Enter username
     */
    public LoginPage enterUsername(String username) {
        sendKeys(usernameField, username);
        return this;
    }
    
    /**
     * Enter password
     */
    public LoginPage enterPassword(String password) {
        sendKeys(passwordField, password);
        return this;
    }
    
    /**
     * Click sign in button
     */
    public DashboardPage clickSignIn() {
        clickElement(signInButton);
        waitFor(2);
        return new DashboardPage(driver);
    }
    
    /**
     * Perform complete login
     * @throws InterruptedException 
     */
    public DashboardPage login(String username, String password) throws InterruptedException {
    	Thread.sleep(2000);
        return enterUsername(username)
                .enterPassword(password)
                .clickSignIn();
    }
}