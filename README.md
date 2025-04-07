# Modern BDD Cucumber Test Automation Framework

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

- **DriverManager**: Singleton class for browser management

- **WaitUtility**: Synchronization methods for waits

### Page Objects

- **BasePage**: Parent class with common methods

- **HomePage**: Home page specific interactions

- **SearchResultsPage**: Search results functionality


### Step Definitions

- **BrightHorizonsStepDefinitions**: Connects feature files to code

## Configuration

The `config.properties` file contains settings that can be modified without changing code:


## Running Tests

```bash
# Default browser (Chrome)
mvn clean test

# Specific browser
mvn clean test -Dbrowser=firefox
mvn clean test -Dbrowser=edge

```

## Cucumber Reports

The framework generates multiple report formats after test execution:

### HTML Reports (Default)

- **Location**: `target/cucumber-reports/cucumber-pretty.html`
- **Features**: Interactive UI with passed/failed steps, screenshots, and execution time
- **Access**: Open the HTML file in any browser
