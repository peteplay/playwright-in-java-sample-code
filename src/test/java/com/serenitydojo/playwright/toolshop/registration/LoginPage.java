package com.serenitydojo.playwright.toolshop.registration;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.toolshop.domain.User;

public class LoginPage {
    private final Page page;

    public LoginPage(Page page) {
        this.page = page;
    }

    public void open() {
        page.navigate("https://practicesoftwaretesting.com/auth/login");
    }

    public void loginAs(User user) {
        loginWith(user.email(), user.password());
    }

    public Locator title() {
        return page.getByTestId("page-title");
    }

    public void loginWith(String email, String password) {
        page.getByPlaceholder("Your email").fill(email);
        page.getByPlaceholder("Your password").fill(password);
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Login")).click();
    }

    public Locator alert() {
        return page.getByTestId("login-error");
    }
}
