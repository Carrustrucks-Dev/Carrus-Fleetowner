package com.carrus.fleetowner.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Sunny on 11/23/15 for Fleet Owner.
 */
public class DocModel implements Serializable{

    @SerializedName("consigmentNote")
    public String consigmentNote;

    @SerializedName("invoice")
    public String invoice;

    @SerializedName("pod")
    public String pod;
}
