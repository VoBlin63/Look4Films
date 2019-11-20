package ru.buryachenko.hw_look4films.db;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DaoFilm {
        @Query("SELECT * FROM filmindb")
        List<FilmInDb> getAll();

        @Query("SELECT count(*) FROM filmindb")
        int getCount();

        @Query("SELECT * FROM filmindb WHERE id = :id")
        FilmInDb getById(int id);

        @Insert
        void insert(FilmInDb employee);

        @Update
        void update(FilmInDb employee);

        @Delete
        void delete(FilmInDb employee);

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(List<FilmInDb> employees);
}
