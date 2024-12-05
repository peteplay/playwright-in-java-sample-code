package com.serenitydojo.playwright.toolshop.catalog.workflow;

import com.microsoft.playwright.Page;
import com.serenitydojo.playwright.toolshop.actions.api.UserAPIClient;
import com.serenitydojo.playwright.toolshop.domain.User;
import com.serenitydojo.playwright.toolshop.login.LoginPage;

public class AuthenticationWorkflow {

    private final UserAPIClient userAPI;
    private final LoginPage loginPage;

    public AuthenticationWorkflow(Page page) {
        this.userAPI = new UserAPIClient(page);
        this.loginPage = new LoginPage(page);
    }

    public User registerUserCalled(String firstName) {
        User someUser = User.randomUserNamed(firstName);
        userAPI.registerUser(someUser);
        return someUser;
    }

    public void loginAs(User user) {
        loginPage.open();
        loginPage.loginAs(user);
    }
}
