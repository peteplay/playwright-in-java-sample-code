package com.serenitydojo.playwright.todomvc;

import com.microsoft.playwright.Page;
import com.microsoft.playwright.junit.UsePlaywright;
import com.serenitydojo.playwright.fixtures.ChromeHeadlessOptions;
import com.serenitydojo.playwright.todomvc.pageobjects.TodoMvcAppPage;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Adding and deleting todo items to the list")
@Feature("Adding and deleting todo items to the list")
@UsePlaywright(ChromeHeadlessOptions.class)
class AddingAndDeletingTodoItemsTest {

    TodoMvcAppPage todoMvcApp;

    @BeforeEach
    void openApp(Page page) {
        todoMvcApp = new TodoMvcAppPage(page);
        todoMvcApp.open();
    }

    @Story("When the application starts")
    @DisplayName("When the application starts")
    @Nested
    class WhenTheApplicationStarts {
        @DisplayName("The list should be empty")
        @Test
        void the_list_should_initially_be_empty() {
            // TODO: Implement me
            // 1) Verify that no items are displayed in the todo list
        }

        @DisplayName("The user should be prompted to enter a todo item")
        @Test
        void the_user_should_be_prompted_to_enter_a_value() {
            // TODO: Implement me
            // 1) Verify that the input field is visible
            // 2) Verify that the placeholder text is "What needs to be done?"
        }
    }

    @Story("When we want to add item to the list")
    @DisplayName("When we want to add item to the list")
    @Nested
    class WhenAddingItems {

        @DisplayName("We can add a single item")
        @Test
        void addingASingleItem() {
            // TODO: Implement me
            // 1) Add a single todo item "Feed the cat"
            // 2) Verify that the list contains exactly "Feed the cat"
        }

        @DisplayName("We can add multiple items")
        @Test
        void addingSeveralItem() {
            // TODO: Implement me
            // 1) Add multiple items "Feed the cat" and "Walk the dog"
            // 2) Verify that the list contains exactly "Feed the cat" and "Walk the dog"
        }

        @DisplayName("We can't add an empty item")
        @Test
        void addingAnEmptyItem() {
            // TODO: Implement me
            // 1) Add a valid item "Feed the cat"
            // 2) Attempt to add an empty item
            // 3) Verify that the list contains only "Feed the cat"
        }

        @DisplayName("We can add duplicate items")
        @Test
        void addingDuplicateItem() {
            // TODO: Implement me
            // 1) Add items "Feed the cat", "Walk the dog", and "Feed the cat" again
            // 2) Verify that the list contains duplicates in the order they were added
        }

        @DisplayName("We can add items with non-English characters")
        @Test
        void addingNonEnglishItems() {
            // TODO: Implement me
            // 1) Add items in various languages (e.g., "Feed the cat", "喂猫", "إطعام القط")
            // 2) Verify that each item appears in the list as added
        }
    }

    @Story("When we want to delete item in the list")
    @DisplayName("When we want to delete item in the list")
    @Nested
    class WhenDeletingItems {

        @DisplayName("We can delete an item in the middle of the list")
        @Test
        void deletingAnItemInTheMiddleOfTheList() {
            // TODO: Implement me
            // 1) Add items "Feed the cat", "Walk the dog", "Buy some milk"
            // 2) Delete "Walk the dog"
            // 3) Verify that the list contains "Feed the cat" and "Buy some milk"
        }

        @DisplayName("We can delete an item at the end of the list")
        @Test
        void deletingAnItemAtTheEndOfTheList() {
            // TODO: Implement me
            // 1) Add items "Feed the cat", "Walk the dog", "Buy some milk"
            // 2) Delete "Buy some milk"
            // 3) Verify that the list contains "Feed the cat" and "Walk the dog"
        }

        @DisplayName("We can delete an item at the start of the list")
        @Test
        void deletingAnItemAtTheStartOfTheList() {
            // TODO: Implement me
            // 1) Add items "Feed the cat", "Walk the dog", "Buy some milk"
            // 2) Delete "Feed the cat"
            // 3) Verify that the list contains "Walk the dog" and "Buy some milk"
        }
    }
}
