package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 12/21/15.
 */
public class TrucksType implements Serializable {

    @SerializedName("_id")
    @Expose
    private String Id;

    @SerializedName("currentCoordinates")
    @Expose
    private CurrentCoordinates currentCoordinates;

    @SerializedName("truckerColor")
    @Expose
    private String truckerColor;

    @SerializedName("status")
    @Expose
    private String status;

    private List<Booking> booking = new ArrayList<>();

    @SerializedName("trucker")
    @Expose
    private List<Trucker> trucker = new ArrayList<Trucker>();

    /**
     *
     * @return
     * The trucker
     */
    public List<Trucker> getTrucker() {
        return trucker;
    }

    /**
     *
     * @param trucker
     * The trucker
     */
    public void setTrucker(List<Trucker> trucker) {
        this.trucker = trucker;
    }


    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public CurrentCoordinates getCurrentCoordinates() {
        return currentCoordinates;
    }

    public void setCurrentCoordinates(CurrentCoordinates currentCoordinates) {
        this.currentCoordinates = currentCoordinates;
    }

    public String getTruckerColor() {
        return truckerColor;
    }

    public void setTruckerColor(String truckerColor) {
        this.truckerColor = truckerColor;
    }

    public List<Booking> getBooking() {
        return booking;
    }

    public void setBooking(List<Booking> booking) {
        this.booking = booking;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
