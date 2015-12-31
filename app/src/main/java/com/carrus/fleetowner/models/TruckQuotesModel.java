package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 12/10/15.
 */
public class TruckQuotesModel implements Serializable {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<TruckQuotesDetails> data = new ArrayList<>();

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
    public List<TruckQuotesDetails> getData() {
        return data;
    }

    /**
     * @param data The data
     */
    public void setData(List<TruckQuotesDetails> data) {
        this.data = data;
    }
}
