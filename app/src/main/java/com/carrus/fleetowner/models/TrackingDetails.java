package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/17/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class TrackingDetails implements Serializable{

    @SerializedName("_id")
    public String _id;

    @SerializedName("bookingId")
    public String bookingId;


    @SerializedName("truckerId")
    public String truckerId;


    @SerializedName("createdAt")
    public String createdAt;


    @SerializedName("lat")
    public Double lat;

    @SerializedName("long")
    public Double longg;

}
