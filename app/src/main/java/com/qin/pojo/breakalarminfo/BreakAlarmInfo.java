/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.breakalarminfo;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Auto-generated: 2018-05-09 15:29:40
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BreakAlarmInfo implements Parcelable{

    private String bapxian_name;
    private List<Police> police;
    private List<Rescue> rescue;
    private String bapxian_tel;
    private String code;

    protected BreakAlarmInfo(Parcel in) {
        bapxian_name = in.readString();
        bapxian_tel = in.readString();
        code = in.readString();
    }

    public static final Creator<BreakAlarmInfo> CREATOR = new Creator<BreakAlarmInfo>() {
        @Override
        public BreakAlarmInfo createFromParcel(Parcel in) {
            return new BreakAlarmInfo(in);
        }

        @Override
        public BreakAlarmInfo[] newArray(int size) {
            return new BreakAlarmInfo[size];
        }
    };

    public void setBapxian_name(String bapxian_name) {
         this.bapxian_name = bapxian_name;
     }
     public String getBapxian_name() {
         return bapxian_name;
     }

    public void setPolice(List<Police> police) {
         this.police = police;
     }
     public List<Police> getPolice() {
         return police;
     }

    public void setRescue(List<Rescue> rescue) {
         this.rescue = rescue;
     }
     public List<Rescue> getRescue() {
         return rescue;
     }

    public void setBapxian_tel(String bapxian_tel) {
         this.bapxian_tel = bapxian_tel;
     }
     public String getBapxian_tel() {
         return bapxian_tel;
     }

    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bapxian_name);
        dest.writeString(bapxian_tel);
        dest.writeString(code);
    }
}