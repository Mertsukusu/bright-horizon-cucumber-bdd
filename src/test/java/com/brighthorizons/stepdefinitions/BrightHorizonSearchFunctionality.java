package com.brighthorizons.stepdefinitions;
import com.brighthorizons.pages.BasePage;
import com.brighthorizons.pages.HomePage;
import com.brighthorizons.pages.SearchResultsPage;
import com.brighthorizons.utils.DriverManager;
import com.brighthorizons.utils.WaitUtility;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * Step definitions for Bright Horizons search functionality tests
 */
public class BrightHorizonSearchFunctionality {

    private WebDriver driver;
    private HomePage homePage;
    private SearchResultsPage searchResultsPage;
    private Scenario scenario;
    private BasePage basePage;

    @Before
    public void setup(Scenario scenario) {
        // Initialize test context and page objects
        this.scenario = scenario;
        driver = DriverManager.getDriver();
        homePage = new HomePage(driver);
        searchResultsPage = new SearchResultsPage(driver);
        basePage = new BasePage(driver);
    }

    @After
    public void tearDown(Scenario scenario) {
        // Capture screenshot on test failure
        if (driver != null && scenario.isFailed()) {
            try {
                final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Screenshot of failure");
            } catch (Exception e) {
                // Unable to take screenshot
            }
        }
        DriverManager.quitDriver();
    }

    @Given("I am on the Bright Horizons homepage")
    public void i_am_on_the_bright_horizons_homepage() {
        // Navigate to homepage and log action
        homePage.navigateToHomePage();
        scenario.log("Navigated to Bright Horizons homepage");
    }

    @When("I scroll to the footer section")
    public void i_scroll_to_the_footer_section() {
        // Scroll to footer and capture screenshot
        homePage.scrollToFooter();
        scenario.log("Scrolled to footer section");

        try {
            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Footer section");
        } catch (Exception ignored) {
        }
    }

    @Then("each footer section should contain a title with at least {int} characters")
    public void each_footer_section_should_contain_a_title_with_at_least_characters(Integer minLength) {
        // Log all footer title details
        homePage.getFooterTitleDetails().forEach(scenario::log);

        // Verify all titles meet minimum length requirement
        boolean areAllTitlesValid = homePage.areAllFooterTitlesValid(minLength);
        Assert.assertTrue(
                "Not all footer titles have at least " + minLength + " characters",
                areAllTitlesValid);

        scenario.log("Verified footer section titles have at least " + minLength + " characters");
        //Take ScreenShots
        WaitUtility.setImplicitWait(driver, 2);
        try {
            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Footer section");
        } catch (Exception ignored) {
        }
    }

    @When("I click on the search icon")
    public void i_click_on_the_search_icon() {
        // Click search icon and verify search box appears
        homePage.clickSearchIcon();
        scenario.log("Clicked on search icon");

        try {
            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "After clicking search icon");
        } catch (Exception ignored) {
        }

        try {
            By searchFieldLocator = By.xpath("//input[@id='search-field'][1]");
            WaitUtility.waitForElementVisible(driver, searchFieldLocator, 10);
            scenario.log("Search box is visible after clicking search icon");
        } catch (Exception e) {
            scenario.log("Warning:Search box may not be visible after clicking search icon");
        }
    }

    @When("I search for {string}")
    public void i_search_for(String searchText) {
        try {
            // Locate and enter text in search field
            By searchInputLocator = By.xpath("//input[@id='search-field'][1]");
            WebElement searchInput = WaitUtility.waitForElementVisible(driver, searchInputLocator, 10);
            basePage.sendKeys(searchInput, searchText);
            WaitUtility.setImplicitWait(driver, 2);

            // Submit search
            homePage.clickSearchButton();
            scenario.log("Entered and submitted search: " + searchText);
        } catch (Exception e) {
            // Use page object method as fallback
            scenario.log("Direct input failed,using page object search method");
            homePage.searchFor(searchText);
        }

        try {
            // Capture search results screenshot
            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "After search");
        } catch (Exception ignored) {
        }

        // Wait for results to load
        WaitUtility.waitForPageLoad(driver, 2);
    }

    @Then("the first search result should exactly match {string}")
    public void the_first_search_result_should_exactly_match(String expectedText) {
        try {
            // Capture search results screenshot
            final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Search results");
        } catch (Exception ignored) {
        }

        // Log comparison details for reporting
        String comparisonResult = searchResultsPage.getComparisonResult(expectedText);
        scenario.log(comparisonResult);

        // Verify search result matches expected text
        boolean isMatch = searchResultsPage.isFirstSearchResultMatch(expectedText);
        Assert.assertTrue(
                "Search result assertion failed! Expected: '" + expectedText + "'",
                isMatch);

        scenario.log("Verified first search result matches: " + expectedText);

        // Scroll to footer
        searchResultsPage.scrollToFooter();
        WaitUtility.setImplicitWait(driver, 2);
    }
}