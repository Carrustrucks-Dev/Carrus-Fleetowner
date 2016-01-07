package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 12/10/15 for Fleet Owner for Fleet Owner.
 */
public class TruckQuotesDetails implements Serializable{
    @SerializedName("_id")
    @Expose
    private String Id;
    @SerializedName("shipper")
    @Expose
    private Shipper shipper;
    @SerializedName("budget")
    @Expose
    private Integer budget;
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
    private String acceptPrice;
    @SerializedName("trucker")
    @Expose
    private List<Object> trucker = new ArrayList<>();
    @SerializedName("truck")
    @Expose
    private Truck truck;
    @SerializedName("cargo")
    @Expose
    private Cargo cargo;
    @SerializedName("dropOff")
    @Expose
    private DropOff dropOff;
    @SerializedName("pickUp")
    @Expose
    private PickUp pickUp;
    @SerializedName("booking")
    @Expose
    private List<Object> booking = new ArrayList<>();
    @SerializedName("__v")
    @Expose
    private Integer V;
    @SerializedName("offerCost")
    @Expose
    private Long offerCost;
    @SerializedName("quoteStatus")
    @Expose
    private String quoteStatus;
    @SerializedName("quoteNote")
    @Expose
    private String quoteNote;
    @SerializedName("quoteId")
    @Expose
    private String quoteId;
    @SerializedName("fleetOwner")
    @Expose
    private String fleetOwner;
    @SerializedName("acceptQuote")
    @Expose
    private String acceptQuote;

    /**
     *
     * @return
     * The Id
     */
    public String getId() {
        return Id;
    }

    /**
     *
     * @param Id
     * The _id
     */
    public void setId(String Id) {
        this.Id = Id;
    }

    /**
     *
     * @return
     * The shipper
     */
    public Shipper getShipper() {
        return shipper;
    }

    /**
     *
     * @param shipper
     * The shipper
     */
    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }

    /**
     *
     * @return
     * The budget
     */
    public Integer getBudget() {
        return budget;
    }

    /**
     *
     * @param budget
     * The budget
     */
    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    /**
     *
     * @return
     * The note
     */
    public String getNote() {
        return note;
    }

    /**
     *
     * @param note
     * The note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The createdAt
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The paymentStatus
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     *
     * @param paymentStatus
     * The paymentStatus
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     *
     * @return
     * The bidStatus
     */
    public String getBidStatus() {
        return bidStatus;
    }

    /**
     *
     * @param bidStatus
     * The bidStatus
     */
    public void setBidStatus(String bidStatus) {
        this.bidStatus = bidStatus;
    }

    /**
     *
     * @return
     * The tracking
     */
    public String getTracking() {
        return tracking;
    }

    /**
     *
     * @param tracking
     * The tracking
     */
    public void setTracking(String tracking) {
        this.tracking = tracking;
    }

    /**
     *
     * @return
     * The requestedDateTime
     */
    public String getRequestedDateTime() {
        return requestedDateTime;
    }

    /**
     *
     * @param requestedDateTime
     * The requestedDateTime
     */
    public void setRequestedDateTime(String requestedDateTime) {
        this.requestedDateTime = requestedDateTime;
    }

    /**
     *
     * @return
     * The acceptPrice
     */
    public String getAcceptPrice() {
        return acceptPrice;
    }

    /**
     *
     * @param acceptPrice
     * The acceptPrice
     */
    public void setAcceptPrice(String acceptPrice) {
        this.acceptPrice = acceptPrice;
    }

    /**
     *
     * @return
     * The trucker
     */
    public List<Object> getTrucker() {
        return trucker;
    }

    /**
     *
     * @param trucker
     * The trucker
     */
    public void setTrucker(List<Object> trucker) {
        this.trucker = trucker;
    }

    /**
     *
     * @return
     * The truck
     */
    public Truck getTruck() {
        return truck;
    }

    /**
     *
     * @param truck
     * The truck
     */
    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    /**
     *
     * @return
     * The cargo
     */
    public Cargo getCargo() {
        return cargo;
    }

    /**
     *
     * @param cargo
     * The cargo
     */
    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    /**
     *
     * @return
     * The dropOff
     */
    public DropOff getDropOff() {
        return dropOff;
    }

    /**
     *
     * @param dropOff
     * The dropOff
     */
    public void setDropOff(DropOff dropOff) {
        this.dropOff = dropOff;
    }

    /**
     *
     * @return
     * The pickUp
     */
    public PickUp getPickUp() {
        return pickUp;
    }

    /**
     *
     * @param pickUp
     * The pickUp
     */
    public void setPickUp(PickUp pickUp) {
        this.pickUp = pickUp;
    }

    /**
     *
     * @return
     * The booking
     */
    public List<Object> getBooking() {
        return booking;
    }

    /**
     *
     * @param booking
     * The booking
     */
    public void setBooking(List<Object> booking) {
        this.booking = booking;
    }

    /**
     *
     * @return
     * The V
     */
    public Integer getV() {
        return V;
    }

    /**
     *
     * @param V
     * The __v
     */
    public void setV(Integer V) {
        this.V = V;
    }

    /**
     *
     * @return
     * The offerCost
     */
    public Long getOfferCost() {
        return offerCost;
    }

    /**
     *
     * @param offerCost
     * The offerCost
     */
    public void setOfferCost(Long offerCost) {
        this.offerCost = offerCost;
    }

    /**
     *
     * @return
     * The quoteStatus
     */
    public String getQuoteStatus() {
        return quoteStatus;
    }

    /**
     *
     * @param quoteStatus
     * The quoteStatus
     */
    public void setQuoteStatus(String quoteStatus) {
        this.quoteStatus = quoteStatus;
    }

    /**
     *
     * @return
     * The quoteNote
     */
    public String getQuoteNote() {
        return quoteNote;
    }

    /**
     *
     * @param quoteNote
     * The quoteNote
     */
    public void setQuoteNote(String quoteNote) {
        this.quoteNote = quoteNote;
    }

    /**
     *
     * @return
     * The quoteId
     */
    public String getQuoteId() {
        return quoteId;
    }

    /**
     *
     * @param quoteId
     * The quoteId
     */
    public void setQuoteId(String quoteId) {
        this.quoteId = quoteId;
    }

    /**
     *
     * @return
     * The fleetOwner
     */
    public String getFleetOwner() {
        return fleetOwner;
    }

    /**
     *
     * @param fleetOwner
     * The fleetOwner
     */
    public void setFleetOwner(String fleetOwner) {
        this.fleetOwner = fleetOwner;
    }

    /**
     *
     * @return
     * The acceptQuote
     */
    public String getAcceptQuote() {
        return acceptQuote;
    }

    /**
     *
     * @param acceptQuote
     * The acceptQuote
     */
    public void setAcceptQuote(String acceptQuote) {
        this.acceptQuote = acceptQuote;
    }


}
