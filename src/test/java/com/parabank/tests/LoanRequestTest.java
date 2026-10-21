package com.parabank.tests;

import com.parabank.base.BaseTest;
import com.parabank.models.UserModel;
import com.parabank.pages.AccountOverviewPage;
import com.parabank.pages.LoginPage;
import com.parabank.pages.RegisterPage;
import com.parabank.pages.RequestLoanPage;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class LoanRequestTest extends BaseTest {

    private AccountOverviewPage registerFreshUser() {
        LoginPage loginPage = new LoginPage(getDriver());
        RegisterPage registerPage = loginPage.navigateToRegister();
        UserModel user = UserModel.generateRandomUser();
        registerPage.registerNewUser(user);

        AccountOverviewPage overviewPage = new AccountOverviewPage(getDriver());
        overviewPage.refreshOverview();
        return overviewPage;
    }

    @Test(groups = {"smoke", "regression"}, description = "TC-09A: Verify approved loan request with sufficient down payment")
    public void testLoanRequestApproval() {
        AccountOverviewPage overviewPage = registerFreshUser();

        Map<String, Double> balances = overviewPage.getAllAccountBalances();
        Assert.assertFalse(balances.isEmpty(), "User should have an initialized funding account.");
        String fundingAccountId = balances.keySet().iterator().next();

        RequestLoanPage loanPage = overviewPage.navigateToRequestLoan();
        loanPage.applyForLoan(1000.00, 200.00, fundingAccountId);

        Assert.assertTrue(loanPage.isLoanApproved(),
                String.format("Expected loan status 'Approved' but got '%s'", loanPage.getLoanStatus()));

        String newAccountId = loanPage.getGeneratedLoanAccountId();
        Assert.assertNotNull(newAccountId, "Approved loan should generate a new account ID.");
        Assert.assertTrue(newAccountId.matches("\\d+"),
                String.format("Generated loan account ID should be numeric: %s", newAccountId));
    }

    @Test(groups = {"regression"}, description = "TC-09B: Verify denied loan request with excessive loan amount")
    public void testLoanRequestDenial() {
        AccountOverviewPage overviewPage = registerFreshUser();

        Map<String, Double> balances = overviewPage.getAllAccountBalances();
        Assert.assertFalse(balances.isEmpty(), "User should have an initialized funding account.");
        String fundingAccountId = balances.keySet().iterator().next();

        RequestLoanPage loanPage = overviewPage.navigateToRequestLoan();
        // Excessive loan request exceeding normal down payment equity thresholds
        loanPage.applyForLoan(1000000.00, 1.00, fundingAccountId);

        Assert.assertEquals(loanPage.getLoanStatus(), "Denied",
                "Loan request with excessive amount should be Denied.");
    }
}