package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/7/15 for Fleet Owner for Fleet Owner.
 */
public class CoordinatesModel implements Serializable{

    @SerializedName("dropOffLat")
    public Double dropOffLat;

    @SerializedName("dropOffLong")
    public Double dropOffLong;


}
