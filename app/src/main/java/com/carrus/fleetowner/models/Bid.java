package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Bid implements Serializable {

@SerializedName("_id")
@Expose
private String Id;
@SerializedName("note")
@Expose
private String note;
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