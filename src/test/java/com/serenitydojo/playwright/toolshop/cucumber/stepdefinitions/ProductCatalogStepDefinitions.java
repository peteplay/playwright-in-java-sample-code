package com.serenitydojo.playwright.toolshop.cucumber.stepdefinitions;

import com.serenitydojo.playwright.toolshop.catalog.pageobjects.NavBar;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.ProductList;
import com.serenitydojo.playwright.toolshop.catalog.pageobjects.SearchComponent;
import com.serenitydojo.playwright.toolshop.fixtures.ProductSummary;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.DataTableType;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java.eo.Se;
import io.cucumber.messages.types.Product;
import org.assertj.core.api.Assertions;

import java.util.List;
import java.util.Map;

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

    @DataTableType
    public ProductSummary productSummaryRow(Map<String, String> productData) {
        return new ProductSummary(productData.get("Product"),productData.get("Price"));
    }

    @Then("the following products should be displayed:")
    public void theFollowingProductsShouldBeDisplayed(List<ProductSummary> expectedProductSummaries) {
        List<ProductSummary> matchingProducts = productList.getProductSummaries();
        Assertions.assertThat(matchingProducts).containsExactlyInAnyOrderElementsOf(expectedProductSummaries);
    }
}
