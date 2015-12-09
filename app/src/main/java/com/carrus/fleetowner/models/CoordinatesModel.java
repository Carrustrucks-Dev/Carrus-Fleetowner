package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/7/15.
 */
public class CoordinatesModel implements Serializable{

    @SerializedName("dropOffLat")
    public Double dropOffLat;

    @SerializedName("dropOffLong")
    public Double dropOffLong;


}