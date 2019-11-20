package ru.buryachenko.hw_look4films.db;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import ru.buryachenko.hw_look4films.api.responce.FilmJson;

@Entity
public class FilmInDb {

    @PrimaryKey
    private Integer id;
    private Double popularity;
    private Integer voteCount;
    private String posterPath;
    private Boolean adult;
    private String originalLanguage;
    private String originalTitle;
    private String title;
    private Float voteAverage;
    private String overview;
    private String releaseDate;

    public FilmInDb(FilmJson filmJson) {
        title = filmJson.getTitle();
        overview = filmJson.getOverview();
        id = filmJson.getId();
        posterPath = "https://image.tmdb.org/t/p/w500/" + filmJson.getPosterPath();
        popularity = filmJson.getPopularity();
        voteCount = filmJson.getVoteCount();
        adult = filmJson.getAdult();
        originalLanguage = filmJson.getOriginalLanguage();
        originalTitle = filmJson.getOriginalTitle();
        voteAverage = filmJson.getVoteAverage();
        releaseDate =  filmJson.getReleaseDate();
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            releaseDate = formatter.parse(filmJson.getReleaseDate());
//        } catch (ParseException ignored) {}
    }

    public FilmInDb() {
    }

    public Integer getId() {
        return id;
    }

    public Double getPopularity() {
        return popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Boolean getAdult() {
        return adult;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public Float getVoteAverage() {
        return voteAverage;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVoteAverage(Float voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}