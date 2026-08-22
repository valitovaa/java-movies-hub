package ru.practicum.moviehub.http;

import org.junit.jupiter.api.Test;
import ru.practicum.moviehub.model.Movie;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoviesDeleteTest extends MoviesApiTest {

    @Test
    void deleteMovieById_whenMovieExists_deletesMovie() throws Exception {

        int id = moviesStore.addMovie(
                new Movie("Матрица", 1999)
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/" + id))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(
                        StandardCharsets.UTF_8
                )
        );

        assertEquals(204, response.statusCode());

        assertEquals(
                "application/json; charset=UTF-8",
                response.headers()
                        .firstValue("Content-Type")
                        .orElse("")
        );

        assertTrue(response.body().isEmpty());

        // Проверяем, что фильм действительно удалён
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/" + id))
                .GET()
                .build();

        HttpResponse<String> getResponse = client.send(
                getRequest,
                HttpResponse.BodyHandlers.ofString(
                        StandardCharsets.UTF_8
                )
        );

        assertEquals(404, getResponse.statusCode());
    }


    @Test
    void deleteMovieById_whenMovieDoesNotExist_returns404()
            throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/999"))
                .DELETE()
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
    void deleteMovieById_whenIdIsNotNumber_returns400()
            throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies/abc"))
                .DELETE()
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