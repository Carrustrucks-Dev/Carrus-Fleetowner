package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Datum implements Serializable{

@SerializedName("_id")
@Expose
private String Id;
@SerializedName("driverId")
@Expose
private String driverId;
@SerializedName("driverName")
@Expose
private String driverName;
@SerializedName("phoneNumber")
@Expose
private String phoneNumber;
@SerializedName("address")
@Expose
private String address;
@SerializedName("stateDl")
@Expose
private String stateDl;
@SerializedName("createdAt")
@Expose
private String createdAt;
@SerializedName("currentCoordinates")
@Expose
private CurrentCoordinates currentCoordinates;
@SerializedName("totalRating")
@Expose
private String totalRating;
@SerializedName("noOfPeopleRating")
@Expose
private String noOfPeopleRating;
@SerializedName("rating")
@Expose
private String rating;
@SerializedName("loginCount")
@Expose
private String loginCount;
@SerializedName("radius")
@Expose
private String radius;
@SerializedName("location")
@Expose
private Location location;
@SerializedName("profilePicture")
@Expose
private ProfilePicture profilePicture;
@SerializedName("isBlocked")
@Expose
private Boolean isBlocked;
@SerializedName("fleetOwner")
@Expose
private List<FleetOwner> fleetOwner = new ArrayList<FleetOwner>();
@SerializedName("userType")
@Expose
private String userType;
@SerializedName("__v")
@Expose
private Integer V;
@SerializedName("drivingLicense")
@Expose
private DrivingLicense drivingLicense;
@SerializedName("VoterId")
@Expose
private VoterId VoterId;
@SerializedName("adharCard")
@Expose
private AdharCard adharCard;
@SerializedName("aviability")
@Expose
private String aviability;
@SerializedName("lastLogin")
@Expose
private String lastLogin;
@SerializedName("deviceDetails")
@Expose
private DeviceDetails deviceDetails;

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
* The driverId
*/
public String getDriverId() {
return driverId;
}

/**
* 
* @param driverId
* The driverId
*/
public void setDriverId(String driverId) {
this.driverId = driverId;
}

/**
* 
* @return
* The driverName
*/
public String getDriverName() {
return driverName;
}

/**
* 
* @param driverName
* The driverName
*/
public void setDriverName(String driverName) {
this.driverName = driverName;
}

/**
* 
* @return
* The phoneNumber
*/
public String getPhoneNumber() {
return phoneNumber;
}

/**
* 
* @param phoneNumber
* The phoneNumber
*/
public void setPhoneNumber(String phoneNumber) {
this.phoneNumber = phoneNumber;
}

/**
* 
* @return
* The address
*/
public String getAddress() {
return address;
}

/**
* 
* @param address
* The address
*/
public void setAddress(String address) {
this.address = address;
}

/**
* 
* @return
* The stateDl
*/
public String getStateDl() {
return stateDl;
}

/**
* 
* @param stateDl
* The stateDl
*/
public void setStateDl(String stateDl) {
this.stateDl = stateDl;
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
* The currentCoordinates
*/
public CurrentCoordinates getCurrentCoordinates() {
return currentCoordinates;
}

/**
* 
* @param currentCoordinates
* The currentCoordinates
*/
public void setCurrentCoordinates(CurrentCoordinates currentCoordinates) {
this.currentCoordinates = currentCoordinates;
}

/**
* 
* @return
* The totalRating
*/
public String getTotalRating() {
return totalRating;
}

/**
* 
* @param totalRating
* The totalRating
*/
public void setTotalRating(String totalRating) {
this.totalRating = totalRating;
}

/**
* 
* @return
* The noOfPeopleRating
*/
public String getNoOfPeopleRating() {
return noOfPeopleRating;
}

/**
* 
* @param noOfPeopleRating
* The noOfPeopleRating
*/
public void setNoOfPeopleRating(String noOfPeopleRating) {
this.noOfPeopleRating = noOfPeopleRating;
}

/**
* 
* @return
* The rating
*/
public String getRating() {
return rating;
}

/**
* 
* @param rating
* The rating
*/
public void setRating(String rating) {
this.rating = rating;
}

/**
* 
* @return
* The loginCount
*/
public String getLoginCount() {
return loginCount;
}

/**
* 
* @param loginCount
* The loginCount
*/
public void setLoginCount(String loginCount) {
this.loginCount = loginCount;
}

/**
* 
* @return
* The radius
*/
public String getRadius() {
return radius;
}

/**
* 
* @param radius
* The radius
*/
public void setRadius(String radius) {
this.radius = radius;
}

/**
* 
* @return
* The location
*/
public Location getLocation() {
return location;
}

/**
* 
* @param location
* The location
*/
public void setLocation(Location location) {
this.location = location;
}

/**
* 
* @return
* The profilePicture
*/
public ProfilePicture getProfilePicture() {
return profilePicture;
}

/**
* 
* @param profilePicture
* The profilePicture
*/
public void setProfilePicture(ProfilePicture profilePicture) {
this.profilePicture = profilePicture;
}

/**
* 
* @return
* The isBlocked
*/
public Boolean getIsBlocked() {
return isBlocked;
}

/**
* 
* @param isBlocked
* The isBlocked
*/
public void setIsBlocked(Boolean isBlocked) {
this.isBlocked = isBlocked;
}

/**
* 
* @return
* The fleetOwner
*/
public List<FleetOwner> getFleetOwner() {
return fleetOwner;
}

/**
* 
* @param fleetOwner
* The fleetOwner
*/
public void setFleetOwner(List<FleetOwner> fleetOwner) {
this.fleetOwner = fleetOwner;
}

/**
* 
* @return
* The userType
*/
public String getUserType() {
return userType;
}

/**
* 
* @param userType
* The userType
*/
public void setUserType(String userType) {
this.userType = userType;
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
* The drivingLicense
*/
public DrivingLicense getDrivingLicense() {
return drivingLicense;
}

/**
* 
* @param drivingLicense
* The drivingLicense
*/
public void setDrivingLicense(DrivingLicense drivingLicense) {
this.drivingLicense = drivingLicense;
}

/**
* 
* @return
* The VoterId
*/
public VoterId getVoterId() {
return VoterId;
}

/**
* 
* @param VoterId
* The VoterId
*/
public void setVoterId(VoterId VoterId) {
this.VoterId = VoterId;
}

/**
* 
* @return
* The adharCard
*/
public AdharCard getAdharCard() {
return adharCard;
}

/**
* 
* @param adharCard
* The adharCard
*/
public void setAdharCard(AdharCard adharCard) {
this.adharCard = adharCard;
}

/**
* 
* @return
* The aviability
*/
public String getAviability() {
return aviability;
}

/**
* 
* @param aviability
* The aviability
*/
public void setAviability(String aviability) {
this.aviability = aviability;
}

/**
* 
* @return
* The lastLogin
*/
public String getLastLogin() {
return lastLogin;
}

/**
* 
* @param lastLogin
* The lastLogin
*/
public void setLastLogin(String lastLogin) {
this.lastLogin = lastLogin;
}

/**
* 
* @return
* The deviceDetails
*/
public DeviceDetails getDeviceDetails() {
return deviceDetails;
}

/**
* 
* @param deviceDetails
* The deviceDetails
*/
public void setDeviceDetails(DeviceDetails deviceDetails) {
this.deviceDetails = deviceDetails;
}

}