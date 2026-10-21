package com.parabank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class TransferFundsPage extends BasePage {

    private final By amountField = By.id("amount");
    private final By fromAccountSelect = By.id("fromAccountId");
    private final By toAccountSelect = By.id("toAccountId");
    private final By transferButton = By.xpath("//input[@value='Transfer']");

    // Use contains(., ...) instead of text() to evaluate the full string regardless of whitespace or child tags
    private final By transferCompleteHeader = By.xpath("//h1[contains(.,'Transfer Complete')]");
    private final By resultAmountText = By.id("amountResult");
    private final By fromAccountResultText = By.id("fromAccountIdResult");
    private final By toAccountResultText = By.id("toAccountIdResult");

    public TransferFundsPage(WebDriver driver) {
        super(driver);
    }

    public void enterAmount(double amount) {
        log.info("Entering transfer amount: {}", amount);
        // Force Locale.US so the decimal separator is always a period '.'
        sendKeys(amountField, String.format(Locale.US, "%.2f", amount));
    }

    public void selectFromAccount(String accountId) {
        log.info("Selecting source account: {}", accountId);
        waitForDropdownOptions(fromAccountSelect);
        new Select(driver.findElement(fromAccountSelect)).selectByVisibleText(accountId);
    }

    public void selectToAccount(String accountId) {
        log.info("Selecting destination account: {}", accountId);
        waitForDropdownOptions(toAccountSelect);
        new Select(driver.findElement(toAccountSelect)).selectByVisibleText(accountId);
    }

    public void clickTransfer() {
        log.info("Clicking Transfer button");
        click(transferButton);
    }

    public void transferFunds(double amount, String fromAccount, String toAccount) {
        enterAmount(amount);
        selectFromAccount(fromAccount);
        selectToAccount(toAccount);
        clickTransfer();

        // ParaBank's public demo backend can take 10-15s to process transactions
        WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(20));
        extendedWait.until(ExpectedConditions.visibilityOfElementLocated(transferCompleteHeader));
    }

    public boolean isTransferCompleteDisplayed() {
        return isDisplayed(transferCompleteHeader);
    }

    public double getConfirmedAmount() {
        String raw = getText(resultAmountText).replace("$", "").replace(",", "").trim();
        return Double.parseDouble(raw);
    }

    public String getConfirmedFromAccount() {
        return getText(fromAccountResultText);
    }

    public String getConfirmedToAccount() {
        return getText(toAccountResultText);
    }

    private void waitForDropdownOptions(By selectLocator) {
        wait.until(driver -> {
            WebElement selectElement = driver.findElement(selectLocator);
            List<WebElement> options = selectElement.findElements(By.tagName("option"));
            return options.size() >= 2 && !options.get(0).getText().trim().isEmpty();
        });
    }
}