package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/17/15 for Fleet Owner for Fleet Owner.
 */
public class Cargo implements Serializable {

    @SerializedName("weight")
    public Long weight;

    public CargoDetails cargoType;

}
