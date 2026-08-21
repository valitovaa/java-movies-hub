package ru.practicum.moviehub.model;

public class Movie {
    String title;
    int year;

    public Movie(String title, int year) {
        this.year = year;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }
}