package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.VisionTrain.VideoInfoActivity;
import com.eyenorse.adapter.MyMessageAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.PushMessageList;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.view.pullableview.PullableListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/26.
 */

public class MyMessageActivity extends BaseActivity {
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.pullToRefreshLayout_store_list)
    PullToRefreshLayout pullToRefreshLayout_store_list;
    @BindView(R.id.listView_message)
    PullableListView listView_message;
    @BindView(R.id.textView_null)
    TextView textView_null;

    private int from = 0;
    public int memberid;
    public String token;
    private List<PushMessageList.PushMessageItem> messages = new ArrayList<>();
    private MyMessageAdapter myMessageAdapter;
    private int count;
    private int lastVisiblePosition;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MyMessageActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        setTop(R.color.black);
        setCentreText("消息",getResources().getColor(R.color.text_color_2));
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        memberid = sp.getInt("memberid", 0);
        token = sp.getString("token","");
        initRefresh();
        initRequest();
    }

    private void initRefresh() {
        pullToRefreshLayout_store_list.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                from = 0;
                lastVisiblePosition =0;
                messages.clear();
                initRequest();
                pullToRefreshLayout_store_list.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                lastVisiblePosition = listView_message.getLastVisiblePosition();
                from += 10;

                if (from>=count){
                    pullToRefreshLayout_store_list.loadmoreFinish(PullToRefreshLayout.NOMORE);
                }else {
                    pullToRefreshLayout_store_list.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    initRequest();
                }
            }
        });
    }

    private void initRequest() {
        getApiRequestData().getPushMessageList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                String error = null;
                try {
                    error = new JSONObject(result).getString("error");
                    if (TextUtil.isNull(error)||"".equals(error)) {
                        BaseBean<PushMessageList> pushMessageList = gson.fromJson(result, new TypeToken<BaseBean<PushMessageList>>() {
                        }.getType());
                        PushMessageList messageList = pushMessageList.getData();
                        count = messageList.getCount();
                        messages.addAll(messageList.getPush());
                        myMessageAdapter = new MyMessageAdapter(MyMessageActivity.this, messages);
                        if (messages != null && messages.size() > 0) {
                            listView_message.setAdapter(myMessageAdapter);
                            listView_message.setSelection(lastVisiblePosition);
                        } else {
                            textView_null.setText("暂无任何消息...");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, from + "", "10", memberid + "", token);
        listView_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PushMessageList.PushMessageItem pushMessageItem = messages.get(position);
                if (pushMessageItem.getType()==1&&pushMessageItem.getType_id()>0){
                    VideoInfoActivity.startActivity(MyMessageActivity.this,pushMessageItem.getType_id()+"");
                }
            }
        });
    }

    @OnClick({R.id.image_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
        }
    }
}
