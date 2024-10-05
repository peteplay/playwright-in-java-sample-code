package com.serenitydojo.playwright.saucedemo;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.serenitydojo.playwright.saucedemo.domain.CartItem;
import com.serenitydojo.playwright.saucedemo.pageobjects.CartPage;
import com.serenitydojo.playwright.saucedemo.pageobjects.CheckoutStepOnePage;
import com.serenitydojo.playwright.saucedemo.pageobjects.CheckoutStepTwoPage;
import com.serenitydojo.playwright.saucedemo.pageobjects.ProductsPage;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.nio.file.Paths;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@DisplayName("Checkout")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Feature("Checkout")
public class CheckoutTest extends TestFixtures {

    private ProductsPage productsPage;
    private CartPage cartPage;
    private CheckoutStepOnePage checkoutPage1;
    private CheckoutStepTwoPage checkoutPage2;

    @BeforeEach
    void setupPages() {
        this.productsPage = new ProductsPage(page);
        this.cartPage = new CartPage(page);
        productsPage.open();
    }

    @Order(1)
    @DisplayName("Should remember the items in the cart when checking out")
    @Test
    void should_invite_shoppers_before_checking_out() {
        productsPage.addItemToCart("Sauce Labs Fleece Jacket");
        productsPage.addItemToCart("Sauce Labs Onesie");
        productsPage.openCart();
        cartPage.checkout();

        page.context().storageState(
                new BrowserContext.StorageStateOptions().setPath(Paths.get("target/shopping-cart.json")));
    }

    private void restoreShoppingCartState() {
        context = browser.newContext(
                new Browser.NewContextOptions().setStorageStatePath(Paths.get("target/shopping-cart.json")));
        page = context.newPage();
        this.checkoutPage1 = new CheckoutStepOnePage(page);
        this.checkoutPage2 = new CheckoutStepTwoPage(page);

    }

    @DisplayName("Should invite users to enter their details when checking out")
    @Test
    @Order(2)
    void should_invite_shoppers_after_checking_out() {
        restoreShoppingCartState();
        checkoutPage1.open();

        assertThat(page.getByPlaceholder("First Name")).isVisible();
        assertThat(page.getByPlaceholder("Last Name")).isVisible();
        assertThat(page.getByPlaceholder("Zip/Postal Code")).isVisible();
    }

    @DisplayName("Customers must provide a first name")
    @Test
    @Order(2)
    void first_name_is_mandatory() {
        restoreShoppingCartState();
        checkoutPage1.open();
        checkoutPage1.enterCustomerDetails("","Smith","111-ZZZ");

        Assertions.assertThat(checkoutPage1.getErrorMessage()).contains("Error: First Name is required");
    }

    @DisplayName("Customers must provide a last name")
    @Test
    @Order(2)
    void last_name_is_mandatory() {
        restoreShoppingCartState();
        checkoutPage1.open();
        checkoutPage1.enterCustomerDetails("Sarah-Jane","","111-ZZZ");

        Assertions.assertThat(checkoutPage1.getErrorMessage()).contains("Error: Last Name is required");
    }

    @DisplayName("Customers must provide a post code")
    @Test
    @Order(2)
    void post_code_is_mandatory() {
        restoreShoppingCartState();
        checkoutPage1.open();
        checkoutPage1.enterCustomerDetails("Sarah-Jane","Smith","");

        Assertions.assertThat(checkoutPage1.getErrorMessage()).contains("Error: Postal Code is required");
    }

    @DisplayName("The checkout page should show all the selected products")
    @Test
    @Order(2)
    void should_show_all_selected_products() {
        restoreShoppingCartState();
        checkoutPage1.open();
        checkoutPage1.enterCustomerDetails("Sarah-Jane","Smith","ZZZ-111");

        List<CartItem> cartItems = checkoutPage2.getCartItems();

        Assertions.assertThat(cartItems)
                .hasSize(2)
                .anyMatch(item -> item.description().equals("Sauce Labs Fleece Jacket"))
                .anyMatch(item -> item.description().equals("Sauce Labs Onesie"));
    }
}
