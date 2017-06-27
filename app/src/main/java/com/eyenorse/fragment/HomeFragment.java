package com.eyenorse.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.eyenorse.R;
import com.eyenorse.VisionTrain.ClassifyAllActivity;
import com.eyenorse.VisionTrain.RecentlyVideoListActivity;
import com.eyenorse.VisionTrain.SearchActivity;
import com.eyenorse.VisionTrain.VideoInfoActivity;
import com.eyenorse.VisionTrain.VisionPlayActivity;
import com.eyenorse.VisionTrain.VisionTrainActivity;
import com.eyenorse.adapter.BiaoqianAdapter;
import com.eyenorse.adapter.ClassifyGridAdapter;
import com.eyenorse.adapter.ZhuantiAdapter;
import com.eyenorse.bean.BannerList;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.CategaryItem;
import com.eyenorse.bean.Classify;
import com.eyenorse.bean.ClassifyVideoList;
import com.eyenorse.bean.LabelItem;
import com.eyenorse.bean.MyselfCheckData;
import com.eyenorse.bean.PersonalInfo;
import com.eyenorse.bean.VideoInfo;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.main.MainActivity;
import com.eyenorse.mine.ActivateAccountActivity;
import com.eyenorse.adapter.HomeRecommendAdapter;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.view.AutoRollLayout;
import com.eyenorse.view.MyGridView;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.welcome.LoginActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * Created by zhengkq on 2016/12/23.
 */

