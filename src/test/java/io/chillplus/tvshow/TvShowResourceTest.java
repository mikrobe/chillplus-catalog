package io.chillplus.tvshow;

import io.quarkus.test.junit.QuarkusTest;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TvShowResourceTest {

    @Test
    @Order(1)
    public void testGetAllEndpoint() {
        given()
          .when().get("/api/tv")
          .then()
             .statusCode(200)
             .body("size()", is(0));
    }

    @Test
    @Order(2)
    public void testCreateSuccessEndpoint() {

        given()
            .contentType("application/json")
            .body(new CreateTvShowCommand("The Shield", "Drama"))
            .when().post("/api/tv")
            .then()
                .statusCode(201);

        given()
          .when().get("/api/tv")
          .then()
             .statusCode(200)
             .contentType(MediaType.APPLICATION_JSON)
             .body("size()", is(1))
             .body(containsString("The Shield"));
    }

    @Test
    @Order(3)
    public void testCreateFailureEndpoint() {

        given()
            .contentType("application/json")
            .body(new CreateTvShowCommand("", "Drama"))
            .when().post("/api/tv")
            .then()
                .statusCode(400);

        given()
          .when().get("/api/tv")
          .then()
             .statusCode(200)
             .body("size()", is(1));
    }

    @Test
    @Order(4)
    public void testGetOneByIdSuccessEndpoint() {

        given()
        .when().get("/api/tv/{id}", 0L)
        .then()
           .statusCode(200)
           .body(containsString("Shield"))
           .body("id", is(0));
    }


    @Test
    @Order(5)
    public void testGetOneByIdFailureEndpoint() {

        given()
        .when().get("/api/tv/{id}", 42L)
        .then()
           .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @Order(6)
    public void testDeleteByIdEndpoint() {
        given()
        .when().delete("/api/tv/{id}", 0L)
        .then()
           .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(7)
    public void testDeleteAllEndpoint() {
        given()
        .when().delete("/api/tv")
        .then()
           .statusCode(Response.Status.OK.getStatusCode());
    }

}