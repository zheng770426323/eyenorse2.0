package com.eyenorse.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.eyenorse.R;
import com.eyenorse.VisionTrain.SearchActivity;
import com.eyenorse.bean.SearchRecordItem;
import java.util.List;


/**
 * Created by zhengkq on 2017/3/29.
 */

public class SearchHistoryAdapter extends BaseAdapter {
    private Context context;
    private List<SearchRecordItem> list;
    LayoutInflater inflater;
    private int memberId;
    private String token;

    public SearchHistoryAdapter(Context context, List<SearchRecordItem> list) {
        this.context = context;
        this.list = list;
        this.memberId = ((SearchActivity)context).memberId;
        this.token = ((SearchActivity)context).token;
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
        SearchHistoryAdapter.ViewHolder viewHolder;
        if (convertView==null){
            convertView =inflater.inflate(R.layout.item_search_history,null);
            viewHolder = new SearchHistoryAdapter.ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (SearchHistoryAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tv_name.setText(list.get(position).getSearchvalue());
        return convertView;
    }
    public class ViewHolder{
        public TextView tv_name;
    }
}
