package com.parabank.tests;

import com.parabank.base.BaseTest;
import com.parabank.models.UserModel;
import com.parabank.pages.AccountOverviewPage;
import com.parabank.pages.LoginPage;
import com.parabank.pages.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "TC-03A: Verify valid authentication with dynamically provisioned account")
    public void testValidUserLogin() {
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();
        UserModel validUser = UserModel.generateRandomUser();
        registerPage.registerNewUser(validUser);

        AccountOverviewPage overviewPage = new AccountOverviewPage(getDriver());
        loginPage = overviewPage.logout();

        overviewPage = loginPage.loginAs(validUser.getUsername(), validUser.getPassword());
        Assert.assertTrue(overviewPage.isLoaded(),
                "Accounts Overview should load after logging in with valid credentials.");
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] getInvalidLoginScenarios() {
        return new Object[][] {
                {"", "Password123", "Please enter a username and password."},
                {"validUser", "", "Please enter a username and password."},
                {"", "", "Please enter a username and password."},
                {"non_existent_user_999", "wrong_pass_999", "The username and password could not be verified."}
        };
    }

    @Test(dataProvider = "invalidLoginData", groups = {"regression"},
            description = "TC-03B: Verify negative login permutations via DataProvider")
    public void testInvalidLoginPermutations(String username, String password, String expectedError) {
        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.loginAs(username, password);

        String actualError = loginPage.getErrorMessage();
        Assert.assertTrue(actualError.contains(expectedError),
                String.format("Expected error containing '%s', but found '%s'", expectedError, actualError));
    }
}