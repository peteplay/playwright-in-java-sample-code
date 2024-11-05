package com.serenitydojo.playwright;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Arrays;
import java.util.List;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

@Execution(ExecutionMode.SAME_THREAD)
public class PlaywrightPageObjectTest {

    protected static Playwright playwright;
    protected static Browser browser;
    protected static BrowserContext browserContext;

    Page page;

    @BeforeAll
    static void setUpBrowser() {
        playwright = Playwright.create();
        playwright.selectors().setTestIdAttribute("data-test");
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

    @BeforeEach
    void openHomePage() {
        page.navigate("https://practicesoftwaretesting.com");
    }

    @Nested
    class WhenSearchingProductsByKeyword {

        @DisplayName("Without Page Objects")
        @Test
        void withoutPageObjects() {
            page.getByPlaceholder("Search").fill("tape");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
            page.waitForResponse("**/products/search?q=tape", () -> {
            });

            assertThat(page.getByTestId("product-name")).hasCount(3);
            Assertions.assertThat(page.getByTestId("product-name").allInnerTexts())
                    .contains("Tape Measure 7.5m", "Measuring Tape", "Tape Measure 5m");

        }

        static class SearchComponent {

            private final Page page;

            public SearchComponent(Page page) {
                this.page = page;
            }

            private Locator searchField() {
                return page.getByTestId("search-query");
            }

            private Locator searchButton() {
                return page.getByTestId("search-submit");
            }

            void searchByKeyword(String keyword) {
                page.waitForResponse(
                        "**/products/search*",
                        () -> {
                            searchField().fill(keyword);
                            searchButton().click();
                        }
                );
            }
        }

        record ProductItem(String name, String image, double price) {
        }

        ;

        static class ProductList {
            private final Page page;

            public ProductList(Page page) {
                this.page = page;
            }

            private Locator getProductCards() {
                return page.locator(".card");
            }

            private double extractPrice(Locator priceElement) {
                return Double.parseDouble(
                        priceElement.textContent()
                                .trim()
                                .replace("$", "")
                );
            }

            public List<ProductItem> getProducts() {
                return getProductCards()
                        .all()
                        .stream()
                        .map(card -> new ProductItem(
                                card.getByTestId("product-name").innerText(),
                                card.locator(".card-img-top").getAttribute("src"),
                                extractPrice(card.getByTestId("product-price"))
                        ))
                        .toList();
            }

            public void viewProduct(String productName) {
                page.locator(".card").getByText(productName).click();
            }
        }

        @Test
        void withPageObjects() {
            SearchComponent searchComponent = new SearchComponent(page);
            ProductList productList = new ProductList(page);

            searchComponent.searchByKeyword("Tape");
            List<ProductItem> matchingProducts = productList.getProducts();

            Assertions.assertThat(matchingProducts)
                    .as("Should find correct number of tape measures")
                    .hasSize(3);

            Assertions.assertThat(matchingProducts)
                    .as("All products should have valid prices")
                    .allMatch(product -> product.price() > 0.0);

            Assertions.assertThat(matchingProducts)
                    .extracting(ProductItem::name)
                    .as("Should find all tape measure products")
                    .containsExactlyInAnyOrder(
                            "Tape Measure 7.5m",
                            "Measuring Tape",
                            "Tape Measure 5m"
                    );
        }
    }

    @Nested
    class WhenAddingItemsToTheCart {

        @DisplayName("Without Page Objects")
        @Test
        void withoutPageObjects() {
            page.getByPlaceholder("Search").fill("pliers");
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Search")).click();
            page.waitForResponse("**/products/search?q=pliers", () -> {
            });
            page.locator(".card").getByText("Combination Pliers").click();
            page.getByTestId("increase-quantity").click();
            page.getByTestId("increase-quantity").click();
            page.getByText("Add to cart").click();
            page.waitForCondition(() -> page.getByTestId("cart-quantity").textContent().equals("3"));
            page.getByTestId("nav-cart").click();

            assertThat(page.locator(".product-title").getByText("Combination Pliers")).isVisible();
            assertThat(page.getByTestId("cart-quantity").getByText("3")).isVisible();
        }

        @DisplayName("With Page Objects")
        @Test
        void withPageObjects() {
            WhenSearchingProductsByKeyword.SearchComponent searchComponent = new WhenSearchingProductsByKeyword.SearchComponent(page);
            WhenSearchingProductsByKeyword.ProductList productList = new WhenSearchingProductsByKeyword.ProductList(page);
            ProductDetails productDetails = new ProductDetails(page);
            NavBar navBar = new NavBar(page);
            CheckoutCart checkoutCart = new CheckoutCart(page);

            searchComponent.searchByKeyword("pliers");
            productList.viewProduct("Combination Pliers");

            productDetails.addToCart();
            navBar.openCart();

            List<CheckoutItem> checkoutItems = checkoutCart.getCheckoutItems();

            Assertions.assertThat(checkoutItems).hasSize(1);
            Assertions.assertThat(checkoutItems.get(0).title()).contains("Combination Pliers");
            Assertions.assertThat(checkoutItems.get(0).quantity()).isEqualTo(1);

        }

        @DisplayName("With Page Objects and multiple items")
        @Test
        void withPageObjectsAndMultipleItems() {
            WhenSearchingProductsByKeyword.SearchComponent searchComponent = new WhenSearchingProductsByKeyword.SearchComponent(page);
            WhenSearchingProductsByKeyword.ProductList productList = new WhenSearchingProductsByKeyword.ProductList(page);
            ProductDetails productDetails = new ProductDetails(page);
            NavBar navBar = new NavBar(page);
            CheckoutCart checkoutCart = new CheckoutCart(page);

            searchComponent.searchByKeyword("pliers");
            productList.viewProduct("Combination Pliers");

            productDetails.increaseQuantityBy(2);
            productDetails.addToCart();
            navBar.openCart();

            List<CheckoutItem> checkoutItems = checkoutCart.getCheckoutItems();

            Assertions.assertThat(checkoutItems).hasSize(1);
            Assertions.assertThat(checkoutItems.get(0).title()).contains("Combination Pliers");
            Assertions.assertThat(checkoutItems.get(0).quantity()).isEqualTo(3);
        }


        @DisplayName("With Page Objects and multiple products")
        @Test
        void withPageObjectsAndMultipleProducts() {
            WhenSearchingProductsByKeyword.SearchComponent searchComponent = new WhenSearchingProductsByKeyword.SearchComponent(page);
            WhenSearchingProductsByKeyword.ProductList productList = new WhenSearchingProductsByKeyword.ProductList(page);
            ProductDetails productDetails = new ProductDetails(page);
            NavBar navBar = new NavBar(page);
            CheckoutCart checkoutCart = new CheckoutCart(page);

            searchComponent.searchByKeyword("pliers");
            productList.viewProduct("Combination Pliers");
            productDetails.addToCart();

            navBar.openHomePage();
            ;
            productList.viewProduct("Bolt Cutters");
            productDetails.addToCart();

            navBar.openCart();

            List<CheckoutItem> checkoutItems = checkoutCart.getCheckoutItems();

            Assertions.assertThat(checkoutItems).hasSize(2);
        }


        static class ProductDetails {
            private final Page page;

            ProductDetails(Page page) {
                this.page = page;
            }

            public void increaseQuantityBy(int increment) {
                for (int i = 0; i < increment; i++) {
                    increaseQuantityButton().click();
                }
            }

            public void addToCart() {
                page.waitForResponse(
                        response -> response.url().contains("/carts/") && response.request().method().equals("POST"),
                        () -> {
                            addToCartButton().click();
                        }
                );
            }

            private Locator increaseQuantityButton() {
                return page.getByTestId("increase-quantity");
            }

            private Locator addToCartButton() {
                return page.getByText("Add to cart");
            }
        }

        static class NavBar {
            private final Page page;

            NavBar(Page page) {
                this.page = page;
            }

            public void openCart() {
                page.navigate("https://practicesoftwaretesting.com/checkout");
                page.waitForSelector(".product-title");
            }

            public void openHomePage() {
                page.navigate("https://practicesoftwaretesting.com");
            }
        }

        static class CheckoutCart {
            private final Page page;

            CheckoutCart(Page page) {
                this.page = page;
            }

            List<CheckoutItem> getCheckoutItems() {
                return page.locator("app-cart tbody tr")
                        .all()
                        .stream()
                        .map(
                                checkoutItemRow -> {
                                    List<Locator> itemRowCells = checkoutItemRow.locator("td").all();
                                    String title = stripBlankCharactersFrom(checkoutItemRow.locator(".product-title").innerText());
                                    String itemQuantity = checkoutItemRow.locator("td").all().get(1).locator("input").inputValue();
                                    int quantity = Integer.parseInt(itemQuantity);
                                    double itemPrice = extractPrice(itemRowCells.get(2).textContent());
                                    double totalPrice = extractPrice(itemRowCells.get(3).textContent());
                                    return new CheckoutItem(title, quantity, itemPrice, totalPrice);
                                }
                        ).toList();
            }

            private String stripBlankCharactersFrom(String value) {
                return value.strip().replaceAll("\u00A0+$", "");
            }

            private double extractPrice(String priceValue) {
                return Double.parseDouble(priceValue.replace("$", ""));
            }

        }

        record CheckoutItem(String title, int quantity, double price, double total) {
        }
    }
}
