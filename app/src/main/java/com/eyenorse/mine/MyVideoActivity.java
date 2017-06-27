package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.VisionTrain.VideoInfoActivity;
import com.eyenorse.adapter.MyVideoEditAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.bean.MyVideoBean;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.DateTimeHelper;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.view.pullableview.PullableListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.eyenorse.R.id.tv_select_count;

/**
 * Created by zhengkq on 2017/3/6.
 * 我的播单
 */

public class MyVideoActivity extends BaseActivity {
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.pullToRefreshLayout_store_list)
    PullToRefreshLayout pullToRefreshLayout;
    @BindView(R.id.listView_message)
    PullableListView listView;
    @BindView(R.id.textView_null)
    TextView textView_null;
    @BindView(R.id.ll_selectall)
    LinearLayout ll_selectall;
    @BindView(R.id.iv_selectall)
    ImageView iv_selectall;
    @BindView(R.id.tv_select_count)
    TextView tv_select_count;
    @BindView(R.id.tv_del)
    TextView tv_del;
    @BindView(R.id.tv_edit1)
    TextView tv_edit;
    @BindView(R.id.rl_bottom)
    RelativeLayout rl_bottom;

    public int memberId;
    public String token;
    private Intent intent;
    public int count;
    private int from;
    private List<MyVideoBean.MyVideoItem> allVideoList = new ArrayList<>();
    private int lastVisiblePosition;
    public int type = 0;//1:编辑 2:取消
    private MyVideoEditAdapter adapter;
    private boolean isLeastOneSel = false;
    private boolean isSelectAll = false;
    private String today;

    public static void startActivity(Context context, int memberId, String token) {
        Intent intent = new Intent(context, MyVideoActivity.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("token", token);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_video);
        EventBus.getDefault().register(this);
        setTop(R.color.black);
        setCentreText("我的播单", getResources().getColor(R.color.text_color_2));
        //tv_edit.setText("编辑");
        tv_edit.setVisibility(View.GONE);
        intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
        getApiRequestData().ShowDialog(null);
        initRequest();
        initRefresh();
    }

    private void initRefresh() {
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                from = 0;
                allVideoList.clear();
                initRequest();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                from += 5;
                lastVisiblePosition = listView.getLastVisiblePosition();
                if (from>=count){
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                }else {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    initRequest();
                }
            }
        });
    }

    private void initRequest() {
        getApiRequestData().getMyBuyVideos(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                String error = null;
                try {
                    error = new JSONObject(result).getString("error");
                    if (TextUtil.isNull(error) || error.length() == 0) {
                        BaseBean<MyVideoBean> bean = gson.fromJson(result, new TypeToken<BaseBean<MyVideoBean>>() {
                        }.getType());
                        MyVideoBean data = bean.getData();
                        List<MyVideoBean.MyVideoItem> myVideoList = data.getData();
                        count = data.getCount();
                        allVideoList.addAll(myVideoList);
                        for (int i = 0;i<allVideoList.size();i++){
                            allVideoList.get(i).setSelect(false);
                        }
                        if (allVideoList.size() > 0) {
                            adapter = new MyVideoEditAdapter(MyVideoActivity.this,allVideoList);
                            listView.setAdapter(adapter);
                            /*today = DateTimeHelper.getDateTime().substring(0, 11);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    MyVideoBean.MyVideoItem myVideoItem = allVideoList.get(position);
                                    try {
                                        int daysBetween = DateTimeHelper.daysBetween(today, myVideoItem.getExpire_time(), DateTimeHelper.mFormat_chattime);
                                        if (daysBetween>=0){
                                           VideoInfoActivity.startActivity(MyVideoActivity.this,myVideoItem.getId()+"");
                                        }
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });*/
                            listView.smoothScrollToPosition(lastVisiblePosition);
                        }else {
                            textView_null.setText("暂无视频");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, from + "", "10", memberId + "", token);
    }

    public void setBottomText(){
        isSelectAll = isSelectAll();
        if (isSelectAll){
            tv_select_count.setText("已选择"+allVideoList.size()+"个");
            iv_selectall.setImageDrawable(getResources().getDrawable(R.mipmap.selected));
        }else {
            tv_select_count.setText("");
            iv_selectall.setImageDrawable(getResources().getDrawable(R.mipmap.unselect));
        }
    }

    public boolean isSelectAll() {
        for (MyVideoBean.MyVideoItem item:allVideoList){
            if (!item.isSelect()){
                isSelectAll = false;
                return isSelectAll;
            }
        }
        return true;
    }

    @OnClick({R.id.image_back,R.id.tv_edit1,R.id.tv_del,R.id.ll_selectall})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.tv_edit1:
                if (tv_edit.getText().toString().equals("编辑")){
                    if (allVideoList.size()>0){
                        rl_bottom.setVisibility(View.VISIBLE);
                        tv_edit.setText("取消");
                        type =2;
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    rl_bottom.setVisibility(View.GONE);
                    tv_edit.setText("编辑");
                    for (int i =0;i<allVideoList.size();i++){
                        allVideoList.get(i).setSelect(false);
                    }
                    setBottomText();
                    type =1;
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.tv_del:
                type = 0;
                isLeastOneSel = false;
                Iterator<MyVideoBean.MyVideoItem> iterator = allVideoList.iterator();
                while(iterator.hasNext())
                {
                    MyVideoBean.MyVideoItem item = iterator.next();
                    if (item.isSelect()){
                        isLeastOneSel = true;
                        iterator.remove();
                        Log.e("111111","1111");
                    }
                }
                adapter.notifyDataSetChanged();
                setBottomText();
                if (allVideoList.size()==0){
                    rl_bottom.setVisibility(View.GONE);
                    tv_edit.setText("编辑");
                }
                if (!isLeastOneSel){
                    Toast.makeText(MyVideoActivity.this,"您还未选择任何视频!",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_selectall:
                isSelectAll = !isSelectAll;
                for (int i =0;i<allVideoList.size();i++){
                    allVideoList.get(i).setSelect(isSelectAll);
                }
                //listView.setAdapter(adapter);
                type = 0;
                adapter.notifyDataSetChanged();
                if (isSelectAll){
                    tv_select_count.setText("已选择"+allVideoList.size()+"个");
                    iv_selectall.setImageDrawable(getResources().getDrawable(R.mipmap.selected));
                }else {
                    tv_select_count.setText("");
                    iv_selectall.setImageDrawable(getResources().getDrawable(R.mipmap.unselect));
                }
                break;
        }
    }

    public void onEventMainThread(LoginInfo info) {
        this.memberId = info.getMemberid();
        this.token = info.getToken();
        from = 0;
        lastVisiblePosition =0;
        allVideoList.clear();
        initRequest();
    }
}
