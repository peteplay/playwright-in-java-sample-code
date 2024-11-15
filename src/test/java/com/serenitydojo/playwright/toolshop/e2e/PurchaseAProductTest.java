package com.serenitydojo.playwright.toolshop.e2e;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PurchaseAProductTest extends PlaywrightTestCase {

    record CartItem(String productTitle, int quantity, double amount, double total) {}

    // Customers should be able to add items to the shopping cart
    @Test
    void addToShoppingCart() {
        page.navigate("https://practicesoftwaretesting.com");
        // Search for claw hammer
        page.getByPlaceholder("Search").fill("claw hammer");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();

        // View product details
        page.getByTestId("product-name")
                .filter(new Locator.FilterOptions().setHasText("Claw Hammer with Shock Reduction Grip"))
                .click();
        double price = Double.parseDouble(page.getByTestId("unit-price").textContent());

        // Add two claw hammers to the cart
        page.getByTestId("increase-quantity").click();
        page.getByTestId("add-to-cart").click();
        page.getByRole(AriaRole.ALERT).click();
        page.getByTestId("nav-cart").click();

        // Wait for the cart rows to appear
        page.locator("app-cart tbody tr").waitFor();

        // Check that the cart contains the right elements
        List<CartItem> displayedCartItems = page.locator("app-cart tbody tr")
                .all()
                .stream()
                .map(
                        row -> {
                            String productTitle = stripWhiteSpace(row.getByTestId("product-title").textContent());
                            int quantity = Integer.parseInt(row.getByTestId("product-quantity").inputValue());
                            double amount = Double.parseDouble(priceFrom(row.getByTestId("product-price").textContent()));
                            double total = Double.parseDouble(priceFrom(row.getByTestId("line-price").textContent()));
                            return new CartItem(productTitle, quantity, amount, total);
                        }
                ).toList();

        System.out.println(displayedCartItems);
        // Check that the total price is correct

        assertThat(displayedCartItems).hasSize(1);
        assertThat(displayedCartItems.get(0))
                .isEqualTo(new CartItem("Claw Hammer with Shock Reduction Grip",2,13.41,26.82));

    }

    private String stripWhiteSpace(String text) {
        return text.strip().replace("\u00A0","");
    }

    private String priceFrom(String value) {
        return value.replace("$", "").replace(",", ".");
    }
}
