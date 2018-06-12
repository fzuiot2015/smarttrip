/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.breakrule;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Auto-generated: 2018-04-07 13:33:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Result implements Parcelable,Comparable<Result> {

    private String province;
    private String city;
    private String town;
    private String address;
    private String content;
    private String num;
    private String lat;
    private String lng;
    private String distance;

    protected Result(Parcel in) {
        province = in.readString();
        city = in.readString();
        town = in.readString();
        address = in.readString();
        content = in.readString();
        num = in.readString();
        lat = in.readString();
        lng = in.readString();
        distance = in.readString();
    }

    public static final Creator<Result> CREATOR = new Creator<Result>() {
        @Override
        public Result createFromParcel(Parcel in) {
            return new Result(in);
        }

        @Override
        public Result[] newArray(int size) {
            return new Result[size];
        }
    };

    public void setProvince(String province) {
         this.province = province;
     }
     public String getProvince() {
         return province;
     }

    public void setCity(String city) {
         this.city = city;
     }
     public String getCity() {
         return city;
     }

    public void setTown(String town) {
         this.town = town;
     }
     public String getTown() {
         return town;
     }

    public void setAddress(String address) {
         this.address = address;
     }
     public String getAddress() {
         return address;
     }

    public void setContent(String content) {
         this.content = content;
     }
     public String getContent() {
         return content;
     }

    public void setNum(String num) {
         this.num = num;
     }
     public String getNum() {
         return num;
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

    public void setDistance(String distance) {
         this.distance = distance;
     }
     public String getDistance() {
         return distance;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(town);
        dest.writeString(address);
        dest.writeString(content);
        dest.writeString(num);
        dest.writeString(lat);
        dest.writeString(lng);
        dest.writeString(distance);
    }

    @Override
    public String toString() {
        return "Result{" +
                "province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", town='" + town + '\'' +
                ", address='" + address + '\'' +
                ", content='" + content + '\'' +
                ", num='" + num + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }

    @Override
    public int compareTo(@NonNull Result result) {
        return Integer.parseInt(this.getNum())-Integer.parseInt(result.getNum());
    }
}