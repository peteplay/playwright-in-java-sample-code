package com.serenitydojo.playwright.saucedemo;

import com.serenitydojo.playwright.saucedemo.domain.UserType;
import com.serenitydojo.playwright.saucedemo.pageobjects.LoginPage;
import com.serenitydojo.playwright.saucedemo.pageobjects.ProductsPage;
import io.qameta.allure.Feature;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("Customer authentication")
@Feature("Authentication")
public class LoggingOnTest extends TestFixtures {

    private LoginPage loginPage;
    private ProductsPage productsPage;

    @BeforeEach
    void createLocalContextAndPage() {
        context = browser.newContext();
        page = context.newPage();
    }
    @BeforeEach
    void setupPages() {
        this.loginPage = new LoginPage(page);
        this.productsPage = new ProductsPage(page);
    }

    @DisplayName("The one where the username and password are valid")
    @Test
    void theOneWhereTheUsernameAndPasswordAreValid() {
        loginPage.open();
        loginPage.loginAs(UserType.STANDARD_USER);
        Assertions.assertThat(productsPage.getTitle()).isEqualTo("Products");
    }

    @DisplayName("The one where the credentials are invalid")
    @ParameterizedTest
    @CsvSource({
            "standard_user, wrong_password, Username and password do not match any user in this service",
            "locked_out_user, secret_sauce, Sorry, this user has been locked out.",
            "wrong_username, secret_sauce, Username and password do not match any user in this service",
    })
    void testLoginErrors(String username, String password, String expectedErrorMessage) {
        loginPage.open();
        loginPage.loginAs(username, password);
        Assertions.assertThat(loginPage.getErrorMessage()).contains(expectedErrorMessage);

    }

}
