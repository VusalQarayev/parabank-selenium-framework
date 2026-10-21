package com.parabank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

public class OpenAccountPage extends BasePage {

    private final By accountTypeSelect = By.id("type");
    private final By fromAccountIdSelect = By.id("fromAccountId");
    private final By openAccountButton = By.xpath("//input[@value='Open New Account']");
    private final By accountOpenedHeader = By.xpath("//h1[contains(text(),'Account Opened!')]");
    private final By newAccountIdLink = By.id("newAccountId");

    public OpenAccountPage(WebDriver driver) {
        super(driver);
    }

    public void selectAccountType(String accountType) {
        log.info("Selecting account type: {}", accountType);
        WebElement dropdown = waitForVisibility(accountTypeSelect);
        new Select(dropdown).selectByVisibleText(accountType);
    }

    public void selectSourceAccount(String sourceAccountId) {
        log.info("Selecting source account: {}", sourceAccountId);
        WebElement dropdown = waitForVisibility(fromAccountIdSelect);
        new Select(dropdown).selectByVisibleText(sourceAccountId);
    }

    public void clickOpenAccountButton() {
        log.info("Submitting Open New Account form");
        click(openAccountButton);
    }

    public String openNewAccount(String accountType) {
        selectAccountType(accountType);
        // Synchronize on the source account dropdown having at least one populated option
        wait.until(ExpectedConditions.presenceOfNestedElementsLocatedBy(fromAccountIdSelect, By.tagName("option")));
        clickOpenAccountButton();
        waitForVisibility(accountOpenedHeader);
        return getText(newAccountIdLink);
    }

    public boolean isSuccessHeaderDisplayed() {
        return isDisplayed(accountOpenedHeader);
    }

    public String getGeneratedAccountId() {
        return getText(newAccountIdLink);
    }
}