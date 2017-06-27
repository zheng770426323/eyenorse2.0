package com.eyenorse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.eyenorse.R;
import com.eyenorse.VisionTrain.RecentlyVideoListActivity;
import com.eyenorse.bean.AllCategaryItem;
import com.eyenorse.view.MyGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengkq on 2017/3/22.
 */

public class AllCategaryAdapter extends BaseAdapter {
    private Context context;
    //private Map<String,List<CategaryItem>> classifyList;
    LayoutInflater inflater;
    List<AllCategaryItem.Categary> list = new ArrayList<>();
    public AllCategaryAdapter(Context context, List<AllCategaryItem.Categary> list) {
        this.context = context;
        this.list.addAll(list);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AllCategaryAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_all_classify, null);
            viewHolder = new AllCategaryAdapter.ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.gv = (MyGridView) convertView.findViewById(R.id.gv);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AllCategaryAdapter.ViewHolder) convertView.getTag();
        }
        final AllCategaryItem.Categary categaryItems = list.get(position);
        viewHolder.tv_name.setText(categaryItems.getName());
        viewHolder.gv.setNumColumns(4);
        viewHolder.gv.setFocusable(false);
        viewHolder.gv.setAdapter(new CategaryAdapter(context, categaryItems.getSubsetcategory()));
        viewHolder.gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //ClassifyDifActivity.startActivity(context, categaryItems.get(position));
                RecentlyVideoListActivity.startActivity(context,categaryItems.getSubsetcategory().get(position).getName(),categaryItems.getSubsetcategory().get(position).getCategoryid());
            }
        });
        return convertView;
    }

    public class ViewHolder{
        public TextView tv_name;
        public MyGridView gv;
    }
}
