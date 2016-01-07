package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 11/7/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class MyBookingModel   implements Serializable {

    @SerializedName("statusCode")
    public String statusCode;


    @SerializedName("message")
    public String message;


    @SerializedName("data")
    public final List<MyBookingDataModel> mData = new ArrayList<>();
}
