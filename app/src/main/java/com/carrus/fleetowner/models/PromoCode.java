package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PromoCode implements Serializable{

@SerializedName("promoCodeId")
@Expose
private List<Object> promoCodeId = new ArrayList<>();

/**
* 
* @return
* The promoCodeId
*/
public List<Object> getPromoCodeId() {
return promoCodeId;
}

/**
* 
* @param promoCodeId
* The promoCodeId
*/
public void setPromoCodeId(List<Object> promoCodeId) {
this.promoCodeId = promoCodeId;
}

}