package com.qin.pojo.BreakAlarm;

/**
 * Created by Administrator on 2018/5/9.
 */

public class BreakAlarm {

    private int user_id;
    private String lat;
    private String lng;
    private String address;
    private String des_casual;
    private String des_car;
    private String des_person;
    private String des_others;
    private String pic_car;
    private String time;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPic_car() {
        return pic_car;
    }

    public void setPic_car(String pic_car) {
        this.pic_car = pic_car;
    }

    public String getPic_person() {
        return pic_person;
    }

    public void setPic_person(String pic_person) {
        this.pic_person = pic_person;
    }

    private String pic_person;

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

    public String getDes_casual() {
        return des_casual;
    }

    public void setDes_casual(String des_casual) {
        this.des_casual = des_casual;
    }

    public String getDes_car() {
        return des_car;
    }

    public void setDes_car(String des_car) {
        this.des_car = des_car;
    }

    public String getDes_person() {
        return des_person;
    }

    public void setDes_person(String des_person) {
        this.des_person = des_person;
    }

    public String getDes_others() {
        return des_others;
    }

    public void setDes_others(String des_others) {
        this.des_others = des_others;
    }
}
