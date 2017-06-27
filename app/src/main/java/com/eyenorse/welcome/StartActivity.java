package com.eyenorse.welcome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.Window;
import android.view.WindowManager;

import com.eyenorse.R;
import com.eyenorse.base.Config;

/**
 * Created by zhengkq on 2016/12/23.
 */

public class StartActivity extends Activity {

    private int recLen1 = 2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);

        saveMetrics();
        Message message = handler1.obtainMessage(1);
        handler1.sendMessageDelayed(message, 1000);
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

    final Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {         // handle message
            switch (msg.what) {
                case 1:
                    recLen1--;
                    if (recLen1 > 0) {
                        Message message = handler1.obtainMessage(1);
                        handler1.sendMessageDelayed(message, 1000);      // send message
                    } else {
                        skip();
                    }
            }
            super.handleMessage(msg);
        }
    };

    private void skip() {
        Intent intent = new Intent(StartActivity.this, GuideActivity.class);
        startActivity(intent);
        finish();
    }
}
