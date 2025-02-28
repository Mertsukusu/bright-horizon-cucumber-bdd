package com.brighthorizons.utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

import java.time.Duration;

// Singleton class to manage browser drivers
public class DriverManager {

    private static final ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    private static volatile DriverManager instance;

    static {
        // Load config when class is loaded
        ConfigReader.initialize();
    }

    // Private constructor for singleton pattern
    private DriverManager() {
    }

    // Get singleton instance
    public static DriverManager getInstance() {
        if (instance == null) {
            synchronized (DriverManager.class) {
                if (instance == null) {
                    instance = new DriverManager();
                }
            }
        }
        return instance;
    }

    // Get WebDriver - creates new if not exists
    public static WebDriver getDriver() {
        if (driver.get() == null) {
            setupDriver();
        }
        return driver.get();
    }

    // Navigate to home page
    public void navigateToHomePage() {
        WebDriver webDriver = getDriver();
        webDriver.get(ConfigReader.getProperty("base.url"));
        WaitUtility.waitForPageLoad(webDriver);

        // Accept cookies if present
        try {
            WebElement acceptCookies = webDriver.findElement(
                    By.xpath(ConfigReader.getProperty("cookie.accept.xpath")));
            if (acceptCookies.isDisplayed()) {
                acceptCookies.click();
            }
        } catch (Exception ignored) {
            // Continue if no cookie banner
        }
    }

    // Go to specific URL
    public void navigateTo(String url) {
        WebDriver webDriver = getDriver();
        webDriver.get(url);
        WaitUtility.waitForPageLoad(webDriver);
    }

    // Go back to previous page
    public void navigateBack() {
        WebDriver webDriver = getDriver();
        webDriver.navigate().back();
        WaitUtility.waitForPageLoad(webDriver);
    }

    // Go forward to next page
    public void navigateForward() {
        WebDriver webDriver = getDriver();
        webDriver.navigate().forward();
        WaitUtility.waitForPageLoad(webDriver);
    }

    // Refresh current page
    public void refreshPage() {
        WebDriver webDriver = getDriver();
        webDriver.navigate().refresh();
        WaitUtility.waitForPageLoad(webDriver);
    }


    // Create and configure WebDriver
    public static void setupDriver() {
        String browser = ConfigReader.getProperty("browser", "chrome").toLowerCase();

        switch (browser) {
            case "firefox":
                setupFirefoxDriver();
                break;
            case "edge":
                setupEdgeDriver();
                break;
            default:
                setupChromeDriver();
                break;
        }

        driver.get().manage().window().maximize();
        driver.get().manage().timeouts().implicitlyWait(
                Duration.ofSeconds(ConfigReader.getIntProperty("implicit.wait.seconds", 5)));
        driver.get().manage().timeouts().pageLoadTimeout(
                Duration.ofSeconds(ConfigReader.getIntProperty("page.load.timeout.seconds", 30)));
        driver.get().manage().timeouts().scriptTimeout(
                Duration.ofSeconds(ConfigReader.getIntProperty("script.timeout.seconds", 15)));

        driver.get().manage().deleteAllCookies();
    }

    // Setup Chrome browser
    private static void setupChromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();

        // Add options from properties file
        String[] chromeOptions = ConfigReader.getArrayProperty("chrome.options");
        for (String option : chromeOptions) {
            if (!option.trim().isEmpty()) {
                options.addArguments(option.trim());
            }
        }

        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.NORMAL);
        driver.set(new ChromeDriver(options));
    }

    // Setup Firefox browser
    private static void setupFirefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();

        // Add options from properties file
        String[] firefoxOptions = ConfigReader.getArrayProperty("firefox.options");
        for (String option : firefoxOptions) {
            if (!option.trim().isEmpty()) {
                options.addArguments(option.trim());
            }
        }

        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.NORMAL);
        driver.set(new FirefoxDriver(options));
    }

    // Setup Edge browser
    private static void setupEdgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();

        // Add options from properties file
        String[] edgeOptions = ConfigReader.getArrayProperty("edge.options");
        for (String option : edgeOptions) {
            if (!option.trim().isEmpty()) {
                options.addArguments(option.trim());
            }
        }

        options.setPageLoadStrategy(org.openqa.selenium.PageLoadStrategy.NORMAL);
        driver.set(new EdgeDriver(options));
    }

    // Close browser and cleanup
    public static void quitDriver() {
        if (driver.get() != null) {
            try {
                driver.get().quit();
            } catch (Exception e) {
                // Ignore exceptions during quit
            } finally {
                driver.remove();
            }
        }
    }
}