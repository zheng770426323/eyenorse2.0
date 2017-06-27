package com.eyenorse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.VideoUrlClarity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/16.
 */

public class VideoTypeAdapter extends BaseAdapter {
    private Context context;
    List<VideoUrlClarity> mList = new ArrayList<>();
    private LayoutInflater inflater;

    public VideoTypeAdapter(Context context, List<VideoUrlClarity> mList) {
        this.context = context;
        this.mList.addAll(mList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        VideoTypeAdapter.ViewHolder viewHolder;
        if (view==null){
            view =inflater.inflate(R.layout.item_video_type,null);
            viewHolder = new VideoTypeAdapter.ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.textView);
            view.setTag(viewHolder);
        }else {
            viewHolder = (VideoTypeAdapter.ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(mList.get(i).getClarity());
        return view;
    }

    public class ViewHolder{
       private TextView textView;
    }
}
