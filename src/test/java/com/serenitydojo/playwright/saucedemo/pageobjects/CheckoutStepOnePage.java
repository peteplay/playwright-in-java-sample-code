package com.serenitydojo.playwright.saucedemo.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;

public class CheckoutStepOnePage {

    private final Page page;
    private final Locator firstNameField;
    private final Locator lastNameField;
    private final Locator postCodeField;
    private final Locator continueButton;
    private final Locator errorMessage;

    public CheckoutStepOnePage(Page page) {
        this.page = page;
        this.firstNameField = page.getByPlaceholder("First Name");
        this.lastNameField = page.getByPlaceholder("Last Name");
        this.postCodeField = page.getByPlaceholder("Zip/Postal Code");
        this.continueButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Continue"));
        this.errorMessage = page.locator("[data-test=error]");
    }

    public void enterCustomerDetails(String firstName, String lastName, String postCode) {
        firstNameField.fill(firstName);
        lastNameField.fill(lastName);
        postCodeField.fill(postCode);
        continueButton.click();
    }

    public void open() {
        page.navigate("https://www.saucedemo.com/checkout-step-one.html");
    }

    public String getErrorMessage() {
        return errorMessage.textContent();
    }
}
