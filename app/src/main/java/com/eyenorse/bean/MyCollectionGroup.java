package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/13.
 */

public class MyCollectionGroup implements Serializable {
    private String dateTime;
    private List<MyCollectionVideoList.MyCollectionVideo>childList;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public List<MyCollectionVideoList.MyCollectionVideo> getChildList() {
        return childList;
    }

    public void setChildList(List<MyCollectionVideoList.MyCollectionVideo> childList) {
        this.childList = childList;
    }
}
