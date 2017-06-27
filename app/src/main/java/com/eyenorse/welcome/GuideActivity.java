package com.eyenorse.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.eyenorse.R;
import com.eyenorse.base.Config;
import com.eyenorse.main.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengkq on 2017/1/4.
 */

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {

    private ViewPager guidePages;
    private List<View> views;
    private GuidePageAdapter vpAdapter;


    public static void startActivity(Context context){
        Intent intent = new Intent(context,GuideActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_guide);
        saveMetrics();
        initView();
    }

    private void initView() {

        guidePages = (ViewPager) findViewById(R.id.guidePages);
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<View>();
        // 初始化引导图片列表
        views.add(inflater.inflate(R.layout.view_welcome_guide1, null));
        views.add(inflater.inflate(R.layout.view_welcome_guide2, null));
        views.add(inflater.inflate(R.layout.view_welcome_guide3, null));
        // 初始化Adapter
        vpAdapter = new GuidePageAdapter();
        guidePages.setAdapter(vpAdapter);
        guidePages.setOnPageChangeListener(this);
    }

    private void saveMetrics() {
        SharedPreferences preferences = getSharedPreferences(Config.SharedPreferencesData, 0);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        preferences.edit().putInt("width", width).apply();
        preferences.edit().putInt("height", height).apply();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class GuidePageAdapter extends PagerAdapter implements View.OnClickListener {

        //销毁position位置的界面
        @Override
        public void destroyItem(View v, int position, Object arg2) {
            // TODO Auto-generated method stub
            ((ViewPager)v).removeView(views.get(position));

        }

        @Override
        public void finishUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        //获取当前窗体界面数
        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return views.size();
        }

        //初始化position位置的界面
        @Override
        public Object instantiateItem(View v, int position) {
            // TODO Auto-generated method stub
            ((ViewPager) v).addView(views.get(position));

            // 测试页卡1内的按钮事件
            if (position == 2) {
                v.findViewById(R.id.ll_welcome).setOnClickListener(this);
            }
            return views.get(position);
        }

        // 判断是否由对象生成界面
        @Override
        public boolean isViewFromObject(View v, Object arg1) {
            // TODO Auto-generated method stub
            return v == arg1;
        }



        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public int getItemPosition(Object object) {
            // TODO Auto-generated method stub
            return super.getItemPosition(object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void onClick(View v) {
            MainActivity.startActivity(GuideActivity.this);
            SharedPreferences sp = getSharedPreferences("loginUser", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("isGuide",true);
            editor.commit();
            finish();
        }
    }
}
