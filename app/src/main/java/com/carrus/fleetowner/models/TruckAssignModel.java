package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 12/10/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class TruckAssignModel implements Serializable {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<TruckAssignDetails> data = new ArrayList<>();

    /**
     * @return The statusCode
     */
    public Integer getStatusCode() {
        return statusCode;
    }

    /**
     * @param statusCode The statusCode
     */
    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * @return The message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The data
     */
    public List<TruckAssignDetails> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<TruckAssignDetails> data) {
        this.data = data;
    }
}
