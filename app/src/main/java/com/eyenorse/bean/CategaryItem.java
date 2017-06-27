package com.eyenorse.bean;

import java.io.Serializable;

/**
 * Created by zhengkq on 2017/1/11.
 */

public class CategaryItem implements Serializable{
   /* "id": 56,
        "name": "语言文化",
        "image": "http://yanhushi.caifutang.com/uploads/category/1492068544731583.png",
        "prop": "",
        "attrrelateddata": null,
        "parent_category_id": 1,
        "pc_img": null,
        "pcnimg": null,
        "isrecommend": 1,
        "logo": "http://yanhushi.caifutang.com/uploads/category/1492068539132107.png",
        "index_id": 1*/

    private int  id;
    private String name;
    private String image;
    private String logo;
    private String prop;
    private String attrrelateddata;
    private int parent_category_id;
    private String pc_img;
    private String pcnimg;
    private int isrecommend;
    private int index_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public String getAttrrelateddata() {
        return attrrelateddata;
    }

    public void setAttrrelateddata(String attrrelateddata) {
        this.attrrelateddata = attrrelateddata;
    }

    public int getParent_category_id() {
        return parent_category_id;
    }

    public void setParent_category_id(int parent_category_id) {
        this.parent_category_id = parent_category_id;
    }

    public String getPc_img() {
        return pc_img;
    }

    public void setPc_img(String pc_img) {
        this.pc_img = pc_img;
    }

    public String getPcnimg() {
        return pcnimg;
    }

    public void setPcnimg(String pcnimg) {
        this.pcnimg = pcnimg;
    }

    public int getIsrecommend() {
        return isrecommend;
    }

    public void setIsrecommend(int isrecommend) {
        this.isrecommend = isrecommend;
    }

    public int getIndex_id() {
        return index_id;
    }

    public void setIndex_id(int index_id) {
        this.index_id = index_id;
    }
}
