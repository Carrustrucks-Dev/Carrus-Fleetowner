package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FleetOwner implements Serializable {

@SerializedName("_id")
@Expose
private String Id;
@SerializedName("email")
@Expose
private String email;
@SerializedName("fullName")
@Expose
private String fullName;
@SerializedName("phoneNumber")
@Expose
private String phoneNumber;

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
* The email
*/
public String getEmail() {
return email;
}

/**
* 
* @param email
* The email
*/
public void setEmail(String email) {
this.email = email;
}

/**
* 
* @return
* The fullName
*/
public String getFullName() {
return fullName;
}

/**
* 
* @param fullName
* The fullName
*/
public void setFullName(String fullName) {
this.fullName = fullName;
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

}