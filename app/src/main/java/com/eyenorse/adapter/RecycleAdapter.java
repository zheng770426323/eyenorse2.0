package com.eyenorse.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.FootMarkInfo;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by zhengkq on 2017/3/30.
 */

public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<FootMarkInfo> list;
    LayoutInflater inflater;
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public RecycleAdapter(Context context, List<FootMarkInfo> list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_video_list_little,parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.simpleDraweeView);
        viewHolder.tv_title = (TextView) view.findViewById(R.id.tv_title);
        viewHolder.tv_look_count = (TextView) view.findViewById(R.id.tv_look_count);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleAdapter.ViewHolder holder, int position) {
        FootMarkInfo footMarkInfo = list.get(position);
        if (footMarkInfo.getGoodsimages().size()>0){
            holder.simpleDraweeView.setImageURI(footMarkInfo.getGoodsimages().get(0));
        }
        holder.tv_title.setText(footMarkInfo.getGoodstitle());
        holder.tv_look_count.setText(footMarkInfo.getVideopageviews()+"");
        holder.itemView.setTag(footMarkInfo);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , FootMarkInfo data);
    }
    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(FootMarkInfo)v.getTag());
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
