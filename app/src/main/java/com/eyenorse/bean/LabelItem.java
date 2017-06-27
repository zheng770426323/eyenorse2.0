package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/4/14.
 */

public class LabelItem {
   /* "data": [
    {
        "0": 4,
            "1": "少年",
            "2": "uploads/labels/1492068039122489.jpg",
            "3": 1,
            "4": "2017-04-13 15:20:39",
            "5": "2017-04-13 15:20:39",
            "id": 4,
            "name": "少年",
            "img": "http://yanhushi.caifutang.com/uploads/labels/1492068039122489.jpg",
            "isrecommend": 1,
            "cre_time": "2017-04-13 15:20:39",
            "mod_time": "2017-04-13 15:20:39"
    },
    ]*/
    private List<Label> data;

    public List<Label> getData() {
        return data;
    }

    public void setData(List<Label> data) {
        this.data = data;
    }

    public static class Label implements Serializable{
        public int id;
        public String name;
        public String img;
        public int isrecommend;
        public String cre_time;
        public String mod_time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getIsrecommend() {
            return isrecommend;
        }

        public void setIsrecommend(int isrecommend) {
            this.isrecommend = isrecommend;
        }

        public String getCre_time() {
            return cre_time;
        }

        public void setCre_time(String cre_time) {
            this.cre_time = cre_time;
        }

        public String getMod_time() {
            return mod_time;
        }

        public void setMod_time(String mod_time) {
            this.mod_time = mod_time;
        }
    }
}
