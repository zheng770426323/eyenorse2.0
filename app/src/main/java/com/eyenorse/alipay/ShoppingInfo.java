package com.eyenorse.alipay;

/**
 * Created by zhengkq on 2017/3/8.
 */

public class ShoppingInfo {
    private String name;
    private String intro;
    private String count;
    private int memberId;
    private String token;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getCount() {
        return count;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
