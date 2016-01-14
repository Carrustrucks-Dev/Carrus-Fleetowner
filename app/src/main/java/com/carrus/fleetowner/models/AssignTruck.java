package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 1/14/16 for Fleet Owner.
 */
public class AssignTruck implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("truckName")
    public String truckName;

    @SerializedName("truckNumber")
    public String truckNumber;
}
