package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
public class PlaywrightWaitsTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @BeforeEach
    void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @BeforeEach
    void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
        page.waitForCondition(() -> page.getByTestId("product-name").count() > 0);
    }

    @DisplayName("Playwright waits automatically for elements by default")
    @Nested
    class AutoWaits {

        @Test
        @DisplayName("It should wait for the filter checkbox options to appear before clicking")
        void shouldWaitForTheFilterCheckboxes() {
            var screwDriverFilter = page.getByLabel("Screwdriver");
            //
            // The checkboxes take an instant to render on the page
            //
            screwDriverFilter.click();

            assertThat(screwDriverFilter).isChecked();
        }

        @Test
        @DisplayName("It should wait for the product list to update before reading the product names")
        void shouldWaitForProductListToUpdate() {

            page.getByLabel("Screwdriver").click();
            page.getByLabel("Chisels").click();

            page.waitForLoadState(LoadState.LOAD);

            List<String> matchingProducts = page.getByTestId("product-name").allTextContents();

            Assertions.assertThat(matchingProducts)
                    .allMatch( product -> product.contains("Screwdriver") || product.contains("Chisels"));

        }

    }
}
