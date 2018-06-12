/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.usersearch.parking;
import java.util.List;

/**
 * Auto-generated: 2018-04-23 12:34:6
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Contents {

    private int parking_rest;
    private int parking_all;
private int parking_rest_location;

    public int getParking_rest_location() {
        return parking_rest_location;
    }

    public void setParking_rest_location(int parking_rest_location) {
        this.parking_rest_location = parking_rest_location;
    }

    public int getParking_all() {
        return parking_all;
    }

    public void setParking_all(int parking_all) {
        this.parking_all = parking_all;
    }

    private int parking_charge;
    private String tags;
    private long uid;
    private String province;
    private long geotable_id;
    private String district;
    private long create_time;
    private String city;
    private List<String> location;
    private String address;
    private String title;
    private int coord_type;
    private String direction;
    private int type;
    private int distance;
    private int weight;
    public void setParking_rest(int parking_rest) {
         this.parking_rest = parking_rest;
     }
     public int getParking_rest() {
         return parking_rest;
     }

    public void setParking_charge(int parking_charge) {
         this.parking_charge = parking_charge;
     }
     public int getParking_charge() {
         return parking_charge;
     }

    public void setTags(String tags) {
         this.tags = tags;
     }
     public String getTags() {
         return tags;
     }

    public void setUid(long uid) {
         this.uid = uid;
     }
     public long getUid() {
         return uid;
     }

    public void setProvince(String province) {
         this.province = province;
     }
     public String getProvince() {
         return province;
     }

    public void setGeotable_id(long geotable_id) {
         this.geotable_id = geotable_id;
     }
     public long getGeotable_id() {
         return geotable_id;
     }

    public void setDistrict(String district) {
         this.district = district;
     }
     public String getDistrict() {
         return district;
     }

    public void setCreate_time(long create_time) {
         this.create_time = create_time;
     }
     public long getCreate_time() {
         return create_time;
     }

    public void setCity(String city) {
         this.city = city;
     }
     public String getCity() {
         return city;
     }

    public void setLocation(List<String> location) {
         this.location = location;
     }
     public List<String> getLocation() {
         return location;
     }

    public void setAddress(String address) {
         this.address = address;
     }
     public String getAddress() {
         return address;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setCoord_type(int coord_type) {
         this.coord_type = coord_type;
     }
     public int getCoord_type() {
         return coord_type;
     }

    public void setDirection(String direction) {
         this.direction = direction;
     }
     public String getDirection() {
         return direction;
     }

    public void setType(int type) {
         this.type = type;
     }
     public int getType() {
         return type;
     }

    public void setDistance(int distance) {
         this.distance = distance;
     }
     public int getDistance() {
         return distance;
     }

    public void setWeight(int weight) {
         this.weight = weight;
     }
     public int getWeight() {
         return weight;
     }

}