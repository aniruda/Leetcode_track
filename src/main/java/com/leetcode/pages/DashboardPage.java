package com.leetcode.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/**
 * Page Object for LeetCode Dashboard Page
 */
public class DashboardPage extends BasePage {
    
    // Locators
    private final By profileMenuButton = By.xpath("//button[@id='headlessui-menu-button-5']");
    private final By progressLink = By.xpath("//div[contains(text(),\"Progress\")]/ancestor::a");
    
    public DashboardPage(WebDriver driver) {
        super(driver);
    }
    
    /**
     * Click on profile menu
     */
    public DashboardPage clickProfileMenu() {
        clickElement(profileMenuButton);
        return this;
    }
    
    /**
     * Navigate to progress page
     */
    public ProgressPage navigateToProgress() {
        clickProfileMenu();
        clickElement(progressLink);
        switchToWindow(1);
        waitFor(1);
        return new ProgressPage(driver);
    }
}