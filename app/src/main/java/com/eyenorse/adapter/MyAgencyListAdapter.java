package com.eyenorse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.MyAgencyList;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengkq on 2017/4/6.
 */

public class MyAgencyListAdapter extends BaseAdapter {
    private Context context;
    private List<MyAgencyList.AgencyInfo> list = new ArrayList<>();
    private LayoutInflater inflater;

    public MyAgencyListAdapter(Context context, List<MyAgencyList.AgencyInfo> list) {
        this.context = context;
        this.list.addAll(list);
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyAgencyListAdapter.ViewHolder viewHolder;
        if (view==null){
            view =inflater.inflate(R.layout.item_mylower_level,null);
            viewHolder = new MyAgencyListAdapter.ViewHolder();
            viewHolder.simpleDraweeView_head_icon = (SimpleDraweeView) view.findViewById(R.id.simpleDraweeView_head_icon);
            viewHolder.textView_name = (TextView) view.findViewById(R.id.textView_name);
            viewHolder.rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
            view.setTag(viewHolder);
        }else {
            viewHolder = (MyAgencyListAdapter.ViewHolder) view.getTag();
        }
        viewHolder.rl_item.setBackgroundColor(context.getResources().getColor(R.color.white));
        MyAgencyList.AgencyInfo agencyInfo = list.get(i);
        viewHolder.simpleDraweeView_head_icon.setImageURI(agencyInfo.getPicture());
        viewHolder.textView_name.setText(agencyInfo.getUsername());
        return view;
    }
    public class ViewHolder{
        private SimpleDraweeView simpleDraweeView_head_icon;
        private TextView textView_name;
        private RelativeLayout rl_item;
    }
}
