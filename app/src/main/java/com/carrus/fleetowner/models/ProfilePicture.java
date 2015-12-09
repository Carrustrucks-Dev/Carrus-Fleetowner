package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ProfilePicture implements Serializable {

@SerializedName("original")
@Expose
private String original;
@SerializedName("thumb")
@Expose
private String thumb;

/**
* 
* @return
* The original
*/
public String getOriginal() {
return original;
}

/**
* 
* @param original
* The original
*/
public void setOriginal(String original) {
this.original = original;
}

/**
* 
* @return
* The thumb
*/
public String getThumb() {
return thumb;
}

/**
* 
* @param thumb
* The thumb
*/
public void setThumb(String thumb) {
this.thumb = thumb;
}

}