package com.hafidikhsana.formulaonedrivers;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIClient {
    private static final String BASE_URL = "https://api.openf1.org/";
    private static APIClient instance;
    private Retrofit retrofit;

    private APIClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public static APIClient getInstance() {
        if (instance == null) {
            instance = new APIClient();
        }
        return instance;
    }

    public OpenF1API getOpenF1Api() {
        return retrofit.create(OpenF1API.class);
    }
}
