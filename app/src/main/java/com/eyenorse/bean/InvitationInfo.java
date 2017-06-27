package com.eyenorse.bean;

import java.io.Serializable;

/**
 * Created by zhengkq on 2017/1/16.
 */

public class InvitationInfo implements Serializable {
    private int memberid;
    private String headimage;
    private String username;

    public int getMemberid() {
        return memberid;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public String getHeadimage() {
        return headimage;
    }

    public void setHeadimage(String headimage) {
        this.headimage = headimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
