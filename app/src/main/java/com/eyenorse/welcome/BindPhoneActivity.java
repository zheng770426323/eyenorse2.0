package com.eyenorse.welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.main.MainActivity;
import com.eyenorse.utils.ValidatorUtil;
import com.google.gson.Gson;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/2/4.
 */

public class BindPhoneActivity extends BaseActivity {
    @BindView(R.id.textView_next)
    TextView textView_next;
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.text_send_captcha)
    TextView text_send_captcha;
    @BindView(R.id.edit_captcha)
    EditText edit_captcha;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    private boolean isSendCaptcha = true;
    private JSONObject jsonObject;
    private String inputPhone;
    private int recLen = 60;
    private String inputCaptcha;
    private String thirdpartyaccount;
    private int thirdpartytype;
    private int memberid;
    private String token;

    public static void startActivity(Context context, String thirdpartyaccount, int thirdpartytype) {
        Intent intent = new Intent(context, BindPhoneActivity.class);
        intent.putExtra("thirdpartyaccount", thirdpartyaccount);
        intent.putExtra("thirdpartytype", thirdpartytype);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_phone);
        setTop(R.color.black);
        setCentreText("绑定手机",getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        thirdpartyaccount = intent.getStringExtra("thirdpartyaccount");
        thirdpartytype = intent.getIntExtra("thirdpartytype", 0);
    }

    @OnClick({R.id.textView_next, R.id.text_send_captcha,R.id.image_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_next:
                inputCaptcha = edit_captcha.getText().toString();
                inputPhone = edit_phone.getText().toString();
                if (inputCaptcha.length() == 0) {
                    Toast.makeText(BindPhoneActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (inputCaptcha.length() != 0 && inputCaptcha.length() != 6) {
                    Toast.makeText(BindPhoneActivity.this, "请输入6位验证码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (inputCaptcha.length() == 6 && inputPhone.length() == 11) {
                    getApiRequestData().getCaptchaCheckMobile(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                int istrue = jsonObject.getInt("istrue");
                                if (istrue == 1) {
                                    getApiRequestData().getBindThirdParty(new IOAuthReturnCallBack() {
                                        @Override
                                        public void onSuccess(String result, Gson gson) {
                                            try {
                                                jsonObject = new JSONObject(result).getJSONObject("data");
                                                memberid = jsonObject.getInt("memberid");
                                                if (memberid > 0) {
                                                    initRequestThirdPartyLogin();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, inputCaptcha, inputPhone, null, thirdpartyaccount, thirdpartytype + "");
                                } else {
                                    Toast.makeText(BindPhoneActivity.this, "验证码不正确", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, inputCaptcha, inputPhone, "5");
                }
                break;
            case R.id.text_send_captcha:
                if (!isSendCaptcha) {
                    return;
                }
                inputPhone = edit_phone.getText().toString();
                if (inputPhone.length() == 0) {
                    Toast.makeText(BindPhoneActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ValidatorUtil.isMobile(inputPhone)) {
                    getApiRequestData().getMemberMobileCheck(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                boolean isexist = jsonObject.getBoolean("isexist");
                                if (isexist) {
                                    requestSendCaptcha();
                                } else {
                                    Toast.makeText(BindPhoneActivity.this, "该手机号未注册！", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, inputPhone);
                } else {
                    Toast.makeText(BindPhoneActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.image_back:
                finish();
                break;
        }
    }

    private void initRequestThirdPartyLogin() {
        getApiRequestData().getThirdPartyLogin(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    memberid = jsonObject.getInt("memberid");
                    token = jsonObject.getString("token");
                    if (memberid > 0) {
                        SharedPreferences sp = getSharedPreferences("loginUser", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("isload", true);
                        editor.putInt("memberid", memberid);
                        editor.putString("token", token);
                        editor.commit();
                        LoginInfo loginInfo = new LoginInfo();
                        loginInfo.setMemberid(memberid);
                        loginInfo.setIsload(true);
                        loginInfo.setToken(token);
                        EventBus.getDefault().post(loginInfo);
                        MainActivity.startActivity(BindPhoneActivity.this);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, thirdpartyaccount, thirdpartytype + "");
    }

    private void requestSendCaptcha() {
        getApiRequestData().getCaptchaSendMobile(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                //Log.e("result------","result------:"+result);
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    boolean issuccess = jsonObject.getBoolean("issuccess");
                    if (issuccess) {
                        Toast.makeText(BindPhoneActivity.this, "验证码已发送！", Toast.LENGTH_SHORT).show();
                        sendPhone(inputPhone);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, inputPhone, "5");
    }

    private void sendPhone(String s) {
        recLen = 60;
        initSetTime();//设置倒计时
    }

    //设置倒计时
    private void initSetTime() {
        text_send_captcha.setText("重新发送（60s）");
        text_send_captcha.setTextColor(getResources().getColor(R.color.white));
        text_send_captcha.setBackgroundColor(getResources().getColor(R.color.text_hint));
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
                        text_send_captcha.setText("重新发送(" + recLen + "s)");
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);
                        isSendCaptcha = false;// send message
                    } else {
                        text_send_captcha.setTextColor(getResources().getColor(R.color.text_color_red));
                        text_send_captcha.setText("重新获取验证码");
                        text_send_captcha.setBackgroundResource(R.drawable.captcha_enabled);
                        isSendCaptcha = true;
                        text_send_captcha.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String phone = edit_phone.getText().toString();
                                if (phone.length() == 11 && ValidatorUtil.isMobile(phone)) {
                                    requestSendCaptcha();
                                }
                            }
                        });
                    }
            }
            super.handleMessage(msg);
        }
    };
}
