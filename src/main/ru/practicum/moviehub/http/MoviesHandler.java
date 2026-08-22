package ru.practicum.moviehub.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.model.MovieResponse;
import ru.practicum.moviehub.store.MoviesStore;

import java.io.IOException;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MoviesHandler extends BaseHttpHandler {


    private final MoviesStore moviesStore;
    private final MovieValidator validator;
    private final Gson gson = new Gson();

    public MoviesHandler(MoviesStore moviesStore) {
        this.moviesStore = moviesStore;
        this.validator = new MovieValidator();
    }


    @Override
    public void handle(HttpExchange ex) throws IOException {

        String method = ex.getRequestMethod();

        if (method.equalsIgnoreCase("GET")) {

            String path = ex.getRequestURI().getPath();

            if (path.equals("/movies")) {

                if (ex.getRequestURI().getQuery() == null) {

                    List<MovieResponse> movies = moviesStore.getAllMovies();

                    sendJson(ex, 200, gson.toJson(movies));
                    return;
                }

                Integer year = parseYear(ex);

                if (year == null) {
                    return;
                }

                List<MovieResponse> movies = moviesStore.filterMoviesByYear(year);

                sendJson(ex, 200, gson.toJson(movies));
                return;
            }

            if (path.startsWith("/movies/")) {

                Integer id = parseMovieId(ex);

                if (id == null) {
                    return;
                }

                Movie movie = findMovie(ex, id);

                if (movie == null) {
                    return;
                }

                MovieResponse response = new MovieResponse(id, movie);

                sendJson(ex, 200, gson.toJson(response));
                return;
            }
        }

        if (method.equalsIgnoreCase("POST")) {

            String contentType = ex.getRequestHeaders().getFirst("Content-Type");

            if (contentType == null || !contentType.startsWith("application/json")) {

                sendError(ex, 415, "Неподдерживаемый тип содержимого", List.of());
                return;
            }

            try {

                Movie movie = gson.fromJson(new InputStreamReader(ex.getRequestBody(), StandardCharsets.UTF_8), Movie.class);

                List<String> errors = validator.validate(movie);

                if (!errors.isEmpty()) {

                    sendError(ex, 422, "Ошибка валидации", errors);
                    return;
                }

                int id = moviesStore.addMovie(movie);

                MovieResponse response = new MovieResponse(id, movie);

                sendJson(ex, 201, gson.toJson(response));
                return;

            } catch (com.google.gson.JsonSyntaxException e) {

                sendError(ex, 400, "Некорректный JSON", List.of());
                return;
            }
        }

        if (method.equalsIgnoreCase("DELETE")) {

            Integer id = parseMovieId(ex);

            if (id == null) {
                return;
            }

            Movie movie = findMovie(ex, id);

            if (movie == null) {
                return;
            }

            moviesStore.deleteMovieById(id);

            sendNoContent(ex);
            return;
        }

        sendError(ex, 405, "Метод не поддерживается", List.of());
    }

    private Integer parseMovieId(HttpExchange ex) throws IOException {

        String path = ex.getRequestURI().getPath();
        String idString = path.substring("/movies/".length());

        try {
            return Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            sendError(ex, 400, "Некорректный ID", List.of());
            return null;
        }
    }

    private Movie findMovie(HttpExchange ex, int id) throws IOException {

        Movie movie = moviesStore.findMovieById(id);

        if (movie == null) {
            sendError(ex, 404, "Фильм не найден", List.of());
            return null;
        }

        return movie;
    }

    private Integer parseYear(HttpExchange ex) throws IOException {
        String query = ex.getRequestURI().getQuery();

        if (query == null || !query.startsWith("year=")) {
            sendError(ex, 400, "Некорректный параметр запроса — 'year'", List.of());
            return null;
        }

        String yearString = query.substring("year=".length());

        try {
            return Integer.parseInt(yearString);
        } catch (NumberFormatException e) {
            sendError(ex, 400, "Некорректный параметр запроса — 'year'", List.of());
            return null;
        }
    }

}