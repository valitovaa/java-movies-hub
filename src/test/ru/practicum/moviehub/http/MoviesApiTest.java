package ru.practicum.moviehub.http;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import ru.practicum.moviehub.store.MoviesStore;

import java.net.http.HttpClient;
import java.time.Duration;

public abstract class MoviesApiTest {

    protected static final String BASE = "http://localhost:8080";

    protected static MoviesServer server;
    protected static HttpClient client;
    protected static MoviesStore moviesStore;

    @BeforeAll
    static void beforeAll() throws Exception {

        moviesStore = new MoviesStore();

        server = new MoviesServer(moviesStore, 8080);
        server.start();

        client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(2)).build();
    }

    @BeforeEach
    void beforeEach() {
        moviesStore.clearMovieStore();
    }

    @AfterAll
    static void afterAll() {
        server.stop();
    }
}