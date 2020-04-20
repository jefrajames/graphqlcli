package org.jefrajames.graphqlcli.profile.boundary;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

@QuarkusTest
public class ProfileResourceTest {

    @Test
    public void testFindProfileByPersonId() {
        given()
            .pathParam("personId", 10)
        .when()
            .get("/profiles/{personId}")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("person.id", equalTo(10));
    }

    @Test
    public void testFindProfileFull() {
        given()
            .pathParam("personId", 25)
        .when()
            .get("/profiles/full/{personId}")
        .then()
            .contentType(ContentType.JSON)
            .statusCode(200)
            .body("person.id", equalTo(25))
            .body("$", hasKey("scores"));
    }

}
