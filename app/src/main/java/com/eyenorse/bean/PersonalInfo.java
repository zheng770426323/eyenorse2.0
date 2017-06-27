package com.eyenorse.bean;

import java.io.Serializable;

/**
 * Created by zhengkq on 2017/1/10.
 */

public class PersonalInfo implements Serializable {
    private String name;
    private String mobile;
    private String email;
    private String headimage;
    private int isactivation;
    private boolean isvip;
    private String frequency;
    private String validity;
    private int isagent;

    public boolean isvip() {
        return isvip;
    }

    public void setIsvip(boolean isvip) {
        this.isvip = isvip;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getValidity() {
        return validity;
    }

    public void setValidity(String validity) {
        this.validity = validity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public int getIsactivation() {
        return isactivation;
    }

    public void setIsactivation(int isactivation) {
        this.isactivation = isactivation;
    }

    public int getIsagent() {
        return isagent;
    }

    public void setIsagent(int isagent) {
        this.isagent = isagent;
    }
}
