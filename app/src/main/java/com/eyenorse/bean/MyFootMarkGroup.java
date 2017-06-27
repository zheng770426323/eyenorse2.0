package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/16.
 */

public class MyFootMarkGroup implements Serializable {
    private String datetime;
    private List<FootMarkInfo>childList;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public List<FootMarkInfo> getChild() {
        return childList;
    }

    public void setChild(List<FootMarkInfo> child) {
        this.childList = child;
    }
}
