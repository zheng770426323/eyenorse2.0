package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/10.
 */

public class BannerList implements Serializable {
    private List<VideoInfo>banner;

    public List<VideoInfo> getBanner() {
        return banner;
    }

    public void setBanner(List<VideoInfo> banner) {
        this.banner = banner;
    }
}
