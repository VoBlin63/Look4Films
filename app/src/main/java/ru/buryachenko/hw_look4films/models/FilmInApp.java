package ru.buryachenko.hw_look4films.models;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

import ru.buryachenko.hw_look4films.R;
import ru.buryachenko.hw_look4films.utils.RandomPicture;

public class FilmInApp extends Film implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String separator = "&";
    private Boolean liked;
    private String details;
    private String comment;
    private Integer pictureResource;
    private int filmId;
    private boolean disclosed;
    private static Integer selectedFilmId = null;

    public boolean equals(FilmInApp other) {
        return liked == other.liked
                && details.equals(other.details)
                && comment.equals(other.comment);
    }

    public FilmInApp(String name, Integer pictureResource, String details, int filmId) {
        super(name);
        this.details = details;
        this.liked = false;
        this.comment = "";
        this.pictureResource = pictureResource;
        this.filmId = filmId;
        this.disclosed = false;
    }

    public FilmInApp(FilmInApp previous) {
        super(previous.getName());
        liked = previous.liked;
        details = previous.details;
        comment = previous.comment;
        pictureResource = previous.pictureResource;
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

    public String toWidgetString() {
        return filmId + separator + (pictureResource == null ? 0 : pictureResource) + separator + liked;
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

    public String toString() {
        return getName();
    }
}
