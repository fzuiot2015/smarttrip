/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.breakrulequery;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Auto-generated: 2018-04-07 19:54:29
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Result implements Parcelable{

    private String province;
    private String city;
    private String hphm;
    private String hpzl;
    private ArrayList<Lists> lists;

    protected Result(Parcel in) {
        province = in.readString();
        city = in.readString();
        hphm = in.readString();
        hpzl = in.readString();
        lists = in.createTypedArrayList(Lists.CREATOR);
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

    public void setHphm(String hphm) {
         this.hphm = hphm;
     }
     public String getHphm() {
         return hphm;
     }

    public void setHpzl(String hpzl) {
         this.hpzl = hpzl;
     }
     public String getHpzl() {
         return hpzl;
     }

    public void setLists(ArrayList<Lists> lists) {
         this.lists = lists;
     }
     public ArrayList<Lists> getLists() {
         return lists;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(province);
        dest.writeString(city);
        dest.writeString(hphm);
        dest.writeString(hpzl);
        dest.writeTypedList(lists);
    }
}