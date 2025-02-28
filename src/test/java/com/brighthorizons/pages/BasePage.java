package com.brighthorizons.pages;

import com.brighthorizons.utils.WaitUtility;
import org.openqa.selenium.*;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    private final int DEFAULT_TIMEOUT = 20;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        PageFactory.initElements(driver, this);
    }

    public void click(WebElement element) {
        try {
            WaitUtility.waitForElementClickable(driver, element).click();
        } catch (ElementClickInterceptedException e) {
            // Try with JavaScript click if normal click is intercepted
            try {
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", element);
            } catch (Exception ex) {
                System.out.println("Failed to click element using JavaScript");
            }
        } catch (Exception e) {
            // Retry after a short wait
            try {
                WaitUtility.waitForElementClickable(driver, element, 1);
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].click();", element);
            } catch (Exception ex) {
                System.out.println("Failed to click element after retry");
            }
        }
    }

    public void sendKeys(WebElement element, String text) {
        try {
            WaitUtility.waitForElementVisible(driver, element);
            element.clear();
            element.sendKeys(text);
        } catch (Exception e) {
            try {
                // Try with JavaScript if normal sendKeys fails
                JavascriptExecutor js = (JavascriptExecutor) driver;
                js.executeScript("arguments[0].value='" + text + "';", element);
            } catch (Exception ex) {
                System.out.println("Failed to send keys to element using JavaScript");
            }
        }
    }

    protected String getText(WebElement element) {
        try {
            return WaitUtility.waitForElementVisible(driver, element).getText();
        } catch (Exception e) {
            try {
                // Try with JavaScript if normal getText fails
                JavascriptExecutor js = (JavascriptExecutor) driver;
                return (String) js.executeScript("return arguments[0].textContent;", element);
            } catch (Exception ex) {
                System.out.println("Failed to get text from element using JavaScript");
                return "";
            }
        }
    }

    protected List<WebElement> waitForElementsVisible(By locator) {
        return WaitUtility.waitForElementsVisible(driver, locator);
    }

    /**
     * Scrolls to a specific element
     * 
     * @param locator The locator of the element to scroll to
     */
    protected void scrollToElement(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
            // Add a small wait after scrolling
            WaitUtility.waitForElementVisible(driver, element, 1);
        } catch (Exception e) {
            System.out.println("Failed to scroll to element: " + e.getMessage());
        }
    }

    protected void scrollToBottom() {
        try {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
            // Add a wait after scrolling to bottom
            WaitUtility.waitForPageLoad(driver, 2);
        } catch (Exception e) {
            System.out.println("Failed to scroll to bottom");
        }
    }

    protected boolean isElementDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        }
    }

    protected void waitForPageLoad() {
        WaitUtility.waitForPageLoad(driver);
    }

}