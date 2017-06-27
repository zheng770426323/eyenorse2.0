package com.eyenorse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.ClassifyVideoList;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by zhengkq on 2017/3/30.
 */

public class HomeRecommendAdapter extends RecyclerView.Adapter<HomeRecommendAdapter.ViewHolder> implements View.OnClickListener{
    private Context context;
    private List<ClassifyVideoList.ClassifyVideoInfo> list;
    LayoutInflater inflater;
    private HomeRecommendAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;
    private ViewHolder viewHolder;

    public HomeRecommendAdapter(Context context, List<ClassifyVideoList.ClassifyVideoInfo>list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public HomeRecommendAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_video_list_little,parent, false);
        viewHolder = new ViewHolder(view);
        viewHolder.simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.simpleDraweeView);
        viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
        viewHolder.tv_look_count = (TextView) view.findViewById(R.id.tv_look_count);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HomeRecommendAdapter.ViewHolder holder, int position) {
        ClassifyVideoList.ClassifyVideoInfo classifyVideoInfo = list.get(position);
        if (classifyVideoInfo.getImages().size()>0){
            holder.simpleDraweeView.setImageURI(classifyVideoInfo.getImages().get(0));
        }
        holder.tv_title.setText(classifyVideoInfo.getTitle());
        holder.tv_look_count.setText(classifyVideoInfo.getVideopageviews()+"次");
        holder.itemView.setTag(classifyVideoInfo);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , ClassifyVideoList.ClassifyVideoInfo data);
    }
    public void setOnItemClickListener(HomeRecommendAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(ClassifyVideoList.ClassifyVideoInfo)v.getTag());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View view)
        {
            super(view);
        }

        SimpleDraweeView simpleDraweeView;
        TextView tv_title;
        TextView tv_look_count;
    }
}
