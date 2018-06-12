/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.weather;
import java.util.List;

/**
 * Auto-generated: 2018-04-06 15:34:47
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Result {

    private Sk sk;
    private Today today;
    private List<Future> future;
    public void setSk(Sk sk) {
         this.sk = sk;
     }
     public Sk getSk() {
         return sk;
     }

    public void setToday(Today today) {
         this.today = today;
     }
     public Today getToday() {
         return today;
     }

    public void setFuture(List<Future> future) {
         this.future = future;
     }
     public List<Future> getFuture() {
         return future;
     }

}