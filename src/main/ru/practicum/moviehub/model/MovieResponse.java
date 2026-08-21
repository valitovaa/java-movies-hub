package ru.practicum.moviehub.model;

public class MovieResponse {

    private final int id;
    private final String title;
    private final int year;

    public MovieResponse(int id, Movie movie) {
        this.id = id;
        this.title = movie.getTitle();
        this.year = movie.getYear();
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }
}