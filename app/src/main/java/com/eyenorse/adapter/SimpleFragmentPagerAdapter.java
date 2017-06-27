package com.eyenorse.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.eyenorse.bean.AllCategaryItem;
import com.eyenorse.fragment.HomeRecFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengkq on 2017/3/31.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<AllCategaryItem.Categary> list = new ArrayList<>();

    public SimpleFragmentPagerAdapter(FragmentManager fm, Context context, List<AllCategaryItem.Categary> list) {
        super(fm);
        this.context = context;
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return HomeRecFragment.newInstance(list.get(position));
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();
    }
}
