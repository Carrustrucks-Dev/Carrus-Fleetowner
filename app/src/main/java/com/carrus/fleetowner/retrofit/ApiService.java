package com.carrus.fleetowner.retrofit;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by Sunny on 11/3/15 for Fleet Owner.
 */
public interface ApiService {
    String LOGOUT_URL = "/api/v1/fleetOwner/logout";
    String FORGETPASSWORD_URL = "/api/v1/fleetOwner/forgotPassword";
    String AUTHORIZATION = "authorization";
    String LIMIT = "limit";
    String SKIP = "skip";
    String SORT = "sort";
    String BIDID = "bidId";
    String TRACKING = "tracking";
    String OFFERCOST = "offerCost";
    String NOTE = "note";

    /**
     * @param origin
     * @param destination
     * @param sensor
     * @param units
     * @param mode
     */
    @GET("/maps/api/directions/xml")
    void getDriections(@Query("origin") String origin, @Query("destination") String destination, @Query("sensor") String sensor, @Query("units") String units, @Query("mode") String mode, Callback<String> callback);

    /**
     * @param email
     * @param password
     * @param deviceType
     * @param deviceName
     * @param deviceToken
     */
    @FormUrlEncoded
    @POST("/api/v1/fleetOwner/login")
    void login(@Field("email") String email, @Field("password") String password, @SuppressWarnings("SameParameterValue") @Field("deviceType") String deviceType, @Field("deviceName") String deviceName, @Field("deviceToken") String deviceToken, Callback<String> callback);

    /**
     * @param authorization
     * @param limit
     * @param skip
     * @param sort
     */
    @GET("/api/v1/fleetOwner/getUpComing")
    void getOnGoing(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @Query(SORT) String sort, Callback<String> callback);

    /**
     * @param authorization
     * @param search
     * @param limit
     * @param skip
     * @param sort
     * @param driverStatus
     */
    @GET("/api/v1/fleetOwner/allTrucker")
    void getallTrucker(@Header(AUTHORIZATION) String authorization, @Query("search") String search, @Query(LIMIT) String limit, @Query(SKIP) String skip, @Query(SORT) String sort, @Query("driverStatus") String driverStatus, Callback<String> callback);

    /**
     * @param authorization
     * @param limit
     * @param skip
     * @param sort
     */
    @GET("/api/v1/fleetOwner/getPast")
    void getPast(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @SuppressWarnings("SameParameterValue") @Query(SORT) String sort, Callback<String> callback);

    /**
     * @param authorization
     */
    @PUT(LOGOUT_URL)
    void logout(@Header(AUTHORIZATION) String authorization, Callback<String> callback);


    @FormUrlEncoded
    @PUT(FORGETPASSWORD_URL)
    void forgotPassword(@Field("email") String body, Callback<String> callback);

    /**
     * @param authorization
     * @param limit
     * @param skip
     * @param sort
     */
    @GET("/api/v1/fleetOwner/pendingRequest")
    void getPendingRequest(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @SuppressWarnings("SameParameterValue") @Query(SORT) String sort, Callback<String> callback);

    /**
     * @param authorization
     * @param limit
     * @param skip
     * @param sort
     */
    @GET("/api/v1/fleetOwner/pendingQuotes")
    void getPendingQuotes(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @SuppressWarnings("SameParameterValue") @Query(SORT) String sort, Callback<String> callback);

    /**
     * @param authorization
     * @param limit
     * @param skip
     * @param sort
     */
    @GET("/api/v1/fleetOwner/pendingAssignment")
    void getPendingAssignment(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @SuppressWarnings("SameParameterValue") @Query(SORT) String sort, Callback<String> callback);

    /**
     * @param authorization
     * @param bidId
     * @param tracking
     * @param offerCost
     * @param note
     */
    @FormUrlEncoded
    @POST("/api/v1/fleetOwner/quote")
    void addQuotes(@Header(AUTHORIZATION) String authorization, @Field(BIDID) String bidId, @Field(TRACKING) String tracking, @Field(OFFERCOST) String offerCost, @Field(NOTE) String note, Callback<String> callback);

    /**
     * @param authorization
     * @param bidId
     * @param tracking
     * @param offerCost
     * @param note
     */
    @FormUrlEncoded
    @PUT("/api/v1/fleetOwner/quote/{quoteId}")
    void modifyQuotes(@Header(AUTHORIZATION) String authorization, @Field(BIDID) String bidId, @Field(TRACKING) String tracking, @Field(OFFERCOST) String offerCost, @Field(NOTE) String note, @Path("quoteId") String object, Callback<String> callback);

    /**
     * @param authorization
     */
    @PUT("/api/v1/fleetOwner/ignoreRequest/{bidId}")
    void ignoreBid(@Header(AUTHORIZATION) String authorization, @Path(BIDID) String object, Callback<String> callback);

    /**
     * @param authorization
     */
    @FormUrlEncoded
    @PUT("/api/v1/fleetOwner/assignTrucker")
    void assignTrucker(@Header(AUTHORIZATION) String authorization, @Field("driverId") String driverId, @Field("bookingId") String bookingId, Callback<String> callback);

    /**
     * @param authorization
     */
    @Multipart
    @PUT("/api/v1/fleetOwner/uploadProfilePic")
    void uploadProfilePic(@Header(AUTHORIZATION) String authorization, @Part("image") TypedFile body, Callback<String> callback);

    /**
     * @param authorization
     * @param limit
     * @param skip
     * @param sort
     * @param truckStatus
     */
    @GET("/api/v1/fleetOwner/allTruck")
    void getallTruck(@Header(AUTHORIZATION) String authorization, @SuppressWarnings("SameParameterValue") @Query(LIMIT) String limit, @SuppressWarnings("SameParameterValue") @Query(SKIP) String skip, @Query(SORT) String sort, @SuppressWarnings("SameParameterValue") @Query("truckStatus") String truckStatus, Callback<String> callback);

}
