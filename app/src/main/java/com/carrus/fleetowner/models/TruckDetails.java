package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/7/15 for Fleet Owner.
 */
public class TruckDetails  implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("typeTruckName")
    public String typeTruckName;

    @SerializedName("truckNumber")
    private String truckNumber;

    @Override
    public String toString() {
        return truckNumber;
    }
}
