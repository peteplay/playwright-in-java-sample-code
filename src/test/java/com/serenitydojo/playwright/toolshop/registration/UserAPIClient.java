package com.serenitydojo.playwright.toolshop.registration;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.options.RequestOptions;
import com.serenitydojo.playwright.toolshop.domain.User;

public class UserAPIClient {

    private static final String REGISTER_USER_API = "https://api.practicesoftwaretesting.com/users/register";
    private final Page page;

    public UserAPIClient(Page page) {
        this.page = page;
    }

    public void registerUser(User user) {
        var response = page.request().post(
                REGISTER_USER_API,
                RequestOptions.create()
                        .setData(user)
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Accept", "application/json")
        );
        if (response.status() != 201) {
            throw new IllegalArgumentException("Could not create user: " + response.text());
        }
    }
}










