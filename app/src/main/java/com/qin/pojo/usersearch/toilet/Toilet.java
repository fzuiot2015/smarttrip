/**
  * Copyright 2018 bejson.com 
  */
package com.qin.pojo.usersearch.toilet;
import java.util.List;

/**
 * Auto-generated: 2018-04-24 18:18:3
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class Toilet {

    private int status;
    private String message;
    private int total;
    private List<Results> results;
    public void setStatus(int status) {
         this.status = status;
     }
     public int getStatus() {
         return status;
     }

    public void setMessage(String message) {
         this.message = message;
     }
     public String getMessage() {
         return message;
     }

    public void setTotal(int total) {
         this.total = total;
     }
     public int getTotal() {
         return total;
     }

    public void setResults(List<Results> results) {
         this.results = results;
     }
     public List<Results> getResults() {
         return results;
     }

}