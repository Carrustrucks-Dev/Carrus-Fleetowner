package com.carrus.fleetowner.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VoterId implements Serializable {

@SerializedName("VoterIdNo")
@Expose
private String VoterIdNo;
@SerializedName("VoterIdDoc")
@Expose
private String VoterIdDoc;

/**
* 
* @return
* The VoterIdNo
*/
public String getVoterIdNo() {
return VoterIdNo;
}

/**
* 
* @param VoterIdNo
* The VoterIdNo
*/
public void setVoterIdNo(String VoterIdNo) {
this.VoterIdNo = VoterIdNo;
}

/**
* 
* @return
* The VoterIdDoc
*/
public String getVoterIdDoc() {
return VoterIdDoc;
}

/**
* 
* @param VoterIdDoc
* The VoterIdDoc
*/
public void setVoterIdDoc(String VoterIdDoc) {
this.VoterIdDoc = VoterIdDoc;
}

}