package ru.practicum.moviehub.http;

import org.junit.jupiter.api.Test;
import ru.practicum.moviehub.model.Movie;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoviesGetTest extends MoviesApiTest {

    @Test
    void getMovies_whenEmpty_returnsEmptyArray() throws Exception {

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE + "/movies")).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(200, response.statusCode());

        assertEquals("application/json; charset=UTF-8", response.headers().firstValue("Content-Type").orElse(""));

        assertEquals("[]", response.body().trim());
    }

    @Test
    void getMovies_whenMoviesExist_returnsMovies() throws Exception {

        moviesStore.addMovie(new Movie("Матрица", 1999));
        moviesStore.addMovie(new Movie("Интерстеллар", 2014));

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE + "/movies")).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(200, response.statusCode());

        assertTrue(response.body().contains("Матрица"));
        assertTrue(response.body().contains("Интерстеллар"));
    }
}