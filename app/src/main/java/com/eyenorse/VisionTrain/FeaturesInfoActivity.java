package com.eyenorse.VisionTrain;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.adapter.HomeListAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.ClassifyVideoList;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/4/11.
 */

public class FeaturesInfoActivity extends BaseActivity {
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.ll_back)
    LinearLayout ll_back;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.iv_play)
    ImageView iv_play;
    private View headerView;
    private List<ClassifyVideoList.ClassifyVideoInfo> goods = new ArrayList<>();
    private HomeListAdapter adapter;
    private int count;
    private TextView tv_title;
    private SimpleDraweeView simpleDraweeView;
    private TextView tv_count;
    private TextView tv_content;
    private TextView tv_zhankai;
    private ImageView iv_zhankai;
    private LinearLayout ll_content_more;
    private ClassifyVideoList.ClassifyVideoInfo info;
    private boolean flag = true;

    public static void startActivity(Context context, ClassifyVideoList.ClassifyVideoInfo info){
        Intent intent = new Intent(context,FeaturesInfoActivity.class);
        intent.putExtra("info",(Serializable)info);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_features_info);
        setTop(R.color.black);

        Intent intent = getIntent();
        info = (ClassifyVideoList.ClassifyVideoInfo) intent.getSerializableExtra("info");
        initView();
    }

    private void initView() {
        LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = lif.inflate(R.layout.header_features_info, null);
        tv_title = (TextView) headerView.findViewById(R.id.tv_title);
        simpleDraweeView = (SimpleDraweeView) headerView.findViewById(R.id.simpleDraweeView_head_icon);
        tv_count = (TextView) headerView.findViewById(R.id.tv_count);
        tv_content = (TextView) headerView.findViewById(R.id.tv_content);
        tv_zhankai = (TextView) headerView.findViewById(R.id.tv_zhankai);
        iv_zhankai = (ImageView) headerView.findViewById(R.id.iv_zhankai);
        ll_content_more = (LinearLayout) headerView.findViewById(R.id.ll_content_more);
        lv.addHeaderView(headerView);

        lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            private int lastVisibleItemPosition = 1;// 标记上次滑动位置
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 当开始滑动且ListView的第一个child不可见时，显示或隐藏播放按钮
                if (firstVisibleItem > lastVisibleItemPosition) {// 上滑
                    iv_play.setVisibility(View.VISIBLE);
                    image_back.setBackgroundColor(getResources().getColor(R.color.transparent_20));
                    tv_name.setText(info.getTitle());
                } else if (firstVisibleItem < lastVisibleItemPosition&&lv.getFirstVisiblePosition() == 0) {// 下滑
                    iv_play.setVisibility(View.GONE);
                    image_back.setBackgroundColor(Color.TRANSPARENT);
                    tv_name.setText("");
                } else {
                    return;
                }
                lastVisibleItemPosition = firstVisibleItem;
            }
        });
        tv_title.setText(info.getTitle());
        simpleDraweeView.setImageURI(info.getImages().get(0));
        tv_content.setText(info.getSummary());
        ll_content_more.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (flag) {
                    flag = false;
                    tv_zhankai.setText("收缩");
                    iv_zhankai.setImageDrawable(getResources().getDrawable(R.mipmap.content_more_up));
                    tv_content.setEllipsize(null);//展开
                    tv_content.setSingleLine(false);//清除android:lines="2"的样式
                } else {
                    flag = true;
                    tv_zhankai.setText("展开");
                    iv_zhankai.setImageDrawable(getResources().getDrawable(R.mipmap.content_more));
                    tv_content.setLines(2);//设置行数为3
                    tv_content.setEllipsize(TextUtils.TruncateAt.END); // 收缩
                }
            }
        });
        initRequest();
    }

    private Random mRandom = new Random();
    private int prevRandomInt = -1;
    private int currRandomInt = -1;

    private void updateText(){
        currRandomInt = mRandom.nextInt(info.getSummary().length());
        while (prevRandomInt == currRandomInt){
            currRandomInt = mRandom.nextInt(info.getSummary().length());
        }
        prevRandomInt = currRandomInt;

        //mTVComparison.setText(newCS);//作为对比示例
        tv_content.setText(info.getSummary());;//效果显示
    }
    private void initRequest() {
        getApiRequestData().getOrderVideoList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                BaseBean<ClassifyVideoList> data = gson.fromJson(result, new TypeToken<BaseBean<ClassifyVideoList>>() {
                }.getType());
                ClassifyVideoList classifyVideoList = data.getData();
                count = classifyVideoList.getCount();
                List<ClassifyVideoList.ClassifyVideoInfo> goodsList = classifyVideoList.getGoods();
                goods.addAll(goodsList);
                tv_count.setText(count+"");
                adapter = new HomeListAdapter(FeaturesInfoActivity.this,goods);
                if (goodsList != null && goods.size() > 0) {
                    lv.setAdapter(adapter);
                }
            }
        }, "0", "5", "2");
    }

    @OnClick({R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                finish();
                break;
        }
    }
}
