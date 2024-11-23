package com.serenitydojo.playwright.toolshop.domain;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public record User (
        String first_name,
        String last_name,
        String address,
        String city,
        String state,
        String country,
        String postcode,
        String phone,
        String dob,
        String password,
        String email
) {
    public static User randomUser() {
        Faker faker = new Faker();

        Date birthday = faker.date().birthday();
        LocalDate localDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return new User(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().streetAddress(),
                faker.address().city(),
                faker.address().state(),
                faker.address().country(),
                faker.address().zipCode(),
                faker.phoneNumber().phoneNumber(),
                formattedDate,
                "Az123@#$",
                faker.internet().emailAddress()
        );
    }
}









