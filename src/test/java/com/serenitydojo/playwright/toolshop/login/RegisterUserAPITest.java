package com.serenitydojo.playwright.toolshop.login;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.junit.UsePlaywright;
import com.microsoft.playwright.options.RequestOptions;
import com.serenitydojo.playwright.toolshop.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@UsePlaywright
public class RegisterUserAPITest {

    private APIRequestContext request;
    private Gson gson = new Gson();

    @BeforeEach
    void setup(Playwright playwright) {
        request = playwright.request().newContext(
                new APIRequest.NewContextOptions()
                        .setBaseURL("https://api.practicesoftwaretesting.com")
        );
    }

    @AfterEach
    void tearDown() {
        if (request != null) {
            request.dispose();
        }
    }

    @Test
    void should_register_user() {
        User validUser = User.randomUser();

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(validUser)
        );

        String responseBody = response.text();
        User createdUser = gson.fromJson(responseBody, User.class);

        JsonObject responseObject = gson.fromJson(responseBody, JsonObject.class);

        assertSoftly(softly -> {
            softly.assertThat(response.status())
                    .as("Registration should return 201 created status code")
                    .isEqualTo(201);

            softly.assertThat(createdUser)
                    .as("Created user should match the specified user without the password")
                    .isEqualTo(validUser.withPassword(null));

            assertThat(responseObject.has("password"))
                    .as("No password should be returned")
                    .isFalse();

            softly.assertThat(responseObject.get("id").getAsString())
                    .as("Registered user should have an id")
                    .isNotEmpty();

            softly.assertThat(
                    response.headers().get("content-type")
            ).contains("application/json");
        });
    }

    @Test
    void first_name_is_mandatory() {
        User userWithNoName = new User(
                null,
                "Smith",
                "Some street",
                "Some city",
                "Some state",
                "Some country",
                "123445",
                "12345678899",
                "1979-01-01",
                "ABC123!£$%",
                "sample@email.com"
        );

        var response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(userWithNoName)
        );

        assertSoftly(softly -> {
            softly.assertThat(response.status()).isEqualTo(422);

            JsonObject responseObject = gson.fromJson(response.text(), JsonObject.class);

            softly.assertThat(responseObject.has("first_name")).isTrue();

            String errorMessage = responseObject.get("first_name").getAsString();

            softly.assertThat(errorMessage).isEqualTo("The first name field is required.");
        });

    }
    @ParameterizedTest(name = "Password ''{0}'' should fail with error: {1}")
    @CsvSource(delimiter = '|', quoteCharacter = '"', textBlock = """
            ''                         | The password field must be at least 8 characters.
            short                      | The password field must be at least 8 characters.
            longerpassword             | The password field must contain at least one uppercase and one lowercase letter.
            LONGERPASSWORD             | The password field must contain at least one uppercase and one lowercase letter.
            LongerPassword             | The password field must contain at least one symbol.
            LongerPassword!            | The password field must contain at least one number.
            password123                | The password field must contain at least one uppercase and one lowercase letter.
            PASSWORD123                | The password field must contain at least one uppercase and one lowercase letter.
            """)
    void testInvalidPasswords(String invalidPassword, String expectedError) {
        User user = User.randomUser().withPassword(invalidPassword);

        APIResponse response = request.post("/users/register",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setData(user));

        assertThat(response.status()).isEqualTo(422);
        assertThat(response.text()).contains(expectedError);
    }

}
