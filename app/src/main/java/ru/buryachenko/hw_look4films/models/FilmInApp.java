package ru.buryachenko.hw_look4films.models;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;
import ru.buryachenko.hw_look4films.api.responce.FilmJson;

public class FilmInApp extends Film implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String separator = "&";
    private static Long selectedFilmId = null;

    private static final AtomicLong NEXT_ID = new AtomicLong(1);

    private Boolean liked;
    private String details;
    private String comment;
    private String imageUrl;
    private long filmId;
    private boolean disclosed;

    public FilmInApp(String name, String imageUrl, String details, int filmId) {
        super(name);
        this.details = details;
        this.liked = false;
        this.comment = "";
        this.imageUrl = imageUrl;
        this.filmId = NEXT_ID.getAndIncrement();
        this.disclosed = false;
    }

    public FilmInApp(FilmJson filmJson) {
        super(filmJson.getTitle());
        liked = false;
        details = filmJson.getOverview();
        comment = "";
        this.filmId = NEXT_ID.getAndIncrement();
        imageUrl = "https://image.tmdb.org/t/p/w500/" + filmJson.getPosterPath();
        disclosed = false;
    }

    public FilmInApp(FilmInApp previous) {
        super(previous.getName());
        liked = previous.liked;
        details = previous.details;
        comment = previous.comment;
        imageUrl = previous.imageUrl;
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

    public static Long getSelected() {
        return selectedFilmId;
    }

    public String getDetails() {
        return details;
    }

    public String getComment() {
        return comment;
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

    public long getFilmId() {
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

    public void setFilmId(long filmId) {
        this.filmId = filmId;
    }

    public String toWidgetString() {
        return filmId + separator + (imageUrl == null ? 0 : imageUrl) + separator + liked;
    }

    public static Long filmIdFromWidgetString(String str) {
        if (str.isEmpty())
            return 0L;
        return Long.parseLong(str.split(separator)[0]);
    }

    public static String imageUrlFromWidgetString(String str) {
        if (str.isEmpty())
            return "";
        return str.split(separator)[1];
    }

    public static boolean likedFromWidgetString(String str) {
        if (str.isEmpty())
            return false;
        return Boolean.parseBoolean(str.split(separator)[2]);
    }

    public String toString() {
        return getName();
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
