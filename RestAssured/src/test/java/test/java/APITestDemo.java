
package test.java;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import io.restassured.response.Response;
import org.testng.annotations.Test;

public class APITestDemo {

    private static final String BASE_URI = "http://openlibrary.org";
    private static final String AUTHOR_PATH = "/authors/OL1A.json";

    @Test
    public void verifyAuthorFields_BDD() {
        given()
                .baseUri(BASE_URI)
                .log().uri()            // Log request URI for demo
                .when()
                .get(AUTHOR_PATH)
                .then()
                .log().ifValidationFails()
                .statusCode(200)        // HTTP OK
                .body("personal_name", equalTo("Sachi Rautroy"))
                .body("alternate_names", hasItem("Yugashrashta Sachi Routray"));
    }

    @Test
    public void printResponse_ForDemo() {
        Response response = given()
                .baseUri(BASE_URI)
                .log().uri()
                .when()
                .get(AUTHOR_PATH);

        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response:\n" + response.getBody().asPrettyString());

        response.then()
                .statusCode(200)
                .body("personal_name", equalTo("Sachi Rautroy"))
                .body("alternate_names", hasItem("Yugashrashta Sachi Routray"));
    }
}
