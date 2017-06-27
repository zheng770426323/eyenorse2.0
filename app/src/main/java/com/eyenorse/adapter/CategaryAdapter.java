package com.eyenorse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.AllCategaryItem;
import com.eyenorse.bean.CategaryItem;
import com.eyenorse.utils.TextUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zhengkq on 2017/4/13.
 */

public class CategaryAdapter extends BaseAdapter {
    List<AllCategaryItem.Categary> list;
    private Context context;
    LayoutInflater inflater;

    public CategaryAdapter(Context context, List<AllCategaryItem.Categary> list) {
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
        CategaryAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            Fresco.initialize(context);
            convertView = inflater.inflate(R.layout.item_grid_classify, null);
            viewHolder = new CategaryAdapter.ViewHolder();
            viewHolder.image_icon = (ImageView) convertView.findViewById(R.id.image_icon);
            viewHolder.textView_name = (TextView) convertView.findViewById(R.id.textView_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CategaryAdapter.ViewHolder) convertView.getTag();
        }

        if (!TextUtil.isNull(list.get(i).getImage())) {
            //viewHolder.image_icon.setImageURI(list.get(i).getImage());
            Picasso.with(context)
                    .load(list.get(i).getImage())
                    .into(viewHolder.image_icon);
        }
        viewHolder.textView_name.setText(list.get(i).getName());

        return convertView;
    }

    public class ViewHolder {
        private ImageView image_icon;
        private TextView textView_name;
    }
}
