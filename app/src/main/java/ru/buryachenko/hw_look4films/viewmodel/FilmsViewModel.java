package ru.buryachenko.hw_look4films.viewmodel;

import android.app.Application;
import android.os.Build;
import android.util.Log;

import com.bumptech.glide.BuildConfig;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import ru.buryachenko.hw_look4films.App;
import ru.buryachenko.hw_look4films.db.FilmInDb;
import ru.buryachenko.hw_look4films.models.FilmInApp;
import ru.buryachenko.hw_look4films.utils.SharedPreferencesOperation;

import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;
import static ru.buryachenko.hw_look4films.utils.Constants.PREFERENCES_FAVORITES_LIST;
import static ru.buryachenko.hw_look4films.utils.Constants.PREFERENCES_SELECTED_FILM;

public class FilmsViewModel extends AndroidViewModel {
    private MutableLiveData<FilmInApp> changedFilm = new MutableLiveData<>();
    private Map<Long, FilmInApp> films = new HashMap<>();
    private Set<Long> favorites = new HashSet<>();

    public FilmsViewModel(@NonNull Application application) {
        super(application);
        loadFilms();
    }


    private void loadFilms() {
        Executors.newCachedThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                films.clear();
                for (FilmInDb filmDb : App.getInstance().filmsDb.daoFilm().getAll()) {
                    films.put(filmDb.getId().longValue(), new FilmInApp(filmDb));
                }
                FilmInApp saved = loadSavedSelected();
                if (saved != null) {
                    films.put(saved.getFilmId(), saved);
                }
                if (BuildConfig.DEBUG) {
                    Log.d(LOGTAG, "We have " + films.size() + " records in DB");
                }
            }
        });
    }

    public List<FilmInApp> getList() {
        ArrayList<FilmInApp> res = new ArrayList<>(films.values());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            res.sort((f1, f2) -> f1.getName().compareTo(f2.getName()));
        }
        return res;
    }

    public void addInFavorites(FilmInApp film) {
        if (!isFavorite(film)) {
            favorites.add(film.getFilmId());
            putFilm(film);
        }
    }

    public void removeFromFavorites(FilmInApp film) {
        if (isFavorite(film)) {
            favorites.remove(film.getFilmId());
            putFilm(film);
        }
    }

    public List<FilmInApp> getFavorites() {
        ArrayList<FilmInApp> res = new ArrayList<>();
        for (FilmInApp film : films.values()) {
            if (isFavorite(film))
                res.add(film);
        }
        return res;
    }

    public List<FilmInApp> getNonFavorites() {
        ArrayList<FilmInApp> res = new ArrayList<>();
        for (FilmInApp film : films.values()) {
            if (!isFavorite(film))
                res.add(film);
        }
        return res;
    }

    public boolean isFavorite(FilmInApp film) {
        return favorites.contains(film.getFilmId());
    }

    public void saveFavorites() {
        StringBuffer listStr = new StringBuffer("");
        for (Long id : favorites) {
            listStr = listStr.append(id).append(FilmInApp.separator);
        }
        SharedPreferencesOperation.save(PREFERENCES_FAVORITES_LIST, listStr.toString());
    }

    public void loadFavorites() {
        String savedList = SharedPreferencesOperation.load(PREFERENCES_FAVORITES_LIST, "");
        favorites.clear();
        for (String tmp : savedList.split(FilmInApp.separator)) {
            if (!tmp.isEmpty())
                favorites.add(Long.parseLong(tmp));
        }
    }


    public FilmInApp loadSavedSelected() {
        if ((films == null) || (films.isEmpty()))
            return null;
        String savedData = SharedPreferencesOperation.load(PREFERENCES_SELECTED_FILM, "");
        if ((savedData == null) || (savedData.isEmpty()))
            return null;
        Long filmId = FilmInApp.filmIdFromWidgetString(savedData);
        boolean liked = FilmInApp.likedFromWidgetString(savedData);
        if (films.containsKey(filmId)) {
            FilmInApp res = new FilmInApp(films.get(filmId));
            res.setSelected();
            res.setLiked(liked);
            return res;
        }
        return null;
    }

    public void putFilm(FilmInApp film) {
        if (film.getFilmId() < 0) {
            long newId = Collections.max(films.keySet()) + 1;
            film.setFilmId(newId);
        }
        films.put(film.getFilmId(), film);
        changedFilm.setValue(film);
    }

    public LiveData<FilmInApp> getChangedFilm() {
        return changedFilm;
    }

    public FilmInApp getSelected() {
        if ((films == null) || (films.isEmpty()))
            return null;
        Long selectedId = FilmInApp.getSelected();
        if (selectedId == null)
            return null;
        return films.get(selectedId);
    }

    public int whoWasChanged(FilmInApp film) {
        return getList().indexOf(film);
    }

}
