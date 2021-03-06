package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 1/15/16 for Fleet Owner.
 */
public class StateCityInfo implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("name")
    public String name;

    @SerializedName("state")
    public String state;

}
