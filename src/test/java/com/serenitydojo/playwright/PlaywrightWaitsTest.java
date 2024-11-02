package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import com.microsoft.playwright.options.WaitForSelectorState;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Arrays;
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
        page.waitForSelector("[data-test='product-name']");
    }

    @DisplayName("Playwright waits automatically for elements by default")
    @Nested
    class AutoWaits {

        // Automatic wait
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

        // Explicit wait
        @Test
        @DisplayName("Specify customized options when waiting for an element")
        void shouldWaitForAnElement() {

            // Open the categories menu
            page.getByRole(AriaRole.MENUBAR).getByText("Categories").click();
            page.getByRole(AriaRole.MENUBAR).getByText("Power Tools").click();


            page.waitForSelector(".card",
                    new Page.WaitForSelectorOptions()
                            .setState(WaitForSelectorState.VISIBLE)
                            .setTimeout(5000));

            assertThat(page.getByTestId("product-name").getByText("Sheet Sander")).isVisible();
        }

        // Wait for an element to appear or disappear
        @Test
        @DisplayName("It should display a toaster message when an item is added to the cart")
        void shouldDisplayToasterMessage() {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            // Wait for the toaster message to appear
            assertThat(page.getByRole(AriaRole.ALERT)).isVisible();
            assertThat(page.getByRole(AriaRole.ALERT)).hasText("Product added to shopping cart.");

            // Wait for the toaster message to disappear
            page.waitForCondition(() -> page.getByRole(AriaRole.ALERT).isHidden());
        }

        // Wait for an element to have a particular state
        @Test
        @DisplayName("It should display a toaster message when an item is added to the cart")
        void shouldDisplayTheCartItemCount() {
            page.getByText("Bolt Cutters").click();
            page.getByText("Add to cart").click();

            // Wait for the cart quantity to update
            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("1"));
            // Or
            page.waitForSelector("[data-test='cart-quantity']:has-text('1')");
        }


        // Wait for an API call to respond after an action
        @Test
        @DisplayName("It should wait for the product list to update before reading the product names")
        void shouldWaitForProductListToUpdate() {

            // Wait for the results to come back from the API call
            page.waitForResponse(
                    "**/product*",
                    () -> {
                        page.getByLabel("Screwdriver").click();
                        page.getByLabel("Chisels").click();
                    });

            List<String> matchingProducts = page.getByTestId("product-name").allTextContents();
            Assertions.assertThat(matchingProducts)
                    .allMatch(product -> product.contains("Screwdriver") || product.contains("Chisels"));

        }
    }
}
