package com.qin.pojo.breakrepair;

/**
 * Created by Administrator on 2018/5/9.
 */

public class BreakRepair {
    private int user_id;
    private String lat;
    private String lng;
    private String address;
    private String time;
    private String description;
    private String models;
    private String fault_photo;

    public String getFault_photo() {
        return fault_photo;
    }

    public void setFault_photo(String fault_photo) {
        this.fault_photo = fault_photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModels() {
        return models;
    }

    public void setModels(String models) {
        this.models = models;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    private String others;
}
