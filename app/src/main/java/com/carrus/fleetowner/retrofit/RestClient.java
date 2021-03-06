package com.carrus.fleetowner.retrofit;

import com.carrus.fleetowner.utils.Constants;

import retrofit.RestAdapter;


/**
 * Rest client
 */
public class RestClient {
    private static ApiService apiService = null;
    private static ApiService googleApiService = null;


    public static ApiService getGoogleApiService() {
        if (googleApiService == null) {
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.GOOGLE_URL)
                    .setConverter(new StringConverter())    //converter for response type
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            googleApiService = restAdapter.create(ApiService.class);
        }
        return googleApiService;

    }


    public static ApiService getApiService() {
        if (apiService == null) {
//            OkHttpClient okHttpClient = new OkHttpClient();
            RestAdapter restAdapter = new RestAdapter.Builder()
                    .setEndpoint(Constants.ROOT)
//                    .setClient(new OkClient(okHttpClient))
                    .setConverter(new StringConverter())    //converter for response type
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .build();
            apiService = restAdapter.create(ApiService.class);
        }
        return apiService;
    }


}
