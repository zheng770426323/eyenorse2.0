package com.eyenorse.bean;

import java.io.Serializable;

/**
 * Created by zhengkq on 2017/1/10.
 */

public class VideoInfo implements Serializable {
    private String title;
    private String bannerurl;
    private String img;
    private int video_id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBannerurl() {
        return bannerurl;
    }

    public void setBannerurl(String bannerurl) {
        this.bannerurl = bannerurl;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }
}
