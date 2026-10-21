package com.parabank.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AdminUtils {
    private static final Logger log = LogManager.getLogger(AdminUtils.class);
    private static final By cleanDbButton = By.xpath("//button[@value='CLEAN']");
    private static final By dbCleanedMessage = By.xpath("//div[@id='rightPanel']//b[text()='Database Cleaned']");

    public static void resetDatabase(WebDriver driver) {
        String adminUrl = ConfigReader.get("admin.url");
        log.info("Navigating to Admin page to reset database: {}", adminUrl);
        driver.get(adminUrl);

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        wait.until(ExpectedConditions.elementToBeClickable(cleanDbButton)).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(dbCleanedMessage));
        log.info("ParaBank database successfully reset to clean state.");
    }
}
