package com.eyenorse.VisionTrain;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.VideoAllInfos;
import com.eyenorse.bean.VideoUrlClarity;
import com.eyenorse.main.MainActivity;
import com.eyenorse.utils.ValidatorUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhengkq on 2017/4/12.
 */

public class PlayAnimActivity extends BaseActivity {

    @BindView(R.id.iv_v)
    ImageView iv_v;
    @BindView(R.id.iv_h)
    ImageView iv_h;
    private int recLen;
    private String titleName;
    private String mVideoPath;
    private String mVideoPath1;
    //private TranslateAnimation animation;
    private ObjectAnimator animator;
    private VideoAllInfos videoAllInfos;
    private boolean isFinish;

    public static void startActivity(Context context, String titleName, String mVideoPath, String mVideoPath1) {
        Intent intent = new Intent(context, PlayAnimActivity.class);
        intent.putExtra("titleName", titleName);
        intent.putExtra("mVideoPath", mVideoPath);
        intent.putExtra("mVideoPath1", mVideoPath1);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, VideoAllInfos videoAllInfos, String mVideoPath, String mVideoPath1) {
        Intent intent = new Intent(context, PlayAnimActivity.class);
        intent.putExtra("videoAllInfos", (Serializable) videoAllInfos);
        intent.putExtra("mVideoPath", mVideoPath);
        intent.putExtra("mVideoPath1", mVideoPath1);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_anim);

        setTop(R.color.black);
        Intent intent = getIntent();
        titleName = intent.getStringExtra("titleName");
        mVideoPath = intent.getStringExtra("mVideoPath");
        mVideoPath1 = intent.getStringExtra("mVideoPath1");
        videoAllInfos = (VideoAllInfos) intent.getSerializableExtra("videoAllInfos");

        recLen = 4;
        Message message = handler.obtainMessage(1);     // Message
        if (!isFinish){
            handler.sendMessageDelayed(message, 1000);
        }

        TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 200f);
        animation.setFillAfter(false);// True:图片停在动画结束位置
        animation.setDuration(1000);
        iv_v.startAnimation(animation);

        /*float curTranslationY = iv_v.getTranslationY();
        animator = ObjectAnimator.ofFloat(iv_v, "translationY", curTranslationY, 350f);
        animator.setDuration(2000);
        animator.start();*/
    }

    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {  // handle message
            switch (msg.what) {
                case 1:
                    recLen--;
                    if (!isFinish){
                        if (recLen > 0) {
                            Message message = handler.obtainMessage(1); // Message
                            handler.sendMessageDelayed(message, 1000);
                            if (recLen==3||recLen==1){
                                iv_v.setVisibility(View.INVISIBLE);
                                iv_h.setVisibility(View.VISIBLE);
                                float curTranslationY = iv_h.getTranslationY();
                                TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 200f);
                                animation.setFillAfter(false);// True:图片停在动画结束位置
                                animation.setDuration(1000);
                                iv_h.startAnimation(animation);
                            }else {
                                iv_v.setVisibility(View.VISIBLE);
                                iv_h.setVisibility(View.INVISIBLE);
                                TranslateAnimation animation = new TranslateAnimation(0, 0, 0, 200f);
                                animation.setFillAfter(false);// True:图片停在动画结束位置
                                animation.setDuration(1000);
                                iv_v.startAnimation(animation);
                            }
                        } else {
                            iv_v.setVisibility(View.INVISIBLE);
                            iv_h.setVisibility(View.INVISIBLE);
                            if (videoAllInfos==null){
                                VideoViewActivity.startActivity(PlayAnimActivity.this, titleName, mVideoPath, mVideoPath1);
                            }else {
                                VideoViewActivity.startActivity(PlayAnimActivity.this, videoAllInfos, mVideoPath, mVideoPath1);
                            }
                            finish();
                        }
                    }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isFinish = true;
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
