package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/16.
 */

public class MyInvitationList implements Serializable {
    private int count;
    private List<InvitationInfo>invite;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<InvitationInfo> getInvite() {
        return invite;
    }

    public void setInvite(List<InvitationInfo> invite) {
        this.invite = invite;
    }
}
