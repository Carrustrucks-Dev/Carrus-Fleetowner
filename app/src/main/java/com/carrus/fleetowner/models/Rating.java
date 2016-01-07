package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/7/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class Rating  implements Serializable {

    @SerializedName("shipperRate")
    public String shipperRate;

    @SerializedName("truckerRate")
    public String truckerRate;

    @SerializedName("shipperComment")
    public String shipperComment;

    @SerializedName("truckerComment")
    public String truckerComment;

}
