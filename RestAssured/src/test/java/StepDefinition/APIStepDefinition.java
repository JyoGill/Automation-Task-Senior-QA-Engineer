
package StepDefinition;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

public class APIStepDefinition {

    private static final String BASE_URI = "https://openlibrary.org";
    private Response response;
    private RequestSpecification request;

    @Given("the Open Library Authors API")
    public void the_open_library_authors_api() {
        request = given().baseUri(BASE_URI).log().uri();
    }

    @When("I GET {string}")
    public void i_get(String path) {
        response = request.when().get(path);
    }

    @Then("the response status code should be {int}")
    public void the_response_status_code_should_be(Integer statusCode) {
        response.then().log().ifValidationFails().statusCode(statusCode);
    }

    @Then("the field {string} should be {string}")
    public void the_field_should_be(String jsonPath, String expectedValue) {
        response.then().body(jsonPath, equalTo(expectedValue));
    }

    @Then("the array {string} should contain {string}")
    public void the_array_should_contain(String jsonPath, String expectedItem) {
        response.then().body(jsonPath, hasItem(expectedItem));
    }
}
