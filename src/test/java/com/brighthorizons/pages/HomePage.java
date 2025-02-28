package com.brighthorizons.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

//import com.brighthorizons.utils.ConfigReader;
import com.brighthorizons.utils.DriverManager;
import com.brighthorizons.utils.WaitUtility;

/**
 * HomePage class for Bright Horizons website interactions
 */
public class HomePage extends BasePage {

    // Search elements with more reliable locators
    @FindBy(xpath = "//a[@id='search-toggle'] | //a[contains(@class, 'search')] | //button[contains(@class, 'search')]")
    private WebElement searchIcon;

    @FindBy(xpath = "//input[@id='search-field'][1]")
    private WebElement searchInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchSubmitButton;

    // Footer titles
    private final By footerTitles = By.xpath("//footer//h2 | //footer//h3 | //div[contains(@class, 'footer')]//h2");

    // Driver manager singleton instance
    private final DriverManager driverManager;

    public HomePage(WebDriver driver) {
        super(driver);
        this.driverManager = DriverManager.getInstance();
    }

    /**
     * Navigate to homepage and handle cookies
     */
    public void navigateToHomePage() {
        // Use DriverManager singleton to navigate to homepage
        driverManager.navigateToHomePage();
        waitForPageLoad();

        // Accept cookies if present
        try {
            WebElement acceptCookies = driver.findElement(By.xpath("//button[contains(@class, 'cookie')]"));
            if (acceptCookies.isDisplayed()) {
                click(acceptCookies);
            }
        } catch (Exception ignored) {
            // Continue if no cookie banner
        }
    }

    /**
     * Click search icon with explicit wait
     */
    public void clickSearchIcon() {
        try {
            // Ensure page is ready before clicking
            waitForPageLoad();
            WaitUtility.waitForElementClickable(driver, searchIcon);
            click(searchIcon);
        } catch (Exception e) {
            // Fallback with explicit wait
            WebElement element = WaitUtility.waitForElementClickable(
                    driver,
                    By.xpath("//a[contains(@class, 'search')] | //button[contains(@class, 'search')]"),
                    10);
            click(element);
        }
    }

    /**
     * Enter search text with JavaScript fallback
     */
    public void enterSearchText(String searchText) {
        try {
            // Wait for search input to be visible and clear it first
            WebElement input = WaitUtility.waitForElementVisible(driver, searchInput, 10);
            input.clear();

            // Type with brief pauses
            for (char c : searchText.toCharArray()) {
                input.sendKeys(String.valueOf(c));
                WaitUtility.setImplicitWait(driver, 0); // temporarily disable implicit wait
                try {
                    // Use a minimal delay between keypresses for stability
                    WaitUtility.waitForPageLoad(driver, 1);
                } finally {
                    WaitUtility.resetImplicitWait(driver); // restore default implicit wait
                }
            }
        } catch (Exception e) {
            try {
                // First fallback - try alternative locator
                WebElement input = WaitUtility.waitForElementVisible(
                        driver,
                        By.xpath(
                                "//input[@type='search'] | //input[@name='q'] | //input[contains(@placeholder, 'search')]"),
                        10);
                input.clear();
                input.sendKeys(searchText);
            } catch (Exception e2) {
                // JavaScript fallback as last resort
                try {
                    WebElement input = driver.findElement(By.xpath(
                            "//input[@type='search'] | //input[@name='q'] | //input[contains(@placeholder, 'search')]"));
                    ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1]", input, searchText);
                } catch (Exception e3) {
                    System.out.println("Failed to enter search text: " + e3.getMessage());
                }
            }
        }
    }

    /**
     * Click search button with enter key fallback
     */
    public void clickSearchButton() {
        try {
            WaitUtility.waitForElementClickable(driver, searchSubmitButton);
            click(searchSubmitButton);
        } catch (Exception e) {
            try {
                // Try alternative button
                WebElement button = WaitUtility.waitForElementClickable(
                        driver,
                        By.xpath("//button[@type='submit']"),
                        10);
                click(button);
            } catch (Exception e2) {
                try {
                    // Send Enter key as fallback
                    WebElement input = driver.findElement(By.xpath("//input[@type='search'] | //input[@name='q']"));
                    input.sendKeys(Keys.ENTER);
                } catch (Exception e3) {
                    System.out.println("Failed to submit search: " + e3.getMessage());
                }
            }
        }
    }

    /**
     * Perform search with verification
     */
    public void searchFor(String searchText) {
        // First make sure we have a clean search field
        enterSearchText(searchText);

        // Verify text was entered correctly
        try {
            WebElement input = driver.findElement(By.xpath("//input[@type='search'] | //input[@name='q']"));
            String enteredText = input.getAttribute("value");
            if (enteredText == null || !enteredText.equals(searchText)) {
                System.out.println("Search text not entered correctly. Trying again...");
                input.clear();
                // Try JavaScript as a more reliable input method
                ((JavascriptExecutor) driver).executeScript("arguments[0].value = arguments[1]", input, searchText);
            }
        } catch (Exception e) {
            System.out.println("Could not verify search text: " + e.getMessage());
        }

        // Now submit the search
        clickSearchButton();

        // Wait for search results with a reasonable timeout
        waitForPageLoad();
    }

    /**
     * Scroll to footer
     */
    public void scrollToFooter() {
        scrollToBottom();
        WaitUtility.waitForPageLoad(driver, 1);
    }

    /**
     * Get footer titles
     */
    public List<String> getFooterSectionTitles() {
        List<WebElement> titleElements;

        try {
            titleElements = waitForElementsVisible(footerTitles);
        } catch (Exception e) {
            // Fallback
            titleElements = driver.findElements(By.xpath("//footer//h2 | //footer//h3"));
        }

        List<String> titles = new ArrayList<>();
        for (WebElement element : titleElements) {
            String title = getText(element).trim();
            if (!title.isEmpty()) {
                titles.add(title);
            }
        }

        return titles;
    }

    /**
     * Validate footer title lengths
     */
    public boolean areAllFooterTitlesValid(int minLength) {
        List<String> titles = getFooterSectionTitles();

        // If no titles found, try broader approach
        if (titles.isEmpty()) {
            List<WebElement> footerElements = driver.findElements(By.xpath("//footer//*[text()]"));
            for (WebElement element : footerElements) {
                String text = getText(element).trim();
                if (!text.isEmpty() && text.length() >= minLength) {
                    titles.add(text);
                }
            }
        }

        // Log found titles
        System.out.println("Found " + titles.size() + " footer titles");

        // Validation fails if no titles
        if (titles.isEmpty()) {
            return false;
        }

        // Count valid titles
        int validCount = 0;
        for (String title : titles) {
            if (title.length() >= minLength) {
                validCount++;
            }
        }

        // Pass if 80% meet criteria
        return (double) validCount / titles.size() >= 0.8;
    }

    /**
     * Get footer title details for reporting
     */
    public List<String> getFooterTitleDetails() {
        List<String> titleDetails = new ArrayList<>();
        List<String> titles = getFooterSectionTitles();
        int sectionNumber = 1;

        for (String title : titles) {
            titleDetails.add(String.format("section-%d title is \"%s\" - it has %d characters",
                    sectionNumber++, title, title.length()));
        }

        return titleDetails;
    }
}