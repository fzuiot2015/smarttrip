/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.allcartype.carmodel;
import java.util.List;

/**
 * Auto-generated: 2018-04-18 14:31:5
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class CarModel {

    private String status;
    private String msg;
    private List<Result> result;
    public void setStatus(String status) {
         this.status = status;
     }
     public String getStatus() {
         return status;
     }

    public void setMsg(String msg) {
         this.msg = msg;
     }
     public String getMsg() {
         return msg;
     }

    public void setResult(List<Result> result) {
         this.result = result;
     }
     public List<Result> getResult() {
         return result;
     }

}