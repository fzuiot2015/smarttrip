/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.allcity;
import java.util.List;

/**
 * Auto-generated: 2018-04-18 0:45:52
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Info {

    private long id;
    private String areaname;
    private String shortname;
    private int parentid;
    private int level;
    private List<ChildList> childList;
    public void setId(long id) {
         this.id = id;
     }
     public long getId() {
         return id;
     }

    public void setAreaname(String areaname) {
         this.areaname = areaname;
     }
     public String getAreaname() {
         return areaname;
     }

    public void setShortname(String shortname) {
         this.shortname = shortname;
     }
     public String getShortname() {
         return shortname;
     }

    public void setParentid(int parentid) {
         this.parentid = parentid;
     }
     public int getParentid() {
         return parentid;
     }

    public void setLevel(int level) {
         this.level = level;
     }
     public int getLevel() {
         return level;
     }

    public void setChildList(List<ChildList> childList) {
         this.childList = childList;
     }
     public List<ChildList> getChildList() {
         return childList;
     }

}