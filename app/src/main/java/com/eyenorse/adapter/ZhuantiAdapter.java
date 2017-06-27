package com.eyenorse.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.eyenorse.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by zhengkq on 2017/3/31.
 */

public class ZhuantiAdapter extends RecyclerView.Adapter<ZhuantiAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private List<Integer>list;
    LayoutInflater inflater;
    private ZhuantiAdapter.OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public ZhuantiAdapter(Context context, List<Integer>list) {
        this.context = context;
        this.list = list;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ZhuantiAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_zhuanti,parent, false);
        ZhuantiAdapter.ViewHolder viewHolder = new ZhuantiAdapter.ViewHolder(view);
        viewHolder.simpleDraweeView = (SimpleDraweeView) view.findViewById(R.id.simpleDraweeView);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ZhuantiAdapter.ViewHolder holder, int position) {
        int id = list.get(position);
        holder.simpleDraweeView.setImageResource(id);
        holder.itemView.setTag(id);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , int data);
    }
    public void setOnItemClickListener(ZhuantiAdapter.OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取数据
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View view)
        {
            super(view);
        }
        SimpleDraweeView simpleDraweeView;
    }
}
