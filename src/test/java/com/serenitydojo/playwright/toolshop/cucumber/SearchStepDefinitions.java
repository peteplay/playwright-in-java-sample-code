package com.serenitydojo.playwright.toolshop.cucumber;

import com.serenitydojo.playwright.toolshop.catalog.pageobjects.NavBar;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.ProductList;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.SearchComponent;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SearchStepDefinitions {

    NavBar navBar;
    ProductList productList;
    SearchComponent searchComponent;

    @Before
    public void setUp() {
        navBar = new NavBar(PlaywrightHooks.getPage());
        searchComponent = new SearchComponent(PlaywrightHooks.getPage());
        productList = new ProductList(PlaywrightHooks.getPage());
    }

    @Given("Sharon is on the home page")
    public void sharon_is_on_the_home_page() {
        navBar.openHomePage();
    }

    @When("she searches for {string}")
    public void she_searches_for(String searchTerm) {
        searchComponent.searchBy(searchTerm);

    }

    @Then("she should be presented with the following products:")
    public void she_should_be_presented_with_the_following_products(List<String> expectedProducts) {

        var matchingProducts = productList.getProductNames();

        assertThat(matchingProducts).containsExactlyElementsOf(expectedProducts);
    }

    @Then("she should not see any products")
    public void she_should_not_see_any_products() {
        var matchingProducts = productList.getProductNames();
        assertThat(matchingProducts).isEmpty();
    }

}
