package ru.practicum.moviehub.http;

import org.junit.jupiter.api.Test;
import ru.practicum.moviehub.model.Movie;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoviesGetByIdTest extends MoviesApiTest {

    @Test
    void getMovieById_whenMovieExists_returnsMovie() throws Exception {

        int id = moviesStore.addMovie(
                new Movie("Матрица", 1999)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/" + id))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(
                        StandardCharsets.UTF_8
                )
        );

        assertEquals(200, response.statusCode());

        assertEquals(
                "application/json; charset=UTF-8",
                response.headers()
                        .firstValue("Content-Type")
                        .orElse("")
        );

        assertTrue(response.body().contains("\"id\":" + id));
        assertTrue(response.body().contains("Матрица"));
        assertTrue(response.body().contains("1999"));
    }


    @Test
    void getMovieById_whenMovieDoesNotExist_returns404()
            throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/999"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(
                        StandardCharsets.UTF_8
                )
        );

        assertEquals(404, response.statusCode());

        assertTrue(response.body().contains("\"error\""));
    }


    @Test
    void getMovieById_whenIdIsNotNumber_returns400()
            throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/abc"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(
                        StandardCharsets.UTF_8
                )
        );

        assertEquals(400, response.statusCode());

        assertTrue(response.body().contains("\"error\""));
    }
}