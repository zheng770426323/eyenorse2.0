package com.eyenorse.VisionTrain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.eyenorse.R;
import com.eyenorse.adapter.AllCategaryAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.AllCategaryItem;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.view.pullableview.PullableListView;
import com.eyenorse.welcome.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ypy.eventbus.EventBus;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/28.
 */

public class ClassifyAllActivity extends BaseActivity {
    @BindView(R.id.listView)
    PullableListView listView;
    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    private List<AllCategaryItem.Categary> classifyList;
    private int memberid;
    private String token;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ClassifyAllActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classify_all);
        EventBus.getDefault().register(this);
        setTop(R.color.black);

        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        memberid = sp.getInt("memberid", 0);
        token = sp.getString("token","");
        initView();
    }

    private void initView() {
        getApiRequestData().ShowDialog(null);
        initRefresh();
        initRequest();
    }

    private void initRefresh() {
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                classifyList.clear();
                initRequest();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }

    private void initRequest() {
        getApiRequestData().getAllGategoryList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                BaseBean<AllCategaryItem> allCategaryItem = gson.fromJson(result, new TypeToken<BaseBean<AllCategaryItem>>() {
                }.getType());
                classifyList = allCategaryItem.getData().getCategory();
                if (classifyList != null && classifyList.size() > 0) {
                    listView.setAdapter(new AllCategaryAdapter(ClassifyAllActivity.this,classifyList));
                }
            }
        },"2","0");//全部类目列表
    }

    @OnClick({R.id.iv_search,R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                if (memberid>0){
                    SearchActivity.startActivity(ClassifyAllActivity.this,memberid,token);
                }else {
                    LoginActivity.startActivity(ClassifyAllActivity.this);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public void onEventMainThread(LoginInfo info) {
        this.memberid = info.getMemberid();
        this.token = info.getToken();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
