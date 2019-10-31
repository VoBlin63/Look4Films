package ru.buryachenko.hw_look4films.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.buryachenko.hw_look4films.App;
import ru.buryachenko.hw_look4films.api.responce.FilmJson;
import ru.buryachenko.hw_look4films.api.responce.WholeResponse;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;
import static ru.buryachenko.hw_look4films.utils.Constants.PREFERENCES_SELECTED_FILM;

public class FilmsViewModel extends AndroidViewModel {
    private MutableLiveData<FilmInApp> changedFilm = new MutableLiveData<>();
    private MutableLiveData<Boolean> isBusy = new MutableLiveData<>();
    private Map<Long, FilmInApp> films = new HashMap<>();
    private Set<Long> favorites = new HashSet<>();

    private static final String apiKey = "c3e17ff26735628669886b00d573ab4d";
    private static final String language = "ru-RU";
    private static final String region = "RU";
    private static int page = 1;

    public FilmsViewModel(@NonNull Application application) {
        super(application);
        isBusy.setValue(false);
        loadNext();
    }

    public void loadNext() {
        if (isBusy.getValue() == null || isBusy.getValue()) {
            return;
        }
        isBusy.setValue(true);
        page = page + 1;
        App.getInstance().service.getFilms(apiKey, page, language, region).enqueue(new Callback<WholeResponse>() {
            @Override
            public void onResponse(Call<WholeResponse> call, Response<WholeResponse> response) {
                if (response.isSuccessful()) {
                    for (FilmJson filmJson : response.body().getResults()) {
                        putFilm(new FilmInApp(filmJson));
                    }
                    FilmInApp saved = loadSavedSelected();
                    if (saved != null) {
                        films.put(saved.getFilmId(), saved);
                    }
                } else {
                    Log.d(LOGTAG, "Что-то пошло не так : response.message() = " + response.message());
                }
                isBusy.setValue(false);
            }

            @Override
            public void onFailure(Call<WholeResponse> call, Throwable throwable) {
                Log.d(LOGTAG, "Что-то пошло не так : onFailure throwable = " + throwable.getMessage());
                isBusy.setValue(false);
            }
        });
    }

    public List<FilmInApp> getList() {
        return new ArrayList<>(films.values());
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

    public FilmInApp loadSavedSelected() {
        if ((films == null) || (films.isEmpty()))
            return null;
        Context context = getApplication();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        if (settings.contains(PREFERENCES_SELECTED_FILM)) {
            String savedData = settings.getString(PREFERENCES_SELECTED_FILM, "");
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

    public MutableLiveData<Boolean> getIsBusy() {
        return isBusy;
    }
}
