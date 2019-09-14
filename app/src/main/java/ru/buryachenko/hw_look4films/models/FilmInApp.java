package ru.buryachenko.hw_look4films.models;

public class FilmInApp extends Film {
    private Boolean liked = false;
    private String details = "";
    private String comment = "";
    public FilmInApp(String name) {
        super(name);
    }
}
