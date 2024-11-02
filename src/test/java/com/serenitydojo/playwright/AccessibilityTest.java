package com.serenitydojo.playwright;

import com.deque.html.axecore.playwright.AxeBuilder;
import com.deque.html.axecore.results.AxeResults;
import com.microsoft.playwright.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class AccessibilityTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;

    Page page;

    @BeforeAll
    public static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        .setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox","--disable-extensions","--disable-gpu"))
        );
        browserContext = browser.newContext();
    }

    @BeforeEach
    public void setUp() {
        page = browserContext.newPage();
    }

    @AfterAll
    public static void tearDown() {
        browser.close();
        playwright.close();
    }

    // This test will fail
    @Test
    @Disabled
    void homePageShouldNotHaveAccessibilityIssues() {
        page.navigate("https://practicesoftwaretesting.com");

        AxeResults accessibilityScanResults = new AxeBuilder(page).analyze();

        assertThat(accessibilityScanResults.getViolations()).isEmpty();
    }

    @Test
    void navBarShouldNotHaveAnyAccessibilityIssues() {
        AxeResults accessibilityScanResults = new AxeBuilder(page)
                .include(List.of(".navbar-nav"))
                .analyze();

        assertThat(accessibilityScanResults.getViolations()).isEmpty();
    }

}
