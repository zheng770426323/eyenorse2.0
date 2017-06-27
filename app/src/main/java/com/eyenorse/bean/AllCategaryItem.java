package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhengkq on 2017/4/13.
 */

public class AllCategaryItem {
   /* "category": [
    {
        "categoryid": 1,
            "name": "视频",
            "logo": "http://yanhushi.caifutang.com/uploads/category/1492068275981382.jpg",
            "image": "http://yanhushi.caifutang.com/uploads/category/1492068278101916.jpg",
            "parentcategoryid": 0,
            "subsetcategory": [
        {
            "categoryid": 56,
                "name": "语言文化",
                "logo": "http://yanhushi.caifutang.com/uploads/category/1492068539132107.png",
                "image": "http://yanhushi.caifutang.com/uploads/category/1492068544731583.png",
                "parentcategoryid": 1,
                "subsetcategory": null
        }
        ]
    }
    ]*/

    private List<Categary> category;

    public List<Categary> getCategory() {
        return category;
    }

    public void setCategory(List<Categary> category) {
        this.category = category;
    }

    public static class Categary implements Serializable{
        public int categoryid;
        public String name;
        public String logo;
        public String image;
        public int parentcategoryid;
        public List<Categary> subsetcategory;

        public int getCategoryid() {
            return categoryid;
        }

        public void setCategoryid(int categoryid) {
            this.categoryid = categoryid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getParentcategoryid() {
            return parentcategoryid;
        }

        public void setParentcategoryid(int parentcategoryid) {
            this.parentcategoryid = parentcategoryid;
        }

        public List<Categary> getSubsetcategory() {
            return subsetcategory;
        }

        public void setSubsetcategory(List<Categary> subsetcategory) {
            this.subsetcategory = subsetcategory;
        }
    }
}
