package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Booking implements Serializable {

    @SerializedName("_id")
    @Expose
    private String Id;
    @SerializedName("dropOff")
    @Expose
    private DropOff dropOff;
    @SerializedName("pickUp")
    @Expose
    private PickUp pickUp;
    @SerializedName("crn")
    @Expose
    private String crn;

    /**
     * @return The Id
     */
    public String getId() {
        return Id;
    }

    /**
     * @param Id The _id
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     * @return The dropOff
     */
    public DropOff getDropOff() {
        return dropOff;
    }

    /**
     * @param dropOff The dropOff
     */
    public void setDropOff(DropOff dropOff) {
        this.dropOff = dropOff;
    }

    /**
     * @return The pickUp
     */
    public PickUp getPickUp() {
        return pickUp;
    }

    /**
     * @param pickUp The pickUp
     */
    public void setPickUp(PickUp pickUp) {
        this.pickUp = pickUp;
    }

    /**
     * @return The crn
     */
    public String getCrn() {
        return crn;
    }

    /**
     * @param crn The crn
     */
    public void setCrn(String crn) {
        this.crn = crn;
    }

}