package com.parabank.pages;

import com.parabank.models.UserModel;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class RegisterPage extends BasePage {

    // Input fields
    private final By firstNameField = By.id("customer.firstName");
    private final By lastNameField = By.id("customer.lastName");
    private final By addressField = By.id("customer.address.street");
    private final By cityField = By.id("customer.address.city");
    private final By stateField = By.id("customer.address.state");
    private final By zipCodeField = By.id("customer.address.zipCode");
    private final By phoneField = By.id("customer.phoneNumber");
    private final By ssnField = By.id("customer.ssn");
    private final By usernameField = By.id("customer.username");
    private final By passwordField = By.id("customer.password");
    private final By confirmPasswordField = By.id("repeatedPassword");
    private final By registerButton = By.xpath("//input[@value='Register']");

    // Success elements
    private final By welcomeHeader = By.xpath("//div[@id='rightPanel']/h1[@class='title']");
    private final By successMessage = By.xpath("//div[@id='rightPanel']/p");

    // Inline field validation error locators (TC-02)
    private final By firstNameError = By.id("customer.firstName.errors");
    private final By lastNameError = By.id("customer.lastName.errors");
    private final By addressError = By.id("customer.address.street.errors");
    private final By cityError = By.id("customer.address.city.errors");
    private final By stateError = By.id("customer.address.state.errors");
    private final By zipCodeError = By.id("customer.address.zipCode.errors");
    private final By ssnError = By.id("customer.ssn.errors");
    private final By usernameError = By.id("customer.username.errors");
    private final By passwordError = By.id("customer.password.errors");
    private final By confirmPasswordError = By.id("repeatedPassword.errors");

    public RegisterPage(WebDriver driver) {
        super(driver);
    }

    public void fillRegistrationForm(UserModel user) {
        log.info("Populating registration fields for: {}", user.getUsername());
        sendKeys(firstNameField, user.getFirstName());
        sendKeys(lastNameField, user.getLastName());
        sendKeys(addressField, user.getStreet());
        sendKeys(cityField, user.getCity());
        sendKeys(stateField, user.getState());
        sendKeys(zipCodeField, user.getZipCode());
        sendKeys(phoneField, user.getPhone());
        sendKeys(ssnField, user.getSsn());
        sendKeys(usernameField, user.getUsername());
        sendKeys(passwordField, user.getPassword());
        sendKeys(confirmPasswordField, user.getPassword());
    }

    public void enterConfirmPassword(String confirmPassword) {
        sendKeys(confirmPasswordField, confirmPassword);
    }

    public void clickRegister() {
        log.info("Submitting registration form");
        click(registerButton);
    }

    public void registerNewUser(UserModel user) {
        fillRegistrationForm(user);
        clickRegister();
        // Wait for confirmation header to guarantee registration is saved on the server
        waitForVisibility(welcomeHeader);
    }

    public String getWelcomeTitle() {
        return getText(welcomeHeader);
    }

    public String getSuccessConfirmationMessage() {
        return getText(successMessage);
    }

    public String getFirstNameError() { return getText(firstNameError); }
    public String getLastNameError() { return getText(lastNameError); }
    public String getAddressError() { return getText(addressError); }
    public String getCityError() { return getText(cityError); }
    public String getStateError() { return getText(stateError); }
    public String getZipCodeError() { return getText(zipCodeError); }
    public String getSsnError() { return getText(ssnError); }
    public String getUsernameError() { return getText(usernameError); }
    public String getPasswordError() { return getText(passwordError); }
    public String getConfirmPasswordError() { return getText(confirmPasswordError); }
}