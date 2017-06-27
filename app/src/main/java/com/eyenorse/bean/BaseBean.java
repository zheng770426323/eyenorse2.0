package com.eyenorse.bean;

import java.io.Serializable;

/**
 * Created by zhengkq on 2017/3/20.
 */

public class BaseBean<T> implements Serializable {
    private T data;
    private String error;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
