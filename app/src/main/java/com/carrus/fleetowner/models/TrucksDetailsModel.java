package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 12/10/15.
 */
public class TrucksDetailsModel implements Serializable {

    @SerializedName("_id")
    @Expose
    private String Id;
    @SerializedName("shipper")
    @Expose
    private Shipper shipper;
    @SerializedName("budget")
    @Expose
    private String budget;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("bidStatus")
    @Expose
    private String bidStatus;
    @SerializedName("tracking")
    @Expose
    private String tracking;
    @SerializedName("requestedDateTime")
    @Expose
    private String requestedDateTime;
    @SerializedName("acceptPrice")
    @Expose
    private Integer acceptPrice;
    @SerializedName("trucker")
    @Expose
    private List<Object> trucker = new ArrayList<Object>();
    @SerializedName("truck")
    @Expose
    private Truck truck;
    @SerializedName("cargo")
    @Expose
    private Cargo cargo;
    @SerializedName("dropOff")
    @Expose
    public DropOff dropOff;
    @SerializedName("pickUp")
    @Expose
    public PickUp pickUp;
    @SerializedName("booking")
    @Expose
    private List<Object> booking = new ArrayList<Object>();
    @SerializedName("__v")
    @Expose
    private Integer V;

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
     * @return The shipper
     */
    public Shipper getShipper() {
        return shipper;
    }

    /**
     * @param shipper The shipper
     */
    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    /**
     * @return The budget
     */
    public String getBudget() {
        return budget;
    }

    /**
     * @param budget The budget
     */
    public void setBudget(String budget) {
        this.budget = budget;
    }

    /**
     * @return The note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note The note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt The createdAt
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return The paymentStatus
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * @param paymentStatus The paymentStatus
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * @return The bidStatus
     */
    public String getBidStatus() {
        return bidStatus;
    }

    /**
     * @param bidStatus The bidStatus
     */
    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    /**
     * @return The tracking
     */
    public String getTracking() {
        return tracking;
    }

    /**
     * @param tracking The tracking
     */
    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    /**
     * @return The requestedDateTime
     */
    public String getRequestedDateTime() {
        return requestedDateTime;
    }

    /**
     * @param requestedDateTime The requestedDateTime
     */
    public void setRequestedDateTime(String requestedDateTime) {
        this.requestedDateTime = requestedDateTime;
    }

    /**
     * @return The acceptPrice
     */
    public Integer getAcceptPrice() {
        return acceptPrice;
    }

    /**
     * @param acceptPrice The acceptPrice
     */
    public void setAcceptPrice(Integer acceptPrice) {
        this.acceptPrice = acceptPrice;
    }

    /**
     * @return The trucker
     */
    public List<Object> getTrucker() {
        return trucker;
    }

    /**
     * @param trucker The trucker
     */
    public void setTrucker(List<Object> trucker) {
        this.trucker = trucker;
    }

    /**
     * @return The truck
     */
    public Truck getTruck() {
        return truck;
    }

    /**
     * @param truck The truck
     */
    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    /**
     * @return The cargo
     */
    public Cargo getCargo() {
        return cargo;
    }

    /**
     * @param cargo The cargo
     */
    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }


    /**
     * @return The booking
     */
    public List<Object> getBooking() {
        return booking;
    }

    /**
     * @param booking The booking
     */
    public void setBooking(List<Object> booking) {
        this.booking = booking;
    }

    /**
     * @return The V
     */
    public Integer getV() {
        return V;
    }

    /**
     * @param V The __v
     */
    public void setV(Integer V) {
        this.V = V;
    }


}
