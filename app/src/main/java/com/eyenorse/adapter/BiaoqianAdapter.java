package com.eyenorse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.ClassifyVideoList;
import com.eyenorse.bean.LabelItem;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zhengkq on 2017/4/12.
 */

public class BiaoqianAdapter extends BaseAdapter{
    private List<LabelItem.Label> list;
    private Context context;
    LayoutInflater inflater;

    public BiaoqianAdapter(Context context,List<LabelItem.Label> list) {
        this.list = list;
        this.context = context;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        BiaoqianAdapter.ViewHolder viewHolder;
        if (convertView==null){
            Fresco.initialize(context);
            convertView =inflater.inflate(R.layout.item_home_biaoqian,null);
            viewHolder = new BiaoqianAdapter.ViewHolder();
            viewHolder.simpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.simpleDraweeView);
            viewHolder.simpleDraweeView_bg = (SimpleDraweeView) convertView.findViewById(R.id.simpleDraweeView_bg);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (BiaoqianAdapter.ViewHolder) convertView.getTag();
        }
        LabelItem.Label label = list.get(i);
        viewHolder.simpleDraweeView.setImageURI(label.getImg());
        viewHolder.tv_title.setText(label.getName());
        return convertView;
    }

    public class ViewHolder{
        private SimpleDraweeView simpleDraweeView;
        private SimpleDraweeView simpleDraweeView_bg;
        private TextView tv_title;
    }
}
