/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.breakrulequery;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Auto-generated: 2018-04-07 19:54:29
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Lists implements Parcelable{

    private String date;
    private String area;
    private String act;
    private String code;
    private String fen;
    private String money;
    private String handled;
    private String longitude;
    private String latitude;
    private String PayNo;
    private String CollectOrgan;

    protected Lists(Parcel in) {
        date = in.readString();
        area = in.readString();
        act = in.readString();
        code = in.readString();
        fen = in.readString();
        money = in.readString();
        handled = in.readString();
        longitude = in.readString();
        latitude = in.readString();
        PayNo = in.readString();
        CollectOrgan = in.readString();
    }

    public static final Creator<Lists> CREATOR = new Creator<Lists>() {
        @Override
        public Lists createFromParcel(Parcel in) {
            return new Lists(in);
        }

        @Override
        public Lists[] newArray(int size) {
            return new Lists[size];
        }
    };

    public void setDate(String date) {
         this.date = date;
     }
     public String getDate() {
         return date;
     }

    public void setArea(String area) {
         this.area = area;
     }
     public String getArea() {
         return area;
     }

    public void setAct(String act) {
         this.act = act;
     }
     public String getAct() {
         return act;
     }

    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

    public void setFen(String fen) {
         this.fen = fen;
     }
     public String getFen() {
         return fen;
     }

    public void setMoney(String money) {
         this.money = money;
     }
     public String getMoney() {
         return money;
     }

    public void setHandled(String handled) {
         this.handled = handled;
     }
     public String getHandled() {
         return handled;
     }

    public void setLongitude(String longitude) {
         this.longitude = longitude;
     }
     public String getLongitude() {
         return longitude;
     }

    public void setLatitude(String latitude) {
         this.latitude = latitude;
     }
     public String getLatitude() {
         return latitude;
     }

    public void setPayNo(String PayNo) {
         this.PayNo = PayNo;
     }
     public String getPayNo() {
         return PayNo;
     }

    public void setCollectOrgan(String CollectOrgan) {
         this.CollectOrgan = CollectOrgan;
     }
     public String getCollectOrgan() {
         return CollectOrgan;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(area);
        dest.writeString(act);
        dest.writeString(code);
        dest.writeString(fen);
        dest.writeString(money);
        dest.writeString(handled);
        dest.writeString(longitude);
        dest.writeString(latitude);
        dest.writeString(PayNo);
        dest.writeString(CollectOrgan);
    }
}