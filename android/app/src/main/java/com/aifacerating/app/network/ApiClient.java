package com.aifacerating.app.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Configurable Production Server Base URL with fallback to emulator/local host
    public static String BASE_URL = "http://10.0.2.2:8000/"; 
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
