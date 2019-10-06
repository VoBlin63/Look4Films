package ru.buryachenko.hw_look4films.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Arrays;

import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.utils.RandomPicture;

import static ru.buryachenko.hw_look4films.utils.Constants.LOGTAG;
import static ru.buryachenko.hw_look4films.utils.Constants.PREFERENCES_SELECTED_FILM;

public class FilmInApp extends Film implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String separator = " ";
    private Boolean liked;
    private String details;
    private String comment;
    private Integer pictureResource;
    private Boolean selected;
    private int filmId;
    private boolean disclosed;

    public FilmInApp(String name, Integer pictureResource, String details, int filmId) {
        super(name);
        this.details = details;
        this.liked = false;
        this.comment = "";
        this.pictureResource = pictureResource;
        this.selected = false;
        this.filmId = filmId;
        this.disclosed = false;
    }

    public FilmInApp(FilmInApp previous) {
        super(previous.getName());
        liked = previous.liked;
        details = previous.details;
        comment = previous.comment;
        pictureResource = previous.pictureResource;
        selected = previous.selected;
        filmId = previous.filmId;
        disclosed = previous.disclosed;
    }
    public static FilmInApp create(String name, String details) {
        name = name.trim();
        details = details.trim();
        if (name.isEmpty()) {
            return null;
        }
        if (details.isEmpty()) {
            return null;
        }
        return new FilmInApp(name, null, details, -1);
    }

    public Boolean getLiked() {
        return liked;
    }

    public String getDetails() {
        return details;
    }

    public String getComment() {
        return comment;
    }

    public Drawable getPicture(Context context) {
        if (pictureResource == null) {
            return RandomPicture.make(context.getResources().getDimensionPixelSize(R.dimen.recyclerImageWidth), context.getResources().getDimensionPixelSize(R.dimen.recyclerImageHeight));
        } else {
            return context.getResources().getDrawable(pictureResource);
        }
    }

    public Boolean isSelected() {
        return selected;
    }

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public int getFilmId() {
        return filmId;
    }

    public String getLinkToShare() {
        //потом здесь будет link
        return getName() + " " + (getLiked() ? "***" : "*");
    }

    public boolean isDisclosed() {
        return disclosed;
    }

    public void setDisclosed(boolean disclosed) {
        this.disclosed = disclosed;
    }

    public void setPictureResource(Integer pictureResource) {
        this.pictureResource = pictureResource;
    }

    public void setFilmId(int filmId) {
        this.filmId = filmId;
    }

    public String toWidgetString() {
        return filmId + separator + (pictureResource == null? 0 : pictureResource) + separator + liked;
    }

    public static int filmIdFromWidgetString(String str) {
        if (str.isEmpty())
            return 0;
        return Integer.parseInt(str.split(separator)[0]);
    }

    public static int pictureResourceFromWidgetString(String str) {
        if (str.isEmpty())
            return 0;
        return Integer.parseInt(str.split(separator)[1]);
    }

    public static boolean likedFromWidgetString(String str) {
        if (str.isEmpty())
            return false;
        return Boolean.parseBoolean(str.split(separator)[2]);
    }

    public void saveSelectedFilm(Context context) {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = settings.edit();
        this.setSelected(true);
        editor.putString(PREFERENCES_SELECTED_FILM, this.toWidgetString());
        editor.apply();
    }

    public String toString() {
        return " id" +filmId + " liked=" + liked + " selected=" + selected + " name=" + getName();
    }
}
