package com.eyenorse.fragment;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * Created by zhengkq on 2016/12/23.
 */

public class BaseFragment extends Fragment {
    public View view;
    //public boolean isLogin = false;
    //public int memberid = 0;

    public void setContentView(LayoutInflater inflater, ViewGroup container, int layoutResID){
        view = inflater.inflate(layoutResID, container, false);
        ButterKnife.bind(this, view);
    }

}
