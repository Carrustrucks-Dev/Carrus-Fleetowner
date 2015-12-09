package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DrivingLicense implements Serializable {

@SerializedName("drivingLicenseNo")
@Expose
private String drivingLicenseNo;
@SerializedName("drivingLicenseDoc")
@Expose
private String drivingLicenseDoc;
@SerializedName("validity")
@Expose
private String validity;

/**
* 
* @return
* The drivingLicenseNo
*/
public String getDrivingLicenseNo() {
return drivingLicenseNo;
}

/**
* 
* @param drivingLicenseNo
* The drivingLicenseNo
*/
public void setDrivingLicenseNo(String drivingLicenseNo) {
this.drivingLicenseNo = drivingLicenseNo;
}

/**
* 
* @return
* The drivingLicenseDoc
*/
public String getDrivingLicenseDoc() {
return drivingLicenseDoc;
}

/**
* 
* @param drivingLicenseDoc
* The drivingLicenseDoc
*/
public void setDrivingLicenseDoc(String drivingLicenseDoc) {
this.drivingLicenseDoc = drivingLicenseDoc;
}

/**
* 
* @return
* The validity
*/
public String getValidity() {
return validity;
}

/**
* 
* @param validity
* The validity
*/
public void setValidity(String validity) {
this.validity = validity;
}

}