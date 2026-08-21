package ru.practicum.moviehub.model;
//title — не пустая строка, длина ≤ 100 символов.
//year — число от 1888 (год самого раннего из сохранившихся фильмов) до текущий год + 1.

public class Movie {
    String title;
    int year;

    public Movie( String title, int year) {
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