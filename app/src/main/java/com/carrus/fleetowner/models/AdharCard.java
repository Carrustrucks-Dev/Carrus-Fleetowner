package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdharCard implements Serializable {

@SerializedName("adharNo")
@Expose
private String adharNo;
@SerializedName("adharDoc")
@Expose
private String adharDoc;

/**
* 
* @return
* The adharNo
*/
public String getAdharNo() {
return adharNo;
}

/**
* 
* @param adharNo
* The adharNo
*/
public void setAdharNo(String adharNo) {
this.adharNo = adharNo;
}

/**
* 
* @return
* The adharDoc
*/
public String getAdharDoc() {
return adharDoc;
}

/**
* 
* @param adharDoc
* The adharDoc
*/
public void setAdharDoc(String adharDoc) {
this.adharDoc = adharDoc;
}

}