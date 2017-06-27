package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/16.
 */

public class FootMarkList implements Serializable {
    private int count;
    private List<FootMarkInfo>footmark;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<FootMarkInfo> getFootmark() {
        return footmark;
    }

    public void setFootmark(List<FootMarkInfo> footmark) {
        this.footmark = footmark;
    }
}
