package com.serenitydojo.playwright.toolshop.contact;

import com.microsoft.playwright.options.AriaRole;
import com.serenitydojo.playwright.toolshop.fixtures.PlaywrightTestCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
public class ContactFormTest extends PlaywrightTestCase {

    ContactForm contactForm;

    @DisplayName("When submitting a request")
    @BeforeEach
    void openContactPage() {
        contactForm = new ContactForm(page);
        page.navigate("https://practicesoftwaretesting.com/contact");
    }

    @DisplayName("Customers can use the contact form to contact us")
    @Test
    void completeForm() throws URISyntaxException {
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A very long message to the warranty service about a warranty on a product!");
        contactForm.selectSubject("Warranty");

        Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());
        contactForm.setAttachment(fileToUpload);

        contactForm.submitForm();

        Assertions.assertThat(contactForm.getAlertMessage())
                .contains("Thanks for your message! We will contact you shortly.");
    }

    @DisplayName("First name, last name, email and message are mandatory")
    @ParameterizedTest
    @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
    void mandatoryFields(String fieldName) {
        // Fill in the field values
        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A very long message to the warranty service about a warranty on a product!");
        contactForm.selectSubject("Warranty");

        // Clear one of the fields
        contactForm.clearField(fieldName);

        contactForm.submitForm();

        // Check the error message for that field
        var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");

        assertThat(errorMessage).isVisible();
    }

    @DisplayName("The message must be at least 50 characters long")
    @Test
    void messageField() {

        contactForm.setFirstName("Sarah-Jane");
        contactForm.setLastName("Smith");
        contactForm.setEmail("sarah@example.com");
        contactForm.setMessage("A short long message.");
        contactForm.selectSubject("Warranty");

        contactForm.submitForm();
        ;

        assertThat(page.getByRole(AriaRole.ALERT)).hasText("Message must be minimal 50 characters");
    }


}
