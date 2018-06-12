/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.usersearch.parking;
import java.util.List;

/**
 * Auto-generated: 2018-04-23 12:34:6
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Parking {

    private int status;
    private int total;
    private int size;
    private List<Contents> contents;
    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setTotal(int total) {
         this.total = total;
     }
     public int getTotal() {
         return total;
     }

    public void setSize(int size) {
         this.size = size;
     }
     public int getSize() {
         return size;
     }

    public void setContents(List<Contents> contents) {
         this.contents = contents;
     }
     public List<Contents> getContents() {
         return contents;
     }

}