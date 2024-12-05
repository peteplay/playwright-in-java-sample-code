package com.serenitydojo.playwright.toolshop.domain;

import com.github.javafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * {
 *   "first_name": "John",
 *   "last_name": "Doe",
 *   "address": "Street 1",
 *   "city": "City",
 *   "state": "State",
 *   "country": "Country",
 *   "postcode": "1234AA",
 *   "phone": "0987654321",
 *   "dob": "1970-01-01",
 *   "password": "S1!uper-secret",
 *   "email": "john@doe.example"
 * }
 */
public record User(
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
    public static User randomUserNamed(String firstName) {
        Faker fake = new Faker();
        int year = fake.number().numberBetween(1970,2000);
        int month = fake.number().numberBetween(1,12);
        int day = fake.number().numberBetween(1,28);
        LocalDate date = LocalDate.of(year,month,day);
        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return new User(
                firstName,
                fake.name().lastName(),
                fake.address().streetAddress(),
                fake.address().city(),
                fake.address().state(),
                fake.address().country(),
                fake.address().zipCode(),
                fake.phoneNumber().phoneNumber(),
                formattedDate,
                "Az1234£!3",
                fake.internet().emailAddress()
        );
    }

    public User withPassword(String password) {
        return new User(first_name, last_name, address, city, state, country, postcode, phone, dob, password, email);
    }
}
