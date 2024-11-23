package com.serenitydojo.playwright.toolshop.registration;

import com.serenitydojo.playwright.toolshop.domain.User;
import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

class LoginWithRegisteredUserTest extends PlaywrightTestCase {
    // Register a user with the /users/register API and then login with that user
    @Test
    @DisplayName("Should be able to login with a registered user")
    void shouldBeAbleToLoginWithARegisteredUser() {
        // Register a user via the API
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        // Login via the login page
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginAs(user);

        // Check that we are on the account page
        assertThat(loginPage.title()).hasText("My account");
    }

    @Test
    @DisplayName("Should not login if invalid credentials are provided")
    void shouldNotBeAbleToLoginWithInvalidCredentials() {
        // Register a user via the API
        User user = User.randomUser();
        UserAPIClient userAPIClient = new UserAPIClient(page);
        userAPIClient.registerUser(user);

        // Login via the login page
        LoginPage loginPage = new LoginPage(page);
        loginPage.open();
        loginPage.loginWith(user.email(), "wrong-password");

        // Check that we are on the account page
        assertThat(loginPage.alert()).hasText("Invalid email or password");
    }
}














