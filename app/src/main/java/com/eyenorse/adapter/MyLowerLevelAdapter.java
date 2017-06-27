package com.eyenorse.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.InvitationInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengkq on 2016/12/27.
 */

public class MyLowerLevelAdapter extends BaseAdapter {
    private Context context;
    private List<InvitationInfo> list = new ArrayList<>();
    private LayoutInflater inflater;

    public MyLowerLevelAdapter(Context context, List<InvitationInfo> list) {
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
        MyLowerLevelAdapter.ViewHolder viewHolder;
        if (view==null){
            view =inflater.inflate(R.layout.item_mylower_level,null);
            viewHolder = new MyLowerLevelAdapter.ViewHolder();
            viewHolder.simpleDraweeView_head_icon = (SimpleDraweeView) view.findViewById(R.id.simpleDraweeView_head_icon);
            viewHolder.textView_name = (TextView) view.findViewById(R.id.textView_name);
            viewHolder.rl_item = (RelativeLayout) view.findViewById(R.id.rl_item);
            view.setTag(viewHolder);
        }else {
            viewHolder = (MyLowerLevelAdapter.ViewHolder) view.getTag();
        }
        if (i%2==0){
            viewHolder.rl_item.setBackgroundColor(context.getResources().getColor(R.color.white));
        }else {
            viewHolder.rl_item.setBackgroundColor(context.getResources().getColor(R.color.colorLittleBlue));
        }
        InvitationInfo invitationInfo = list.get(i);
        viewHolder.simpleDraweeView_head_icon.setImageURI(invitationInfo.getHeadimage());
        viewHolder.textView_name.setText(invitationInfo.getUsername());
        return view;
    }
    public class ViewHolder{
        private SimpleDraweeView simpleDraweeView_head_icon;
        private TextView textView_name;
        private RelativeLayout rl_item;
    }
}
