package com.parabank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AccountOverviewPage extends BasePage {

    private final By titleHeader = By.xpath("//h1[contains(text(),'Accounts Overview')]");
    private final By logoutLink = By.linkText("Log Out");
    private final By openNewAccountLink = By.linkText("Open New Account");
    private final By accountsOverviewLink = By.linkText("Accounts Overview");
    private final By transferFundsLink = By.linkText("Transfer Funds");
    private final By billPayLink = By.linkText("Bill Pay");
    private final By findTransactionsLink = By.linkText("Find Transactions");
    private final By requestLoanLink = By.linkText("Request Loan");
    private final By accountTableRows = By.cssSelector("#accountTable tbody tr");
    private final By totalBalanceCell = By.xpath("//td[b[text()='Total']]/following-sibling::td[1]");

    public AccountOverviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return isDisplayed(titleHeader);
    }

    public LoginPage logout() {
        log.info("Logging out current user session");
        click(logoutLink);
        LoginPage loginPage = new LoginPage(driver);
        loginPage.waitForLoginForm();
        return loginPage;
    }

    public OpenAccountPage navigateToOpenAccount() {
        log.info("Navigating to Open New Account page");
        click(openNewAccountLink);
        return new OpenAccountPage(driver);
    }

    public TransferFundsPage navigateToTransferFunds() {
        log.info("Navigating to Transfer Funds page");
        click(transferFundsLink);
        return new TransferFundsPage(driver);
    }

    public BillPayPage navigateToBillPay() {
        log.info("Navigating to Bill Pay page");
        click(billPayLink);
        return new BillPayPage(driver);
    }

    public FindTransactionsPage navigateToFindTransactions() {
        log.info("Navigating to Find Transactions page");
        click(findTransactionsLink);
        return new FindTransactionsPage(driver);
    }

    public RequestLoanPage navigateToRequestLoan() {
        log.info("Navigating to Request Loan page");
        click(requestLoanLink);
        return new RequestLoanPage(driver);
    }

    public AccountOverviewPage refreshOverview() {
        log.info("Refreshing Accounts Overview");
        click(accountsOverviewLink);
        waitForVisibility(titleHeader);
        return this;
    }

    public Map<String, Double> getAllAccountBalances() {
        wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(accountTableRows));
        Map<String, Double> balances = new HashMap<>();
        List<WebElement> rows = driver.findElements(accountTableRows);

        for (WebElement row : rows) {
            List<WebElement> cells = row.findElements(By.tagName("td"));
            if (cells.size() < 2) {
                continue;
            }
            String accountId = cells.get(0).getText().trim();
            if (!accountId.matches("\\d+")) {
                continue;
            }
            balances.put(accountId, parseCurrency(cells.get(1).getText()));
        }
        log.info("Extracted account balances: {}", balances);
        return balances;
    }

    public double getDisplayedTotalBalance() {
        return parseCurrency(getText(totalBalanceCell));
    }

    private double parseCurrency(String rawAmount) {
        return Double.parseDouble(rawAmount.replace("$", "").replace(",", "").trim());
    }
}