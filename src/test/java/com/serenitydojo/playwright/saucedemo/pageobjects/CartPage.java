package com.serenitydojo.playwright.saucedemo.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.saucedemo.domain.CartItem;

import java.util.List;

public class CartPage {

    private final Page page;

    // Define locators for elements that are reused multiple times
    private final Locator cartItemLocator;
    private final Locator checkoutButtonLocator;

    // Constructor
    public CartPage(Page page) {
        this.page = page;

        // Initialize locators
        this.cartItemLocator = page.locator(".cart_item");
        this.checkoutButtonLocator = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Checkout"));
    }

    // Method to retrieve all items in the cart
    public List<CartItem> getCartItems() {
        return cartItemLocator.all().stream().map(item -> {
            // Extract quantity, description, and price from each cart item
            int quantity = Integer.parseInt(item.locator(".cart_quantity").textContent().trim());
            String description = item.locator(".inventory_item_name").textContent().trim();
            double price = Double.parseDouble(item.locator(".inventory_item_price").textContent().trim().replace("$", ""));

            // Return a CartItem object
            return new CartItem(description, quantity, price);
        }).toList();
    }

    // Method to remove a specific item from the cart by its name
    public void removeItemFromCart(String itemName) {
        page.locator(".cart_item:has-text('" + itemName + "')")
                .getByRole(AriaRole.BUTTON, new Locator.GetByRoleOptions().setName("Remove"))
                .click();
    }

    // Method to click on the checkout button
    public void checkout() {
        checkoutButtonLocator.click();
    }
}
