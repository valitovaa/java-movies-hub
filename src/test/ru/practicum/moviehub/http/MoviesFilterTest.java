package ru.practicum.moviehub.http;

import org.junit.jupiter.api.Test;
import ru.practicum.moviehub.model.Movie;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoviesFilterTest extends MoviesApiTest {

    @Test
    void getMoviesByYear_whenMoviesExist_returnsMatchingMovies() throws Exception {

        moviesStore.addMovie(new Movie("Матрица", 1999));
        moviesStore.addMovie(new Movie("Интерстеллар", 2014));
        moviesStore.addMovie(new Movie("Другой фильм", 1999));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies?year=1999"))
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

        assertTrue(response.body().contains("Матрица"));
        assertTrue(response.body().contains("Другой фильм"));
        assertFalse(response.body().contains("Интерстеллар"));
    }


    @Test
    void getMoviesByYear_whenNoMoviesExist_returnsEmptyList() throws Exception {

        moviesStore.addMovie(new Movie("Матрица", 1999));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies?year=2020"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofString(
                        StandardCharsets.UTF_8
                )
        );

        assertEquals(200, response.statusCode());

        assertEquals("[]", response.body().trim());
    }


    @Test
    void getMoviesByYear_whenYearIsNotNumber_returns400() throws Exception {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE + "/movies?year=abc"))
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