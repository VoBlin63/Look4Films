package ru.buryachenko.hw_look4films.viewmodel;

import java.util.ArrayList;

import androidx.lifecycle.ViewModel;
import ru.buryachenko.hw_look4films.models.FilmInApp;

public class FilmsViewModel extends ViewModel {
    private ArrayList<FilmInApp> films = new ArrayList<>();
    public String[] getList() {
        return new String[]{"Фильм1", "Фильм2"};
    }
}
