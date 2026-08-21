package ru.practicum.moviehub.store;

import ru.practicum.moviehub.model.Movie;
import ru.practicum.moviehub.model.MovieResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoviesStore {


    private Map<Integer, Movie> movies = new HashMap<>();
    private int nextId = 1;


    public MoviesStore() {
        movies = new HashMap<>();
    }

    public MoviesStore(List<Movie> movieList) {
        int id = 1;

        for (Movie movie : movieList) {
            movies.put(id++, movie);
        }
        nextId = id;
    }

    public int addMovie(Movie movie) {
        int id = nextId++;
        movies.put(id, movie);
        return id;
    }

    public Movie findMovieById(int id) {
        return movies.get(id);
    }


    public List<MovieResponse> filterMoviesByYear(int neededYear) {
        return movies.entrySet().stream().filter(entry -> entry.getValue().getYear() == neededYear).map(entry -> new MovieResponse(entry.getKey(), entry.getValue())).toList();
    }

    public List<MovieResponse> getAllMovies() {
        return movies.entrySet().stream().map(entry -> new MovieResponse(entry.getKey(), entry.getValue())).toList();
    }

    public void deleteMovieById(int id) {
        movies.remove(id);
    }

    public void clearMovieStore() {
        movies.clear();
    }


}
