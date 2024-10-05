package com.serenitydojo.playwright.saucedemo.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.SelectOption;
import com.microsoft.playwright.options.AriaRole;

import java.util.List;

public class ProductsPage {

    private final Page page;

    // Define locators as fields
    private final Locator titleLocator;
    private final Locator productTitlesLocator;
    private final Locator sortDropdownLocator;
    private final Locator cartItemCountLocator;
    private final Locator shoppingCartLinkLocator;

    // Constructor
    public ProductsPage(Page page) {
        this.page = page;

        // Initialize locators
        this.titleLocator = page.locator("[data-test=title]");
        this.productTitlesLocator = page.locator("[data-test=inventory-item-name]");
        this.sortDropdownLocator = page.locator("[data-test=product-sort-container]");
        this.cartItemCountLocator = page.locator(".shopping_cart_badge");
        this.shoppingCartLinkLocator = page.locator("[data-test=shopping-cart-link]");
    }

    public String getTitle() {
        return titleLocator.textContent();
    }

    public List<String> getAllProductTitles() {
        return productTitlesLocator.allTextContents();
    }

    public void open() {
        page.navigate("https://www.saucedemo.com/inventory.html");
    }

    public void sortBy(String sortOption) {
        sortDropdownLocator.selectOption(new SelectOption().setLabel(sortOption));
    }

    public void addItemToCart(String itemName) {
        page.locator(".inventory_item:has-text('" + itemName + "')")
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Add to cart"))
                .click();
    }

    public void removeItemFromCart(String itemName) {
        page.locator(".inventory_item:has-text('" + itemName + "')")
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Remove"))
                .click();
    }

    public int getShoppingCartItemCount() {
        return Integer.parseInt(cartItemCountLocator.textContent());
    }

    public void openCart() {
        shoppingCartLinkLocator.click();
    }
}

