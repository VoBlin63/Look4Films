package ru.buryachenko.hw_look4films;

import android.app.Application;

import com.google.maps.GeoApiContext;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.buryachenko.hw_look4films.api.FilmsApiService;

public class App extends Application {
    public FilmsApiService service;
    public GeoApiContext geoApiContext;

    private static App instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initRetrofit();
        initGeoApi();
    }

    public static App getInstance() {
        return instance;
    }

    private void initRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(FilmsApiService.class);
    }

    private void initGeoApi() {
        GeoApiContext.Builder builder = new GeoApiContext.Builder();
        builder.apiKey(getString(R.string.googleMapApiKey));
        geoApiContext = builder
                .build();
    }
}
