package com.carrus.fleetowner.retrofit;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Query;

/**
 * Created by Sunny on 11/3/15.
 */
public interface ApiService {
    String LOGOUT_URL = "/api/v1/fleetOwner/logout";
    String FORGETPASSWORD_URL = "/api/v1/fleetOwner/forgotPassword";

    String AUTHORIZATION = "authorization";

    @GET("/maps/api/directions/xml")
    public void getDriections(@Query("origin") String origin, @Query("destination") String destination, @Query("sensor") String sensor, @Query("units") String units, @Query("mode") String mode, Callback<String> callback);

    /**
     * @param email
     * @param password
     * @param deviceType
     * @param deviceName
     * @param deviceToken
     */
    @FormUrlEncoded
    @POST("/api/v1/fleetOwner/login")
    public void login(@Field("email") String email, @Field("password") String password, @Field("deviceType") String deviceType, @Field("deviceName") String deviceName, @Field("deviceToken") String deviceToken, Callback<String> callback);

    @GET("/api/v1/fleetOwner/getUpComing")
    public void getOnGoing(@Header(AUTHORIZATION) String authorization, @Query("limit") String limit, @Query("skip") String skip, @Query("sort") String sort, Callback<String> callback);

    @GET("/api/v1/fleetOwner/allTrucker")
    public void getallTrucker(@Header(AUTHORIZATION) String authorization, @Query("limit") String limit, @Query("skip") String skip, @Query("sort") String sort,@Query("driverStatus") String driverStatus, Callback<String> callback);

    @GET("/api/v1/fleetOwner/getPast")
    public void getPast(@Header(AUTHORIZATION) String authorization, @Query("limit") String limit, @Query("skip") String skip, @Query("sort") String sort, Callback<String> callback);

    @PUT(LOGOUT_URL)
    public void logout(@Header(AUTHORIZATION) String authorization, Callback<String> callback);

    @FormUrlEncoded
    @PUT(FORGETPASSWORD_URL)
    public void forgotPassword(@Field("email") String body, Callback<String> callback);

    @GET("/api/v1/fleetOwner/pendingRequest")
    public void getPendingRequest(@Header(AUTHORIZATION) String authorization, @Query("limit") String limit, @Query("skip") String skip, @Query("sort") String sort, Callback<String> callback);

    @GET("/api/v1/fleetOwner/pendingQuotes")
    public void getPendingQuotes(@Header(AUTHORIZATION) String authorization, @Query("limit") String limit, @Query("skip") String skip, @Query("sort") String sort, Callback<String> callback);

    @GET("/api/v1/fleetOwner/pendingAssignment")
    public void getPendingAssignment(@Header(AUTHORIZATION) String authorization, @Query("limit") String limit, @Query("skip") String skip, @Query("sort") String sort, Callback<String> callback);

}
