package com.serenitydojo.playwright.saucedemo;

import com.serenitydojo.playwright.saucedemo.pageobjects.ProductsPage;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Product catalog")
@Feature("Product catalog")
public class ProductPageTest extends TestFixtures {

    private ProductsPage productsPage;

    @BeforeEach
    void setupPages() {
        this.productsPage = new ProductsPage(page);
        productsPage.open();
    }

    @DisplayName("Should show all the available products on the catalog page")
    @Test
    void should_show_all_available_products_on_catalog() {
        Assertions.assertThat(productsPage.getAllProductTitles())
                .contains("Sauce Labs Backpack", "Sauce Labs Bike Light", "Sauce Labs Bolt T-Shirt",
                        "Sauce Labs Fleece Jacket", "Sauce Labs Onesie", "Test.allTheThings() T-Shirt (Red)");
    }

    @DisplayName("Should be able to sort products by price (low to high)")
    @Test
    void should_be_able_to_sort_products_by_price() {
        productsPage.sortBy("Price (low to high)");
        Assertions.assertThat(productsPage.getAllProductTitles())
                .contains("Sauce Labs Onesie",
                        "Sauce Labs Bike Light",
                        "Sauce Labs Bolt T-Shirt",
                        "Test.allTheThings() T-Shirt (Red)",
                        "Sauce Labs Backpack",
                        "Sauce Labs Fleece Jacket");
    }

    @DisplayName("Should be able to sort products by price (high to low)")
    @Test
    void should_be_able_to_sort_products_by_price_high_to_low() {
        productsPage.sortBy("Price (high to low)");
        Assertions.assertThat(productsPage.getAllProductTitles())
                .contains("Sauce Labs Fleece Jacket",
                        "Sauce Labs Backpack",
                        "Test.allTheThings() T-Shirt (Red)",
                        "Sauce Labs Bolt T-Shirt",
                        "Sauce Labs Bike Light",
                        "Sauce Labs Onesie");
    }


    @DisplayName("Should be able to add an item to the cart")
    @Test
    void should_be_able_to_add_an_item_to_the_cart() {
        productsPage.addItemToCart("Sauce Labs Fleece Jacket");

        Assertions.assertThat(productsPage.getShoppingCartItemCount()).isEqualTo(1);
    }

    @DisplayName("Should be able to add two item to the cart")
    @Test
    void should_be_able_to_add_two_items_to_the_cart() {
        productsPage.addItemToCart("Sauce Labs Fleece Jacket");
        productsPage.addItemToCart("Sauce Labs Onesie");

        Assertions.assertThat(productsPage.getShoppingCartItemCount()).isEqualTo(2);
    }

    @DisplayName("Should be able to add and remove items to/from the cart")
    @Test
    void should_be_able_to_add_and_remove_items_to_and_from_the_cart() {
        productsPage.addItemToCart("Sauce Labs Fleece Jacket");
        productsPage.addItemToCart("Sauce Labs Onesie");
        productsPage.removeItemFromCart("Sauce Labs Fleece Jacket");

        Assertions.assertThat(productsPage.getShoppingCartItemCount()).isEqualTo(1);
    }

}
