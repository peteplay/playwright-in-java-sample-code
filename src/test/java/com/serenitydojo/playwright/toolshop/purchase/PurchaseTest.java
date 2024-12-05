package com.serenitydojo.playwright.toolshop.purchase;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.toolshop.catalog.workflow.AuthenticationWorkflow;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.*;
import com.serenitydojo.playwright.toolshop.catalog.workflow.PurchaseWorkflow;
import com.serenitydojo.playwright.toolshop.domain.User;
import com.serenitydojo.playwright.toolshop.fixtures.ChromeHeadlessOptions;
import com.serenitydojo.playwright.toolshop.fixtures.TakesFinalScreenshot;
import com.serenitydojo.playwright.toolshop.fixtures.WithTracing;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Purchase")
@Feature("Purchase")
@UsePlaywright(ChromeHeadlessOptions.class)
public class PurchaseTest implements TakesFinalScreenshot, WithTracing {

    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    NavBar navBar;
    ShoppingCart shoppingCart;

    AuthenticationWorkflow authentication;
    PurchaseWorkflow purchases;

    @BeforeEach
    void openHomePage() {
        navBar.openHomePage();
    }

    @BeforeEach
    void setUp(Page page) {
        searchComponent = new SearchComponent(page);
        productList = new ProductList(page);
        productDetails = new ProductDetails(page);
        navBar = new NavBar(page);
        shoppingCart = new ShoppingCart(page);

        authentication = new AuthenticationWorkflow(page);
        purchases = new PurchaseWorkflow(page);


    }

    @Story("Purchase items")
    @Nested
    class WhenPurchasingItems {

        @Test
        @DisplayName("Purchasing a number of items after logging on")
        void whenPurchasingANumberOfItemsAfterLoggingOn() {
            // Given Sharon has an account
            User sharon = authentication.registerUserCalled("Sharon");
            // And Sharon has logged on
            authentication.loginAs(sharon);

            // When she adds 2 items to the cart
            purchases.addProductToCart("Combination Pliers", 2);
            purchases.addProductToCart("Claw Hammer with Fiberglass Handle", 1);

            // And she checks out
            purchases.checkOutCart();
            purchases.proceedToCheckoutAfterAuthentication();
            purchases.confirmAddress();

            // And she completes the purchase
            purchases.choosePaymentMethod("Cash on Delivery");

            // Then she should receive a thank you message
            Assertions.assertThat(purchases.confirmationMessage()).contains("Thanks for your order!");
        }

        @Test
        @DisplayName("Logging on during the purchase process")
        void whenLoggingOnDuringThePurchaseProcess() {
            // Given Sharon has an account
            User sharon = authentication.registerUserCalled("Sharon");

            // When she adds 2 items to the cart
            purchases.addProductToCart("Combination Pliers", 2);
            purchases.addProductToCart("Claw Hammer with Fiberglass Handle", 1);

            // And she checks out
            purchases.checkOutCart();

            // And she logs on
            authentication.loginAs(sharon);
            purchases.checkOutCart(); // again

            purchases.proceedToCheckoutAfterAuthentication();
            purchases.confirmAddress();

            // And she completes the purchase
            purchases.choosePaymentMethod("Cash on Delivery");

            // Then she should receive a thank you message
            Assertions.assertThat(purchases.confirmationMessage()).contains("Thanks for your order!");
        }

    }
}