package com.carrus.fleetowner.models;

/**
 * Created by Saurbhv on 10/31/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class ExpandableChildItem {
    private String name;
    private String detail;
    private int type;


    public ExpandableChildItem(String name,String detail, int type) {
        this.name=name;
        this.detail=detail;
        this.type=type;

    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDetail() {

        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
