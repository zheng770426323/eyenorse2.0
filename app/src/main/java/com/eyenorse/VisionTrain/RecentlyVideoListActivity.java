package com.eyenorse.VisionTrain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.adapter.HomeListAdapter;
import com.eyenorse.adapter.MyFragmentPagerAdapter;
import com.eyenorse.adapter.SimpleFragmentPagerAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.AllCategaryItem;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.ClassifyVideoList;
import com.eyenorse.bean.LabelItem;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.view.pullableview.InitViewPager;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.view.pullableview.PullableListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ypy.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/3/31.
 */

public class RecentlyVideoListActivity extends BaseActivity {
    @BindView(R.id.ll_pager)
    LinearLayout ll_pager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.vp)
    InitViewPager vp;
    @BindView(R.id.rl_listview)
    RelativeLayout rl_listview;
    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @BindView(R.id.listView)
    PullableListView listView;
    @BindView(R.id.textView_null)
    TextView textView_null;
    private Intent intent;
    private int categoryid;
    private List<AllCategaryItem.Categary> subsetcategory;
    private List<ClassifyVideoList.ClassifyVideoInfo> allGoods = new ArrayList<>();
    private int from;
    private int count;
    private int lastVisiblePosition;
    private String title;
    private int labelId;
    private HomeListAdapter adapter;

    public static void startActivity(Context context, LabelItem.Label label) {
        Intent intent = new Intent(context, RecentlyVideoListActivity.class);
        intent.putExtra("label", (Serializable) label);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String title, int categoryid) {
        Intent intent = new Intent(context, RecentlyVideoListActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("categoryid", categoryid);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String title) {
        Intent intent = new Intent(context, RecentlyVideoListActivity.class);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rec_vlist);
        EventBus.getDefault().register(this);

        setTop(R.color.black);
        intent = getIntent();
        title = intent.getStringExtra("title");
        LabelItem.Label label = (LabelItem.Label) intent.getSerializableExtra("label");
        if (label != null) {
            title = label.getName();
        }
        setCentreText(title, getResources().getColor(R.color.text_color_2));
        categoryid = intent.getIntExtra("categoryid", 0);
        initRefresh();
        Log.e("categoryid", categoryid + "");
        if (label != null) {
            labelId = label.getId();
            ll_pager.setVisibility(View.GONE);
            rl_listview.setVisibility(View.VISIBLE);
            initListRequest();
        } else if (categoryid == 0) {
            ll_pager.setVisibility(View.GONE);
            rl_listview.setVisibility(View.VISIBLE);
            initListRequest();
        } else {
            initRequest();
        }
    }

    private void initRefresh() {
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                from = 0;
                allGoods.clear();
                lastVisiblePosition = 0;
                initListRequest();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                from += 10;
                lastVisiblePosition = listView.getLastVisiblePosition();
                if (from >= count) {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                } else {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    initListRequest();
                }
            }
        });
    }

    private void initRequest() {
        getApiRequestData().getSubsetList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                BaseBean<AllCategaryItem> data = gson.fromJson(result, new TypeToken<BaseBean<AllCategaryItem>>() {
                }.getType());
                String error = data.getError();
                if (TextUtil.isNull(error) || "".equals(error)) {
                    AllCategaryItem allCategaryItem = data.getData();
                    if (allCategaryItem.getCategory().size() > 0) {
                        AllCategaryItem.Categary categary = allCategaryItem.getCategory().get(0);
                        subsetcategory = categary.getSubsetcategory();
                        if (subsetcategory == null || subsetcategory.size() == 0) {
                            ll_pager.setVisibility(View.GONE);
                            rl_listview.setVisibility(View.VISIBLE);
                            initListRequest();
                        } else {
                            rl_listview.setVisibility(View.GONE);
                            ll_pager.setVisibility(View.VISIBLE);
                            initView();
                        }
                    } else {
                        rl_listview.setVisibility(View.GONE);
                        ll_pager.setVisibility(View.VISIBLE);
                        initView();
                    }
                }

            }
        }, categoryid + "");
    }

    private void initListRequest() {
        if (title.equals("最新视频")) {
            getApiRequestData().getHomeRecentlyVideoList(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    initResult(result, gson);
                }
            }, from + "", "10", "5");//最新
        } else if (title.equals("热门推荐")) {
            getApiRequestData().getHomeRecommendVideoList(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    initResult(result, gson);
                }
            }, 1, from + "", "10", "6");//推荐
        } else if (title.equals("收费视频")) {
            getApiRequestData().getHomeRecommendVideoList(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    initResult(result, gson);
                }
            }, 2, from + "", "10", "0");//收费
        } else if (labelId > 0) {
            getApiRequestData().getLabelVideoList(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    initResult(result, gson);
                }
            }, from + "", "10", labelId + "");//标签
        } else {
            getApiRequestData().getCategaryVideoList(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    initResult(result, gson);
                }
            }, from + "", "10", categoryid + "");
        }
    }

    private void initResult(String result, Gson gson) {
        BaseBean<ClassifyVideoList> bean = gson.fromJson(result, new TypeToken<BaseBean<ClassifyVideoList>>() {
        }.getType());
        ClassifyVideoList classifyVideoList = bean.getData();
        if (classifyVideoList != null) {
            List<ClassifyVideoList.ClassifyVideoInfo> goods = classifyVideoList.getGoods();
            count = classifyVideoList.getCount();
            if (goods != null) {
                allGoods.addAll(goods);
            }
            if (allGoods.size() > 0) {
                adapter = new HomeListAdapter(RecentlyVideoListActivity.this, allGoods);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        VideoInfoActivity.startActivity(RecentlyVideoListActivity.this, allGoods.get(position).getGoodsid() + "");
                    }
                });
                listView.setSelection(lastVisiblePosition);
            } else {
                textView_null.setText("暂无视频");
            }
        }
    }

    private void initView() {
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager(), this, subsetcategory);
        vp.setAdapter(adapter);
        tabs.setupWithViewPager(vp);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
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
        from = 0;
        allGoods.clear();
        lastVisiblePosition = 0;
        initListRequest();
    }
}
