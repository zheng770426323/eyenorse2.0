package com.eyenorse.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengkq on 2017/1/12.
 */

public class VideoAllInfos implements Serializable {
    /*"iscollection": false,
            "iscollectionshop": false,
            "expire_time": "0000-00-00 00:00:00",
            "duration": 3661,
            "isfree": 1,
            "videopageviews": 23,
            "videourls": [
    {
        "clarity": "标清"
    },
    {
        "clarity": "高清"
    },
    {
        "clarity": "超清"
    }
    ],
            "goodsid": 1,
            "title": "海贼王第一集",
            "shorttitle": null,
            "summary": "是一部连载中的日本少年漫画作品",
            "origin": null,
            "images": [
            "http://yanhushi.caifutang.com/uploads/goods/5/1488613865490376.jpg",
            "http://yanhushi.caifutang.com/uploads/goods/5/1488613866283184.jpg",
            "http://yanhushi.caifutang.com/uploads/goods/5/1488613868715640.jpg",
            "http://yanhushi.caifutang.com/uploads/goods/5/1488613870522555.jpg",
            "http://yanhushi.caifutang.com/uploads/goods/5/1488613871437810.jpg"
            ],
            "content": "http://yanhushi.caifutang.com/api/goods/content?goodsid=1",
            "prop": {
        "购买时间": [
        "1",
                "5",
                "10",
                "30",
                "50",
                "60",
                "100"
        ]
    },
            "propkey": [
    {
        "name": "购买时间",
            "data": [
        "1",
                "5",
                "10",
                "30",
                "50",
                "60",
                "100"
        ]
    }
    ],
            "portattr": {
        "1": {
            "oprice": "1",
                    "price": "1",
                    "stock": 18
        },
        "5": {
            "oprice": "5",
                    "price": "5",
                    "stock": 17
        },
        "10": {
            "oprice": "10",
                    "price": "10",
                    "stock": "20"
        },
        "30": {
            "oprice": "30",
                    "price": "30",
                    "stock": 14
        },
        "50": {
            "oprice": "50",
                    "price": "50",
                    "stock": "20"
        },
        "60": {
            "oprice": "60",
                    "price": "60",
                    "stock": "20"
        },
        "100": {
            "oprice": "100",
                    "price": "100",
                    "stock": 19
        }
    },
            "attrrelateddata": null,
            "maxprice": "100.00",
            "minprice": "1.00",
            "collectioncount": 7,
            "salecount": 3,
            "stock": 148,
            "status": 1,
            "categoryid": 1,
            "shopid": 5,
            "shopname": null,
            "shopintro": null,
            "shoplogo": "",
            "shopmain": null,
            "shopfounder": null,
            "shopfoundingtime": null,
            "deliveryplace": "",
            "sendcost": 0,
            "evaluatecount": 0,
            "evaluateprop": null,
            "evaluateport": null,
            "evaluatecontent": null,
            "evaluatedatetime": null,
            "evaluateusername": null,
            "evaluateheadimg": "http://yanhushi.caifutang.com//lib/avata.jpg"

            "label": [
            {
              "id": 4,
               "name": "少年"
            }
    ]*/

    private boolean iscollection;
    private boolean iscollectionshop;
    private String expire_time;
    private int duration;
    private int isfree;
    private int videopageviews;//观看数量
    private List<VideoUrlClarity> videourls;
    private int goodsid;
    private String title;
    private String shorttitle;
    private String summary;
    private String origin;
    private List<String> images;
    private String content;
    public Map<String, List<String>> prop;
    public Map<String, Map<String, String>> portlist;
    public Map<String, Map<String, String>> portattr;
    public List<PropKey> propkey;
    private String attrrelateddata;
    private String maxprice;
    private String minprice;
    private int collectioncount;
    private int salecount;
    private int stock;
    private int status;
    private int categoryid;
    private int shopid;
    private String shopname;
    private String shopintro;
    private String shoplogo;
    private String shopmain;
    private String shopfounder;
    private String shopfoundingtime;
    private String deliveryplace;
    private int sendcost;
    private int evaluatecount;
    private String evaluateprop;
    private String evaluateport;
    private String evaluatecontent;
    private String evaluatedatetime;
    private String evaluateusername;
    private String evaluateheadimg;

    private List<LabelItem.Label> label;

    public List<LabelItem.Label> getLabel() {
        return label;
    }

    public void setLabel(List<LabelItem.Label> label) {
        this.label = label;
    }

    public int getVideopageviews() {
        return videopageviews;
    }

