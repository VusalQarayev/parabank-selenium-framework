package com.parabank.listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.parabank.reports.ExtentManager;
import com.parabank.reports.ExtentTestManager;
import com.parabank.utils.DriverFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.nio.file.Files;

public class TestListener implements ITestListener, ISuiteListener {
    private static final Logger log = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ISuite suite) {
        ExtentManager.getInstance();
    }

    @Override
    public void onFinish(ISuite suite) {
        ExtentManager.getInstance().flush();
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        String description = result.getMethod().getDescription();
        ExtentTest test = ExtentManager.getInstance().createTest(testName, description);
        ExtentTestManager.setTest(test);
        log.info("Starting test: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentTestManager.getTest().log(Status.PASS, "Test passed successfully.");
        log.info("Test PASSED: {}", result.getName());
        ExtentTestManager.unload();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Test FAILED: {}", result.getName(), result.getThrowable());
        String screenshotPath = captureScreenshot(result.getName());
        ExtentTest test = ExtentTestManager.getTest();
        test.log(Status.FAIL, result.getThrowable());
        if (screenshotPath != null) {
            test.addScreenCaptureFromPath(screenshotPath);
        }
        ExtentTestManager.unload();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentTestManager.getTest().log(Status.SKIP, "Test skipped: " + result.getThrowable());
        log.warn("Test SKIPPED: {}", result.getName());
        ExtentTestManager.unload();
    }

    private String captureScreenshot(String testName) {
        try {
            File src = ((TakesScreenshot) DriverFactory.getDriver()).getScreenshotAs(OutputType.FILE);
            String relativePath = "screenshots/" + testName + "_" + System.currentTimeMillis() + ".png";
            File dest = new File("test-output/" + relativePath);
            dest.getParentFile().mkdirs();
            Files.copy(src.toPath(), dest.toPath());
            // Return relative path from report location (test-output/reports/ -> test-output/screenshots/)
            return "../" + relativePath;
        } catch (Exception e) {
            log.error("Could not capture failure screenshot", e);
            return null;
        }
    }
}