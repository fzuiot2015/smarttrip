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
public class AllCity {

    private String code;
    private List<Info> info;
    private String content;
    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

    public void setInfo(List<Info> info) {
         this.info = info;
     }
     public List<Info> getInfo() {
         return info;
     }

    public void setContent(String content) {
         this.content = content;
     }
     public String getContent() {
         return content;
     }

}