    public void setVideopageviews(int videopageviews) {
        this.videopageviews = videopageviews;
    }

    public Map<String, List<String>> getProp() {
        return prop;
    }

    public void setProp(Map<String, List<String>> prop) {
        this.prop = prop;
    }

    public static class PropKey implements Serializable {
        public String name;
        public List<String> data = new ArrayList<String>();

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<String> getData() {
            return data;
        }

        public void setData(List<String> data) {
            this.data = data;
        }
    }

    public Map<String, Map<String, String>> getPortlist() {
        return portlist;
    }

    public void setPortlist(Map<String, Map<String, String>> portlist) {
        this.portlist = portlist;
    }

    public Map<String, Map<String, String>> getPortattr() {
        return portattr;
    }

    public void setPortattr(Map<String, Map<String, String>> portattr) {
        this.portattr = portattr;
    }

    public List<PropKey> getPropkey() {
        return propkey;
    }

    public void setPropkey(List<PropKey> propkey) {
        this.propkey = propkey;
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

    public boolean iscollection() {
        return iscollection;
    }

    public void setIscollection(boolean iscollection) {
        this.iscollection = iscollection;
    }

    public boolean iscollectionshop() {
        return iscollectionshop;
    }

    public void setIscollectionshop(boolean iscollectionshop) {
        this.iscollectionshop = iscollectionshop;
    }

    public String getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(String expire_time) {
        this.expire_time = expire_time;
    }

    public int getIsfree() {
        return isfree;
    }

    public void setIsfree(int isfree) {
        this.isfree = isfree;
    }

    public List<VideoUrlClarity> getVideourls() {
        return videourls;
    }

    public void setVideourls(List<VideoUrlClarity> videourls) {
        this.videourls = videourls;
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

    public String getShorttitle() {
        return shorttitle;
    }

    public void setShorttitle(String shorttitle) {
        this.shorttitle = shorttitle;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAttrrelateddata() {
        return attrrelateddata;
    }

    public void setAttrrelateddata(String attrrelateddata) {
        this.attrrelateddata = attrrelateddata;
    }

    public int getSalecount() {
        return salecount;
    }

    public void setSalecount(int salecount) {
        this.salecount = salecount;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
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

    public String getShopintro() {
        return shopintro;
    }

    public void setShopintro(String shopintro) {
        this.shopintro = shopintro;
    }

    public String getShoplogo() {
        return shoplogo;
    }

    public void setShoplogo(String shoplogo) {
        this.shoplogo = shoplogo;
    }

    public String getShopmain() {
        return shopmain;
    }

    public void setShopmain(String shopmain) {
        this.shopmain = shopmain;
    }

    public String getShopfounder() {
        return shopfounder;
    }

    public void setShopfounder(String shopfounder) {
        this.shopfounder = shopfounder;
    }

    public String getShopfoundingtime() {
        return shopfoundingtime;
    }

    public void setShopfoundingtime(String shopfoundingtime) {
        this.shopfoundingtime = shopfoundingtime;
    }

    public String getDeliveryplace() {
        return deliveryplace;
    }

    public void setDeliveryplace(String deliveryplace) {
        this.deliveryplace = deliveryplace;
    }

    public int getSendcost() {
        return sendcost;
    }

    public void setSendcost(int sendcost) {
        this.sendcost = sendcost;
    }

    public int getEvaluatecount() {
        return evaluatecount;
    }

    public void setEvaluatecount(int evaluatecount) {
        this.evaluatecount = evaluatecount;
    }

    public String getEvaluateprop() {
        return evaluateprop;
    }

    public void setEvaluateprop(String evaluateprop) {
        this.evaluateprop = evaluateprop;
    }

    public String getEvaluateport() {
        return evaluateport;
    }

    public void setEvaluateport(String evaluateport) {
        this.evaluateport = evaluateport;
    }

    public String getEvaluatecontent() {
        return evaluatecontent;
    }

    public void setEvaluatecontent(String evaluatecontent) {
        this.evaluatecontent = evaluatecontent;
    }

    public String getEvaluatedatetime() {
        return evaluatedatetime;
    }

    public void setEvaluatedatetime(String evaluatedatetime) {
        this.evaluatedatetime = evaluatedatetime;
    }

    public String getEvaluateusername() {
        return evaluateusername;
    }

    public void setEvaluateusername(String evaluateusername) {
        this.evaluateusername = evaluateusername;
    }

    public String getEvaluateheadimg() {
        return evaluateheadimg;
    }

    public void setEvaluateheadimg(String evaluateheadimg) {
        this.evaluateheadimg = evaluateheadimg;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
