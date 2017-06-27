package com.eyenorse.VisionTrain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.bean.PersonalInfo;
import com.eyenorse.dialog.LogOutDialog;
import com.eyenorse.dialog.VipOpenDialog;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.mine.ActivateAccountActivity;
import com.eyenorse.mine.MyMessageActivity;
import com.eyenorse.mine.MyVideoActivity;
import com.eyenorse.mine.PersonalInfoActivity;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.utils.WifiAndNetworkUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.ypy.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;


/**
 * Created by zhengkq on 2016/12/29.
 */

public class VisionPlayActivity extends BaseActivity {
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.textView_confirm)
    TextView textView_confirm;
    @BindView(R.id.edit_left)
    EditText edit_left;
    @BindView(R.id.edit_right)
    EditText edit_right;
    @BindView(R.id.simpleDraweeView_head_icon)
    SimpleDraweeView simpleDraweeView_head_icon;
    /*@BindView(R.id.vv)
    VideoView vv;
*/
    private int memberId;
    private String token;
    private JSONObject jsonObject;
    private PersonalInfo personalInfo;
    private int isactivation;

    public static void startActivity(Context context, int memberId,String token) {
        Intent intent = new Intent(context, VisionPlayActivity.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_vision_play);
        EventBus.getDefault().register(this);
        //Vitamio.initialize(this);
        setTop(R.color.black);

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
        setCentreText("测试", getResources().getColor(R.color.text_color_2));
        getApiRequestData().ShowDialog(null);
        initView();
    }

    private void initView() {
        simpleDraweeView_head_icon.setImageResource(R.mipmap.checkbg);
        getApiRequestData().getMemberInfo(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                    personalInfo = gson.fromJson(jsonObject.toString(), PersonalInfo.class);
                    isactivation = personalInfo.getIsactivation();
                    if (isactivation <=0){
                        VipOpenDialog dialog = new VipOpenDialog(VisionPlayActivity.this, "如需观看，请", "激活账号", "激活账号");
                        dialog.setCancelable(false);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setOnClickChoose(new VipOpenDialog.OnClickChoose() {
                            @Override
                            public void onClick(boolean f) {
                                if (f) {
                                    ActivateAccountActivity.startActivity(VisionPlayActivity.this, memberId,token);
                                    finish();
                                } else {
                                    finish();
                                }
                            }
                        });
                        dialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, memberId +"",token);
    }

    private void inWify() {
        boolean isWifi = WifiAndNetworkUtil.isWifi(VisionPlayActivity.this);
        if (!isWifi) {
            final LogOutDialog dialog = new LogOutDialog(VisionPlayActivity.this, "无可用wifi，使用移动网络继续观看?", "去设置", "继续观看");
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnClickChoose(new LogOutDialog.OnClickChoose() {
                                        @Override
                                        public void onClick(boolean f) {
                                            if (f) {
                                                initPlay();
                                            } else {
                                                // Go to the activity of settings of wireless
                                                //Intent intent = new Intent("android.settings.WIRELESS_SETTINGS");
                                                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                                                startActivity(intent);
                                                //finish();
                                            }
                                        }
                                    }
            );
            dialog.show();
            dialog.image_mis.setVisibility(View.VISIBLE);
            dialog.image_mis.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initPlay();
                    dialog.dismiss();
                }
            });
        } else {
            initPlay();
        }
    }

    private void initPlay() {
        PlayAnimActivity.startActivity(VisionPlayActivity.this,"测试视频","http://eyenursevideo.caifutang.com/Look%E5%8A%A8%E7%94%BB/2%E9%AB%98%E6%B8%85/kexuemofangzhongji.mp4","");
                /*jcVideoPlayerStandard.setUp("http://eyenursevideo.caifutang.com/Look%E5%8A%A8%E7%94%BB/1%E8%B6%85%E6%B8%85/4in1.MP4"
                , "测试视频");*/
        /*jcVideoPlayerStandard.setUp("http://eyenursevideo.caifutang.com/Look%E5%8A%A8%E7%94%BB/2%E9%AB%98%E6%B8%85/kexuemofangzhongji.mp4"
                , "测试视频");
        ll_transparent.setVisibility(View.GONE);
        jcVideoPlayerStandard.startButton.performClick();*/
    }


    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
        //MobclickAgent.onPause(this);//友盟统计
    }

    @OnClick({R.id.image_back,R.id.textView_confirm,R.id.rl_play})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.rl_play:
                inWify();
                break;
            case R.id.textView_confirm:
                String left = edit_left.getText().toString();
                String right = edit_right.getText().toString();
                if (left.length() == 0) {
                    Toast.makeText(VisionPlayActivity.this, "请输入左眼的视力！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (right.length() == 0) {
                    Toast.makeText(VisionPlayActivity.this, "请输入右眼的视力！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (left.length() > 3 || right.length() > 3) {
                    Toast.makeText(VisionPlayActivity.this, "请输入正确的数据！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    getApiRequestData().getInputCheckData(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                String error = new JSONObject(result).getString("error");
                                if (TextUtil.isNull(error) || "".equals(error)) {
                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                    int issuccess = jsonObject.getInt("issuccess");
                                    if (issuccess > 0) {
                                        Toast.makeText(VisionPlayActivity.this, "数据录入成功！", Toast.LENGTH_SHORT).show();
                                        VisionCheckEndActivity.startActivity(VisionPlayActivity.this, memberId, token);
                                    } else {
                                        Toast.makeText(VisionPlayActivity.this, "数据录入失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, left, memberId + "", right, "2",token);

                }
                break;
        }
    }

    public void onEventMainThread(LoginInfo info) {
        this.memberId = info.getMemberid();
        this.token = info.getToken();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}