package com.parabank.tests;

import com.parabank.base.BaseTest;
import com.parabank.models.UserModel;
import com.parabank.pages.AccountOverviewPage;
import com.parabank.pages.LoginPage;
import com.parabank.pages.OpenAccountPage;
import com.parabank.pages.RegisterPage;
import com.parabank.pages.TransferFundsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransferFundsTest extends BaseTest {

    @Test(groups = {"smoke", "regression"}, description = "TC-06: Verify fund transfer execution and post-transaction balance reconciliation")
    public void testFundsTransferAndBalanceReconciliation() {
        // Step 1: Register dynamic user
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();
        UserModel user = UserModel.generateRandomUser();
        registerPage.registerNewUser(user);

        AccountOverviewPage overviewPage = new AccountOverviewPage(getDriver());

        // Step 2: Open a secondary account to guarantee two accounts exist
        OpenAccountPage openAccountPage = overviewPage.navigateToOpenAccount();
        String secondaryAccountId = openAccountPage.openNewAccount("CHECKING");

        // Step 3: Capture starting balances for both accounts
        overviewPage.refreshOverview();
        Map<String, Double> initialBalances = overviewPage.getAllAccountBalances();
        List<String> accountIds = new ArrayList<>(initialBalances.keySet());
        Assert.assertTrue(accountIds.size() >= 2, "Expected at least 2 accounts for transfer test.");

        String fromAccount = accountIds.get(0);
        String toAccount = accountIds.get(1);
        double transferAmount = 50;

        double initialFromBalance = initialBalances.get(fromAccount);
        double initialToBalance = initialBalances.get(toAccount);

        // Step 4: Execute the transfer
        TransferFundsPage transferPage = overviewPage.navigateToTransferFunds();
        transferPage.transferFunds(transferAmount, fromAccount, toAccount);

        // Step 5: Verify confirmation screen details
        Assert.assertTrue(transferPage.isTransferCompleteDisplayed(),
                "Transfer Complete header was not displayed.");
        Assert.assertEquals(transferPage.getConfirmedAmount(), transferAmount, 0.01,
                "Confirmed transfer amount mismatch.");
        Assert.assertEquals(transferPage.getConfirmedFromAccount(), fromAccount,
                "Confirmed source account mismatch.");
        Assert.assertEquals(transferPage.getConfirmedToAccount(), toAccount,
                "Confirmed destination account mismatch.");

        // Step 6: Verify balance math on Overview page
        overviewPage.refreshOverview();
        Map<String, Double> finalBalances = overviewPage.getAllAccountBalances();

        double expectedFromBalance = initialFromBalance - transferAmount;
        double expectedToBalance = initialToBalance + transferAmount;

        double actualFromBalance = finalBalances.get(fromAccount);
        double actualToBalance = finalBalances.get(toAccount);

        Assert.assertEquals(actualFromBalance, expectedFromBalance, 0.01,
                String.format("Source account %s balance did not deduct transfer amount. Expected: %.2f, Actual: %.2f",
                        fromAccount, expectedFromBalance, actualFromBalance));

        Assert.assertEquals(actualToBalance, expectedToBalance, 0.01,
                String.format("Destination account %s balance did not credit transfer amount. Expected: %.2f, Actual: %.2f",
                        toAccount, expectedToBalance, actualToBalance));
    }
}