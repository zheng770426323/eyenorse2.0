package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.adapter.MyAgencyListAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.MyAgencyList;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.view.pullableview.PullableListView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/4/2.
 */

public class MyAgencyListActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @BindView(R.id.centre_text)
    TextView centre_text;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @BindView(R.id.listView_partner)
    PullableListView listView_partner;
    @BindView(R.id.textView_null)
    TextView textView_null;
    private Intent intent;
    private int memberId;
    private String token;
    private int from;
    private JSONObject jsonObject;
    private List<MyAgencyList.AgencyInfo> agencyList = new ArrayList<>();
    private int lastVisiblePosition;
    private View headerView;
    private TextView tv_count;
    private int count;
    private MyAgencyList myAgencyList;

    public static void startActivity(Context context, int memberId, String token) {
        Intent intent = new Intent(context, MyAgencyListActivity.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("token", token);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_my_agencylist);
        setTop(R.color.black);
        intiView();
    }

    private void intiView() {
        setCentreText("我是代理商",getResources().getColor(R.color.text_color_2));
        intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
        LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = lif.inflate(R.layout.header_agency_list, null);
        tv_count = (TextView) headerView.findViewById(R.id.tv_count);
        listView_partner.addHeaderView(headerView);
        initRefresh();
        initRequest();
    }

    private void initRefresh() {
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                from = 0;
                agencyList.clear();
                lastVisiblePosition = 0;
                initRequest();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                from += 15;
                lastVisiblePosition = listView_partner.getLastVisiblePosition();
                if (from>count){
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                }else {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    initRequest();
                }
            }
        });
    }

    private void initRequest() {
        getApiRequestData().getAgencyList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    String error = new JSONObject(result).getString("error");
                    if (TextUtil.isNull(error) || error.length() == 0) {
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        myAgencyList = gson.fromJson(jsonObject.toString(), MyAgencyList.class);
                        agencyList.addAll(myAgencyList.getData());
                        tv_count.setText(agencyList.size() + "人");
                        MyAgencyListAdapter myAgencyListAdapter = new MyAgencyListAdapter(MyAgencyListActivity.this, agencyList);
                        if (myAgencyList != null && agencyList.size() > 0) {
                            listView_partner.setAdapter(myAgencyListAdapter);
                            listView_partner.setSelection(lastVisiblePosition);
                        } else {
                            textView_null.setText("暂无代理商");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },memberId+"",token);
        listView_partner.setOnItemClickListener(this);
    }

    @OnClick({R.id.image_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        MyAgencyList.AgencyInfo agencyInfo = agencyList.get(position-1);
        //MyLowerAgencyActivity.startActivity(MyAgencyListActivity.this,agencyInfo,memberId,token);
        MyLowerAgencyActivity.startActivity(MyAgencyListActivity.this,agencyInfo,memberId,token);
    }
}
