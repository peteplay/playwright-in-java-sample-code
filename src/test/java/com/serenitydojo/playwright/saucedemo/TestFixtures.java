package com.serenitydojo.playwright.saucedemo;

import com.microsoft.playwright.*;
import com.serenitydojo.playwright.saucedemo.domain.UserType;
import com.serenitydojo.playwright.saucedemo.pageobjects.LoginPage;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestFixtures {
    // Shared between all tests in the class.
    Playwright playwright;
    Browser browser;

    @BeforeAll
    void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
        );
        storeLoginCredentials(browser);

    }

    static void storeLoginCredentials(Browser browser) {
        // Create the browser context and login once
        var context = browser.newContext();
        var page = context.newPage();
        LoginPage loginPage = new LoginPage(page);

        // Perform the login action
        loginPage.open();
        loginPage.loginAs(UserType.STANDARD_USER);

        // Save the storage state (which contains the login session)
        page.context().storageState(
                new BrowserContext.StorageStateOptions().setPath(Paths.get("target/state.json")));
    }


    @AfterAll
    void closeBrowser() {
        playwright.close();
    }

    // New instance for each test method.
    BrowserContext context;
    Page page;

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext(
                new Browser.NewContextOptions().setStorageStatePath(Paths.get("target/state.json")));
        page = context.newPage();
    }

    @AfterEach
    void closeContext() {
        context.close();
    }
}