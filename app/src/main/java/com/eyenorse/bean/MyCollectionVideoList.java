package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/11.
 */

public class MyCollectionVideoList implements Serializable {
    private List<MyCollectionVideo>collection;
    private int count;

    public List<MyCollectionVideo> getCollection() {
        return collection;
    }

    public void setCollection(List<MyCollectionVideo> collection) {
        this.collection = collection;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public static class MyCollectionVideo{
        /*"collectionid": 10,
                "goodsid": 1,
                "shopid": 5,
                "shopname": null,
                "shopcity": "",
                "datetime": "2017-03-23 10:06:21",
                "goodstitle": "海贼王第一集",
                "goodsimages": [
                "http://yanhushi.caifutang.com/uploads/goods/5/s_1488613865490376.jpg",
                "http://yanhushi.caifutang.com/uploads/goods/5/s_1488613866283184.jpg",
                "http://yanhushi.caifutang.com/uploads/goods/5/s_1488613868715640.jpg",
                "http://yanhushi.caifutang.com/uploads/goods/5/s_1488613870522555.jpg",
                "http://yanhushi.caifutang.com/uploads/goods/5/s_1488613871437810.jpg"
                ],
                "goodsmaxprice": "100.00",
                "goodsminprice": "1.00",
                "goodscollectioncount": 7,
                "goodssalecount": 3,
                "videopageviews": 11*/
        public int collectionid;
        public int goodsid;
        public int shopid;
        public String shopname;
        public String shopcity;
        public String  goodstitle;
        public List<String> goodsimages;
        public String goodsmaxprice;
        public String goodsminprice;
        public int goodscollectioncount;
        public int goodssalecount;
        public String datetime;
        public int videopageviews;

        public int getCollectionid() {
            return collectionid;
        }

        public void setCollectionid(int collectionid) {
            this.collectionid = collectionid;
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
    }

}
