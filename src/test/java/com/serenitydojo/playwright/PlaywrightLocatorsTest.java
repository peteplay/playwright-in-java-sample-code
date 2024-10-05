package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class PlaywrightLocatorsTest {

    private static Playwright playwright;
    private static Browser browser;
    private static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(false)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
        browserContext = browser.newContext();
    }

    @BeforeEach
    void setUp() {
        page = browserContext.newPage();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @DisplayName("Locating elements using CSS")
    @Nested
    class LocatingElementsUsingCSS {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("By id")
        @Test
        void locateTheFirstNameFieldByID() {
            page.locator("#first_name").fill("Sarah-Jane");
            PlaywrightAssertions.assertThat(page.locator("#first_name")).hasValue("Sarah-Jane");
        }

        @DisplayName("By CSS class")
        @Test
        void locateTheSendButtonByCssClass() {
            page.locator("#first_name").fill("Sarah-Jane");
            page.locator(".btnSubmit").click();
            List<String> alertMessages = page.locator(".alert").allTextContents();
            Assertions.assertTrue(!alertMessages.isEmpty());

        }

        @DisplayName("By attribute")
        @Test
        void locateTheSendButtonByAttribute() {
            page.locator("input[placeholder='Your last name *']").fill("Smith");
            PlaywrightAssertions.assertThat(page.locator("#last_name")).hasValue("Smith");
        }
    }

    @DisplayName("Locating elements by text using CSS")
    @Nested
    class LocatingElementsByTextUsingCSS {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        // :has-text matches any element containing specified text somewhere inside.
        @DisplayName("Using :has-text")
        @Test
        void locateTheSendButtonByText() {
            page.locator("#first_name").fill("Sarah-Jane");
            page.locator("#last_name").fill("Smith");
            page.locator("input:has-text('Send')").click();
        }

        // :text matches the smallest element containing specified text.
        @DisplayName("Using :text")
        @Test
        void locateAProductItemByText() {
            page.locator(".navbar :text('Home')").click();
            page.locator(".card :text('Bolt')").click();
            PlaywrightAssertions.assertThat(page.locator("[data-test=product-name]")).hasText("Bolt Cutters");
        }

        // Exact matches
        @DisplayName("Using :text-is")
        @Test
        void locateAProductItemByTextIs() {
            page.locator(".navbar :text('Home')").click();
            page.locator(".card :text-is('Bolt Cutters')").click();
            PlaywrightAssertions.assertThat(page.locator("[data-test=product-name]")).hasText("Bolt Cutters");
        }

        // matching with regular expressions
        @DisplayName("Using :text-matches")
        @Test
        void locateAProductItemByTextMatches() {
            page.locator(".navbar :text('Home')").click();
            page.locator(".card :text-matches('Bolt \\\\w+')").click();
            PlaywrightAssertions.assertThat(page.locator("[data-test=product-name]")).hasText("Bolt Cutters");
        }
    }

    @DisplayName("Locating visible elements")
    @Nested
    class LocatingVisibleElements {
        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com");
        }

        @DisplayName("Finding visible and invisible elements")
        @Test
        void locateVisibleAndInvisibleItems() {
            int dropdownItems = page.locator(".dropdown-item").count();
            Assertions.assertTrue(dropdownItems > 0);
        }

        @DisplayName("Finding only visible elements")
        @Test
        void locateVisibleItems() {
            int dropdownItems = page.locator(".dropdown-item:visible").count();
            Assertions.assertTrue(dropdownItems == 0);
        }
    }

    @DisplayName("Locating elements by role")
    @Nested
    class LocatingElementsByRole {

        @DisplayName("Using the BUTTON role")
        @Test
        void byButton() {
            page.navigate("https://practicesoftwaretesting.com/contact");

            List<String> errorMessages = page.getByRole(AriaRole.ALERT).allTextContents();

            page.getByRole(AriaRole.BUTTON,
                            new Page.GetByRoleOptions().setName("Sign in"))
                    .click();
            Assertions.assertTrue(!errorMessages.isEmpty());
        }

        @DisplayName("Using the HEADING role")
        @Test
        void byHeaderRole() {
            page.navigate("https://practicesoftwaretesting.com");

            page.locator("#search-query").fill("Pliers");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search"))
                    .click();
            Locator searchHeading = page.getByRole(AriaRole.HEADING,
                    new Page.GetByRoleOptions().setName(Pattern.compile("Searched for:.*")));

            PlaywrightAssertions.assertThat(searchHeading).isVisible();
            PlaywrightAssertions.assertThat(searchHeading).hasText("Searched for: Pliers");
        }
    }
}
