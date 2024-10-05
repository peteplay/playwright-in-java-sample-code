package com.serenitydojo.playwright.saucedemo;

import com.serenitydojo.playwright.saucedemo.domain.CartItem;
import com.serenitydojo.playwright.saucedemo.pageobjects.CartPage;
import com.serenitydojo.playwright.saucedemo.pageobjects.ProductsPage;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

@DisplayName("Shopping cart")
@Feature("Shopping cart")
public class ShoppingCartTest extends TestFixtures {

    private ProductsPage productsPage;
    private CartPage cartPage;

    @BeforeEach
    void setupPages() {
        this.productsPage = new ProductsPage(page);
        this.cartPage = new CartPage(page);
        productsPage.open();
    }

    @DisplayName("Should show all the added items in the cart")
    @Test
    void should_show_all_added_items_in_the_cart() {
        productsPage.addItemToCart("Sauce Labs Fleece Jacket");
        productsPage.addItemToCart("Sauce Labs Onesie");

        productsPage.openCart();

        List<CartItem> cartItems = cartPage.getCartItems();

        Assertions.assertThat(cartItems)
                .hasSize(2)
                .anyMatch(item -> item.description().equals("Sauce Labs Fleece Jacket"))
                .anyMatch(item -> item.description().equals("Sauce Labs Onesie"));
    }

    @DisplayName("Should be able to remove an item from the cart")
    @Test
    void should_remove_an_item_from_cart() {

        productsPage.addItemToCart("Sauce Labs Fleece Jacket");
        productsPage.addItemToCart("Sauce Labs Onesie");

        productsPage.openCart();
        cartPage.removeItemFromCart("Sauce Labs Fleece Jacket");

        List<CartItem> cartItems = cartPage.getCartItems();

        Assertions.assertThat(cartItems)
                .hasSize(1)
                .anyMatch(item -> item.description().equals("Sauce Labs Onesie"));

    }

}
