package com.eyenorse.base;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.dialog.OutLineNoticeDialog;
import com.eyenorse.http.ApiRequestData;
import com.eyenorse.manager.SystemBarTintManager;
import com.eyenorse.mine.SettingActivity;
import com.eyenorse.welcome.LoginActivity;
import com.ypy.eventbus.EventBus;

import butterknife.ButterKnife;

/**
 * Created by zhengkq on 2016/12/22.
 */

public class BaseActivity extends AppCompatActivity {
    private boolean isHaveTop = true;
    private ApiRequestData apiRequestData;
    private MyReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        registerBroadcast();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isHaveTop = true;
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintResource(R.color.transparent);//通知栏所需颜色
        } else {
            isHaveTop = false;
        }
    }

    public ApiRequestData getApiRequestData(){
        if (apiRequestData==null&&ApiRequestData.getInstance(this)!=this.apiRequestData){
            apiRequestData = ApiRequestData.getInstance(this);
        }
        return apiRequestData;
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void setContentView(int layoutResID) {
        // TODO Auto-generated method stub
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }

    public void setTop(int id){
        View imageView_top = findViewById(R.id.imageView_top);
        if (isHaveTop) {
            imageView_top.setVisibility(View.VISIBLE);
        } else {
            imageView_top.setVisibility(View.GONE);
        }
        imageView_top.setBackgroundColor(getResources().getColor(id));
    }

    public void setLeftText(String text){
        TextView leftText = (TextView) findViewById(R.id.leftText);
        leftText.setText(text);
    }

    public void setCentreText(String text, int color){
        TextView centreText = (TextView) findViewById(R.id.centre_text);
        centreText.setText(text);
        centreText.setTextColor(color);
    }

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    /*EditText失去焦点关闭虚拟键盘*/
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    private void registerBroadcast() {
        // 注册广播接收者
        receiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("exit_app");
        registerReceiver(receiver,filter);
    }

    public class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if ("exit_app".equals(intent.getAction())) {
                finish();
            }
        }
    }

   /* protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
