package ru.buryachenko.hw_look4films.models;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import java.io.Serializable;

public class FilmInApp extends Film implements Serializable {
    private static final long serialVersionUID = 1L;
    private Boolean liked;
    private String details;
    private String comment;
    private int pictureResource;
    private Boolean selected;
    private int filmId;
    private boolean disclosed;

    public FilmInApp(String name, int pictureResource, String details, int filmId) {
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

    public BitmapDrawable getPicture(Context context) {
        return (BitmapDrawable) context.getResources().getDrawable(pictureResource);
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
