package com.eyenorse.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.VisionTrain.VideoInfoActivity;
import com.eyenorse.VisionTrain.VideoViewActivity;
import com.eyenorse.adapter.VideoTypeAdapter;
import com.eyenorse.bean.VideoAllInfos;
import com.eyenorse.bean.VideoUrlClarity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.vov.vitamio.widget.MediaController;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */
public class CustomMediaController extends MediaController {
    private Context mContext;
    private View mVolumeBrightnessLayout;
    private ImageView mOperationBg;
    private ImageView mOperationPercent;
    private AudioManager mAudioManager;
    private int mMaxVolume;
    private int mVolume = -1;
    private float mBrightness = -1f;
    private GestureDetector mGestureDetector;

    VideoViewActivity activity;
    //private ImageView mediacontroller_snapshot;
    private ImageView mediacontroller_previous;
    private ImageView mediacontroller_next;
    private RelativeLayout rl_select;
    private TextView textView_video_type;
    private boolean popWindowIsShow = false;
    private PopupWindow popWindow;
    private View inflate;
    private ListView lv;
    private boolean isPlaying = false;
    private List<VideoUrlClarity> popWindowUrls = new ArrayList<>();
    private VideoAllInfos videoAllInfos;

    //private ImageView mediacontroller_screen_fit;
    /**
     * public static final int VIDEO_LAYOUT_ORIGIN
     缩放参数，原始画面大小。
     常量值：0

     public static final int VIDEO_LAYOUT_SCALE
     缩放参数，画面全屏。
     常量值：1

     public static final int VIDEO_LAYOUT_STRETCH
     缩放参数，画面拉伸。
     常量值：2

     public static final int VIDEO_LAYOUT_ZOOM
     缩放参数，画面裁剪。
     常量值：3
     */
    private String[] strDialogs=new String[]{"100%","全屏","拉伸","裁剪"};
    private int[] imgs=new int[]{R.mipmap.mediacontroller_sreen_size_100,R.mipmap.mediacontroller_screen_fit,R.mipmap.mediacontroller_screen_size,R.mipmap.mediacontroller_sreen_size_crop};
    private int mCurrentPageSize=2;

    private TextView currenttime_tv;

    /**
     * 弹幕相关
     */
    private IDanmakuView mDanmakuView;
    private Switch mtanMuSwitch;
    private DanmakuContext mDanmakuContext;
    private BaseDanmakuParser mParser;
    private String titleName;

