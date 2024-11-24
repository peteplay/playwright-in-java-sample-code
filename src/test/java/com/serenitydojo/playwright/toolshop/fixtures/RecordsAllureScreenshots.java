package com.serenitydojo.playwright.toolshop.fixtures;

import com.microsoft.playwright.Page;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterEach;

import java.io.ByteArrayInputStream;

public interface RecordsAllureScreenshots {
    @AfterEach
    default void tearDown(Page page) {
        Allure.addAttachment("End of Test Screenshot", new ByteArrayInputStream(page.screenshot()));
    }
}
