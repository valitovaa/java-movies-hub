package ru.practicum.moviehub.http;


import ru.practicum.moviehub.model.Movie;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class MovieValidator {

    public List<String> validate(Movie movie) {
        List<String> errors = new ArrayList<>();

        if (movie.getTitle() == null || movie.getTitle().isBlank()) {
            errors.add("название не должно быть пустым");
        } else if (movie.getTitle().length() > 100) {
            errors.add("название не должно быть длиннее 100 символов");
        }

        int currentYear = Year.now().getValue();

        if (movie.getYear() < 1888 || movie.getYear() > currentYear + 1) {
            errors.add(
                    "год должен быть между 1888 и " + (currentYear + 1)
            );
        }

        return errors;
    }
}