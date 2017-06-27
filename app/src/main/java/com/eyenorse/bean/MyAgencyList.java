package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/4/6.
 */

public class MyAgencyList {
    private List<AgencyInfo> data;

    public List<AgencyInfo> getData() {
        return data;
    }

    public void setData(List<AgencyInfo> data) {
        this.data = data;
    }

    public static class AgencyInfo implements Serializable{
        public String realname;
        public int id;
        public String username;
        public String picture;

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPicture() {
            return picture;
        }

        public void setPicture(String picture) {
            this.picture = picture;
        }
    }
}
