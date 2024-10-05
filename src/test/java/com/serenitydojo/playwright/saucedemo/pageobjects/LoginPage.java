package com.serenitydojo.playwright.saucedemo.pageobjects;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.saucedemo.domain.UserType;

public class LoginPage {

    // Fields for locators
    private final Locator usernameField;
    private final Locator passwordField;
    private final Locator loginButton;
    private final Locator errorMessageLocator;

    private final Page page;

    // Constructor
    public LoginPage(Page page) {
        this.page = page;

        // Define locators as fields
        this.usernameField = page.getByPlaceholder("Username");
        this.passwordField = page.getByPlaceholder("Password");
        this.loginButton = page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login"));
        this.errorMessageLocator = page.locator("[data-test=error]");
    }

    public void open() {
        page.navigate("https://www.saucedemo.com");
    }

    public void loginAs(UserType userType) {
        loginAs(userType.getUsername(), userType.getPassword());
    }

    public void loginAs(String userName, String password) {
        usernameField.fill(userName);
        passwordField.fill(password);
        loginButton.click();
    }

    public String getErrorMessage() {
        return errorMessageLocator.textContent();
    }
}
