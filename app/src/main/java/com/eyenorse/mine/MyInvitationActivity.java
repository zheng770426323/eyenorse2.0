package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.adapter.MyFragmentPagerAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.base.SysApplication;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.dialog.OutLineNoticeDialog;
import com.eyenorse.fragment.MyTeamFragment;
import com.eyenorse.fragment.StartInvitationFragment;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.welcome.LoginActivity;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/26.
 */

public class MyInvitationActivity extends BaseActivity {
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.tv_start_invitation)
    TextView tv_start_invitation;
    @BindView(R.id.tv_my_team)
    TextView tv_my_team;
    @BindView(R.id.cursor)
    LinearLayout cursor;
    @BindView(R.id.vPager)
    ViewPager vPager;
    private List<Fragment>fragmentList;
    private int currIndex = 0;
    //private int offset = 0;// 动画图片偏移量
    //private int bmpW = 0;
    private int one = 0 ;// 页卡1 -> 页卡2 偏移量
    private Animation animation = null;
    private Intent intent;
    public int memberId;
    public String token;
    private JSONObject jsonObject;

    public static void startActivity(Context context,int memberId,String token){
        Intent intent = new Intent(context,MyInvitationActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invitation);
        setTop(R.color.black);
        setCentreText("我的邀请",getResources().getColor(R.color.text_color_2));
        intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
        initView();
    }

    private void initView() {
        getApiRequestData().ShowDialog(null);
        initFragmentView();
    }
   /* private void initMessage() {
        getApiRequestData().getNoneReadMessage(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    String error = new JSONObject(result).getString("error");
                    if (TextUtil.isNull(error)||"".equals(error)){
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        int noticenum = jsonObject.getInt("noticenum");
                        if (noticenum==0){
                            image_message_icon.setVisibility(View.INVISIBLE);
                        }else {
                            image_message_icon.setVisibility(View.VISIBLE);
                        }
                        initFragmentView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },memberId+"",token);
    }*/

    private void initFragmentView() {
        fragmentList = new ArrayList<>();
        fragmentList.add(new StartInvitationFragment());
        fragmentList.add(new MyTeamFragment());
        initCursorImageView();

        vPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList));
        vPager.setCurrentItem(0);
        vPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        if (currIndex==1){
                            animation = new TranslateAnimation(one,0,0,0);
                        }
                        break;
                    case 1:
                        if (currIndex==0){
                            animation = new TranslateAnimation(0,one,0,0);
                        }
                        break;
                }
                currIndex = position;
                animation.setFillAfter(true);// True:图片停在动画结束位置
                animation.setDuration(300);
                cursor.startAnimation(animation);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void initCursorImageView() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) cursor.getLayoutParams();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        layoutParams.width = screenW/2;
        one = screenW/2;
        cursor.setLayoutParams(layoutParams);
    }

    @OnClick({R.id.image_back,R.id.tv_start_invitation,R.id.tv_my_team})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.tv_start_invitation:
                if (currIndex==1){
                    animation = new TranslateAnimation(one,0,0,0);
                    currIndex = 0;
                    initAnimation();
                }
                break;
            case R.id.tv_my_team:
                if (currIndex==0){
                    animation = new TranslateAnimation(0,one,0,0);
                    currIndex = 1;
                    initAnimation();
                }
                break;
        }
    }

    private void initAnimation() {
        vPager.setCurrentItem(currIndex);
        animation.setFillAfter(true);// True:图片停在动画结束位置
        animation.setDuration(300);
        cursor.startAnimation(animation);
    }
}
