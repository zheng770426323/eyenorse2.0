package com.eyenorse.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zheng on 2017/1/20.
 */

public class MyselfCheckData implements Serializable {
    private String beforelefteye;
    private String beforerighteye;
    private String beforedatetime;
    private List<AfterCheckData>detectionrecords;

    public String getBeforedatetime() {
        return beforedatetime;
    }

    public void setBeforedatetime(String beforedatetime) {
        this.beforedatetime = beforedatetime;
    }

    public static class AfterCheckData{
        private String lefteye;
        private String righteye;
        private String datetime;

        public String getLefteye() {
            return lefteye;
        }

        public void setLefteye(String lefteye) {
            this.lefteye = lefteye;
        }

        public String getRighteye() {
            return righteye;
        }

        public void setRighteye(String righteye) {
            this.righteye = righteye;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }
    }

    public String getBeforelefteye() {
        return beforelefteye;
    }

    public void setBeforelefteye(String beforelefteye) {
        this.beforelefteye = beforelefteye;
    }

    public String getBeforerighteye() {
        return beforerighteye;
    }

    public void setBeforerighteye(String beforerighteye) {
        this.beforerighteye = beforerighteye;
    }

    public List<AfterCheckData> getDetectionrecords() {
        return detectionrecords;
    }

    public void setDetectionrecords(List<AfterCheckData> detectionrecords) {
        this.detectionrecords = detectionrecords;
    }
}
