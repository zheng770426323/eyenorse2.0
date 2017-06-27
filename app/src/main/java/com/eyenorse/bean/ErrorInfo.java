package com.eyenorse.bean;

/**
 * Created by zhengkq on 2017/3/17.
 */

public class ErrorInfo {
    private String error;
    private String status;
    private String logintime;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }
}
