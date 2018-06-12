/**
 * Copyright 2018 bejson.com
 */
package com.qin.pojo.limitd.city;

import java.util.List;

/**
 * Auto-generated: 2018-04-08 3:30:27
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
public class PlateNumLimitedCity {

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