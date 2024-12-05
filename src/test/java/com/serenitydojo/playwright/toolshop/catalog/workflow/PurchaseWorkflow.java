package com.serenitydojo.playwright.toolshop.catalog.workflow;

import com.microsoft.playwright.Page;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.*;
import io.qameta.allure.Step;

public class PurchaseWorkflow {

    Page page;
    NavBar navBar;
    SearchComponent searchComponent;
    ProductList productList;
    ProductDetails productDetails;
    ShoppingCart shoppingCart;
    AddressForm addressForm;
    PaymentForm paymentForm;

    public PurchaseWorkflow(Page page) {
        this.page = page;
        this.navBar = new NavBar(page);
        this.searchComponent = new SearchComponent(page);
        this.productList = new ProductList(page);
        this.productDetails = new ProductDetails(page);
        this.shoppingCart = new ShoppingCart(page);
        this.addressForm = new AddressForm(page);
        this.paymentForm = new PaymentForm(page);
    }

    @Step("Add product to cart")
    public void addProductToCart(String productName, int quantity) {
        navBar.openHomePage();
        searchComponent.searchBy(productName);
        productList.viewProductDetails(productName);
        productDetails.setQuantityTo(quantity);
        productDetails.addToCart();
    }

    @Step("Check out the cart")
    public void checkOutCart() {
        navBar.openCart();
        shoppingCart.proceedToCheckout();
    }

    public void proceedToCheckoutAfterAuthentication() {
        shoppingCart.proceedToCheckoutAfterAuthentication();
    }

    @Step("Confirm address")
    public void confirmAddress() {
        addressForm.confirmAddress();
    }

    @Step("Choose payment method")
    public void choosePaymentMethod(String paymentMethod) {
        paymentForm.choosePaymentMethod(paymentMethod);
    }

    public String confirmationMessage() {
        return page.locator("#order-confirmation").textContent();
    }
}
