package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/17/15 for Fleet Owner.
 */
public class CargoDetails implements Serializable {

    @SerializedName("_id")
    public String id;

    @SerializedName("typeCargoName")
    public String typeCargoName;
}
