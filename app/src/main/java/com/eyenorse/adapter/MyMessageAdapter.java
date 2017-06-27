package com.eyenorse.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.PushMessageList;
import com.eyenorse.utils.DateTimeHelper;
import com.eyenorse.utils.TextUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/13.
 */

public class MyMessageAdapter extends BaseAdapter {
    private Context context;
    List<PushMessageList.PushMessageItem> myList = new ArrayList<>();
    private LayoutInflater inflater;

    public MyMessageAdapter(Context context, List<PushMessageList.PushMessageItem> myList) {
        this.context = context;
        this.myList.addAll(myList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Object getItem(int i) {
        return myList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyMessageAdapter.ViewHolder viewHolder;
        if (view==null){
            Fresco.initialize(context);
            view =inflater.inflate(R.layout.item_my_message,null);
            viewHolder = new MyMessageAdapter.ViewHolder();
            viewHolder.simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.simpleDraweeView);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(viewHolder);
        }else {
            viewHolder = (MyMessageAdapter.ViewHolder) view.getTag();
        }
        PushMessageList.PushMessageItem myMessageItem = myList.get(i);
        viewHolder.tv_title.setText(myMessageItem.getTitle());
        if (!myMessageItem.getImg().equals("")|| !TextUtil.isNull(myMessageItem.getImg())){
            viewHolder.simpleDraweeView.setImageURI(myMessageItem.getImg());
            viewHolder.simpleDraweeView.setVisibility(View.VISIBLE);
        }else {
            viewHolder.simpleDraweeView.setVisibility(View.GONE);
        }
        viewHolder.tv_content.setText(myMessageItem.getContent());
        String time = DateTimeHelper.toSystemFriendlyTime3(DateTimeHelper.getDate(myMessageItem.getDatetime()));
        viewHolder.tv_time.setText(time.substring(5,time.length()));
        return view;
    }

    public class ViewHolder{
        private SimpleDraweeView simpleDraweeView;
        private TextView tv_title;
        private TextView tv_content;
        private TextView tv_time;
    }
}
