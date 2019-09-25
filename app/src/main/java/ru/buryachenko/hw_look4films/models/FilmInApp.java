package ru.buryachenko.hw_look4films.models;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
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
            return RandomPicture.make(context.getResources().getDimensionPixelSize(R.dimen.recyclerImageWidth),context.getResources().getDimensionPixelSize(R.dimen.recyclerImageHeight));
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
}
