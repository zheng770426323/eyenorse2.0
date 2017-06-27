package com.eyenorse.bean;

import java.io.Serializable;

/**
 * Created by zhengkq on 2017/1/17.
 */

public class WXpayforInfo implements Serializable {
    private String productname;
    private double totalamount;

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public double getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(double totalamount) {
        this.totalamount = totalamount;
    }
}
