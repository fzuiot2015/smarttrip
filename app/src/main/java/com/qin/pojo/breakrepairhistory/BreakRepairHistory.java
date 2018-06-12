/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.breakrepairhistory;
import java.util.List;

/**
 * Auto-generated: 2018-05-10 15:41:28
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BreakRepairHistory {

    private List<Result> result;
    private int code;
    public void setResult(List<Result> result) {
         this.result = result;
     }
     public List<Result> getResult() {
         return result;
     }

    public void setCode(int code) {
         this.code = code;
     }
     public int getCode() {
         return code;
     }

}