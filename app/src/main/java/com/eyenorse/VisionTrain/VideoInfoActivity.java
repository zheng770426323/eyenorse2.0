package com.eyenorse.VisionTrain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eyenorse.R;
import com.eyenorse.adapter.HomeRecommendAdapter;
import com.eyenorse.adapter.VideoTypeAdapter;
import com.eyenorse.alipay.ShoppingInfo;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.ClassifyVideoList;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.bean.PersonalInfo;
import com.eyenorse.bean.VideoAllInfos;
import com.eyenorse.bean.VideoUrlClarity;
import com.eyenorse.bean.WXpayforInfo;
import com.eyenorse.dialog.LogOutDialog;
import com.eyenorse.dialog.VideoPayOffDialog;
import com.eyenorse.dialog.VipOpenDialog;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.mine.ActivateAccountActivity;
import com.eyenorse.utils.AliPayUtil;
import com.eyenorse.utils.DateTimeHelper;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.utils.WXpayUtil;
import com.eyenorse.utils.WifiAndNetworkUtil;
import com.eyenorse.view.ObservableScrollView;
import com.eyenorse.welcome.LoginActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ypy.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by zhengkq on 2016/12/29.
 */

public class VideoInfoActivity extends BaseActivity {
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.rl_select)
    RelativeLayout rl_select;
    @BindView(R.id.image_below_arror)
    ImageView image_below_arror;
    @BindView(R.id.textView_video_type)
    TextView textView_video_type;
    @BindView(R.id.ll_like)
    LinearLayout ll_like;
    @BindView(R.id.ll_share)
    LinearLayout ll_share;
    @BindView(R.id.tv_look_count)
    TextView tv_look_count;
    @BindView(R.id.textView_title)
    TextView textView_title;
    @BindView(R.id.textView_content)
    TextView textView_content;
    @BindView(R.id.tv_go_buy)
    TextView tv_go_buy;
    @BindView(R.id.simpleDraweeView_head_icon)
    SimpleDraweeView simpleDraweeView_head_icon;
    @BindView(R.id.rv_recommend)
    RecyclerView rv_recommend;
    @BindView(R.id.rv_like)
    RecyclerView rv_like;
    @BindView(R.id.tv_price)
    TextView tv_price;
    @BindView(R.id.ll_content_more)
    LinearLayout ll_content_more;
    @BindView(R.id.tv_zhankai)
    TextView tv_zhankai;
    @BindView(R.id.iv_zhankai)
    ImageView iv_zhankai;
    @BindView(R.id.sv)
    ObservableScrollView sv;
    @BindView(R.id.rl_play)
    RelativeLayout rl_play;
    @BindView(R.id.iv_play)
    ImageView iv_play;
    @BindView(R.id.tv_good_time)
    TextView tv_good_time;
    @BindView(R.id.tv_play_time)
    TextView tv_play_time;
    @BindView(R.id.tv_type)
    TextView tv_type;
    @BindView(R.id.ig_good)
    ImageView ig_good;
    @BindView(R.id.ll_bottom)
    LinearLayout ll_bottom;
    private int memberid;
    private String token;
    private JSONObject jsonObject;
    private PersonalInfo personalInfo;
    private int isactivation;
    private View inflate;
    private boolean popWindowIsShow = false;
    private PopupWindow popWindow;
    private String goodsid;
    private VideoAllInfos videoAllInfos;
    private String videotitle;
    private String intro;
    private List<String> videoimage;
    private int videopageviews;
    private boolean iscollection;
    private List<VideoUrlClarity> videourls;
    private List<VideoUrlClarity> popWindowUrls = new ArrayList<>();
    private int isfree;
    private ListView lv;
    private String temp;
    private String videourl;
    private String videourl1;
    private String clarity;
    private String invitationurl;
    private OnekeyShare oks;
    private int daysBetween;
    private String expire_time;
    private boolean isPlaying = false;
    private String price;
    private boolean vip;
    private List<ClassifyVideoList.ClassifyVideoInfo> recommendVideoList;
    ArrayList<String> list = new ArrayList<>();
    private boolean flag = true;
    private int categaryId;
    private long firstClick;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, VideoInfoActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String goodsid) {
        Intent intent = new Intent(context, VideoInfoActivity.class);
        intent.putExtra("goodsid", goodsid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_vedio_info);
        EventBus.getDefault().register(this);
        ShareSDK.initSDK(VideoInfoActivity.this, "1aebf9cd09000");
        setTop(R.color.black);

        Intent intent = getIntent();
        goodsid = intent.getStringExtra("goodsid");
        image_below_arror.setVisibility(View.VISIBLE);

        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        memberid = sp.getInt("memberid", 0);
        token = sp.getString("token","");

        getApiRequestData().ShowDialog(null);
        initView();
    }

    private void initView() {
        sv.setScrollViewListener(new ObservableScrollView.ScrollViewListener() {
            int measuredHeight = rl_play.getMeasuredHeight();
            @Override
            public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                int scrollY=scrollView.getScrollY();
                int measuredHeight = rl_play.getMeasuredHeight();
                if(scrollY>measuredHeight){
                    iv_play.setVisibility(View.VISIBLE);
                    image_back.setBackgroundColor(getResources().getColor(R.color.white));
                    tv_name.setText(videotitle);
                }else {
                    iv_play.setVisibility(View.GONE);
                    image_back.setBackgroundColor(Color.TRANSPARENT);
                    tv_name.setText("");
                }
            }
        });
        getApiRequestData().getHomeRecommendVideoList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    ClassifyVideoList classify = gson.fromJson(jsonObject.toString(), ClassifyVideoList.class);
                    recommendVideoList = classify.getGoods();
                    if (recommendVideoList!=null){
                        if (classify != null && recommendVideoList.size() > 0) {
                            //设置布局管理器
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VideoInfoActivity.this);
                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                            rv_recommend.setLayoutManager(linearLayoutManager);
                            HomeRecommendAdapter adapter = new HomeRecommendAdapter(VideoInfoActivity.this, recommendVideoList);
                            rv_recommend.setAdapter(adapter);
                            adapter.setOnItemClickListener(new HomeRecommendAdapter.OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, ClassifyVideoList.ClassifyVideoInfo data) {
                                    VideoInfoActivity.startActivity(VideoInfoActivity.this,data.getGoodsid()+"");
                                }
                            });
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 1, "0", "4", "6");//推荐

        if (memberid>0&&token.length()>0){
            getApiRequestData().getVideoInfo(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    BaseBean<VideoAllInfos> baseBean = gson.fromJson(result,new TypeToken< BaseBean<VideoAllInfos>>(){}.getType());
                    videoAllInfos = baseBean.getData();
                    initVideoAllInfos();
                }
            }, goodsid,memberid + "",token);
        }else {
            getApiRequestData().getVideoInfo(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    BaseBean<VideoAllInfos> baseBean = gson.fromJson(result,new TypeToken< BaseBean<VideoAllInfos>>(){}.getType());
                    videoAllInfos = baseBean.getData();
                    initVideoAllInfos();
                }
            }, goodsid);
        }
    }

    private void initVideoAllInfos() {
        if (videoAllInfos != null) {
            videotitle = videoAllInfos.getTitle();
            textView_title.setText(videotitle);
            intro = videoAllInfos.getSummary();
            textView_content.setText(intro);
            videopageviews = videoAllInfos.getVideopageviews();
            tv_look_count.setText(videopageviews + "次");
            tv_good_time.setText(videoAllInfos.getCollectioncount()+"");
            iscollection = videoAllInfos.iscollection();
            if (iscollection){
                ig_good.setImageResource(R.mipmap.good_icon2);
            }else {
                ig_good.setImageResource(R.mipmap.good_icon);
            }
            isfree = videoAllInfos.getIsfree();
            tv_play_time.setText("时长"+DateTimeHelper.getHourMinSS(videoAllInfos.getDuration()));
            if (videoAllInfos.getMinprice().equals(videoAllInfos.getMaxprice())){
                tv_price.setText(videoAllInfos.getMinprice());
            }else {
                tv_price.setText(videoAllInfos.getMinprice()+"-"+videoAllInfos.getMaxprice());
            }
            if (isfree==1){
                ll_bottom.setVisibility(View.GONE);
                //tv_go_buy.setBackgroundColor(getResources().getColor(R.color.transparent_half));
            }else {
                ll_bottom.setVisibility(View.VISIBLE);
                tv_go_buy.setBackgroundColor(getResources().getColor(R.color.text_color_orange));
            }
            if (videoAllInfos.getLabel()!=null&&videoAllInfos.getLabel().size()>0){
                categaryId = videoAllInfos.getLabel().get(0).getId();
            }
            String typeName = "";
            for (int i =0;i< videoAllInfos.getLabel().size();i++){
                String name = videoAllInfos.getLabel().get(i).getName();
                typeName+=name+"";
            }
            tv_type.setText(typeName);

            getApiRequestData().getLikeVideoList(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    BaseBean<ClassifyVideoList> bean = gson.fromJson(result, new TypeToken<BaseBean<ClassifyVideoList>>(){}.getType());
                    ClassifyVideoList classifyVideoList = bean.getData();
                    String error = bean.getError();
                    if (error.equals("")||TextUtil.isNull(error)){
                        final List<ClassifyVideoList.ClassifyVideoInfo> goods = classifyVideoList.getGoods();
                        if (goods!=null){
                            if (goods.size() > 0) {
                                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VideoInfoActivity.this);
                                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                                rv_like.setLayoutManager(linearLayoutManager);
                                HomeRecommendAdapter adapter = new HomeRecommendAdapter(VideoInfoActivity.this, goods);
                                rv_like.setAdapter(adapter);
                                adapter.setOnItemClickListener(new HomeRecommendAdapter.OnRecyclerViewItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, ClassifyVideoList.ClassifyVideoInfo data) {
                                        VideoInfoActivity.startActivity(VideoInfoActivity.this,data.getGoodsid()+"");
                                    }
                                });
                            }
                        }
                    }
                }
            },videoAllInfos.getGoodsid()+"",memberid+"",token);

            videourls = videoAllInfos.getVideourls();
            for (VideoUrlClarity videoUrlClarity:videourls){
                list.add(videoUrlClarity.getClarity());
            }
            expire_time = videoAllInfos.getExpire_time();
            if (videoAllInfos.getVideourls().size()>0){
                clarity = videoAllInfos.getVideourls().get(0).getClarity();
                textView_video_type.setText(videoAllInfos.getVideourls().get(0).getClarity());
                popWindowUrls.clear();//刷新页面再次请求数据则删除之前的集合
                popWindowUrls.addAll(videourls);
                popWindowUrls.remove(0);
            }
            videoimage = videoAllInfos.getImages();
            if (videoimage.size()>0){
                simpleDraweeView_head_icon.setImageURI(videoimage.get(0));
            }
            initRequest();
        }
    }

    private void initRequest() {
        if (memberid>0){
            getApiRequestData().getMemberInfo(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    try {
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        personalInfo = gson.fromJson(jsonObject.toString(), PersonalInfo.class);
                        isactivation = personalInfo.getIsactivation();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, memberid + "",token);
        }
    }

    private void initPayoffDialog() {
        if (videoAllInfos != null) {
            final VideoPayOffDialog videoPayOffDialog = new VideoPayOffDialog(VideoInfoActivity.this, videoAllInfos);
            WindowManager.LayoutParams attributes = videoPayOffDialog.getWindow().getAttributes();
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            attributes.gravity = Gravity.BOTTOM;
            videoPayOffDialog.setCanceledOnTouchOutside(true);
            videoPayOffDialog.setOnClickChoose(new VideoPayOffDialog.OnClickChoose() {
                @Override
                public void onClick(boolean f) {
                    if (f) {
                        price = videoPayOffDialog.tv_price.getText().toString();
                        OrdernumberActivity.startActivity(VideoInfoActivity.this,goodsid,videoPayOffDialog.prop,price);
                    } else {
                        videoPayOffDialog.dismiss();
                    }
                }
            });
            videoPayOffDialog.show();
        }else {
            Toast.makeText(VideoInfoActivity.this,"未请求到数据！",Toast.LENGTH_SHORT).show();
        }
    }

    private void inWifi() {
        boolean isWifi = WifiAndNetworkUtil.isWifi(VideoInfoActivity.this);
        if (!isWifi) {
            final LogOutDialog dialog = new LogOutDialog(VideoInfoActivity.this, "无可用wifi，使用移动网络继续观看?", "去设置", "继续观看");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnClickChoose(new LogOutDialog.OnClickChoose() {
                @Override
                public void onClick(boolean f) {
                    if (f) {
                        initPlay();
                    } else {
                        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                        startActivity(intent);
                    }
                }
            });
            dialog.show();
            dialog.image_mis.setVisibility(View.VISIBLE);
            dialog.image_mis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else {
            initPlay();
        }
    }

    private void initPlay() {
        if (isPlaying){//播放中切换视频
            getApiRequestData().getClarityChange(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    try {
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        videourl = jsonObject.getString("videourl");
                        initSecondUrl();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, clarity, memberid + "", goodsid + "",token);
        }else {
            getApiRequestData().getVideoPlay(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    try {
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        videourl = jsonObject.getString("videourl");
                        initSecondUrl();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, clarity, memberid + "", goodsid + "",token);
        }
    }

    private void initSecondUrl() {
         getApiRequestData().getPromoteVideoUrl(new IOAuthReturnCallBack() {
             @Override
             public void onSuccess(String result, Gson gson) {
                 try {
                     jsonObject = new JSONObject(result).getJSONObject("data");
                     videourl1 = jsonObject.getString("video_url");
                     PlayAnimActivity.startActivity(VideoInfoActivity.this,videoAllInfos,videourl,videourl1);
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }
         });
    }

    /*private void popWindow() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        inflate = layoutInflater.inflate(R.layout.popwindow_video_type_select, null);
        inflate.setBackgroundColor(Color.TRANSPARENT);
        lv = (ListView) inflate.findViewById(R.id.lv);
        lv.setAdapter(new VideoTypeAdapter(VideoInfoActivity.this, popWindowUrls));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                temp = clarity;
                clarity = popWindowUrls.get(position).getClarity();
                textView_video_type.setText(clarity);
                popWindowUrls.get(position).setClarity(temp);
                playVideo();
                popWindow.dismiss();
            }
        });
        //以PopupWindow弹出
        popWindow = new PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popWindow.setContentView(inflate);
        popWindow.setFocusable(false);
        popWindow.showAsDropDown(rl_select, 0, 2);
        popWindow.setOutsideTouchable(true);
    }*/

    @OnClick({R.id.ll_back, R.id.rl_select, R.id.ll_like, R.id.ll_share,R.id.tv_go_buy,R.id.rv_recommend
              ,R.id.ll_content_more,R.id.ll_transparent,R.id.iv_play,R.id.rl_no_free})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                //JCVideoPlayer.releaseAllVideos();
                finish();
                break;
            case R.id.ll_content_more:
                if (flag) {
                    flag = false;
                    tv_zhankai.setText("收起");
                    iv_zhankai.setImageDrawable(getResources().getDrawable(R.mipmap.content_more_up));
                    textView_content.setEllipsize(null);//展开
                    textView_content.setSingleLine(false);//清除android:lines="2"的样式
                } else {
                    flag = true;
                    tv_zhankai.setText("展开");
                    iv_zhankai.setImageDrawable(getResources().getDrawable(R.mipmap.content_more));
                    textView_content.setMaxLines(3);//设置行数为3
                    textView_content.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                }
                break;
            case R.id.rv_recommend:
                RecentlyVideoListActivity.startActivity(VideoInfoActivity.this,"热门推荐");
                break;
           /* case R.id.rl_select:
                if (popWindowIsShow) {
                    popWindow.dismiss();
                } else {
                    isPlaying = true;
                    popWindow();
                }
                popWindowIsShow = !popWindowIsShow;
                break;*/
            case R.id.ll_like:
                long secondClick = System.currentTimeMillis();
                if (secondClick - firstClick < 1000) {
                    firstClick = secondClick;
                    return;
                }
                if (iscollection) {
                    /*getApiRequestData().getMyCollectionsDelete(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                String error = new JSONObject(result).getString("error");
                                if (error.length()==0||TextUtil.isNull(error)) {
                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                    int issuccess = jsonObject.getInt("issuccess");
                                    if (issuccess == 1) {
                                        Toast.makeText(VideoInfoActivity.this, "取消收藏！", Toast.LENGTH_SHORT).show();
                                        //image_like.setImageDrawable(getResources().getDrawable(R.mipmap.shoucang1));
                                        iscollection = false;
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, memberid + "", goodsid + "",token);*/
                    return;
                } else {
                    getApiRequestData().getMyCollectionsAdd(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                String error = new JSONObject(result).getString("error");
                                if (error.length()==0||TextUtil.isNull(error)) {
                                    if (new JSONObject(result).getJSONObject("data") != null) {
                                        jsonObject = new JSONObject(result).getJSONObject("data");
                                        int collectionid = jsonObject.getInt("collectionid");
                                        if (collectionid > 0) {
                                            tv_good_time.setText(videoAllInfos.getCollectioncount()+1+"");
                                            ig_good.setImageResource(R.mipmap.good_icon2);
                                            Toast.makeText(VideoInfoActivity.this, "点赞成功！", Toast.LENGTH_SHORT).show();
                                            LoginInfo loginInfo = new LoginInfo();
                                            loginInfo.setMemberid(memberid);
                                            loginInfo.setIsload(true);
                                            loginInfo.setToken(token);
                                            EventBus.getDefault().post(loginInfo);
                                            //image_like.setImageDrawable(getResources().getDrawable(R.mipmap.shoucang2));
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, memberid + "", goodsid + "",token);
                }
                break;
            case R.id.ll_share:
                ShareSDK.initSDK(this);
                getApiRequestData().getInvitationAdress(new IOAuthReturnCallBack() {
                    @Override
                    public void onSuccess(String result, Gson gson) {
                        try {
                            String error = new JSONObject(result).getString("error");
                            if (error.length()==0||TextUtil.isNull(error)) {
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                invitationurl = jsonObject.getString("invitationurl");
                                oks = new OnekeyShare();
                                //关闭sso授权
                                oks.disableSSOWhenAuthorize();
                                // site是分享此内容的网站名称，仅在QQ空间使用
                                oks.setSite(VideoInfoActivity.this.getString(R.string.app_name));
                                //oks.setImageUrl(videoimage);
                                oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
                                    @Override
                                    public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                                        if ("QZone".equals(platform.getName())) {
                                            paramsToShare.setText(videotitle);
                                            paramsToShare.setTitle("眼护士-\"每天给我一点时间 让你拥有清晰光明\"");
                                            paramsToShare.setTitleUrl(invitationurl);
                                            paramsToShare.setImageUrl(videoimage.get(0));
                                        }
                                        if ("QQ".equals(platform.getName())) {
                                            paramsToShare.setText(videotitle);
                                            paramsToShare.setTitle("眼护士-\"每天给我一点时间 让你拥有清晰光明\"");
                                            paramsToShare.setImageUrl(videoimage.get(0));
                                            paramsToShare.setTitleUrl(invitationurl);
                                        }
                                        if ("SinaWeibo".equals(platform.getName())) {
                                            paramsToShare.setUrl(null);
                                            paramsToShare.setText("眼护士-\"每天给我一点时间 让你拥有清晰光明\"" + videotitle + invitationurl);
                                            paramsToShare.setImageUrl(videoimage.get(0));
                                        }
                                        if ("Wechat".equals(platform.getName())) {
                                            paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                                            paramsToShare.setText(videotitle);
                                            paramsToShare.setTitle("眼护士-\"每天给我一点时间 让你拥有清晰光明\"");
                                            // url仅在微信（包括好友和朋友圈）中使用
                                            paramsToShare.setUrl(invitationurl);
                                            //paramsToShare.setTitleUrl("http://sweetystory.com/");
                                            paramsToShare.setImageUrl(videoimage.get(0));
                                        }
                                        if ("WechatMoments".equals(platform.getName())) {
                                            paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                                            //paramsToShare.setText("一起来做眼护士VR眼保健操吧...");
                                            paramsToShare.setTitle("眼护士-\"每天给我一点时间 让你拥有清晰光明\"" + videotitle);
                                            // url仅在微信（包括好友和朋友圈）中使用
                                            paramsToShare.setUrl(invitationurl);
                                            paramsToShare.setImageUrl(videoimage.get(0));
                                        }
                                    }
                                });
                                // 启动分享GUI
                                oks.show(VideoInfoActivity.this);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, memberid + "",token);
                break;
            case R.id.tv_go_buy:
                if (isfree==0){
                    if (memberid>0){
                        if (isactivation==0){
                            VipOpenDialog dialog = new VipOpenDialog(VideoInfoActivity.this, "如需购买观看，请先", "激活账号", "激活账号");
                            dialog.setCancelable(false);
                            dialog.setCanceledOnTouchOutside(false);
                            dialog.setOnClickChoose(new VipOpenDialog.OnClickChoose() {
                                @Override
                                public void onClick(boolean f) {
                                    if (f) {
                                        ActivateAccountActivity.startActivity(VideoInfoActivity.this, memberid,token);
                                        finish();
                                    }
                                }
                            });
                            dialog.show();
                        }else {
                            initPayoffDialog();
                        }
                    }else {
                        LoginActivity.startActivity(VideoInfoActivity.this);
                        finish();
                    }
                }
                break;
            case R.id.ll_transparent:
                playVideo();
                break;
            case R.id.iv_play:
                playVideo();
                break;
            case R.id.rl_no_free:
                RecentlyVideoListActivity.startActivity(VideoInfoActivity.this, "热门推荐");
                break;
        }
    }

    private void playVideo() {
        if (memberid==0){
            LoginActivity.startActivity(VideoInfoActivity.this);
            finish();
        }else {
            if (isactivation==0){
                VipOpenDialog dialog = new VipOpenDialog(VideoInfoActivity.this, "如需观看，请先", "激活账号", "激活账号");
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setOnClickChoose(new VipOpenDialog.OnClickChoose() {
                    @Override
                    public void onClick(boolean f) {
                        if (f) {
                            ActivateAccountActivity.startActivity(VideoInfoActivity.this, memberid,token);
                            finish();
                        }
                    }
                });
                dialog.show();
            }else {
                if (isfree==0){
                    String today = DateTimeHelper.getDateTime().substring(0, 11);
                    try {
                        if (!TextUtil.isNull(expire_time)) {
                            daysBetween = DateTimeHelper.daysBetween(today, expire_time, DateTimeHelper.mFormat_chattime);
                        } else {
                            daysBetween = -1;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    /*if (vip){
                        daysBetween = 1;
                    }*/
                    if (daysBetween < 0) {
                        VipOpenDialog dialog = new VipOpenDialog(VideoInfoActivity.this, "如需观看，请购买该视频", "是否购买", "购买视频");
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setOnClickChoose(new VipOpenDialog.OnClickChoose() {
                            @Override
                            public void onClick(boolean f) {
                                if (f) {
                                    initPayoffDialog();
                                }
                            }
                        });
                        dialog.show();
                    } else {
                        inWifi();
                    }
                }else {
                    inWifi();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //JCVideoPlayer.releaseAllVideos();
        //MobclickAgent.onPause(this);//友盟统计
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void onEventMainThread(LoginInfo info) {
        this.memberid = info.getMemberid();
        this.token = info.getToken();
        vip = info.isVip();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
