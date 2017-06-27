package com.eyenorse.bean;

import android.widget.LinearLayout;

/**
 * Created by zhengkq on 2017/4/6.
 */

public class NumberStatus {
    public int id;
    public boolean isSelect;
    public boolean isEdit;
    public String number;
    public LinearLayout linearLayout;

    public LinearLayout getLinearLayout() {
        return linearLayout;
    }

    public void setLinearLayout(LinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
