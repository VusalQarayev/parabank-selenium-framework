package com.parabank.tests;

import com.parabank.base.BaseTest;
import com.parabank.models.PayeeModel;
import com.parabank.models.UserModel;
import com.parabank.pages.AccountOverviewPage;
import com.parabank.pages.BillPayPage;
import com.parabank.pages.LoginPage;
import com.parabank.pages.RegisterPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class BillPayTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "TC-07: Verify bill payment execution and account debit reconciliation")
    public void testBillPaymentExecutionAndBalanceDeduction() {
        // Step 1: Register dynamic user
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();
        UserModel user = UserModel.generateRandomUser();
        registerPage.registerNewUser(user);

        // Step 2: Navigate from register.htm to overview.htm and capture initial balance
        AccountOverviewPage overviewPage = new AccountOverviewPage(getDriver());
        overviewPage.refreshOverview(); // Navigates to overview.htm where #accountTable exists

        Map<String, Double> initialBalances = overviewPage.getAllAccountBalances();
        Assert.assertFalse(initialBalances.isEmpty(), "User should have at least one initialized account.");

        String fundingAccountId = initialBalances.keySet().iterator().next();
        double initialBalance = initialBalances.get(fundingAccountId);
        double billAmount = 35.50;

        // Step 3: Fill and submit bill pay form
        PayeeModel payee = PayeeModel.generateRandomPayee();
        BillPayPage billPayPage = overviewPage.navigateToBillPay();
        billPayPage.sendBillPayment(payee, billAmount, fundingAccountId);

        // Step 4: Verify confirmation view details
        Assert.assertTrue(billPayPage.isPaymentCompleteDisplayed(),
                "Bill Payment Complete confirmation header was not displayed.");
        Assert.assertEquals(billPayPage.getConfirmedPayeeName(), payee.getName(),
                "Confirmed payee name did not match submitted data.");
        Assert.assertEquals(billPayPage.getConfirmedAmount(), billAmount, 0.01,
                "Confirmed payment amount mismatch.");
        Assert.assertEquals(billPayPage.getConfirmedFromAccount(), fundingAccountId,
                "Confirmed funding account ID mismatch.");

        // Step 5: Verify post-payment balance on Overview page
        overviewPage.refreshOverview();
        Map<String, Double> updatedBalances = overviewPage.getAllAccountBalances();

        double expectedBalance = initialBalance - billAmount;
        double actualBalance = updatedBalances.get(fundingAccountId);

        Assert.assertEquals(actualBalance, expectedBalance, 0.01,
                String.format("Funding account %s balance did not deduct bill amount. Expected: %.2f, Actual: %.2f",
                        fundingAccountId, expectedBalance, actualBalance));
    }
}