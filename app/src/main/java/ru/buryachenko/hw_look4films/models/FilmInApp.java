package ru.buryachenko.hw_look4films.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.utils.RandomPicture;

public class FilmInApp extends Film implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean liked;
    private String details;
    private String comment;
    private Integer pictureResource;
    private int filmId;
    private boolean disclosed;
    private static Integer selectedFilmId = null;

    public FilmInApp(String name, Integer pictureResource, String details, int filmId) {
        super(name);
        this.details = details;
        this.liked = false;
        this.comment = "";
        this.pictureResource = pictureResource;
        this.filmId = filmId;
        this.disclosed = false;
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

    public Boolean isSelected() {
        return selectedFilmId != null && filmId == selectedFilmId;
    }

    public void setSelected() {
        selectedFilmId = filmId;
    }

    public static void clearSelected() {
        selectedFilmId = null;
    }

    public static Integer getSelected() {
        return selectedFilmId;
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

    public void setLiked(Boolean liked) {
        this.liked = liked;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
