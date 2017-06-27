package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.VisionTrain.VideoInfoActivity;
import com.eyenorse.adapter.CollectionExpandableAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.base.SysApplication;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.bean.MyCollectionGroup;
import com.eyenorse.bean.MyCollectionVideoList;
import com.eyenorse.bean.PersonInfoReset;
import com.eyenorse.dialog.OutLineNoticeDialog;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.view.pullableview.PullableExpandableListView;
import com.eyenorse.welcome.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by zhengkq on 2016/12/27.
 */

public class MyCollectionActivity extends BaseActivity {
    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @BindView(R.id.listView_all_colloction)
    PullableExpandableListView listView_all_colloction;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.textView_null)
    TextView textView_null;
    public List<MyCollectionGroup> groupList = new ArrayList<>();
    private List<MyCollectionVideoList.MyCollectionVideo> childList = new ArrayList<>();
    private CollectionExpandableAdapter adapter;
    public int memberId;
    public String token;
    private int from = 0;
    private JSONObject jsonObject = null;
    private MyCollectionVideoList myCollectionVideoList = new MyCollectionVideoList();
    private List<MyCollectionVideoList.MyCollectionVideo> collection = new ArrayList<>();
    private MyCollectionGroup myCollectionGroup;
    private List<String> dateList = new ArrayList<String>();
    public int count;
    private List<MyCollectionVideoList.MyCollectionVideo> collectionNew;
    private int lastVisiblePosition = 0;
    private boolean isNotify = false;//若我的收藏全部取消收藏后，则需通过该判断是否刷新adapter
    private ErrorInfo errorInfo;

    public static void startActivity(Context context, int memberId,String token) {
        Intent intent = new Intent(context, MyCollectionActivity.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);
        EventBus.getDefault().register(this);
        setTop(R.color.black);

        setCentreText("我的视频",getResources().getColor(R.color.text_color_2));
        ShareSDK.initSDK(MyCollectionActivity.this, "1aebf9cd09000");
        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
        initView();
        getApiRequestData().ShowDialog(null);
        initRequest();
    }

    private void initView() {
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                collection.clear();
                from = 0;
                initRequest();
                //adapter.notifyDataSetChanged();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                lastVisiblePosition = listView_all_colloction.getLastVisiblePosition();
                from += 10;
                initRequest();
                //pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
            }
        });
    }

    /*private void initMessage() {
        getApiRequestData().getNoneReadMessage(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    String error = new JSONObject(result).getString("error");
                    if (TextUtil.isNull(error)||"".equals(error)) {
                        JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                        int noticenum = jsonObject.getInt("noticenum");
                        if (noticenum == 0) {
                            image_message_icon.setVisibility(View.INVISIBLE);
                        } else {
                            image_message_icon.setVisibility(View.VISIBLE);
                        }
                        initRequest();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, memberId + "",token);
    }*/

    public void initRequest() {
        getApiRequestData().getMyCollections(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    String error = new JSONObject(result).getString("error");
                    if (TextUtil.isNull(error)||"".equals(error)) {
                        BaseBean<MyCollectionVideoList> bean = gson.fromJson(result, new TypeToken<BaseBean<MyCollectionVideoList>>() {
                        }.getType());
                        myCollectionVideoList = bean.getData();
                        collectionNew = myCollectionVideoList.getCollection();
                        if (from==0){
                            MyCollectionActivity.this.collection.clear();
                        }
                        MyCollectionActivity.this.collection.addAll(collectionNew);
                        Log.e("collection",collection.size()+"");
                        count = myCollectionVideoList.getCount();
                        if (collectionNew.size() > 0) {
                            dateList.clear();
                            groupList.clear();
                            for (int i = 0; i < MyCollectionActivity.this.collection.size(); i++) {
                                String datetime = MyCollectionActivity.this.collection.get(i).getDatetime();
                                String substring = datetime.substring(0, 11);
                                if (!dateList.contains(substring)) {
                                    dateList.add(substring);
                                    childList = new ArrayList<>();
                                    myCollectionGroup = new MyCollectionGroup();
                                    myCollectionGroup.setDateTime(substring);
                                    myCollectionGroup.setChildList(childList);
                                    groupList.add(myCollectionGroup);
                                }
                            }
                            for (int j = 0; j < groupList.size(); j++) {
                                for (int z = 0; z < MyCollectionActivity.this.collection.size(); z++) {
                                    String datetime = MyCollectionActivity.this.collection.get(z).getDatetime();
                                    String substring = datetime.substring(0, 11);
                                    if (groupList.get(j).getDateTime().equals(substring)) {
                                        groupList.get(j).getChildList().add(MyCollectionActivity.this.collection.get(z));
                                        Log.e("222222","1111111");
                                    }
                                }
                            }
                            //initSetText();
                            adapter = new CollectionExpandableAdapter(MyCollectionActivity.this);
                            setExpandaGroup();
                            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                        } else {
                            pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                        }
                        if (collection.size() == 0) {
                            initNoneData();
                            if (isNotify) {
                                groupList.clear();
                                adapter.notifyDataSetChanged();
                            }
                        }else {
                            textView_null.setText("");
                        }
                        initSetText();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, from + "", "10", memberId + "",token);

    }

    public void initNoneData() {
        textView_null.setText("暂无收藏视频...");
    }

    public void initSetText() {
        if (count>0){
            setLeftText("我的收藏" +"("+ count + ")");
        }else {
            setLeftText("我的收藏");
        }
    }

    public void setExpandaGroup() {
        listView_all_colloction.setAdapter(adapter);
        listView_all_colloction.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                VideoInfoActivity.startActivity(MyCollectionActivity.this, groupList.get(groupPosition).getChildList().get(childPosition).getGoodsid()+"");
                return false;
            }
        });
        if (groupList.size() > 0) {
            for (int i = 0; i < groupList.size(); i++) {
                listView_all_colloction.expandGroup(i);
            }
            listView_all_colloction.setSelection(lastVisiblePosition);
        }
        listView_all_colloction.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                listView_all_colloction.expandGroup(i);
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

    public void onEventMainThread(LoginInfo info) {
        collection.clear();
        memberId = info.getMemberid();
        token = info.getToken();
        Log.e("collection1",collection.size()+"");
        from = 0;
        isNotify = true;
        initRequest();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
