package ru.buryachenko.hw_look4films.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.buryachenko.hw_look4films.api.responce.FilmJson;
import ru.buryachenko.hw_look4films.api.responce.WholeResponce;

public interface FilmsApiService {
    @GET("3/search/movie")
    Call<WholeResponce> getFilms(@Query("api_key") String apiKey,
                                       @Query("query") String query,
                                       @Query("language") String language,
                                       @Query("region") String region);
}



