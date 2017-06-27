package com.eyenorse.VisionTrain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.eyenorse.R;
import com.eyenorse.adapter.FeaturesAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.ClassifyVideoList;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.view.pullableview.PullableListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/4/11.
 */

public class FeaturesListActivity extends BaseActivity {
    @BindView(R.id.pullToRefreshLayout_store_list)
    PullToRefreshLayout pullToRefreshLayout_store_list;
    @BindView(R.id.listView_message)
    PullableListView listView_message;
    @BindView(R.id.textView_null)
    TextView textView_null;
    private int from;
    private int count;
    private List<ClassifyVideoList.ClassifyVideoInfo> goods = new ArrayList<>();
    private FeaturesAdapter adapter;

    public static void startActivity(Context context){
        Intent intent  = new Intent(context,FeaturesListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        setTop(R.color.black);
        setCentreText("专题", getResources().getColor(R.color.text_color_2));

        initRefresh();
        initRequest();
    }

    private void initRefresh() {
        pullToRefreshLayout_store_list.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                from = 0;
                goods.clear();
                initRequest();
                pullToRefreshLayout_store_list.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                from += 10;
                initRequest();
                if (from>=count){
                    pullToRefreshLayout_store_list.loadmoreFinish(PullToRefreshLayout.NOMORE);
                }else {
                    pullToRefreshLayout_store_list.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        });
    }

    private void initRequest() {
        getApiRequestData().getOrderVideoList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                BaseBean<ClassifyVideoList> data = gson.fromJson(result, new TypeToken<BaseBean<ClassifyVideoList>>() {}.getType());
                ClassifyVideoList classifyVideoList = data.getData();
                count = classifyVideoList.getCount();
                List<ClassifyVideoList.ClassifyVideoInfo> goodsList = classifyVideoList.getGoods();
                goods.addAll(goodsList);
                adapter = new FeaturesAdapter(FeaturesListActivity.this,goods);
                if (goodsList != null && goods.size() > 0) {
                    listView_message.setAdapter(adapter);
                } else {
                    textView_null.setText("暂无专题");
                }
            }
        },from+"","5","2");

        listView_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FeaturesInfoActivity.startActivity(FeaturesListActivity.this,goods.get(position));
            }
        });
    }

    @OnClick({R.id.image_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
        }
    }
}
