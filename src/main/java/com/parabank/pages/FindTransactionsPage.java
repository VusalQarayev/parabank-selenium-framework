package com.parabank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FindTransactionsPage extends BasePage {

    // Account selector
    private final By accountSelect = By.id("accountId");

    // Search by Amount (supports id="amount" as well as name="criteria.amount")
    private final By amountField = By.xpath("//input[@id='amount' or @name='criteria.amount' or @name='amount']");
    private final By findByAmountButton = By.id("findByAmount");

    // Search by Date
    private final By onDateField = By.xpath("//input[@id='onDate' or @name='criteria.onDate' or @name='onDate']");
    private final By findByDateButton = By.id("findByDate");

    // Results table (tr[td] targets only data rows, safely excluding header th rows)
    private final By resultsTable = By.id("transactionTable");
    private final By resultDataRows = By.xpath("//table[@id='transactionTable']//tr[td]");

    public FindTransactionsPage(WebDriver driver) {
        super(driver);
    }

    public void selectAccount(String accountId) {
        log.info("Selecting account for transaction search: {}", accountId);
        waitForDropdownOptions(accountSelect);
        new Select(driver.findElement(accountSelect)).selectByVisibleText(accountId);
    }

    public void searchByAmount(double amount) {
        log.info("Searching transactions by amount: {}", amount);
        sendKeys(amountField, String.format(Locale.US, "%.2f", amount));
        click(findByAmountButton);
        waitForResultsTable();
    }

    public void searchByDate(String dateFormatted) {
        log.info("Searching transactions by date: {}", dateFormatted);
        sendKeys(onDateField, dateFormatted);
        click(findByDateButton);
        waitForResultsTable();
    }

    public List<Double> getTransactionAmounts() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(resultDataRows));
        List<Double> amounts = new ArrayList<>();
        List<WebElement> rows = driver.findElements(resultDataRows);

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            // Columns: Date (0) | Transaction Description (1) | Debit (-) (2) | Credit (+) (3)
            if (cells.size() >= 3) {
                String debitText = cells.get(2).getText().replace("\u00A0", "").trim();
                String creditText = cells.size() >= 4 ? cells.get(3).getText().replace("\u00A0", "").trim() : "";

                if (debitText.matches(".*\\d.*")) {
                    amounts.add(parseCurrency(debitText));
                } else if (creditText.matches(".*\\d.*")) {
                    amounts.add(parseCurrency(creditText));
                }
            }
        }
        log.info("Extracted transaction amounts: {}", amounts);
        return amounts;
    }

    public boolean hasTransactionResults() {
        try {
            waitForResultsTable();
            return !driver.findElements(resultDataRows).isEmpty();
        } catch (Exception e) {
            return false;
        }
    }

    private void waitForResultsTable() {
        WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        extendedWait.until(ExpectedConditions.visibilityOfElementLocated(resultsTable));
        extendedWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(resultDataRows));
    }

    private void waitForDropdownOptions(By selectLocator) {
        wait.until(driver -> {
            WebElement selectElement = driver.findElement(selectLocator);
            List<WebElement> options = selectElement.findElements(By.tagName("option"));
            return !options.isEmpty() && !options.get(0).getText().trim().isEmpty();
        });
    }

    private double parseCurrency(String rawAmount) {
        return Double.parseDouble(rawAmount.replace("$", "").replace(",", "").trim());
    }
}