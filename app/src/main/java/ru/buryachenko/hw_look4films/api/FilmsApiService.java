package ru.buryachenko.hw_look4films.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.buryachenko.hw_look4films.api.responce.WholeResponse;

public interface FilmsApiService {
    @GET("3/discover/movie")
    Call<WholeResponse> getFilms(@Query("api_key") String apiKey,
                                 @Query("page") int page,
                                 @Query("language") String language,
                                 @Query("region") String region);
}



