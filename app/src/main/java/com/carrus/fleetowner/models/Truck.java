package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/7/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class Truck  implements Serializable {

    @SerializedName("truckNumber")
    public Long truckNumber;

    public TruckDetails truckType;

}
