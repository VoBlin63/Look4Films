package ru.buryachenko.hw_look4films.models;

import android.graphics.drawable.BitmapDrawable;

public class FilmInApp extends Film {
    private Boolean liked;
    private String details;
    private String comment;
    private BitmapDrawable picture;
    private Boolean isSelected;
    public FilmInApp(String name, BitmapDrawable picture, String details) {
        super(name);
        this.details = details;
        this.liked = false;
        this.comment = "";
        this.picture = picture;
        this.isSelected = false;
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

    public BitmapDrawable getPicture() {
        return picture;
    }

    public Boolean getSelected() {
        return isSelected;
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
        isSelected = selected;
    }
}
