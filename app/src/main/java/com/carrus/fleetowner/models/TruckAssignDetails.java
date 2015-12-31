package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sunny on 12/10/15.
 */
public class TruckAssignDetails implements Serializable {
    @SerializedName("_id")
    @Expose
    private String Id;
    @SerializedName("bid")
    @Expose
    private String bid;
    @SerializedName("shipper")
    @Expose
    private Shipper shipper;
    @SerializedName("paymentMode")
    @Expose
    private String paymentMode;
    @SerializedName("paymentOn")
    @Expose
    private String paymentOn;
    @SerializedName("jobNote")
    @Expose
    private String jobNote;
    @SerializedName("bookingStatus")
    @Expose
    private String bookingStatus;
    @SerializedName("fleetOwner")
    @Expose
    private String fleetOwner;
    @SerializedName("transactionLogs")
    @Expose
    private List<Object> transactionLogs = new ArrayList<>();
    @SerializedName("tracking")
    @Expose
    private String tracking;
    @SerializedName("bookingCreatedAt")
    @Expose
    private String bookingCreatedAt;
    @SerializedName("acceptPrice")
    @Expose
    private Integer acceptPrice;
    @SerializedName("rating")
    @Expose
    private Rating rating;
    @SerializedName("doc")
    @Expose
    private DocModel doc;
    @SerializedName("truck")
    @Expose
    private Truck truck;
    @SerializedName("cargo")
    @Expose
    private Cargo cargo;
    @SerializedName("paymentStatus")
    @Expose
    private String paymentStatus;
    @SerializedName("dropOff")
    @Expose
    private DropOffModel dropOff;
    @SerializedName("pickUp")
    @Expose
    private PickUpModel pickUp;
    @SerializedName("__v")
    @Expose
    private Integer V;
    @SerializedName("truckerNote")
    @Expose
    private Object truckerNote;
    @SerializedName("promoCode")
    @Expose
    private PromoCode promoCode;

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
     * The bid
     */
    public String getBid() {
        return bid;
    }

    /**
     *
     * @param bid
     * The bid
     */
    public void setBid(String bid) {
        this.bid = bid;
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
     * The paymentMode
     */
    public String getPaymentMode() {
        return paymentMode;
    }

    /**
     *
     * @param paymentMode
     * The paymentMode
     */
    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    /**
     *
     * @return
     * The paymentOn
     */
    public String getPaymentOn() {
        return paymentOn;
    }

    /**
     *
     * @param paymentOn
     * The paymentOn
     */
    public void setPaymentOn(String paymentOn) {
        this.paymentOn = paymentOn;
    }

    /**
     *
     * @return
     * The jobNote
     */
    public String getJobNote() {
        return jobNote;
    }

    /**
     *
     * @param jobNote
     * The jobNote
     */
    public void setJobNote(String jobNote) {
        this.jobNote = jobNote;
    }

    /**
     *
     * @return
     * The bookingStatus
     */
    public String getBookingStatus() {
        return bookingStatus;
    }

    /**
     *
     * @param bookingStatus
     * The bookingStatus
     */
    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
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
     * The transactionLogs
     */
    public List<Object> getTransactionLogs() {
        return transactionLogs;
    }

    /**
     *
     * @param transactionLogs
     * The transactionLogs
     */
    public void setTransactionLogs(List<Object> transactionLogs) {
        this.transactionLogs = transactionLogs;
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
     * The bookingCreatedAt
     */
    public String getBookingCreatedAt() {
        return bookingCreatedAt;
    }

    /**
     *
     * @param bookingCreatedAt
     * The bookingCreatedAt
     */
    public void setBookingCreatedAt(String bookingCreatedAt) {
        this.bookingCreatedAt = bookingCreatedAt;
    }

    /**
     *
     * @return
     * The acceptPrice
     */
    public Integer getAcceptPrice() {
        return acceptPrice;
    }

    /**
     *
     * @param acceptPrice
     * The acceptPrice
     */
    public void setAcceptPrice(Integer acceptPrice) {
        this.acceptPrice = acceptPrice;
    }

    /**
     *
     * @return
     * The rating
     */
    public Rating getRating() {
        return rating;
    }

    /**
     *
     * @param rating
     * The rating
     */
    public void setRating(Rating rating) {
        this.rating = rating;
    }

    /**
     *
     * @return
     * The doc
     */
    public DocModel getDoc() {
        return doc;
    }

    /**
     *
     * @param doc
     * The doc
     */
    public void setDoc(DocModel doc) {
        this.doc = doc;
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
     * The dropOff
     */
    public DropOffModel getDropOff() {
        return dropOff;
    }

    /**
     *
     * @param dropOff
     * The dropOff
     */
    public void setDropOff(DropOffModel dropOff) {
        this.dropOff = dropOff;
    }

    /**
     *
     * @return
     * The pickUp
     */
    public PickUpModel getPickUp() {
        return pickUp;
    }

    /**
     *
     * @param pickUp
     * The pickUp
     */
    public void setPickUp(PickUpModel pickUp) {
        this.pickUp = pickUp;
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
     * The truckerNote
     */
    public Object getTruckerNote() {
        return truckerNote;
    }

    /**
     *
     * @param truckerNote
     * The truckerNote
     */
    public void setTruckerNote(Object truckerNote) {
        this.truckerNote = truckerNote;
    }

    /**
     *
     * @return
     * The promoCode
     */
    public PromoCode getPromoCode() {
        return promoCode;
    }

    /**
     *
     * @param promoCode
     * The promoCode
     */
    public void setPromoCode(PromoCode promoCode) {
        this.promoCode = promoCode;
    }
}
