package com.eyenorse.bean;

import java.io.Serializable;

/**
 * Created by zhengkq on 2017/1/7.
 */

public class PersonInfoReset implements Serializable {
    private String content;
    private int id;

    public PersonInfoReset(String content, int id) {
        this.content = content;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
