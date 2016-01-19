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
import retrofit.mime.TypedString;

/**
 * Created by Sunny on 11/3/15 for Fleet Owner for Fleet Owner for Fleet Owner.
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
     * @param origin The Source points
     * @param destination The Destination points
     * @param sensor The sensor
     * @param units The Units
     * @param mode The  type of mode by car, foot and Bus etc
     */
    @GET("/maps/api/directions/xml")
    void getDriections(@Query("origin") String origin, @Query("destination") String destination, @Query("sensor") String sensor, @Query("units") String units, @Query("mode") String mode, Callback<String> callback);

    /**
     * Login Api
     *
     * @param email The user email
     * @param password The password
     * @param deviceType Type of the device
     * @param deviceName The device name
     * @param deviceToken The device token for the notification
     */
    @FormUrlEncoded
    @POST("/api/v1/fleetOwner/login")
    void login(@Field("email") String email, @Field("password") String password, @SuppressWarnings("SameParameterValue") @Field("deviceType") String deviceType, @Field("deviceName") String deviceName, @Field("deviceToken") String deviceToken, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     * @param limit The limit of the data
     * @param skip The limit to skip the old data
     * @param sort The type of sorting
     */
    @GET("/api/v1/fleetOwner/getUpComing")
    void getOnGoing(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip,@SuppressWarnings("SameParameterValue") @Query(SORT) String sort, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     * @param search The data to be searched
     * @param limit The limit of the data
     * @param skip The limit to skip the old data
     * @param sort The type of sorting
     * @param driverStatus The status fo the driver
     */
    @GET("/api/v1/fleetOwner/allTrucker")
    void getallTrucker(@Header(AUTHORIZATION) String authorization, @Query("search") String search, @Query(LIMIT) String limit, @Query(SKIP) String skip, @SuppressWarnings("SameParameterValue") @Query(SORT) String sort, @Query("driverStatus") String driverStatus, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     * @param limit The limit of the data
     * @param skip The limit to skip the old data
     * @param sort The type of sorting
     */
    @GET("/api/v1/fleetOwner/getPast")
    void getPast(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @SuppressWarnings("SameParameterValue") @Query(SORT) String sort, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     */
    @PUT(LOGOUT_URL)
    void logout(@Header(AUTHORIZATION) String authorization, Callback<String> callback);


    @FormUrlEncoded
    @PUT(FORGETPASSWORD_URL)
    void forgotPassword(@Field("email") String body, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     * @param limit The limit of the data
     * @param skip The limit to skip the old data
     * @param sort The type of sorting
     */
    @GET("/api/v1/fleetOwner/pendingRequest")
    void getPendingRequest(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @SuppressWarnings("SameParameterValue") @Query(SORT) String sort, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     * @param limit The limit of the data
     * @param skip The limit to skip the old data
     * @param sort The type of sorting
     */
    @GET("/api/v1/fleetOwner/pendingQuotes")
    void getPendingQuotes(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @SuppressWarnings("SameParameterValue") @Query(SORT) String sort, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     * @param limit The limit of the data
     * @param skip The limit to skip the old data
     * @param sort The type of sorting
     */
    @GET("/api/v1/fleetOwner/pendingAssignment")
    void getPendingAssignment(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @SuppressWarnings("SameParameterValue") @Query(SORT) String sort, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     * @param bidId The bid id
     * @param tracking The tracking id
     * @param offerCost The cost of the offer
     * @param note The note
     */
    @FormUrlEncoded
    @POST("/api/v1/fleetOwner/quote")
    void addQuotes(@Header(AUTHORIZATION) String authorization, @Field(BIDID) String bidId, @Field(TRACKING) String tracking, @Field(OFFERCOST) String offerCost, @Field(NOTE) String note, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     * @param bidId The bid id
     * @param tracking The tracking id
     * @param offerCost The cost of the offer
     * @param note The note
     */
    @FormUrlEncoded
    @PUT("/api/v1/fleetOwner/quote/{quoteId}")
    void modifyQuotes(@Header(AUTHORIZATION) String authorization, @Field(BIDID) String bidId, @Field(TRACKING) String tracking, @Field(OFFERCOST) String offerCost, @Field(NOTE) String note, @Path("quoteId") String object, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     */
    @PUT("/api/v1/fleetOwner/ignoreRequest/{bidId}")
    void ignoreBid(@Header(AUTHORIZATION) String authorization, @Path(BIDID) String object, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     */
    @FormUrlEncoded
    @PUT("/api/v1/fleetOwner/assignTrucker")
    void assignTrucker(@Header(AUTHORIZATION) String authorization, @Field("driverId") String driverId, @Field("bookingId") String bookingId, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     */
    @Multipart
    @PUT("/api/v1/fleetOwner/uploadProfilePic")
    void uploadProfilePic(@Header(AUTHORIZATION) String authorization, @Part("image") TypedFile body, Callback<String> callback);

    /**
     * @param authorization The access token for authorization
     * @param limit The limit of the data
     * @param skip The limit to skip the old data
     * @param truckStatus The status of the truck
     */
    @GET("/api/v1/fleetOwner/allTruck")
    void getallTruck(@Header(AUTHORIZATION) String authorization, @SuppressWarnings("SameParameterValue") @Query(LIMIT) String limit, @SuppressWarnings("SameParameterValue") @Query(SKIP) String skip, @SuppressWarnings("SameParameterValue") @Query(SORT) String sort, @SuppressWarnings("SameParameterValue") @Query("truckStatus") String truckStatus, Callback<String> callback);


    @GET("/api/v1/appVersion")
    void getAppVersion(@Query("appType") String appType, Callback<String> callback);

    @GET("/api/v1/typeCargo")
    void getTypeCargo(Callback<String> callback);

    @Multipart
    @PUT("/api/v1/fleetOwner")
    void register(@Part("userType") TypedString userType,@Part("email") TypedString email,@Part("fullName") TypedString fullName,@Part("password") TypedString password,@Part("phoneNumber") TypedString phoneNumber,@Part("companyName") TypedString companyName,@Part("areaOfOperation") TypedString areaOfOperation,@Part("numberOfTrucks") TypedString numberOfTrucks,@Part("address") TypedString address,@Part("city") TypedString city,@Part("state") TypedString state,@Part("pinCode") TypedString pinCode,@Part("country") TypedString country,@Part("typeOfCargo") TypedString typeOfCargo,@Part("deviceType") TypedString deviceType,@Part("deviceName") TypedString deviceName,@Part("deviceToken") TypedString deviceToken, Callback<String> callback);

}
