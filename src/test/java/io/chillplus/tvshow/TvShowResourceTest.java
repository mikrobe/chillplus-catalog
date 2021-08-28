package io.chillplus.tvshow;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectSpy;


@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
public class TvShowResourceTest {

    @InjectSpy
    TvShowService tvShowService;

    List<TvShow> mockShows = new ArrayList<>();

    @BeforeAll
    public void prepareMockShow() {
        mockShows.add(new TvShow(0L, "Mork & Mindy", "Sit-com"));
        mockShows.add(new TvShow(1L, "Seinfeld", "Sit-com"));
    }
    
    @Test
    @Order(1)
    public void getEmptyTvShows() {

        given()
          .when().get("/api/tv")
          .then()
             .statusCode(200)
             .body("size()", is(0));
    }

    @Test
    @Order(1)
    public void getAllTvShows() {

        when(tvShowService.getTvShows()).thenReturn(mockShows);
        given()
          .when().get("/api/tv")
          .then()
             .statusCode(200)
             .body("size()", is(2));
    }

    @Test
    @Order(2)
    public void createTvShow() {

        given()
            .contentType("application/json")
            .body(new CreateTvShowCommand("The Shield", "Drama"))
            .when().post("/api/tv")
            .then()
                .statusCode(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body("title", is("The Shield"),
                      "category", is("Drama"));
    }

    @Test
    @Order(3)
    public void checkTvShowTitleIsNotBlank() {

        given()
            .contentType("application/json")
            .body(new CreateTvShowCommand("", "Drama"))
            .when().post("/api/tv")
            .then()
                .statusCode(400);
    }

    @Test
    @Order(4)
    public void getOneTvShow() {

        when(tvShowService.getTvShows()).thenReturn(mockShows);
        given()
        .when().get("/api/tv/{id}", 0L)
        .then()
           .statusCode(200)
           .body(containsString("Mork & Mindy"))
           .body("id", is(0));
    }


    @Test
    @Order(5)
    public void getNonExistingTvShow() {

        when(tvShowService.getTvShows()).thenReturn(mockShows);
        given()
        .when().get("/api/tv/{id}", 42L)
        .then()
           .statusCode(Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    @Order(6)
    public void deleteOneTvShow() {
        when(tvShowService.getTvShows()).thenReturn(mockShows);
        given()
        .when().delete("/api/tv/{id}", 0L)
        .then()
           .statusCode(Response.Status.OK.getStatusCode());
    }

    @Test
    @Order(7)
    public void deleteAllTvShows() {
        when(tvShowService.getTvShows()).thenReturn(mockShows);
        given()
        .when().delete("/api/tv")
        .then()
           .statusCode(Response.Status.OK.getStatusCode());
    }

}