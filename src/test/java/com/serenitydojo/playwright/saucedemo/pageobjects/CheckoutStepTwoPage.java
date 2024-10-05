package com.serenitydojo.playwright.saucedemo.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.saucedemo.domain.CartItem;

import java.util.List;

public class CheckoutStepTwoPage {

    private final Locator cartItems;

    public CheckoutStepTwoPage(Page page) {
        this.cartItems = page.locator(".cart_item");
    }

    public List<CartItem> getCartItems() {
        return cartItems.all().stream().map(item -> {
            // Extract the quantity, description, and price from each item
            int quantity = Integer.parseInt(item.locator(".cart_quantity").textContent().trim());
            String description = item.locator(".inventory_item_name").textContent().trim();
            double price = Double.parseDouble(item.locator(".inventory_item_price").textContent().trim().replace("$",""));

            // Return a CartItem record
            return new CartItem(description, quantity, price);
        }).toList();
    }
}
