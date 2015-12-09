
package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CurrentCoordinates implements Serializable {

@SerializedName("long")
@Expose
private String _long;
@SerializedName("lat")
@Expose
private String lat;

/**
* 
* @return
* The _long
*/
public String getLong() {
return _long;
}

/**
* 
* @param _long
* The long
*/
public void setLong(String _long) {
this._long = _long;
}

/**
* 
* @return
* The lat
*/
public String getLat() {
return lat;
}

/**
* 
* @param lat
* The lat
*/
public void setLat(String lat) {
this.lat = lat;
}

}