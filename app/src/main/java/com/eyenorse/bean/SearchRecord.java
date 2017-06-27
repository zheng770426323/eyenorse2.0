package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/13.
 */

public class SearchRecord implements Serializable {
    private List<SearchRecordItem>search;

    public List<SearchRecordItem> getSearch() {
        return search;
    }

    public void setSearch(List<SearchRecordItem> search) {
        this.search = search;
    }
}
