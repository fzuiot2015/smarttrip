/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.carcontrol;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Auto-generated: 2018-04-17 19:47:52
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Lists implements Parcelable {

    private String title;
    private String time;
    private String src;
    private String category;
    private String pic;
    private String url;
    private String weburl;
    private String content;
    private String gallery;
    private String addtime;

    protected Lists(Parcel in) {
        title = in.readString();
        time = in.readString();
        src = in.readString();
        category = in.readString();
        pic = in.readString();
        url = in.readString();
        weburl = in.readString();
        content = in.readString();
        gallery = in.readString();
        addtime = in.readString();
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

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

    public void setTime(String time) {
         this.time = time;
     }
     public String getTime() {
         return time;
     }

    public void setSrc(String src) {
         this.src = src;
     }
     public String getSrc() {
         return src;
     }

    public void setCategory(String category) {
         this.category = category;
     }
     public String getCategory() {
         return category;
     }

    public void setPic(String pic) {
         this.pic = pic;
     }
     public String getPic() {
         return pic;
     }

    public void setUrl(String url) {
         this.url = url;
     }
     public String getUrl() {
         return url;
     }

    public void setWeburl(String weburl) {
         this.weburl = weburl;
     }
     public String getWeburl() {
         return weburl;
     }

    public void setContent(String content) {
         this.content = content;
     }
     public String getContent() {
         return content;
     }

    public void setGallery(String gallery) {
         this.gallery = gallery;
     }
     public String getGallery() {
         return gallery;
     }

    public void setAddtime(String addtime) {
         this.addtime = addtime;
     }
     public String getAddtime() {
         return addtime;
     }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(time);
        dest.writeString(src);
        dest.writeString(category);
        dest.writeString(pic);
        dest.writeString(url);
        dest.writeString(weburl);
        dest.writeString(content);
        dest.writeString(gallery);
        dest.writeString(addtime);
    }
}