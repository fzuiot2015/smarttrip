/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.breakalarmhistory;
import java.util.List;

/**
 * Auto-generated: 2018-05-10 15:57:42
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BreakAlarmHistory {

    private List<Result> result;
    private String code;
    public void setResult(List<Result> result) {
         this.result = result;
     }
     public List<Result> getResult() {
         return result;
     }

    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

}