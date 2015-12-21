package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
}
