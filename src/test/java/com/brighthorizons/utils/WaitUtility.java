package com.brighthorizons.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Utility class that provides reusable wait methods for Selenium WebDriver.
 * Handles both explicit and implicit waits with fallback strategies for
 * stability.
 */
public class WaitUtility {

    private static final int DEFAULT_TIMEOUT = 20;
    private static final int SHORT_TIMEOUT = 5;
    private static final int LONG_TIMEOUT = 30;

    // Private constructor to prevent instantiation
    private WaitUtility() {
    }

    // Sets implicit wait for a WebDriver instance

    public static void setImplicitWait(WebDriver driver, int seconds) {
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(seconds));
    }

    // Resets implicit wait to default timeout

    public static void resetImplicitWait(WebDriver driver) {
        setImplicitWait(driver, DEFAULT_TIMEOUT);
    }

    //Waits for an element to be visible

    public static WebElement waitForElementVisible(WebDriver driver, By locator) {
        return waitForElementVisible(driver, locator, DEFAULT_TIMEOUT);
    }

    // Waits for an element to be visible with custom timeout

    public static WebElement waitForElementVisible(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (Exception e) {
            // Try scrolling to the element
            try {
                WebElement element = driver.findElement(locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            } catch (Exception ex) {
                return null;
            }
        }
    }

    //Waits for a specific WebElement to be visible

    public static WebElement waitForElementVisible(WebDriver driver, WebElement element) {
        return waitForElementVisible(driver, element, DEFAULT_TIMEOUT);
    }

    // Waits for a specific WebElement to be visible with custom timeout

    public static WebElement waitForElementVisible(WebDriver driver, WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            return wait.until(ExpectedConditions.visibilityOf(element));
        } catch (Exception e) {
            // Try scrolling to element
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                return wait.until(ExpectedConditions.visibilityOf(element));
            } catch (Exception ex) {
                return null;
            }
        }
    }

    //Waits for an element to be clickable
    public static WebElement waitForElementClickable(WebDriver driver, By locator) {
        return waitForElementClickable(driver, locator, DEFAULT_TIMEOUT);
    }

    //Waits for an element to be clickable with custom timeout
    public static WebElement waitForElementClickable(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(locator));
        } catch (Exception e) {
            // Try scrolling to element
            try {
                WebElement element = driver.findElement(locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                return wait.until(ExpectedConditions.elementToBeClickable(locator));
            } catch (Exception ex) {
                return null;
            }
        }
    }

    // Waits for a specific WebElement to be clickable

    public static WebElement waitForElementClickable(WebDriver driver, WebElement element) {
        return waitForElementClickable(driver, element, DEFAULT_TIMEOUT);
    }

    //Waits for a specific WebElement to be clickable with custom timeout

    public static WebElement waitForElementClickable(WebDriver driver, WebElement element, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            return wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (Exception e) {
            // Try scrolling to element
            try {
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                return wait.until(ExpectedConditions.elementToBeClickable(element));
            } catch (Exception ex) {
                return null;
            }
        }
    }

    //Waits for all elements matching the locator to be visible

    public static List<WebElement> waitForElementsVisible(WebDriver driver, By locator) {
        return waitForElementsVisible(driver, locator, DEFAULT_TIMEOUT);
    }

    // Waits for all elements matching the locator to be visible with custom timeout

    public static List<WebElement> waitForElementsVisible(WebDriver driver, By locator, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (Exception e) {
            // Try scrolling to the middle of the page to locate elements
            try {
                ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight/2);");
                return wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
            } catch (Exception ex) {
                // Just return whatever elements we can find without checking visibility
                return driver.findElements(locator);
            }
        }
    }

    // Waits for page to be fully loaded

    public static void waitForPageLoad(WebDriver driver) {
        waitForPageLoad(driver, DEFAULT_TIMEOUT);
    }

    /**
     * Waits for page to be fully loaded with custom timeout
     *
     */
    public static void waitForPageLoad(WebDriver driver, int timeoutSeconds) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds));
        try {
            wait.until((ExpectedCondition<Boolean>) ddriver -> {
                assert ddriver != null;
                String readyState = ((JavascriptExecutor) ddriver).executeScript("return document.readyState")
                        .toString();
                boolean isJQueryComplete = true;
                try {
                    isJQueryComplete = (Boolean) ((JavascriptExecutor) ddriver)
                            .executeScript("return jQuery.active == 0");
                } catch (Exception e) {
                    // jQuery might not be present, which is fine
                }
                return readyState.equals("complete") && isJQueryComplete;
            });
        } catch (Exception e) {
            // Page might still be usable even if not fully loaded
        }
    }

}