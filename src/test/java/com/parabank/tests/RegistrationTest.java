package com.parabank.tests;

import com.parabank.base.BaseTest;
import com.parabank.models.UserModel;
import com.parabank.pages.AccountOverviewPage;
import com.parabank.pages.LoginPage;
import com.parabank.pages.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

public class RegistrationTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "TC-01: Verify dynamic user registration and re-login")
    public void testDynamicUserRegistration() {
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();

        UserModel randomUser = UserModel.generateRandomUser();
        registerPage.registerNewUser(randomUser);

        Assert.assertEquals(registerPage.getWelcomeTitle(), "Welcome " + randomUser.getUsername(),
                "Welcome header did not match registered username.");
        Assert.assertTrue(registerPage.getSuccessConfirmationMessage()
                        .contains("Your account was created successfully. You are now logged in."),
                "Account creation confirmation message was not displayed.");

        AccountOverviewPage overviewPage = new AccountOverviewPage(getDriver());
        overviewPage.logout();

        loginPage.loginAs(randomUser.getUsername(), randomUser.getPassword());
        Assert.assertTrue(overviewPage.isLoaded(),
                "Newly registered user should be able to log back in and view Accounts Overview.");
    }

    @Test(groups = {"regression"}, description = "TC-02A: Verify required field error messages on empty registration submit")
    public void testEmptyRegistrationFormValidation() {
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();

        registerPage.clickRegister();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertEquals(registerPage.getFirstNameError(), "First name is required.");
        softAssert.assertEquals(registerPage.getLastNameError(), "Last name is required.");
        softAssert.assertEquals(registerPage.getAddressError(), "Address is required.");
        softAssert.assertEquals(registerPage.getCityError(), "City is required.");
        softAssert.assertEquals(registerPage.getStateError(), "State is required.");
        softAssert.assertEquals(registerPage.getZipCodeError(), "Zip Code is required.");
        softAssert.assertEquals(registerPage.getSsnError(), "Social Security Number is required.");
        softAssert.assertEquals(registerPage.getUsernameError(), "Username is required.");
        softAssert.assertEquals(registerPage.getPasswordError(), "Password is required.");
        softAssert.assertEquals(registerPage.getConfirmPasswordError(), "Password confirmation is required.");
        softAssert.assertAll();
    }

    @Test(groups = {"regression"}, description = "TC-02B: Verify error message when password and confirmation do not match")
    public void testPasswordMismatchValidation() {
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();

        UserModel user = UserModel.generateRandomUser();
        registerPage.fillRegistrationForm(user);
        registerPage.enterConfirmPassword("MismatchedPassword999!");
        registerPage.clickRegister();

        Assert.assertEquals(registerPage.getConfirmPasswordError(), "Passwords did not match.",
                "Expected password mismatch validation error.");
    }
}