package ru.practicum.moviehub.http;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoviesPostTest extends MoviesApiTest {

    @Test
    void postMovie_whenDataIsValid_createsMovie() throws Exception {

        String json = """
                {
                    "title": "Матрица",
                    "year": 1999
                }
                """;

        HttpResponse<String> response = sendPost(json);

        assertEquals(201, response.statusCode());

        assertEquals("application/json; charset=UTF-8", response.headers().firstValue("Content-Type").orElse(""));

        assertTrue(response.body().contains("\"id\""));
        assertTrue(response.body().contains("Матрица"));
        assertTrue(response.body().contains("1999"));
    }


    @Test
    void postMovie_whenTitleIsEmpty_returns422() throws Exception {

        String json = """
                {
                    "title": "",
                    "year": 1999
                }
                """;

        HttpResponse<String> response = sendPost(json);

        assertEquals(422, response.statusCode());

        assertTrue(response.body().contains("\"error\""));
        assertTrue(response.body().contains("\"details\""));
    }


    @Test
    void postMovie_whenTitleIsTooLong_returns422() throws Exception {

        String title = "a".repeat(101);

        String json = """
                {
                    "title": "%s",
                    "year": 1999
                }
                """.formatted(title);

        HttpResponse<String> response = sendPost(json);

        assertEquals(422, response.statusCode());

        assertTrue(response.body().contains("\"error\""));
        assertTrue(response.body().contains("\"details\""));
    }


    @Test
    void postMovie_whenYearIsLessThan1888_returns422() throws Exception {

        String json = """
                {
                    "title": "Матрица",
                    "year": 1887
                }
                """;

        HttpResponse<String> response = sendPost(json);

        assertEquals(422, response.statusCode());

        assertTrue(response.body().contains("\"error\""));
        assertTrue(response.body().contains("\"details\""));
    }


    @Test
    void postMovie_whenYearIsGreaterThanCurrentYearPlusOne_returns422() throws Exception {

        int invalidYear = Year.now().getValue() + 2;

        String json = """
                {
                    "title": "Матрица",
                    "year": %d
                }
                """.formatted(invalidYear);

        HttpResponse<String> response = sendPost(json);

        assertEquals(422, response.statusCode());

        assertTrue(response.body().contains("\"error\""));
        assertTrue(response.body().contains("\"details\""));
    }


    @Test
    void postMovie_whenContentTypeIsWrong_returns415() throws Exception {

        String json = """
                {
                    "title": "Матрица",
                    "year": 1999
                }
                """;

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE + "/movies")).header("Content-Type", "text/plain").POST(HttpRequest.BodyPublishers.ofString(json)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(415, response.statusCode());

        assertTrue(response.body().contains("\"error\""));
    }


    @Test
    void postMovie_whenJsonIsInvalid_returns400() throws Exception {

        String json = """
                {
                    "title": "Матрица",
                    "year":
                """;

        HttpResponse<String> response = sendPost(json);

        assertEquals(400, response.statusCode());

        assertTrue(response.body().contains("\"error\""));
    }


    private HttpResponse<String> sendPost(String json) throws Exception {

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE + "/movies")).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(json)).build();

        return client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    }
}