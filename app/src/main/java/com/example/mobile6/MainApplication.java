package com.example.mobile6;

import android.app.Application;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

public class MainApplication extends Application {
    private String baseUrl = "";
    private Retrofit retrofit;
    private OkHttpClient okHttp;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public MainDatabase getDatabase() {
        return MainDatabase.getInstance(getApplicationContext());
    }

    public Preferences getPreferences() {
        return Preferences.getInstance(getApplicationContext());
    }

    public Retrofit getRetrofit() {
        if (retrofit != null) return retrofit;
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(getOkHttp())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public OkHttpClient getOkHttp() {
        if (okHttp != null) return okHttp;
        okHttp = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
        return okHttp;
    }
}
