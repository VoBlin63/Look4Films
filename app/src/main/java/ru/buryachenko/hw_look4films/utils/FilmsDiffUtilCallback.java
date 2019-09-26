package ru.buryachenko.hw_look4films.utils;

import java.util.List;

import androidx.recyclerview.widget.DiffUtil;
import ru.buryachenko.hw_look4films.models.FilmInApp;

public class FilmsDiffUtilCallback extends DiffUtil.Callback {

    private final List<FilmInApp> oldList;
    private final List<FilmInApp> newList;

    public FilmsDiffUtilCallback(List<FilmInApp> oldList, List<FilmInApp> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        FilmInApp oldFilmInApp = oldList.get(oldItemPosition);
        FilmInApp newFilmInApp = newList.get(newItemPosition);
        return oldFilmInApp.getFilmId() == newFilmInApp.getFilmId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        FilmInApp oldFilmInApp = oldList.get(oldItemPosition);
        FilmInApp newFilmInApp = newList.get(newItemPosition);
        return oldFilmInApp.getName().equals(newFilmInApp.getName())
                && oldFilmInApp.getComment().equals(newFilmInApp.getComment())
                && oldFilmInApp.getLiked() == newFilmInApp.getLiked();
    }
}
