package com.serenitydojo.playwright.toolshop.cucumber.stepdefinitions;

import com.serenitydojo.playwright.toolshop.catalog.pageobjects.NavBar;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.ProductList;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.SearchComponent;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.eo.Se;
import io.cucumber.messages.types.Product;
import org.assertj.core.api.Assertions;

public class ProductCatalogStepDefinitions {

    NavBar navBar;
    SearchComponent searchComponent;
    ProductList productList;

    @Before
    public void setupPageObjects() {
         navBar = new NavBar(PlaywrightCucumberFixtures.getPage());
         searchComponent = new SearchComponent(PlaywrightCucumberFixtures.getPage());
         productList = new ProductList(PlaywrightCucumberFixtures.getPage());
    }

    @Given("Sally is on the home page")
    public void sally_is_on_the_home_page() {
        navBar.openHomePage();
    }
    @When("she searches for {string}")
    public void she_searches_for(String searchTerm) {
        searchComponent.searchBy(searchTerm);
    }
    @Then("the {string} product should be displayed")
    public void the_product_should_be_displayed(String productName) {
        var matchingProducts = productList.getProductNames();
        Assertions.assertThat(matchingProducts).contains(productName);
    }

}
