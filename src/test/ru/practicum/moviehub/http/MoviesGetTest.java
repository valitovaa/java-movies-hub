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
        HttpResponse<String> response = sendGet("/movies");

        assertEquals(200, response.statusCode());

        assertEquals("application/json; charset=UTF-8", response.headers().firstValue("Content-Type").orElse(""));

        String body = response.body().trim();

        assertTrue(body.startsWith("[") && body.endsWith("]"), "Ожидается JSON-массив");
    }

    @Test
    void getMovies_whenMoviesExist_returnsMovies() throws Exception {
        moviesStore.addMovie(new Movie("Матрица", 1999));
        moviesStore.addMovie(new Movie("Интерстеллар", 2014));

        HttpResponse<String> response = sendGet("/movies");

        assertEquals(200, response.statusCode());

        assertTrue(response.body().contains("Матрица"));
        assertTrue(response.body().contains("Интерстеллар"));
    }

    private HttpResponse<String> sendGet(String path) throws Exception {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE + path)).GET().build();

        return client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
}