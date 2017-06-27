package com.eyenorse.bean;

import java.io.Serializable;

/**
 * Created by zhengkq on 2017/1/18.
 */

public class LoginInfo implements Serializable {
    private int memberid;
    private boolean isload;
    private boolean isVip;//用于支付宝微信支付成功播放的参数
    private String token;

    public int getMemberid() {
        return memberid;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public boolean isload() {
        return isload;
    }

    public void setIsload(boolean isload) {
        this.isload = isload;
    }

    public boolean isVip() {
        return isVip;
    }

    public void setVip(boolean vip) {
        isVip = vip;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
