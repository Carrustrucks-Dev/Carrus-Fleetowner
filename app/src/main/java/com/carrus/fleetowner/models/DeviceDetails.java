package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeviceDetails implements Serializable {

@SerializedName("deviceType")
@Expose
private String deviceType;
@SerializedName("deviceName")
@Expose
private String deviceName;
@SerializedName("deviceToken")
@Expose
private String deviceToken;

/**
* 
* @return
* The deviceType
*/
public String getDeviceType() {
return deviceType;
}

/**
* 
* @param deviceType
* The deviceType
*/
public void setDeviceType(String deviceType) {
this.deviceType = deviceType;
}

/**
* 
* @return
* The deviceName
*/
public String getDeviceName() {
return deviceName;
}

/**
* 
* @param deviceName
* The deviceName
*/
public void setDeviceName(String deviceName) {
this.deviceName = deviceName;
}

/**
* 
* @return
* The deviceToken
*/
public String getDeviceToken() {
return deviceToken;
}

/**
* 
* @param deviceToken
* The deviceToken
*/
public void setDeviceToken(String deviceToken) {
this.deviceToken = deviceToken;
}

}