    private Handler mDismissHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0&&mVolumeBrightnessLayout!=null) {
                mVolumeBrightnessLayout.setVisibility(View.GONE);
            }
        }
    };
    private String temp;
    private String clarity;
    private List<VideoUrlClarity> videourls;
    private ListView ll_clarity;
    private String  mTitle;


    public CustomMediaController(Context context,String titleName,VideoAllInfos videoAllInfos) {
        super(context);
        this.mContext = context;
        this. mTitle  = titleName;
        this.videoAllInfos = videoAllInfos;
        activity=(VideoViewActivity)context;
        mAudioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        mGestureDetector = new GestureDetector(mContext, new VolumeBrightnesGestureListener());
    }

    @Override
    protected View makeControllerView() {
        return ((LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE)).
                inflate(getResources().getIdentifier("mediacontroller", "layout", mContext.getPackageName()), this);
    }

    @Override
    protected void initOtherView() {

        mtanMuSwitch= (Switch) mRoot.findViewById(R.id.switch_tanmu);
        mtanMuSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    mtanMuSwitch.setBackgroundColor(getResources().getColor(R.color.video_red_color));
                    //开启弹幕
                    mDanmakuView.prepare(mParser, mDanmakuContext);
                    mDanmakuView.show();
                }else{
                    mtanMuSwitch.setBackgroundColor(getResources().getColor(R.color.video_gray_color));
                    //关闭弹幕
                    mDanmakuView.hide();
                }
            }
        });
       // mediacontroller_snapshot= (ImageView) mRoot.findViewById(R.id.mediacontroller_snapshot);
        mediacontroller_previous= (ImageView) mRoot.findViewById(R.id.mediacontroller_previous);
        mediacontroller_next= (ImageView) mRoot.findViewById(R.id.mediacontroller_next);
        //mediacontroller_screen_fit= (ImageView) mRoot.findViewById(R.id.mediacontroller_screen_fit);
        rl_select= (RelativeLayout) mRoot.findViewById(R.id.rl_select);
        textView_video_type = (TextView) mRoot.findViewById(R.id.textView_video_type);
        ll_clarity = (ListView) mRoot.findViewById(R.id.ll_clarity);
        if (videoAllInfos!=null){
            textView_video_type.setVisibility(VISIBLE);
            videourls = videoAllInfos.getVideourls();
            if (videoAllInfos.getVideourls().size()>0){
                clarity = videoAllInfos.getVideourls().get(0).getClarity();
                textView_video_type.setText(videoAllInfos.getVideourls().get(0).getClarity());
                popWindowUrls.clear();//刷新页面再次请求数据则删除之前的集合
                for (int i =videourls.size()-1;i>=0;i--){
                    popWindowUrls.add(videourls.get(i));
                }

            }
        }else {
            textView_video_type.setVisibility(GONE);
        }
        mRoot.findViewById(R.id.image_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VideoViewActivity)mContext).initCurrentPosition();
                activity.finish();
            }
        });
        rl_select.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeClarity();
            }
        });
        /*mediacontroller_snapshot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap currentFrame = activity.getCurrentFrame();
                //保存到本地
                String picturnPath= activity.getExternalCacheDir()+ File.separator+RandomUtil.getRandomLetters(6)+".jpg";
                boolean saveSuccess = BitmapUtil.saveBitmap(currentFrame, new File(picturnPath));
                if(saveSuccess){
                    T.showLong(activity,"截屏保存到"+picturnPath);
                }else{
                    T.showLong(activity,"截屏失败");
                }
            }
        });*/
        mediacontroller_previous.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.reverseVideo();
            }
        });
        mediacontroller_next.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.speedVideo();
            }
        });
       /* mediacontroller_screen_fit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPageSize++;
                if(mCurrentPageSize>3){
                    mCurrentPageSize=0;
                }
                mediacontroller_screen_fit.setBackground(getResources().getDrawable(imgs[mCurrentPageSize]));
                activity.setVideoPageSize(mCurrentPageSize);
            }
        });*/

        mRoot.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mGestureDetector.onTouchEvent(event)) {
                    return true;
                }
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        endGesture();
                        break;
                }
                return false;
            }
        });
    }

    public void changeClarity(){
        if (popWindowIsShow) {
            ll_clarity.setVisibility(GONE);
            textView_video_type.setVisibility(VISIBLE);
        } else {
            isPlaying = true;
            ll_clarity.setVisibility(VISIBLE);
            textView_video_type.setVisibility(GONE);
            initClarityAdapter();
        }
        popWindowIsShow = !popWindowIsShow;
    }

    private void initClarityAdapter() {
        ll_clarity.setAdapter(new VideoTypeAdapter(activity, popWindowUrls));
        ll_clarity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                temp = clarity;
                clarity = popWindowUrls.get(position).getClarity();
                textView_video_type.setText(clarity);
                popWindowUrls.get(position).setClarity(temp);
                popWindowUrls.get(popWindowUrls.size()-1).setClarity(clarity);
                ((VideoViewActivity)mContext).initUrl(clarity);
                //popWindow.dismiss();
            }
        });
    }

    private void endGesture() {
        mVolume = -1;
        mBrightness = -1f;
        // 隐藏
        mDismissHandler.removeMessages(0);
        mDismissHandler.sendEmptyMessageDelayed(0, 500);
    }

    public void setTanMuView(IDanmakuView tanMuView, DanmakuContext mDanmakuContext, BaseDanmakuParser mParser ) {
        this.mDanmakuView = tanMuView;
        this.mDanmakuContext=mDanmakuContext;
        this.mParser=mParser;
    }

    private class VolumeBrightnesGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float mOldX = e1.getX(), mOldY = e1.getY();
            int y = (int) e2.getRawY();
            Display disp = ((Activity) mContext).getWindowManager().getDefaultDisplay();
            int windowWidth = disp.getWidth();
            int windowHeight = disp.getHeight();
            if (mOldX > windowWidth * 4.0 / 5) {
                onVolumeSlide((mOldY - y) / windowHeight);
                return true;
            } else if (mOldX < windowWidth / 5.0) {
                onBrightnessSlide((mOldY - y) / windowHeight);
                return true;
            }
            return false;
        }
    }

    /**
     * 声音高低
     *
     * @param percent
     */
    private void onVolumeSlide(float percent) {
        if (mVolume == -1) {
            mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            if (mVolume < 0)
                mVolume = 0;

            mOperationBg.setImageResource(R.mipmap.video_volumn_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }

        int index = (int) (percent * mMaxVolume) + mVolume;
        if (index > mMaxVolume)
            index = mMaxVolume;
        else if (index < 0)
            index = 0;

        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, index, 0);
        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = findViewById(R.id.operation_full).getLayoutParams().width * index / mMaxVolume;
        mOperationPercent.setLayoutParams(lp);
    }

    /**
     * 处理屏幕亮暗
     *
     * @param percent
     */
    private void onBrightnessSlide(float percent) {
        if (mBrightness < 0) {
            mBrightness = ((Activity) mContext).getWindow().getAttributes().screenBrightness;
            if (mBrightness <= 0.00f)
                mBrightness = 0.50f;
            if (mBrightness < 0.01f)
                mBrightness = 0.01f;

            mOperationBg.setImageResource(R.mipmap.video_brightness_bg);
            mVolumeBrightnessLayout.setVisibility(View.VISIBLE);
        }
        WindowManager.LayoutParams lpa = ((Activity) getContext()).getWindow().getAttributes();
        lpa.screenBrightness = mBrightness + percent;
        if (lpa.screenBrightness > 1.0f)
            lpa.screenBrightness = 1.0f;
        else if (lpa.screenBrightness < 0.01f)
            lpa.screenBrightness = 0.01f;
        ((Activity) mContext).getWindow().setAttributes(lpa);

        ViewGroup.LayoutParams lp = mOperationPercent.getLayoutParams();
        lp.width = (int) (findViewById(R.id.operation_full).getLayoutParams().width * lpa.screenBrightness);
        mOperationPercent.setLayoutParams(lp);
    }


}
