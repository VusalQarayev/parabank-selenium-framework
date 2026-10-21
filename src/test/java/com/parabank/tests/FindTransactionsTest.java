package com.parabank.tests;

import com.parabank.base.BaseTest;
import com.parabank.models.UserModel;
import com.parabank.pages.AccountOverviewPage;
import com.parabank.pages.FindTransactionsPage;
import com.parabank.pages.LoginPage;
import com.parabank.pages.OpenAccountPage;
import com.parabank.pages.RegisterPage;
import com.parabank.pages.TransferFundsPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FindTransactionsTest extends BaseTest {

    @Test(groups = {"regression"}, description = "TC-08: Verify transaction search by exact transaction amount")
    public void testFindTransactionsByAmount() {
        // Step 1: Register dynamic user
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();
        UserModel user = UserModel.generateRandomUser();
        registerPage.registerNewUser(user);

        // Step 2: Open a secondary account
        AccountOverviewPage overviewPage = new AccountOverviewPage(getDriver());
        OpenAccountPage openAccountPage = overviewPage.navigateToOpenAccount();
        String secondaryAccountId = openAccountPage.openNewAccount("CHECKING");

        overviewPage.refreshOverview();
        Map<String, Double> balances = overviewPage.getAllAccountBalances();
        List<String> accountIds = new ArrayList<>(balances.keySet());
        String sourceAccountId = accountIds.get(0);

        // Step 3: Execute transfer with an exact, unique amount
        double searchAmount = 17.75;
        TransferFundsPage transferPage = overviewPage.navigateToTransferFunds();
        transferPage.transferFunds(searchAmount, sourceAccountId, secondaryAccountId);

        // Step 4: Search for the transaction on the receiving account
        FindTransactionsPage findTransactionsPage = overviewPage.navigateToFindTransactions();
        findTransactionsPage.selectAccount(secondaryAccountId);
        findTransactionsPage.searchByAmount(searchAmount);

        // Step 5: Verify transaction presence and amount in results
        Assert.assertTrue(findTransactionsPage.hasTransactionResults(),
                "Transaction results table was empty.");

        List<Double> recordedAmounts = findTransactionsPage.getTransactionAmounts();
        Assert.assertTrue(recordedAmounts.contains(searchAmount),
                String.format("Expected transaction amount %.2f not found in results: %s",
                        searchAmount, recordedAmounts));
    }
}