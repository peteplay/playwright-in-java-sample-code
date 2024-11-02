package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
public class PlaywrightFormsTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions().setHeadless(true)
                        .setArgs(Arrays.asList("--no-sandbox", "--disable-extensions", "--disable-gpu"))
        );
    }

    @BeforeEach
    void setUp() {
        browserContext = browser.newContext();
        page = browserContext.newPage();
    }

    @AfterEach
    void closeContext() {
        browserContext.close();
    }

    @AfterAll
    static void tearDown() {
        browser.close();
        playwright.close();
    }

    @DisplayName("Interacting with text fields")
    @Nested
    class WhenInteractingWithTextFields {

        @BeforeEach
        void openContactPage() {
            page.navigate("https://practicesoftwaretesting.com/contact");
        }

        @DisplayName("Complete the form")
        @Test
        void completeForm() throws URISyntaxException {
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailNameField = page.getByLabel("Email");
            var messageField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var uploadField = page.getByLabel("Attachment");

            firstNameField.fill("Sarah-Jane");
            lastNameField.fill("Smith");
            emailNameField.fill("sarah-jane@example.com");
            messageField.fill("Hello, world!");
            subjectField.selectOption("Warranty");

            Path fileToUpload = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());

            page.setInputFiles("#attachment", fileToUpload);

            assertThat(firstNameField).hasValue("Sarah-Jane");
            assertThat(lastNameField).hasValue("Smith");
            assertThat(emailNameField).hasValue("sarah-jane@example.com");
            assertThat(messageField).hasValue("Hello, world!");
            assertThat(subjectField).hasValue("warranty");

            String uploadedFile = uploadField.inputValue();
            org.assertj.core.api.Assertions.assertThat(uploadedFile).endsWith("sample-data.txt");
        }

        @DisplayName("Mandatory fields")
        @ParameterizedTest
        @ValueSource(strings = {"First name", "Last name", "Email", "Message"})
        void mandatoryFields(String fieldName) {
            var firstNameField = page.getByLabel("First name");
            var lastNameField = page.getByLabel("Last name");
            var emailNameField = page.getByLabel("Email");
            var messageField = page.getByLabel("Message");
            var subjectField = page.getByLabel("Subject");
            var sendButton = page.getByText("Send");

            // Fill in the field values
            firstNameField.fill("Sarah-Jane");
            lastNameField.fill("Smith");
            emailNameField.fill("sarah-jane@example.com");
            messageField.fill("Hello, world!");
            subjectField.selectOption("Warranty");

            // Clear one of the fields
            page.getByLabel(fieldName).clear();

            sendButton.click();

            // Check the error message for that field
            var errorMessage = page.getByRole(AriaRole.ALERT).getByText(fieldName + " is required");

            assertThat(errorMessage).isVisible();
        }

        @DisplayName("Text fields")
        @Test
        void textFieldValues() {
            var messageField = page.getByLabel("Message");

            messageField.fill("This is my message");

            assertThat(messageField).hasValue("This is my message");
        }

        @DisplayName("Dropdown lists")
        @Test
        void dropdownFieldValues() {
            var subjectField = page.getByLabel("Subject");

            subjectField.selectOption("Warranty");

            assertThat(subjectField).hasValue("warranty");
        }

        @DisplayName("File uploads")
        @Test
        void fileUploads() throws URISyntaxException {
            var attachmentField = page.getByLabel("Attachment");

            Path attachment = Paths.get(ClassLoader.getSystemResource("data/sample-data.txt").toURI());

            page.setInputFiles("#attachment", attachment);

            String uploadedFile = attachmentField.inputValue();

            org.assertj.core.api.Assertions.assertThat(uploadedFile).endsWith("sample-data.txt");
        }


        @DisplayName("By CSS class")
        @Test
        void locateTheSendButtonByCssClass() {
            page.locator("#first_name").fill("Sarah-Jane");
            page.locator(".btnSubmit").click();
            List<String> alertMessages = page.locator(".alert").allTextContents();
            Assertions.assertTrue(!alertMessages.isEmpty());

        }

        @DisplayName("By attribute")
        @Test
        void locateTheSendButtonByAttribute() {
            page.locator("input[placeholder='Your last name *']").fill("Smith");
            assertThat(page.locator("#last_name")).hasValue("Smith");
        }
    }
}
