package com.carrus.fleetowner.models;

/**
 * Created by Sunny on 11/16/15 for Fleet Owner.
 */
public class Header {
    private String name;
    private boolean isVisible;

    public Header(String name){
        this.name=name;
        this.isVisible= false;
    }


    public boolean isVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
