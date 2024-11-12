package com.serenitydojo.playwright.toolshop.cucumber;

import com.microsoft.playwright.*;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;

import java.util.Arrays;

public class PlaywrightHooks {

    static Playwright playwright;
    static Browser browser;

    static ThreadLocal<BrowserContext> contextHolder = new ThreadLocal<>();
    static ThreadLocal<Page> pageHolder = new ThreadLocal<>();

    @BeforeAll
    public static void beforeAll() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @Before(order = 100)
    public void beforeEach() {
        contextHolder.set(browser.newContext());
        pageHolder.set(browser.newPage());
    }

    @After
    public void shutdownContext() {
        if (contextHolder.get() != null) {
            contextHolder.get().close();
            contextHolder.remove();
        }
    }

    @AfterAll
    public static void afterAll() {
        if (playwright != null) {
            playwright.close();
        }
    }

    public static Page getPage() {
        return pageHolder.get();
    }
}
