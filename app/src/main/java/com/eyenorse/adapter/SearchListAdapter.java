package com.eyenorse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.VisionTrain.SearchListActivity;
import com.eyenorse.bean.ClassifyVideoList;
import com.eyenorse.utils.DateTimeHelper;
import com.eyenorse.view.MyGridView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by zhengkq on 2017/3/30.
 */

public class SearchListAdapter extends BaseAdapter {
    private Context context;
    private List<ClassifyVideoList.ClassifyVideoInfo> goodslist = new ArrayList<>();
    LayoutInflater inflater;
    public SearchListAdapter(Context context, List<ClassifyVideoList.ClassifyVideoInfo> goodslist) {
        this.context = context;
        this.goodslist.addAll(goodslist);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return goodslist.size();
    }

    @Override
    public Object getItem(int position) {
        return goodslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SearchListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            Fresco.initialize(context);
            convertView = inflater.inflate(R.layout.item_video_list, null);
            viewHolder = new SearchListAdapter.ViewHolder();
            viewHolder.simpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.simpleDraweeView);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_look_count = (TextView) convertView.findViewById(R.id.tv_look_count);
            viewHolder.tv_play_time = (TextView) convertView.findViewById(R.id.tv_play_time);
            viewHolder.tv_good_time = (TextView) convertView.findViewById(R.id.tv_good_time);
            //viewHolder.rl_more = (RelativeLayout) convertView.findViewById(R.id.rl_more);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SearchListAdapter.ViewHolder) convertView.getTag();
        }
        ClassifyVideoList.ClassifyVideoInfo classifyVideoInfo = goodslist.get(position);
        if (classifyVideoInfo.getImages()!=null&&classifyVideoInfo.getImages().size()>0){
            viewHolder.simpleDraweeView.setImageURI(classifyVideoInfo.getImages().get(0));
        }
        viewHolder.tv_title.setText(classifyVideoInfo.getTitle());
        viewHolder.tv_look_count.setText(classifyVideoInfo.getVideopageviews()+"");
        viewHolder.tv_play_time.setText("时长"+DateTimeHelper.getHourMinSS(classifyVideoInfo.getDuration()));
        viewHolder.tv_good_time.setText(classifyVideoInfo.getCollectioncount()+"");
        return convertView;
    }

    public class ViewHolder{
        public SimpleDraweeView simpleDraweeView;
        public TextView tv_title;
        public TextView tv_look_count;
        public TextView tv_play_time;
        public TextView tv_good_time;
        //public RelativeLayout rl_more;
    }
}
