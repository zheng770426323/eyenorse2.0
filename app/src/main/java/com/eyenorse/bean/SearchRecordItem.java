package com.eyenorse.bean;

import java.io.Serializable;

/**
 * Created by zhengkq on 2017/1/13.
 */

public class SearchRecordItem implements Serializable{
    private String searchvalue;
    private String datetime;

    public String getSearchvalue() {
        return searchvalue;
    }

    public void setSearchvalue(String searchvalue) {
        this.searchvalue = searchvalue;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }
}
