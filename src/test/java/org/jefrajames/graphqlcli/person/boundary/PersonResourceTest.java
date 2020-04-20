package org.jefrajames.graphqlcli.person.boundary;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import lombok.extern.java.Log;
import static org.hamcrest.CoreMatchers.equalTo;
import org.jefrajames.graphqlcli.person.entity.FindAllPeople;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
@Log
public class PersonResourceTest {

    @Test
    public void testFindAllPeople() {
        Response response = 
                given()
                .when()
                    .get("/persons")
                .then()
                    .statusCode(200)
                    .contentType(ContentType.JSON)
                    .extract()
                    .response();

        List<FindAllPeople> jsonResponse = response.jsonPath().getList("$");
        assertTrue(jsonResponse.size() >= 100);
    }

    @Test
    public void testFindPersonById() {
        given()
            .urlEncodingEnabled(true)
            .pathParam("id", 10)
        .when()
            .get("/persons/{id}")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("id", equalTo(10));
    }

    @Test
    public void testAddPerson() {

        JsonObject requestBody = Json.createObjectBuilder()
                .add("surname", "James")
                .add("names", Json.createArrayBuilder().add("Jean-Francois").add("Alphonse").build())
                .add("birthDate", "1962-04-27")
                .build();

        log.info("JJS => requestBody=" + requestBody.toString());

        given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
        .when()
                .post("/persons")
        .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("surname", equalTo("James"))
                .body("birthDate", equalTo("1962-04-27"));
    }

    @Test
    public void testUpdatePerson() {

        JsonObject requestBody = Json.createObjectBuilder()
                .add("id", 100)
                .add("surname", "James")
                .add("names", Json.createArrayBuilder().add("Jean-Francois").add("Alphonse").build())
                .add("birthDate", "1962-04-27")
                .add("interests", Json.createArrayBuilder().add("Martial arts").add("Food").add("Cinema").build())
                .build();

        given()
                .contentType(ContentType.JSON)
                .body(requestBody.toString())
        .when()
                .patch("/persons")
        .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("surname", equalTo("James"))
                .body("birthDate", equalTo("1962-04-27"));
    }

}
