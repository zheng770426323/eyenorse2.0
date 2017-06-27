package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/16.
 */

public class FootMarkInfo implements Serializable {
   /* "footmarkid": 2145,
        "goodsid": 37,
        "shopid": 0,
        "shopname": null,
        "shopcity": "",
        "datetime": "2017-04-18 12:06:25",
        "goodstitle": "火精灵高级",
        "goodsimages": [
          "http://yanhushi.caifutang.com/uploads/video/s_1490943357349977.png"
        ],
        "goodsmaxprice": "5.00",
        "goodsminprice": "5.00",
        "goodscollectioncount": 8,
        "goodssalecount": 2,
        "videopageviews": 303*/
    private int footmarkid;
    private int goodsid;
    private int shopid;
    private String shopname;
    private String shopcity;
    private String datetime;
    private String goodstitle;
    private List<String> goodsimages;
    private String goodsmaxprice;
    private String goodsminprice;
    private int goodscollectioncount;
    private int goodssalecount;
    private int videopageviews;

    public int getFootmarkid() {
        return footmarkid;
    }

    public void setFootmarkid(int footmarkid) {
        this.footmarkid = footmarkid;
    }

    public int getGoodsid() {
        return goodsid;
    }

    public void setGoodsid(int goodsid) {
        this.goodsid = goodsid;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getShopcity() {
        return shopcity;
    }

    public void setShopcity(String shopcity) {
        this.shopcity = shopcity;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getGoodstitle() {
        return goodstitle;
    }

    public void setGoodstitle(String goodstitle) {
        this.goodstitle = goodstitle;
    }

    public List<String> getGoodsimages() {
        return goodsimages;
    }

    public void setGoodsimages(List<String> goodsimages) {
        this.goodsimages = goodsimages;
    }

    public String getGoodsmaxprice() {
        return goodsmaxprice;
    }

    public void setGoodsmaxprice(String goodsmaxprice) {
        this.goodsmaxprice = goodsmaxprice;
    }

    public String getGoodsminprice() {
        return goodsminprice;
    }

    public void setGoodsminprice(String goodsminprice) {
        this.goodsminprice = goodsminprice;
    }

    public int getGoodscollectioncount() {
        return goodscollectioncount;
    }

    public void setGoodscollectioncount(int goodscollectioncount) {
        this.goodscollectioncount = goodscollectioncount;
    }

    public int getGoodssalecount() {
        return goodssalecount;
    }

    public void setGoodssalecount(int goodssalecount) {
        this.goodssalecount = goodssalecount;
    }

    public int getVideopageviews() {
        return videopageviews;
    }

    public void setVideopageviews(int videopageviews) {
        this.videopageviews = videopageviews;
    }
}
