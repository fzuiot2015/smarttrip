/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.breakrule;
import java.util.List;

/**
 * Auto-generated: 2018-04-07 13:33:50
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class BreakRule {

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