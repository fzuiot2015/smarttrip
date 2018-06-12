/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.up.parking;

/**
 * Auto-generated: 2018-04-23 16:7:38
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class UpParking {

    private String sortby;
    private String tags;
    private String radius;
    private String lat;
    private String page_num;

    public String getPage_index() {
        return page_index;
    }

    public void setPage_index(String page_index) {
        this.page_index = page_index;
    }

    private String page_index;

    public String getPage_num() {
        return page_num;
    }

    public void setPage_num(String page_num) {
        this.page_num = page_num;
    }

    public String getSortby() {
        return sortby;
    }

    public void setSortby(String sortby) {
        this.sortby = sortby;
    }

    private String lng;


    public void setTags(String tags) {
         this.tags = tags;
     }
     public String getTags() {
         return tags;
     }

    public void setRadius(String radius) {
         this.radius = radius;
     }
     public String getRadius() {
         return radius;
     }

    public void setLat(String lat) {
         this.lat = lat;
     }
     public String getLat() {
         return lat;
     }

    public void setLng(String lng) {
         this.lng = lng;
     }
     public String getLng() {
         return lng;
     }

}