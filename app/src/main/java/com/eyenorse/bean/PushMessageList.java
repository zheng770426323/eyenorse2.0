package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/13.
 */

public class PushMessageList implements Serializable {
    /*"data": {
        "count": 2,
                "push": [
        {
                "0": 3,
                "1": "aaa",
                "2": "cccc",
                "3": "2017-04-14 16:09:37",
                "4": "",
                "5": 1,
                "6": 0,
                "id": 3,
                "title": "aaa",
                "content": "cccc",
                "datetime": "2017-04-14 16:09:37",
                "img": "",
                "type": 1,
                "type_id": 0,
                "pushid": 3
        }
        ]
    }*/
    private List<PushMessageItem>push;
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<PushMessageItem> getPush() {
        return push;
    }
    public void setPush(List<PushMessageItem> push) {
        this.push = push;
    }

    public static class PushMessageItem implements Serializable {
        public int id;
        public int pushid;
        public String title;
        public String datetime;
        public String img;
        public int type;
        public int type_id;
        public String content;

        public int getPushid() {
            return pushid;
        }

        public void setPushid(int pushid) {
            this.pushid = pushid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
