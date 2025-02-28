# Bright Horizons Automation Framework

A Selenium WebDriver and Cucumber BDD framework for testing the Bright Horizons website.

## Project Structure

```
bright-horizon-cucumber-bdd/
├── src/
│   └── test/
│       ├── java/                              # Java test code
│       │   └── com/brighthorizons/
│       │       ├── pages/                     # Page Object classes
│       │       │   ├── BasePage.java          # Base class with common methods
│       │       │   ├── HomePage.java          # Home page interactions
│       │       │   └── SearchResultsPage.java # Search results page
│       │       ├── stepdefinitions/           # Cucumber step definitions
│       │       │   └── BrightHorizonsStepDefinitions.java
│       │       ├── utils/                     # Utility classes
│       │       │   ├── ConfigReader.java      # Reads properties file
│       │       │   ├── DriverManager.java     # Manages WebDriver instances
│       │       │   └── WaitUtility.java       # Wait/Synchronization methods
│       │       └── TestRunner.java            # Main test runner
│       └── resources/
│           ├── config.properties              # Configuration settings
│           └── features/                      # Cucumber feature files
│               └── BrightHorizonsSearch.feature
└── pom.xml                                    # Maven dependencies
```

## Main Components

### Utilities

- **ConfigReader**: Reads configuration from properties file

  - Loads browser settings, URLs, timeouts, and element locators
  - Converts properties to different data types (string, int, boolean)

- **DriverManager**: Singleton class for browser management

  - Creates and configures WebDriver instances
  - Handles browser navigation and cleanup
  - Thread-safe for parallel execution

- **WaitUtility**: Synchronization methods
  - Element visibility and clickability waits
  - Page load waits
  - Fallback strategies for stability

### Page Objects

- **BasePage**: Parent class with common methods

  - Element interaction (click, sendKeys)
  - Waits and scrolling
  - Error handling

- **HomePage**: Home page specific interactions

  - Navigation and cookie handling
  - Search functionality
  - Footer section validation

- **SearchResultsPage**: Search results functionality
  - Result validation
  - Result navigation

### Step Definitions

- **BrightHorizonsStepDefinitions**: Connects feature files to code
  - Given/When/Then implementation
  - Creates page objects
  - Handles test assertions

## Configuration

The `config.properties` file contains settings that can be modified without changing code:

```properties
# Browser settings - change browser type and options
browser=chrome
chrome.options=--disable-notifications,--start-maximized

# URLs - update for different environments
base.url=https://www.brighthorizons.com

# Timeouts - adjust for network conditions
page.load.timeout.seconds=30
implicit.wait.seconds=5

# Common element locators
cookie.accept.xpath=//button[contains(@class, 'cookie')]
```

## Running Tests

```bash
# Default browser (Chrome)
mvn clean test

# Specific browser
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge

# Run specific feature
mvn clean test -Dcucumber.filter.tags="@search"
```

## Cucumber Reports

The framework generates multiple report formats after test execution:

### HTML Reports (Default)

- **Location**: `target/cucumber-reports/cucumber-pretty.html`
- **Features**: Interactive UI with passed/failed steps, screenshots, and execution time
- **Access**: Open the HTML file in any browser

### JSON Reports

- **Location**: `target/cucumber-reports/CucumberTestReport.json`
- **Features**: Machine-readable format for CI/CD integration
- **Usage**: Can be used with Jenkins Cucumber Reports plugin

### JUnit XML Reports

- **Location**: `target/surefire-reports/TEST-*.xml`
- **Features**: Compatible with CI servers like Jenkins
- **Usage**: Automatically used for test result tracking

### Console Reports

- Shows real-time execution with colored output
- Displays pass/fail status during test run

### Custom Reports

For custom reporting, add the plugin in the TestRunner class:

```java
@CucumberOptions(
    plugin = {
        "pretty",
        "html:target/cucumber-reports/cucumber-pretty.html",
        "json:target/cucumber-reports/CucumberTestReport.json",
        "junit:target/cucumber-reports/junit-report.xml",
        // Add custom reporter here
    }
)
```

## Extending the Framework

To add a new page class:

1. Create a new class extending BasePage
2. Add WebElement locators using @FindBy
3. Implement page-specific methods

To add new test scenarios:

1. Add a scenario to an existing feature file or create a new one
2. Implement step definitions if needed
3. Run tests to verify functionality
