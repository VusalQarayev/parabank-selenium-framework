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

public class RequestLoanPage extends BasePage {

    // Input fields
    private final By loanAmountField = By.id("amount");
    private final By downPaymentField = By.id("downPayment");
    private final By fromAccountSelect = By.id("fromAccountId");
    private final By applyButton = By.xpath("//input[@value='Apply Now']");

    // Results container
    private final By resultHeader = By.xpath("//h1[contains(.,'Loan Request Processed')]");
    private final By loanStatusText = By.id("loanStatus");
    private final By newAccountIdLink = By.id("newAccountId");
    private final By errorMessageText = By.xpath("//div[@id='loanRequestResult']//p[@class='error']");

    public RequestLoanPage(WebDriver driver) {
        super(driver);
    }

    public void enterLoanAmount(double amount) {
        log.info("Entering loan amount: {}", amount);
        sendKeys(loanAmountField, String.format(Locale.US, "%.2f", amount));
    }

    public void enterDownPayment(double downPayment) {
        log.info("Entering down payment: {}", downPayment);
        sendKeys(downPaymentField, String.format(Locale.US, "%.2f", downPayment));
    }

    public void selectFromAccount(String accountId) {
        log.info("Selecting source account: {}", accountId);
        waitForDropdownOptions(fromAccountSelect);
        new Select(driver.findElement(fromAccountSelect)).selectByVisibleText(accountId);
    }

    public void clickApply() {
        log.info("Submitting loan application");
        click(applyButton);
    }

    public void applyForLoan(double amount, double downPayment, String fromAccountId) {
        enterLoanAmount(amount);
        enterDownPayment(downPayment);
        selectFromAccount(fromAccountId);
        clickApply();

        WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        extendedWait.until(ExpectedConditions.visibilityOfElementLocated(resultHeader));
        extendedWait.until(ExpectedConditions.visibilityOfElementLocated(loanStatusText));
    }

    public String getLoanStatus() {
        return getText(loanStatusText);
    }

    public boolean isLoanApproved() {
        return "Approved".equalsIgnoreCase(getLoanStatus());
    }

    public String getGeneratedLoanAccountId() {
        return getText(newAccountIdLink);
    }

    public String getLoanErrorMessage() {
        return getText(errorMessageText);
    }

    private void waitForDropdownOptions(By selectLocator) {
        wait.until(driver -> {
            WebElement selectElement = driver.findElement(selectLocator);
            List<WebElement> options = selectElement.findElements(By.tagName("option"));
            return !options.isEmpty() && !options.get(0).getText().trim().isEmpty();
        });
    }
}