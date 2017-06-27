package com.eyenorse.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.VisionTrain.VideoInfoActivity;
import com.eyenorse.bean.FootMarkInfo;
import com.eyenorse.bean.MyFootMarkGroup;
import com.facebook.drawee.backends.pipeline.Fresco;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengkq on 2016/12/28.
 */

public class LookHisAdapter extends BaseAdapter {
    private Context context;
    private List<MyFootMarkGroup>groupList = new ArrayList<>();
    LayoutInflater inflater;
    public LookHisAdapter(Context context, List<MyFootMarkGroup> groupList) {
        this.context = context;
        this.groupList.clear();
        this.groupList.addAll(groupList);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return groupList.size();
    }

    @Override
    public Object getItem(int position) {
        return groupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LookHisAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            Fresco.initialize(context);
            convertView = inflater.inflate(R.layout.item_look_his, null);
            viewHolder = new LookHisAdapter.ViewHolder();
            viewHolder.hsl_history = (RecyclerView) convertView.findViewById(R.id.hsl_history);
            viewHolder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LookHisAdapter.ViewHolder) convertView.getTag();
        }
        MyFootMarkGroup myFootMarkGroup = groupList.get(position);
        viewHolder.tv_date.setText(myFootMarkGroup.getDatetime());
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        viewHolder.hsl_history.setLayoutManager(linearLayoutManager);
        //设置适配器
        RecycleAdapter adapter = new RecycleAdapter(context, myFootMarkGroup.getChild());
        viewHolder.hsl_history.setAdapter(adapter);
        adapter.setOnItemClickListener(new RecycleAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, FootMarkInfo data) {
                if (data.getGoodsid()<=0){
                    Toast.makeText(context,"该视频不存在！",Toast.LENGTH_SHORT).show();
                }else {
                    VideoInfoActivity.startActivity(context,data.getGoodsid()+"");
                }
            }
        });
        return convertView;
    }
    public class ViewHolder{
        public TextView tv_date;
        public RecyclerView hsl_history;
    }
}
