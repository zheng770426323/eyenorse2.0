package com.eyenorse.bean;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/11.
 */

public class ClassifyVideoList{
    private int count;
    private List<ClassifyVideoInfo> goods;


    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<ClassifyVideoInfo> getGoods() {
        return goods;
    }

    public void setGoods(List<ClassifyVideoInfo> goods) {
        this.goods = goods;
    }

    public static class ClassifyVideoInfo implements Serializable {
        /* "shopid": 0,
        "shopname": null,
        "shopcity": "",
        "goodsid": 37,
        "title": "火精灵高级",
        "summary": "《五行增视系列》是一部兼具治疗和中国金木水火土理念的VR动画短片，青少年在观看动画的同时还能观赏到丰富的色彩、精美的画面。本片通过金、木、水、火、土五个具有中国传统文化的小精灵，带领大家进行视力训练，提升视力。",
        "images": [
          "http://yanhushi.caifutang.com/uploads/video/s_1490943357349977.png"
        ],
        "maxprice": "5.00",
        "minprice": "5.00",
        "collectioncount": 0,
        "salecount": 0,
        "datetime": "2017-03-31 14:56:10",
        "videopageviews": 224,
        "isfree": 0,
        "duration": 3661*/
        public int shopid;
        public String shopname;
        public String shopcity;
        public int goodsid;
        public String title;
        public String summary;
        public List<String> images;
        public String maxprice;
        public String minprice;
        public int collectioncount;
        public int salecount;
        public String datetime;
        public int videopageviews;
        public int isfree;
        public int duration;

        public int getShopid() {
            return shopid;
        }

        public void setShopid(int shopid) {
            this.shopid = shopid;
        }

        public String getShopcity() {
            return shopcity;
        }

        public void setShopcity(String shopcity) {
            this.shopcity = shopcity;
        }

        public int getGoodsid() {
            return goodsid;
        }

        public void setGoodsid(int goodsid) {
            this.goodsid = goodsid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public String getMaxprice() {
            return maxprice;
        }

        public void setMaxprice(String maxprice) {
            this.maxprice = maxprice;
        }

        public String getMinprice() {
            return minprice;
        }

        public void setMinprice(String minprice) {
            this.minprice = minprice;
        }

        public int getCollectioncount() {
            return collectioncount;
        }

        public void setCollectioncount(int collectioncount) {
            this.collectioncount = collectioncount;
        }

        public int getSalecount() {
            return salecount;
        }

        public void setSalecount(int salecount) {
            this.salecount = salecount;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public int getVideopageviews() {
            return videopageviews;
        }

        public void setVideopageviews(int videopageviews) {
            this.videopageviews = videopageviews;
        }

        public int getIsfree() {
            return isfree;
        }

        public void setIsfree(int isfree) {
            this.isfree = isfree;
        }

        public String getShopname() {
            return shopname;
        }

        public void setShopname(String shopname) {
            this.shopname = shopname;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }
    }
}
