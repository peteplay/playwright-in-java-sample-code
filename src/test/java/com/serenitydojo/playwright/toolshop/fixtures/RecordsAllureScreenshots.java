package com.serenitydojo.playwright.toolshop.fixtures;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;

public interface RecordsAllureScreenshots {
    @AfterEach
    default void tearDown(Page page) {
        recordScreenshot(page,"End of Test Screenshot");
    }

    default void recordScreenshot(Page page, String screenshotName) {
        Allure.addAttachment(screenshotName, new ByteArrayInputStream(page.screenshot()));
    }
}
