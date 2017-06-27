package com.eyenorse.bean;

import java.util.List;

/**
 * Created by zhengkq on 2017/3/22.
 */

public class MyVideoBean {
    /*"expire_time": "2017-04-03 15:15:35",
        "id": 32,
        "title": "科学魔方高级",
        "images": "http://yanhushi.caifutang.com/uploads/video/1490940660730507.jpg",
        "videopageviews": 57,
        "duration": 0,
        "collectioncount": 20*/
    private List<MyVideoItem> data;
    private int count;

    public List<MyVideoItem> getData() {
        return data;
    }

    public void setData(List<MyVideoItem> data) {
        this.data = data;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class MyVideoItem{
        public String expire_time;
        public int id;
        public String title;
        public String images;
        public int videopageviews;
        public int duration;
        public int collectioncount;
        private boolean isSelect;

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        public String getExpire_time() {
            return expire_time;
        }

        public void setExpire_time(String expire_time) {
            this.expire_time = expire_time;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
        }

        public int getVideopageviews() {
            return videopageviews;
        }

        public void setVideopageviews(int videopageviews) {
            this.videopageviews = videopageviews;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getCollectioncount() {
            return collectioncount;
        }

        public void setCollectioncount(int collectioncount) {
            this.collectioncount = collectioncount;
        }
    }
}
