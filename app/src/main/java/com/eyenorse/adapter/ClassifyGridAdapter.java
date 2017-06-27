package com.eyenorse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.CategaryItem;
import com.eyenorse.utils.TextUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zhengkq on 2017/1/11.
 */

public class ClassifyGridAdapter extends BaseAdapter {
    private List<CategaryItem> list;
    private Context context;
    LayoutInflater inflater;

    public ClassifyGridAdapter(Context context, List<CategaryItem> list) {
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
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (convertView == null) {
            Fresco.initialize(context);
            convertView = inflater.inflate(R.layout.item_grid_classify, null);
            viewHolder = new ViewHolder();
            viewHolder.image_icon = (ImageView) convertView.findViewById(R.id.image_icon);
            viewHolder.textView_name = (TextView) convertView.findViewById(R.id.textView_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (i == list.size() - 1) {
            viewHolder.image_icon.setImageResource(R.mipmap.all);
            viewHolder.textView_name.setText("全部");
        } else {
            if (!TextUtil.isNull(list.get(i).getImage())) {
                //viewHolder.image_icon.setImageURI(list.get(i).getImage());
                Picasso.with(context)
                        .load(list.get(i).getImage())
                        .into(viewHolder.image_icon);
            }
            viewHolder.textView_name.setText(list.get(i).getName());
        }
        return convertView;
    }

    public class ViewHolder {
        private ImageView image_icon;
        private TextView textView_name;
    }
}