package com.eyenorse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.ClassifyVideoList;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zheng on 2017/4/11.
 */

public class FeaturesAdapter extends BaseAdapter {
    private Context context;
    List<ClassifyVideoList.ClassifyVideoInfo> myList = new ArrayList<>();
    private LayoutInflater inflater;

    public FeaturesAdapter(Context context, List<ClassifyVideoList.ClassifyVideoInfo> myList) {
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
        FeaturesAdapter.ViewHolder viewHolder;
        if (view==null){
            Fresco.initialize(context);
            view =inflater.inflate(R.layout.item_features,null);
            viewHolder = new FeaturesAdapter.ViewHolder();
            viewHolder.simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.simpleDraweeView);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
            view.setTag(viewHolder);
        }else {
            viewHolder = (FeaturesAdapter.ViewHolder) view.getTag();
        }
        ClassifyVideoList.ClassifyVideoInfo classifyVideoInfo = myList.get(i);
        if (classifyVideoInfo.getImages()!=null){
            if (classifyVideoInfo.getImages().size()>0){
                viewHolder.simpleDraweeView.setImageURI(classifyVideoInfo.getImages().get(0));
            }
        }
        viewHolder.tv_title.setText(classifyVideoInfo.getTitle());
        return view;
    }

    public class ViewHolder{
        private SimpleDraweeView simpleDraweeView;
        private TextView tv_title;
    }
}
