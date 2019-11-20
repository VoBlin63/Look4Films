package ru.buryachenko.hw_look4films.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {FilmInDb.class},
        version = 1,
        exportSchema = false)
public abstract class FilmsDatabase extends RoomDatabase {

    public abstract DaoFilm daoFilm();

}