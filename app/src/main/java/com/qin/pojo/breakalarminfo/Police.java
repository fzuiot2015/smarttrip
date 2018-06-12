/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.breakalarminfo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Auto-generated: 2018-05-09 15:29:40
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Police implements Parcelable{

    private String name;
    private String tel;

    protected Police(Parcel in) {
        name = in.readString();
        tel = in.readString();
        address = in.readString();
        distance = in.readInt();
        lat = in.readDouble();
        lng = in.readInt();
    }

    public static final Creator<Police> CREATOR = new Creator<Police>() {
        @Override
        public Police createFromParcel(Parcel in) {
            return new Police(in);
        }

        @Override
        public Police[] newArray(int size) {
            return new Police[size];
        }
    };

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    private String address;
    private int distance;
    private double lat;
    private int lng;
    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setAddress(String address) {
         this.address = address;
     }
     public String getAddress() {
         return address;
     }

    public void setDistance(int distance) {
         this.distance = distance;
     }
     public int getDistance() {
         return distance;
     }

    public void setLat(double lat) {
         this.lat = lat;
     }
     public double getLat() {
         return lat;
     }

    public void setLng(int lng) {
         this.lng = lng;
     }
     public int getLng() {
         return lng;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(tel);
        dest.writeString(address);
        dest.writeInt(distance);
        dest.writeDouble(lat);
        dest.writeInt(lng);
    }
}