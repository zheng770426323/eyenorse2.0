package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eyenorse.R;
import com.eyenorse.adapter.LookHisAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.bean.FootMarkInfo;
import com.eyenorse.bean.FootMarkList;
import com.eyenorse.bean.MyFootMarkGroup;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.view.pullableview.PullableListView;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/27.
 */

public class LookHistoryActivity extends BaseActivity {
    @BindView(R.id.pullToRefreshLayout_store_list)
    PullToRefreshLayout pullToRefreshLayout;
    @BindView(R.id.lookListView)
    PullableListView lookListView;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.textView_null)
    TextView textView_null;
    private List<FootMarkInfo>childList;
    private List<MyFootMarkGroup>groupList = new ArrayList<>();
    private int memberId;
    private String token;
    private int from;
    private JSONObject jsonObject;
    private List<FootMarkInfo> footmark = new ArrayList<>();
    private List<String>dateList = new ArrayList<>();
    private MyFootMarkGroup myFootMarkGroup;
    private LookHisAdapter adapter;
    private FootMarkList footMarkList;
    private List<FootMarkInfo> footmarkNew;
    private int lastVisiblePosition;
    private ErrorInfo errorInfo;
    private int count;

    public static void startActivity(Context context,int memberId,String token){
        Intent intent = new Intent(context,LookHistoryActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_history);
        setTop(R.color.black);
        setCentreText("观看历史",getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
        getApiRequestData().ShowDialog(null);
        initView();
        initRequest();
    }

    private void initView() {
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                footmark.clear();
                from = 0;
                lastVisiblePosition =0;
                initRequest();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                lastVisiblePosition = lookListView.getLastVisiblePosition();
                from += 15;
                if (from>=count){
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                }else {
                    initRequest();
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        });
    }


    private void initRequest() {
        getApiRequestData().getLookHistory(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    String error = new JSONObject(result).getString("error");
                    if (TextUtil.isNull(error)||"".equals(error)) {
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        footMarkList = gson.fromJson(jsonObject.toString(), FootMarkList.class);
                        footmarkNew = footMarkList.getFootmark();
                        count = footMarkList.getCount();
                        if (footmarkNew!=null){
                            footmark.addAll(footmarkNew);
                            if (footmarkNew.size() > 0) {
                                dateList.clear();
                                groupList.clear();
                                for (int i = 0; i < LookHistoryActivity.this.footmark.size(); i++) {
                                    FootMarkInfo footMarkInfo = footmark.get(i);
                                    if (footMarkInfo.getGoodsid()==0){
                                        continue;
                                    }
                                    String datetime = footMarkInfo.getDatetime();
                                    String substring = datetime.substring(0, 10);
                                    if (!dateList.contains(substring)) {
                                        dateList.add(substring);
                                        childList = new ArrayList<>();
                                        myFootMarkGroup = new MyFootMarkGroup();
                                        myFootMarkGroup.setDatetime(substring);
                                        myFootMarkGroup.setChild(childList);
                                        groupList.add(myFootMarkGroup);
                                    }
                                }
                                for (int j = 0; j < groupList.size(); j++) {
                                    for (int z = 0; z < LookHistoryActivity.this.footmark.size(); z++) {
                                        FootMarkInfo footMarkInfo = footmark.get(z);
                                        if (footMarkInfo.getGoodsid()==0){
                                            continue;
                                        }
                                        String datetime = footMarkInfo.getDatetime();
                                        String substring = datetime.substring(0, 10);
                                        if (groupList.get(j).getDatetime().equals(substring)) {
                                            groupList.get(j).getChild().add(LookHistoryActivity.this.footmark.get(z));
                                        }
                                    }
                                }
                                adapter = new LookHisAdapter(LookHistoryActivity.this, groupList);
                                lookListView.setAdapter(adapter);
                                lookListView.setSelection(lastVisiblePosition);
                            }
                            initNoneData();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },from+"","15",memberId+"",token);

    }

    public void initNoneData() {
        if (footmark.size()==0){
            textView_null.setText("暂无观看历史...");
        }
    }
/*

    private void setExpandaGroup() {
        lookListView.setAdapter(adapter);
        lookListView.setGroupIndicator(null);
        lookListView.setDivider(null);
        lookListView.setChildDivider(null);

        if (groupList.size()>0){
            for (int i = 0;i<groupList.size();i++){
                lookListView.expandGroup(i);
            }
            lookListView.setSelection(lastVisiblePosition);
        }
        lookListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                lookListView.expandGroup(i);
            }
        });
    }
*/

    @OnClick({R.id.image_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
        }
    }
}
