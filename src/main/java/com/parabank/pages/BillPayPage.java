package com.parabank.pages;

import com.parabank.models.PayeeModel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.Locale;

public class BillPayPage extends BasePage {

    // Input fields
    private final By payeeNameField = By.name("payee.name");
    private final By addressField = By.name("payee.address.street");
    private final By cityField = By.name("payee.address.city");
    private final By stateField = By.name("payee.address.state");
    private final By zipCodeField = By.name("payee.address.zipCode");
    private final By phoneField = By.name("payee.phoneNumber");
    private final By accountNumberField = By.name("payee.accountNumber");
    private final By verifyAccountField = By.name("verifyAccount");
    private final By amountField = By.name("amount");
    private final By fromAccountSelect = By.name("fromAccountId");
    private final By sendPaymentButton = By.xpath("//input[@value='Send Payment']");

    // Confirmation container elements
    private final By billPayResultHeader = By.xpath("//div[@id='billpayResult']//h1[contains(.,'Bill Payment Complete')]");
    private final By payeeNameResult = By.id("payeeName");
    private final By amountResult = By.id("amount");
    private final By fromAccountIdResult = By.id("fromAccountId");

    public BillPayPage(WebDriver driver) {
        super(driver);
    }

    public void fillPayeeDetails(PayeeModel payee) {
        log.info("Entering payee details for: {}", payee.getName());
        sendKeys(payeeNameField, payee.getName());
        sendKeys(addressField, payee.getStreet());
        sendKeys(cityField, payee.getCity());
        sendKeys(stateField, payee.getState());
        sendKeys(zipCodeField, payee.getZipCode());
        sendKeys(phoneField, payee.getPhone());
        sendKeys(accountNumberField, payee.getAccountNumber());
        sendKeys(verifyAccountField, payee.getAccountNumber());
    }

    public void enterAmount(double amount) {
        log.info("Entering payment amount: {}", amount);
        sendKeys(amountField, String.format(Locale.US, "%.2f", amount));
    }

    public void selectFromAccount(String accountId) {
        log.info("Selecting source account for bill pay: {}", accountId);
        waitForDropdownOptions(fromAccountSelect);
        new Select(driver.findElement(fromAccountSelect)).selectByVisibleText(accountId);
    }

    public void clickSendPayment() {
        log.info("Submitting bill payment form");
        click(sendPaymentButton);
    }

    public void sendBillPayment(PayeeModel payee, double amount, String fromAccount) {
        fillPayeeDetails(payee);
        enterAmount(amount);
        selectFromAccount(fromAccount);
        clickSendPayment();

        WebDriverWait extendedWait = new WebDriverWait(driver, Duration.ofSeconds(15));
        extendedWait.until(ExpectedConditions.visibilityOfElementLocated(billPayResultHeader));
    }

    public boolean isPaymentCompleteDisplayed() {
        return isDisplayed(billPayResultHeader);
    }

    public String getConfirmedPayeeName() {
        return getText(payeeNameResult);
    }

    public double getConfirmedAmount() {
        String raw = getText(amountResult).replace("$", "").replace(",", "").trim();
        return Double.parseDouble(raw);
    }

    public String getConfirmedFromAccount() {
        return getText(fromAccountIdResult);
    }

    private void waitForDropdownOptions(By selectLocator) {
        wait.until(driver -> {
            WebElement selectElement = driver.findElement(selectLocator);
            List<WebElement> options = selectElement.findElements(By.tagName("option"));
            return !options.isEmpty() && !options.get(0).getText().trim().isEmpty();
        });
    }
}