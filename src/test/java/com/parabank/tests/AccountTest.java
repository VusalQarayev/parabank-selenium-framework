package com.parabank.tests;

import com.parabank.base.BaseTest;
import com.parabank.models.UserModel;
import com.parabank.pages.AccountOverviewPage;
import com.parabank.pages.LoginPage;
import com.parabank.pages.OpenAccountPage;
import com.parabank.pages.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class AccountTest extends BaseTest {

    private AccountOverviewPage registerAndLoginFreshUser() {
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();
        UserModel user = UserModel.generateRandomUser();
        registerPage.registerNewUser(user);
        return new AccountOverviewPage(getDriver());
    }

    @Test(groups = {"smoke", "regression"}, description = "TC-04: Verify opening a new secondary Savings account")
    public void testOpenNewSavingsAccount() {
        AccountOverviewPage overviewPage = registerAndLoginFreshUser();
        OpenAccountPage openAccountPage = overviewPage.navigateToOpenAccount();

        String newAccountId = openAccountPage.openNewAccount("SAVINGS");

        Assert.assertTrue(openAccountPage.isSuccessHeaderDisplayed(),
                "Account Opened confirmation header was not displayed.");
        Assert.assertNotNull(newAccountId, "Generated account ID should not be null.");
        Assert.assertTrue(newAccountId.matches("\\d+"),
                String.format("Expected generated account ID to be numeric, but got '%s'", newAccountId));
    }

    @Test(groups = {"regression"}, description = "TC-05: Verify account table dynamic parsing and total balance calculation")
    public void testAccountOverviewBalanceCalculation() {
        AccountOverviewPage overviewPage = registerAndLoginFreshUser();

        // Open a secondary account to guarantee multiple accounts in the overview table
        OpenAccountPage openAccountPage = overviewPage.navigateToOpenAccount();
        openAccountPage.openNewAccount("CHECKING");

        // Navigate back to overview to parse the populated table
        overviewPage.refreshOverview();
        Map<String, Double> accountBalances = overviewPage.getAllAccountBalances();

        Assert.assertTrue(accountBalances.size() >= 2,
                "Overview table should contain at least 2 accounts.");

        // Calculate expected sum across individual account rows
        double calculatedTotal = accountBalances.values()
                .stream()
                .mapToDouble(Double::doubleValue)
                .sum();

        double displayedTotal = overviewPage.getDisplayedTotalBalance();

        Assert.assertEquals(displayedTotal, calculatedTotal, 0.01,
                String.format("Displayed total balance (%.2f) does not match sum of individual accounts (%.2f)",
                        displayedTotal, calculatedTotal));
    }
}