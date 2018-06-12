/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.consume;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Auto-generated: 2018-05-06 11:19:8
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Consume implements Parcelable{

    private String cate4_cost;
    private String year_cost;
    private String cate2_cost;
    private List<Month_cost> month_cost;
    private String cate3_cost;
    private String cate1_cost;

    protected Consume(Parcel in) {
        cate4_cost = in.readString();
        year_cost = in.readString();
        cate2_cost = in.readString();
        cate3_cost = in.readString();
        cate1_cost = in.readString();
    }

    public static final Creator<Consume> CREATOR = new Creator<Consume>() {
        @Override
        public Consume createFromParcel(Parcel in) {
            return new Consume(in);
        }

        @Override
        public Consume[] newArray(int size) {
            return new Consume[size];
        }
    };

    public void setCate4_cost(String cate4_cost) {
         this.cate4_cost = cate4_cost;
     }
     public String getCate4_cost() {
         return cate4_cost;
     }

    public void setYear_cost(String year_cost) {
         this.year_cost = year_cost;
     }
     public String getYear_cost() {
         return year_cost;
     }

    public void setCate2_cost(String cate2_cost) {
         this.cate2_cost = cate2_cost;
     }
     public String getCate2_cost() {
         return cate2_cost;
     }

    public void setMonth_cost(List<Month_cost> month_cost) {
         this.month_cost = month_cost;
     }
     public List<Month_cost> getMonth_cost() {
         return month_cost;
     }

    public void setCate3_cost(String cate3_cost) {
         this.cate3_cost = cate3_cost;
     }
     public String getCate3_cost() {
         return cate3_cost;
     }

    public void setCate1_cost(String cate1_cost) {
         this.cate1_cost = cate1_cost;
     }
     public String getCate1_cost() {
         return cate1_cost;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cate4_cost);
        dest.writeString(year_cost);
        dest.writeString(cate2_cost);
        dest.writeString(cate3_cost);
        dest.writeString(cate1_cost);
    }
}