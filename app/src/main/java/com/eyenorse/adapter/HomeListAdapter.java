package com.eyenorse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eyenorse.R;
import com.eyenorse.bean.ClassifyVideoList;
import com.eyenorse.utils.DateTimeHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengkq on 2017/3/31.
 */

public class HomeListAdapter extends BaseAdapter {
    private Context context;
    private List<ClassifyVideoList.ClassifyVideoInfo> allGoods = new ArrayList<>();
    LayoutInflater inflater;
    public HomeListAdapter(Context context, List<ClassifyVideoList.ClassifyVideoInfo> allGoods) {
        this.context = context;
        this.allGoods.clear();
        this.allGoods.addAll(allGoods);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return allGoods.size();
    }

    @Override
    public Object getItem(int position) {
        return allGoods.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HomeListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            Fresco.initialize(context);
            convertView = inflater.inflate(R.layout.item_video_list, null);
            viewHolder = new HomeListAdapter.ViewHolder();
            viewHolder.simpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.simpleDraweeView);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_look_count = (TextView) convertView.findViewById(R.id.tv_look_count);
            viewHolder.tv_play_time = (TextView) convertView.findViewById(R.id.tv_play_time);
            viewHolder.tv_good_time = (TextView) convertView.findViewById(R.id.tv_good_time);
            //viewHolder.rl_more = (RelativeLayout) convertView.findViewById(R.id.rl_more);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (HomeListAdapter.ViewHolder) convertView.getTag();
        }
        ClassifyVideoList.ClassifyVideoInfo classifyVideoInfo = allGoods.get(position);
        if (classifyVideoInfo.getImages()!=null&&classifyVideoInfo.getImages().size()>0){
            viewHolder.simpleDraweeView.setImageURI(classifyVideoInfo.getImages().get(0));
        }
        viewHolder.tv_title.setText(classifyVideoInfo.getTitle());
        viewHolder.tv_look_count.setText(classifyVideoInfo.getVideopageviews()+"");
        viewHolder.tv_good_time.setText(classifyVideoInfo.getCollectioncount()+"");
        int duration = classifyVideoInfo.getDuration();
        String hourDate = DateTimeHelper.getHourMinSS(duration);
        viewHolder.tv_play_time.setText("时长"+hourDate);
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
