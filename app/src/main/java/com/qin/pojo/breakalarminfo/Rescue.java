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
public class Rescue implements Parcelable{

    private String name;
    private String address;
    private String tel;
    private int distance;
    private double lat;
    private int lng;

    protected Rescue(Parcel in) {
        name = in.readString();
        address = in.readString();
        tel = in.readString();
        distance = in.readInt();
        lat = in.readDouble();
        lng = in.readInt();
    }

    public static final Creator<Rescue> CREATOR = new Creator<Rescue>() {
        @Override
        public Rescue createFromParcel(Parcel in) {
            return new Rescue(in);
        }

        @Override
        public Rescue[] newArray(int size) {
            return new Rescue[size];
        }
    };

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

    public void setTel(String tel) {
         this.tel = tel;
     }
     public String getTel() {
         return tel;
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
        dest.writeString(address);
        dest.writeString(tel);
        dest.writeInt(distance);
        dest.writeDouble(lat);
        dest.writeInt(lng);
    }
}