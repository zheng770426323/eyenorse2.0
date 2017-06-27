package com.eyenorse.main;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eyenorse.R;
import com.eyenorse.adapter.MyFragmentPagerAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.fragment.HomeFragment;
import com.eyenorse.fragment.MineFragment;
import com.eyenorse.fragment.RecommendsFragment;
import com.eyenorse.view.MyViewPager;
import com.eyenorse.welcome.LoginActivity;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @BindView(R.id.viewPager_main)
    MyViewPager viewPager_main;
    @BindView(R.id.ll_tab1)
    LinearLayout ll_tab1;
    @BindView(R.id.image_tab1)
    ImageView image_tab1;
    @BindView(R.id.textView_tab1)
    TextView textView_tab1;
    @BindView(R.id.ll_tab2)
    LinearLayout ll_tab2;
    @BindView(R.id.image_tab2)
    ImageView image_tab2;
    @BindView(R.id.textView_tab2)
    TextView textView_tab2;

    private HomeFragment visionFragment;

    public List<Fragment> fragmentList;
    private int i;
    public boolean isLogin = false;
    public int memberid;
    public String token;
    private int currentItem = 0;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

        setTop(R.color.black);
        initFragment();
    }


    private void initFragment() {
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        isLogin = sp.getBoolean("isload", false);
        memberid = sp.getInt("memberid", 0);
        token = sp.getString("token","");
        Log.e("token",token);
        fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        //fragmentList.add(new RecommendsFragment());
        fragmentList.add(new MineFragment());
        MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragmentList);


        viewPager_main.setAdapter(adapter);
        viewPager_main.SetNomScroller(true);
        viewPager_main.setNoScroll(true);
        viewPager_main.setOffscreenPageLimit(1);
        viewPager_main.setCurrentItem(currentItem);

        if (currentItem==0||this.memberid==0){
            currentItem = 0;
            image_tab1.setImageResource(R.mipmap.tab1_select);
            textView_tab1.setTextColor(getResources().getColor(R.color.top_bg_color));
            //image_tab2.setImageResource(R.mipmap.tab2_gray);
            //textView_tab2.setTextColor(getResources().getColor(R.color.text_hint));
            image_tab2.setImageResource(R.mipmap.tab3_gray);
            textView_tab2.setTextColor(getResources().getColor(R.color.text_hint));
            viewPager_main.setCurrentItem(currentItem);
        }/*else if (currentItem==1){
            currentItem=1;
            viewPager_main.setCurrentItem(currentItem);
            image_tab1.setImageResource(R.mipmap.tab1_gray);
            textView_tab1.setTextColor(getResources().getColor(R.color.text_hint));
            image_tab2.setImageResource(R.mipmap.tab2_select);
            textView_tab2.setTextColor(getResources().getColor(R.color.top_bg_color));
            image_tab3.setImageResource(R.mipmap.tab3_gray);
            textView_tab3.setTextColor(getResources().getColor(R.color.text_hint));
        }*/else {
            currentItem=1;
            viewPager_main.setCurrentItem(currentItem);
            image_tab1.setImageResource(R.mipmap.tab1_gray);
            textView_tab1.setTextColor(getResources().getColor(R.color.text_hint));
            //image_tab2.setImageResource(R.mipmap.tab2_gray);
            //textView_tab2.setTextColor(getResources().getColor(R.color.text_hint));
            image_tab2.setImageResource(R.mipmap.tab3_select);
            textView_tab2.setTextColor(getResources().getColor(R.color.top_bg_color));
        }
    }

    @OnClick({R.id.ll_tab1,R.id.ll_tab2})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_tab1:
                currentItem = 0;
                image_tab1.setImageResource(R.mipmap.tab1_select);
                textView_tab1.setTextColor(getResources().getColor(R.color.top_bg_color));
                //image_tab2.setImageResource(R.mipmap.tab2_gray);
                //textView_tab2.setTextColor(getResources().getColor(R.color.text_hint));
                image_tab2.setImageResource(R.mipmap.tab3_gray);
                textView_tab2.setTextColor(getResources().getColor(R.color.text_hint));
                viewPager_main.setCurrentItem(currentItem);
                break;
            case R.id.ll_tab2:
                if (memberid>0){
                    currentItem=1;
                    viewPager_main.setCurrentItem(currentItem);
                    image_tab1.setImageResource(R.mipmap.tab1_gray);
                    textView_tab1.setTextColor(getResources().getColor(R.color.text_hint));
                    //image_tab2.setImageResource(R.mipmap.tab2_gray);
                    //textView_tab2.setTextColor(getResources().getColor(R.color.text_hint));
                    image_tab2.setImageResource(R.mipmap.tab3_select);
                    textView_tab2.setTextColor(getResources().getColor(R.color.top_bg_color));
                }else {
                    LoginActivity.startActivity(MainActivity.this);
                }
                break;
        }
    }
   public void onEventMainThread(LoginInfo info) {
        this.memberid = info.getMemberid();
        this.isLogin = info.isload();
        this.token = info.getToken();
        initFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private long firstClick;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            long secondClick = System.currentTimeMillis();
            if (secondClick - firstClick > 1500) {
                Toast.makeText(MainActivity.this, "再次点击退出", Toast.LENGTH_SHORT).show();
                firstClick = secondClick;
            } else {
                finish();
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
