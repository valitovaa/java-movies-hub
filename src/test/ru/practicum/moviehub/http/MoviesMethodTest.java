package ru.practicum.moviehub.http;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MoviesMethodTest extends MoviesApiTest {

    @Test
    void whenMethodIsNotSupported_returns405() throws Exception {

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(BASE + "/movies")).PUT(HttpRequest.BodyPublishers.noBody()).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        assertEquals(405, response.statusCode());

        assertEquals("application/json; charset=UTF-8", response.headers().firstValue("Content-Type").orElse(""));

        assertTrue(response.body().contains("\"error\""));
    }
}