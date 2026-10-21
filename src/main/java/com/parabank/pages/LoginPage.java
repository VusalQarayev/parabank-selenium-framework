package com.parabank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {

    private final By usernameField = By.name("username");
    private final By passwordField = By.name("password");
    private final By loginButton = By.xpath("//input[@value='Log In']");
    private final By registerLink = By.linkText("Register");
    private final By errorMessage = By.cssSelector("#rightPanel p.error");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void waitForLoginForm() {
        waitForVisibility(loginButton);
        waitForVisibility(usernameField);
    }

    public void enterUsername(String username) {
        sendKeys(usernameField, username);
    }

    public void enterPassword(String password) {
        sendKeys(passwordField, password);
    }

    public void clickLoginButton() {
        click(loginButton);
    }

    public AccountOverviewPage loginAs(String username, String password) {
        log.info("Attempting login with username: '{}'", username);
        waitForLoginForm();
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
        return new AccountOverviewPage(driver);
    }

    public RegisterPage navigateToRegister() {
        log.info("Navigating to Registration form");
        click(registerLink);
        return new RegisterPage(driver);
    }

    public String getErrorMessage() {
        return getText(errorMessage);
    }
}