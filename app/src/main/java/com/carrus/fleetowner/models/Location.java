package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Location implements Serializable {

@SerializedName("coordinates")
@Expose
private List<Integer> coordinates = new ArrayList<Integer>();
@SerializedName("type")
@Expose
private String type;

/**
* 
* @return
* The coordinates
*/
public List<Integer> getCoordinates() {
return coordinates;
}

/**
* 
* @param coordinates
* The coordinates
*/
public void setCoordinates(List<Integer> coordinates) {
this.coordinates = coordinates;
}

/**
* 
* @return
* The type
*/
public String getType() {
return type;
}

/**
* 
* @param type
* The type
*/
public void setType(String type) {
this.type = type;
}

}