package com.eyenorse.welcome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.base.Config;
import com.eyenorse.main.MainActivity;

/**
 * Created by zhengkq on 2017/1/4.
 */

public class SelectHomeActivity extends Activity implements View.OnClickListener {

    private TextView tv_time;
    private int recLen;
    private boolean isfinish = false;
    private boolean isGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0){
            finish();
            return;
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_home);
        saveMetrics();
        initView();
        //检验包名和签名与各平台上面注册的是否一致
        //Review.MD5Review(this,"com.eyenorse","363a0a4d5ddcebd0ad95bc512d052482");
        //MobclickAgent.enableEncrypt(true);//友盟-如果enable为true，SDK会对日志进行加密。加密模式可以有效防止网络攻击，提高数据安全性。如果enable为false，SDK将按照非加密的方式来传输日志。

        findViewById(R.id.ll_start).setOnClickListener(this);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_time.setOnClickListener(this);
    }

    private void initView() {
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        isGuide = sp.getBoolean("isGuide", false);
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
    protected void onResume() {
        super.onResume();
        sendPhone();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_start:
                initActivity();
                break;
            case R.id.tv_time:
                initActivity();
                break;
        }
    }

    private void sendPhone() {
        recLen = 3;
        initSetTime();//设置倒计时
    }

    //设置倒计时
    private void initSetTime() {
        tv_time.setText("3S 跳过");
        Message message = handler.obtainMessage(1);     // Message
        handler.sendMessageDelayed(message, 1000);
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {         // handle message
            switch (msg.what) {
                case 1:
                    recLen--;
                    if (recLen > 0) {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);
                    } else {
                        if (!isfinish){
                            initActivity();
                        }
                    }
                    tv_time.setText(recLen + "S 跳过");
            }
            super.handleMessage(msg);
        }
    };

    private void initActivity() {
        if (isGuide){
            MainActivity.startActivity(SelectHomeActivity.this);
        }else {
            GuideActivity.startActivity(SelectHomeActivity.this);
        }
        isfinish = true;
        finish();
    }
}