public class HomeFragment extends BaseFragment {
    @BindView(R.id.rl_search)
    RelativeLayout rl_search;
   /* @BindView(R.id.rollLayout)
    AutoRollLayout rollLayout;*/
    @BindView(R.id.gv_classify)
    MyGridView gv_classify;
    @BindView(R.id.gv_business)
    MyGridView gv_business;
   /* @BindView(R.id.rl_zhuanti)
    RelativeLayout rl_zhuanti;
    @BindView(R.id.rv_zhuanti)
    RecyclerView rv_zhuanti;*/
    @BindView(R.id.iv_ceshi)
    ImageView iv_ceshi;
    @BindView(R.id.rl_recommend)
    RelativeLayout rl_recommend;
    @BindView(R.id.rv_recommend)
    RecyclerView rv_recommend;
    @BindView(R.id.rl_recent)
    RelativeLayout rl_recent;
    @BindView(R.id.rv_recent)
    RecyclerView rv_recent;
    @BindView(R.id.rl_no_free)
    RelativeLayout rl_no_free;
    @BindView(R.id.rv_free)
    RecyclerView rv_free;
    @BindView(R.id.rl_free)
    RelativeLayout rl_free;
    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @BindView(R.id.banner_home)
    BGABanner banner_home;
    private List<ClassifyVideoList.ClassifyVideoInfo> recommendVideoList;
    private List<ClassifyVideoList.ClassifyVideoInfo> recentVideoList;
    private List<ClassifyVideoList.ClassifyVideoInfo> isfreeVideoList;
    private List<CategaryItem> classifyList = new ArrayList<>();
    private List<VideoInfo> bannerList;
    JSONObject jsonObject = null;
    private int memberId;
    private String token;
    private int isactivation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        setContentView(inflater, container, R.layout.fragment_vision);
        initView();
        return view;
    }

    private void initView() {
        memberId = ((MainActivity) getActivity()).memberid;
        token = ((MainActivity) getActivity()).token;
        ((MainActivity) getActivity()).getApiRequestData().ShowDialog(null);
        initGridView();
        initBannerView();
        initRefresh();
    }

    private void initRefresh() {
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
                initGridView();
                initBannerView();
            }
            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.NOMORE);
            }
        });
    }

    private void initBannerView() {
        rl_no_free.setVisibility(View.GONE);
        //rv_free.setVisibility(View.GONE);
        rl_free.setVisibility(View.GONE);
        ((MainActivity) getActivity()).getApiRequestData().getBannerList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    BannerList banner = gson.fromJson(jsonObject.toString(), BannerList.class);
                    bannerList = banner.getBanner();
                   /* if (banner != null && bannerList.size() > 0) {
                        rollLayout.setItems(bannerList);
                        rollLayout.setAntoRoll(true);
                    }*/
                    banner_home.setData(bannerList,null);
                    banner_home.setAdapter(new BGABanner.Adapter<ImageView,VideoInfo>(){
                        @Override
                        public void fillBannerItem(BGABanner banner, ImageView itemView, VideoInfo model, int position) {
                            Picasso.with(getActivity())
                                    .load(model.getImg())
                                    .placeholder(R.mipmap.default_750_450)
                                    .error(R.mipmap.default_750_450)
                                    .into(itemView);
                        }
                    });
                    banner_home.setDelegate(new BGABanner.Delegate<ImageView,VideoInfo>(){
                        @Override
                        public void onBannerItemClick(BGABanner banner, ImageView itemView, VideoInfo model, int position) {
                            if (model.getVideo_id()>0){
                                VideoInfoActivity.startActivity(getActivity(),model.getVideo_id()+"");
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "1");

    }

    private void initGridView() {
        ((MainActivity) getActivity()).getApiRequestData().getGategoryList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                BaseBean<Classify> classify = gson.fromJson(result, new TypeToken<BaseBean<Classify>>() {
                }.getType());
                classifyList = classify.getData().getData();
                CategaryItem categaryItem = new CategaryItem();
                classifyList.add(categaryItem);
                if (classifyList != null && classifyList.size() > 0) {
                    gv_classify.setNumColumns(3);
                    gv_classify.setFocusable(false);
                    gv_classify.setSelector(R.color.transparent);
                    gv_classify.setAdapter(new ClassifyGridAdapter(getActivity(), classifyList));
                    gv_classify.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (position != classifyList.size() - 1) {
                                RecentlyVideoListActivity.startActivity(getActivity(), classifyList.get(position).getName(),classifyList.get(position).getId());
                            } else {
                                ClassifyAllActivity.startActivity(getActivity());
                            }
                        }
                    });
                }
            }
        });//类目列表

        ((MainActivity) getActivity()).getApiRequestData().getLabelList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                BaseBean<LabelItem> data = gson.fromJson(result,new TypeToken<BaseBean<LabelItem>>(){}.getType());
                LabelItem labelItem = data.getData();
                final List<LabelItem.Label> labels = labelItem.getData();
                if (labels==null||labels.size()==0){
                    gv_business.setVisibility(View.GONE);
                }else {
                    gv_business.setNumColumns(2);
                    gv_business.setFocusable(false);
                    gv_business.setAdapter(new BiaoqianAdapter(getActivity(), labels));
                    gv_business.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                              RecentlyVideoListActivity.startActivity(getActivity(),labels.get(position));
                        }
                    });
                }
            }
        },"1");//标签列表


        iv_ceshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (memberId == 0) {
                    LoginActivity.startActivity(getActivity());
                } else {
                    ((MainActivity) getActivity()).getApiRequestData().getCheckData(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                MyselfCheckData myselfCheckData = gson.fromJson(jsonObject.toString(), MyselfCheckData.class);
                                if (TextUtil.isNull(myselfCheckData.getBeforelefteye()) || TextUtil.isNull(myselfCheckData.getBeforerighteye()) || TextUtil.isNull(myselfCheckData.getBeforedatetime())) {
                                    initRequest();
                                } else {
                                    VisionPlayActivity.startActivity(getActivity(), memberId, token);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, memberId + "", token);
                }
            }
        });

        /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_zhuanti.setLayoutManager(linearLayoutManager);
        List<Integer> list = new ArrayList<>();
        list.add(R.mipmap.zhuanti_icon2);
        ZhuantiAdapter adapter = new ZhuantiAdapter(getActivity(), list);
        rv_zhuanti.setAdapter(adapter);
        adapter.setOnItemClickListener(new ZhuantiAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int data) {
                if (data == R.mipmap.zhuanti_icon2) {
                    if (memberId == 0) {
                        LoginActivity.startActivity(getActivity());
                    } else {
                        ((MainActivity) getActivity()).getApiRequestData().getCheckData(new IOAuthReturnCallBack() {
                            @Override
                            public void onSuccess(String result, Gson gson) {
                                try {
                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                    MyselfCheckData myselfCheckData = gson.fromJson(jsonObject.toString(), MyselfCheckData.class);
                                    if (myselfCheckData.getDetectionrecords().size() > 0) {
                                        VisionPlayActivity.startActivity(getActivity(), memberId, token);
                                    } else {
                                        initRequest();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, memberId + "", token);
                    }
                }
            }
        });//商务视频合作*/
        ((MainActivity) getActivity()).getApiRequestData().getHomeRecommendVideoList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    ClassifyVideoList classify = gson.fromJson(jsonObject.toString(), ClassifyVideoList.class);
                    recommendVideoList = classify.getGoods();
                    if (recommendVideoList!=null){
                        if (classify != null && recommendVideoList.size() > 0) {
                            //设置布局管理器
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            rv_recommend.setLayoutManager(linearLayoutManager);
                            HomeRecommendAdapter adapter = new HomeRecommendAdapter(getActivity(), recommendVideoList);
                            rv_recommend.setAdapter(adapter);
                            adapter.setOnItemClickListener(new HomeRecommendAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, ClassifyVideoList.ClassifyVideoInfo data) {
                                    VideoInfoActivity.startActivity(getActivity(), data.getGoodsid()+"");
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 1, "0", "4", "6");//推荐
        ((MainActivity) getActivity()).getApiRequestData().getHomeRecentlyVideoList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    ClassifyVideoList classify = gson.fromJson(jsonObject.toString(), ClassifyVideoList.class);
                    recentVideoList = classify.getGoods();
                    if (recentVideoList!=null){
                        if (classify != null && recentVideoList.size() > 0) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            rv_recent.setLayoutManager(linearLayoutManager);
                            HomeRecommendAdapter adapter = new HomeRecommendAdapter(getActivity(), recentVideoList);
                            rv_recent.setAdapter(adapter);
                            adapter.setOnItemClickListener(new HomeRecommendAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, ClassifyVideoList.ClassifyVideoInfo data) {
                                    VideoInfoActivity.startActivity(getActivity(), data.getGoodsid()+"");
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, "0", "4", "5");//排序模式 可选值:1(综合排序),2(销量优先),3(价格从低到高),4(价格从高到低),5(发布时间),6(视频播放量) 默认值:1
        //最新

       /* ((MainActivity) getActivity()).getApiRequestData().getHomeRecommendVideoList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    ClassifyVideoList classify = gson.fromJson(jsonObject.toString(), ClassifyVideoList.class);
                    isfreeVideoList = classify.getGoods();
                    if (isfreeVideoList!=null){
                        if (classify != null && isfreeVideoList.size() > 0) {
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            rv_free.setLayoutManager(linearLayoutManager);
                            HomeRecommendAdapter adapter = new HomeRecommendAdapter(getActivity(), isfreeVideoList);
                            rv_free.setAdapter(adapter);
                            adapter.setOnItemClickListener(new HomeRecommendAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, ClassifyVideoList.ClassifyVideoInfo data) {
                                    VideoInfoActivity.startActivity(getActivity(), data.getGoodsid()+"");
                                }
                            });
                        } else {
                            rl_no_free.setVisibility(View.GONE);
                            rv_free.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 2, "0", "4", "0");//收费*/
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @OnClick({R.id.rl_search, R.id.rl_no_free, R.id.rl_recommend, R.id.rl_recent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_search:
                if (memberId>0){
                    SearchActivity.startActivity(getActivity(), memberId, token);
                }else {
                    LoginActivity.startActivity(getActivity());
                }
                break;
            case R.id.rl_recommend:
                //ClassifyDifActivity.startActivity(getActivity(), 1, "1");
                RecentlyVideoListActivity.startActivity(getActivity(), "热门推荐");
                break;
            case R.id.rl_recent:
                //ClassifyDifActivity.startActivity(getActivity());
                RecentlyVideoListActivity.startActivity(getActivity(), "最新视频");
                break;
            case R.id.rl_no_free:
                //ClassifyDifActivity.startActivity(getActivity(), 2, "0");
                RecentlyVideoListActivity.startActivity(getActivity(), "收费视频");
                break;
           /* case R.id.rl_zhuanti:
                FeaturesListActivity.startActivity(getActivity());
                break;*/
        }
    }

    private void initRequest() {
        ((MainActivity) getActivity()).getApiRequestData().getMemberInfo(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                    PersonalInfo personalInfo = gson.fromJson(jsonObject.toString(), PersonalInfo.class);
                    isactivation = personalInfo.getIsactivation();
                    if (isactivation == 0) {
                        ActivateAccountActivity.startActivity(getActivity(), memberId, token);
                    } else {
                        VisionTrainActivity.startActivity(getActivity(), memberId, token);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, memberId + "", token);
    }
}
