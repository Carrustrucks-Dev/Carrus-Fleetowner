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
 * Created by Sunny on 11/3/15.
 */
public interface ApiService {
    String LOGOUT_URL = "/api/v1/fleetOwner/logout";
    String FORGETPASSWORD_URL = "/api/v1/fleetOwner/forgotPassword";
    String AUTHORIZATION = "authorization";
    String LIMIT = "limit";
    String SKIP = "skip";
    String SORT = "sort";
    String BIDID = "bidId";

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
     void login(@Field("email") String email, @Field("password") String password, @Field("deviceType") String deviceType, @Field("deviceName") String deviceName, @Field("deviceToken") String deviceToken, Callback<String> callback);

    @GET("/api/v1/fleetOwner/getUpComing")
     void getOnGoing(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @Query(SORT) String sort, Callback<String> callback);

    @GET("/api/v1/fleetOwner/allTrucker")
     void getallTrucker(@Header(AUTHORIZATION) String authorization, @Query("search") String search, @Query(LIMIT) String limit, @Query(SKIP) String skip, @Query(SORT) String sort, @Query("driverStatus") String driverStatus, Callback<String> callback);

    @GET("/api/v1/fleetOwner/getPast")
     void getPast(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @Query(SORT) String sort, Callback<String> callback);

    @PUT(LOGOUT_URL)
     void logout(@Header(AUTHORIZATION) String authorization, Callback<String> callback);

    @FormUrlEncoded
    @PUT(FORGETPASSWORD_URL)
     void forgotPassword(@Field("email") String body, Callback<String> callback);

    @GET("/api/v1/fleetOwner/pendingRequest")
     void getPendingRequest(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @Query(SORT) String sort, Callback<String> callback);

    @GET("/api/v1/fleetOwner/pendingQuotes")
     void getPendingQuotes(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @Query(SORT) String sort, Callback<String> callback);

    @GET("/api/v1/fleetOwner/pendingAssignment")
     void getPendingAssignment(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @Query(SORT) String sort, Callback<String> callback);

    @FormUrlEncoded
    @POST("/api/v1/fleetOwner/quote")
     void addQuotes(@Header(AUTHORIZATION) String authorization, @Field(BIDID) String bidId, @Field("tracking") String tracking, @Field("offerCost") String offerCost, @Field("note") String note, Callback<String> callback);

    @FormUrlEncoded
    @PUT("/api/v1/fleetOwner/quote/{quoteId}")
     void modifyQuotes(@Header(AUTHORIZATION) String authorization, @Field(BIDID) String bidId, @Field("tracking") String tracking, @Field("offerCost") String offerCost, @Field("note") String note,@Path("quoteId") String object, Callback<String> callback);

    @PUT("/api/v1/fleetOwner/ignoreRequest/{bidId}")
     void ignoreBid(@Header(AUTHORIZATION) String authorization, @Path(BIDID) String object, Callback<String> callback);

    @FormUrlEncoded
    @PUT("/api/v1/fleetOwner/assignTrucker")
     void assignTrucker(@Header(AUTHORIZATION) String authorization, @Field("driverId") String driverId, @Field("bookingId") String bookingId, Callback<String> callback);

    @Multipart
    @PUT("/api/v1/fleetOwner/uploadProfilePic")
     void uploadProfilePic(@Header(AUTHORIZATION) String authorization,@Part("image") TypedFile body, Callback<String> callback);

    @GET("/api/v1/fleetOwner/allTruck")
     void getallTruck(@Header(AUTHORIZATION) String authorization, @Query(LIMIT) String limit, @Query(SKIP) String skip, @Query(SORT) String sort, @Query("truckStatus") String truckStatus, Callback<String> callback);

}
