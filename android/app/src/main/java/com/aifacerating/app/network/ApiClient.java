package com.aifacerating.app.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Live Production Server Base URL hosted on Eskiz VPS (185.217.131.246:8000)
    public static String BASE_URL = "http://185.217.131.246:8000/"; 
    private static Retrofit retrofit = null;

    public static void setCustomBaseUrl(String newUrl) {
        if (newUrl != null && !newUrl.isEmpty()) {
            BASE_URL = newUrl.endsWith("/") ? newUrl : newUrl + "/";
            retrofit = null; // Rebuild client
        }
    }

    public static ApiService getService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
