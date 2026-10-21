package com.parabank.base;

import com.parabank.utils.AdminUtils;
import com.parabank.utils.DriverFactory;
import org.testng.annotations.BeforeSuite;

public class SuiteSetup {

    @BeforeSuite(alwaysRun = true)
    public void resetEnvironment() {
        DriverFactory.initDriver();
        try {
            AdminUtils.resetDatabase(DriverFactory.getDriver());
        } finally {
            DriverFactory.quitDriver();
        }
    }
}