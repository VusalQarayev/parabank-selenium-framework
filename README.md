# Enterprise ParaBank Test Automation Framework

A Java-based UI test automation framework for [ParaBank](https://parabank.parasoft.com/parabank/index.htm), built with Selenium WebDriver 4, TestNG, and Maven. The project demonstrates maintainable automation architecture, parallel test execution, dynamic test data generation, detailed reporting, and CI/CD integration.

## Features

- **Page Object Model (POM):** Separates page interactions from test logic.
- **Thread-safe WebDriver:** Uses `ThreadLocal<WebDriver>` to support parallel execution.
- **Explicit Waits:** Synchronizes interactions with dynamic page elements without implicit waits.
- **Dynamic Test Data:** Uses Datafaker models to generate registration details and bill payment information.
- **TestNG:** Supports assertions, data-driven testing, test groups, listeners, and retries.
- **Automated Database Reset:** Resets the ParaBank demo environment before the test suite.
- **ExtentReports:** Generates interactive HTML reports with execution details and failure screenshots.
- **Log4j2:** Provides console and file-based execution logs.
- **Cross-Browser Testing:** Supports Chrome, Firefox, and Edge configuration.
- **GitHub Actions:** Runs automated tests in a headless CI environment and archives test artifacts.

## Tech Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Selenium WebDriver 4 | Browser automation |
| TestNG 7 | Test execution and assertions |
| Apache Maven | Dependency and build management |
| WebDriverManager | Browser driver management |
| ExtentReports 5 | HTML test reporting |
| Datafaker | Dynamic test data generation |
| Apache Log4j2 | Logging |
| GitHub Actions | CI/CD automation |

## Test Coverage

The framework covers the following banking workflows:

| Test Area | Scenarios |
|---|---|
| Registration | Dynamic user registration, required-field validation, password mismatch |
| Authentication | Valid login and invalid credential combinations |
| Accounts | Open savings accounts and validate account balances |
| Fund Transfers | Transfer funds and reconcile account balances |
| Bill Payments | Submit payments and verify account deductions |
| Transactions | Search for transactions by amount |
| Loan Requests | Validate approved and denied loan applications |

Tests are organized into `smoke` and `regression` groups.

## Project Structure

```text
parabank-selenium-framework/
├── .github/
│   └── workflows/
│       └── test-pipeline.yml
├── src/
│   ├── main/
│   │   └── java/com/parabank/
│   │       ├── models/
│   │       ├── pages/
│   │       ├── reports/
│   │       └── utils/
│   └── test/
│       ├── java/com/parabank/
│       │   ├── base/
│       │   ├── listeners/
│       │   └── tests/
│       └── resources/
│           ├── config.properties
│           └── log4j2.xml
├── pom.xml
├── testng.xml
└── README.md
```

## Prerequisites

Install the following before running the tests:

- Java Development Kit (JDK) 21 or later
- Apache Maven 3.9 or later
- A supported browser, such as Google Chrome
- Git (optional, for cloning the repository)

Verify your Java and Maven installations:

```bash
java -version
mvn -version
```

## Installation

Clone the repository and navigate to the project directory:

```bash
git clone <YOUR_REPOSITORY_URL>
cd parabank-selenium-framework
```

Replace `<YOUR_REPOSITORY_URL>` with your GitHub repository URL.

## Running Tests

### Run the Full Test Suite

```bash
mvn clean test
```

### Run in Headless Mode

Useful for CI environments:

```bash
mvn clean test -Dheadless=true
```

### Run Tests in Firefox

```bash
mvn clean test -Dbrowser=firefox
```

### Run a Specific Test Class

```bash
mvn clean test -Dtest=LoginTest
```

```bash
mvn clean test -Dtest=TransferFundsTest
```

### Run Smoke Tests

```bash
mvn clean test -Dgroups=smoke
```

### Run Regression Tests

```bash
mvn clean test -Dgroups=regression
```

### Configure Explicit Waits and Retries

```bash
mvn clean test -Dexplicit.wait=20 -Dretry.count=2
```

Available configuration options are defined in `src/test/resources/config.properties`. Adjust the commands to match the property names supported by your implementation.

## Reports and Logs

After execution, the framework is configured to generate diagnostic artifacts under `test-output/`:

- **HTML Report:** `test-output/reports/ExecutionReport.html`
- **Execution Logs:** `test-output/logs/execution.log`
- **Failure Screenshots:** `test-output/screenshots/`

Open the HTML report in a browser to review test results, execution details, and failure evidence.

## CI/CD Integration

The GitHub Actions workflow is located at:

```text
.github/workflows/test-pipeline.yml
```

The documented pipeline is designed to execute tests on pushes and pull requests, use Java 21 and headless Chrome, and archive reports, screenshots, and logs as workflow artifacts.

Check the workflow configuration in the repository to confirm its triggers, browser setup, and artifact retention settings.

## Engineering Practices Demonstrated

- Separation of concerns using the Page Object Model
- Reusable page interaction methods
- Explicit synchronization for asynchronous UI elements
- Isolated test data and independent test scenarios
- Parallel execution with thread-safe browser management
- Data-driven validation and configurable retry logic
- Centralized logging and automated failure reporting
- CI/CD-oriented test execution

## Target Application

[ParaBank Demo Banking Application](https://parabank.parasoft.com/parabank/index.htm)

**Note:** ParaBank is a demo application. Environment availability, server behavior, and test results may vary. The framework's database-reset functionality depends on the availability and behavior of the application's administrative endpoint.

## Author

Developed as a QA automation portfolio project to demonstrate Java, Selenium WebDriver, TestNG, and practical end-to-end testing techniques.
Made by Vusal Qarayev