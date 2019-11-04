package ru.buryachenko.hw_look4films.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

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
import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.models.FilmInApp;

import static ru.buryachenko.hw_look4films.utils.Constants.PREFERENCES_SELECTED_FILM;

public class FilmsViewModel extends AndroidViewModel {
    private MutableLiveData<FilmInApp> changedFilm = new MutableLiveData<>();
    private Map<Integer, FilmInApp> films;
    private Set<Integer> favorites = new HashSet<>();

    public FilmsViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        if (films == null) {
            films = new HashMap<>();
            //здесь фильмы будут откуда-то подгружаться
            FilmInApp[] filmsFromSomewhere = new FilmInApp[]{
                    new FilmInApp("Анна", R.drawable.anna, "История Анны, чья несравненная красота скрывает поразительную мощь и смертоносный талант опаснейшего наемного убийцы в мире.", 1),
                    new FilmInApp("Образование", R.drawable.booksmart, "Старшеклассницы Молли и Эми последние четыре года были поглощены учебой, настойчиво пытаясь заработать высокие отметки, чтобы поступить в престижные колледжи. В итоге Молли удается пробиться в Йель, а Эми получает желанное место в Колумбийском университете, но лучшие подруги не чувствуют себя счастливыми. Они осознают, что постоянное стремление быть прилежными ученицами лишило их типичных для школьников развлечений, поэтому решают как следует повеселиться в последнюю ночь перед выпускным вечером.", 2),
                    new FilmInApp("Дитя робота", R.drawable.iammother, "После глобального катаклизма человечество вымирает. В подземном бункере автоматически активизируется аварийная программа, и робот-гуманоид «Мать» выращивает из эмбриона человеческого ребенка. Девушка, воспитанная под бережным присмотром «Матери», никогда не видела ни поверхности Земли, ни других людей. Но однажды ее мир переворачивается, когда на пороге убежища появляется женщина с просьбой о помощи...", 3),
                    new FilmInApp("Плюс один", R.drawable.plusone, "Лето — пора свадеб. Закоренелый холостяк Бен и его подруга Элис, которая только что рассталась со своим парнем, приглашены сразу на десять свадеб. Чтобы не искать плюс один, ребята решают притвориться влюбленной парой. Удастся ли им устоять перед магией секса вокруг и остаться друзьями?", 4),
                    new FilmInApp("Дылда", R.drawable.tallgirl, "Джоди, самая высокая девушка в школе, всегда чувствовала себя неловко в собственной шкуре. Но после многих лет насмешек и избегания внимания любой ценой, Джоди, наконец, решает обрести уверенность в себе.", 5),
                    new FilmInApp("Алладин", R.drawable.alladin, "Молодой воришка по имени Аладдин хочет стать принцем, чтобы жениться на принцессе Жасмин. Тем временем визирь Аграбы Джафар, намеревается захватить власть над Аграбой, а для этого он стремится заполучить волшебную лампу, хранящуюся в пещере чудес, доступ к которой разрешен лишь тому, кого называют «алмаз неограненный», и этим человеком является никто иной как сам Аладдин.", 6),
                    new FilmInApp("Паразиты", R.drawable.parasite, "Обычное корейское семейство жизнь не балует. Приходится жить в сыром грязном полуподвале, воровать интернет у соседей и перебиваться случайными подработками. Однажды друг сына семейства, уезжая на стажировку за границу, предлагает тому заменить его и поработать репетитором у старшеклассницы в богатой семье Пак. Подделав диплом о высшем образовании, парень отправляется в шикарный дизайнерский особняк и производит на хозяйку дома хорошее впечатление. Тут же ему в голову приходит необычный план по трудоустройству сестры.", 7),
                    new FilmInApp("Приключения Реми", R.drawable.remi, "Экранизация романа 1878 года французского писателя Гектора Мало. Удивительное путешествие по Франции маленького Реми в компании уличного музыканта, обезьянки и цирковой собаки. Вместе им предстоит пережить неожиданные встречи, приключения и испытания, раскрыть тайну происхождения мальчика.", 8)
            };
            for (FilmInApp filmInApp : filmsFromSomewhere) {
                films.put(filmInApp.getFilmId(), filmInApp);
            }
            FilmInApp saved = loadSavedSelected();
            if (saved != null) {
                films.put(saved.getFilmId(), saved);
            }
        }
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
            int filmId = FilmInApp.filmIdFromWidgetString(savedData);
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
            int newId = Collections.max(films.keySet()) + 1;
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
        Integer selectedId = FilmInApp.getSelected();
        if (selectedId == null)
            return null;
        return films.get(selectedId);
    }

    public int whoWasChanged(FilmInApp film) {
        return getList().indexOf(film);
    }

}
