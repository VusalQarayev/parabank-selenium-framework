package com.parabank.listeners;

import com.parabank.utils.ConfigReader;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
    private int count = 0;
    private static final int MAX_RETRY = ConfigReader.getInt("retry.count");

    @Override
    public boolean retry(ITestResult result) {
        if (!result.isSuccess() && count < MAX_RETRY) {
            count++;
            return true;
        }
        return false;
    }
}