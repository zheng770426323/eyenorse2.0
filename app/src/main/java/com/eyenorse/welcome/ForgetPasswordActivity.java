package com.eyenorse.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.utils.ValidatorUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/23.
 */

public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.text_send_captcha)
    TextView text_send_captcha;
    @BindView(R.id.edit_captcha)
    EditText edit_captcha;
    @BindView(R.id.textView_next)
    TextView textView_next;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    private int recLen = 60;
    private JSONObject jsonObject;
    private String inputPhone;
    private boolean isSendCaptcha = true;
    private String error;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ForgetPasswordActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        setTop(R.color.black);
        setCentreText("忘记密码",getResources().getColor(R.color.text_color_2));
        initView();
    }

    private void initView() {

    }

    @OnClick({R.id.image_back, R.id.text_send_captcha, R.id.textView_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_send_captcha:
                if (!isSendCaptcha) {
                    return;
                }
                inputPhone = edit_phone.getText().toString();
                Log.e("inputPhone",inputPhone);
                if (inputPhone.length() == 0) {
                    Toast.makeText(ForgetPasswordActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(ForgetPasswordActivity.this, "该手机号未注册！", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, inputPhone);
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.textView_next:
                final String inputCaptcha = edit_captcha.getText().toString();
                inputPhone = edit_phone.getText().toString();
                if (inputCaptcha.length() == 0) {
                    Toast.makeText(ForgetPasswordActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else if (inputCaptcha.length() != 0 && inputCaptcha.length() != 6) {
                    Toast.makeText(ForgetPasswordActivity.this, "请输入6位验证码", Toast.LENGTH_SHORT).show();
                } else {
                    getApiRequestData().getCaptchaCheckMobile(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                error = new JSONObject(result).getString("error");
                                if (error.equals("")|| TextUtil.isNull(error)){
                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                    int istrue = jsonObject.getInt("istrue");
                                    if (istrue == 1) {
                                        PassworgResetActivity.startActivity(ForgetPasswordActivity.this, inputCaptcha, inputPhone);
                                    } else if (istrue == 0) {
                                        Toast.makeText(ForgetPasswordActivity.this, "验证码输入错误！", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(ForgetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, inputCaptcha, inputPhone, "2");
                }
                break;
            case R.id.image_back:
                finish();
                break;
        }
    }

    private void requestSendCaptcha() {
        getApiRequestData().getCaptchaSendMobile(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    error = new JSONObject(result).getString("error");
                    if (error.equals("")|| TextUtil.isNull(error)){
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        boolean issuccess = jsonObject.getBoolean("issuccess");
                        if (issuccess) {
                            Toast.makeText(ForgetPasswordActivity.this, "验证码已发送！", Toast.LENGTH_SHORT).show();
                            isSendCaptcha = false;// send message
                            sendPhone(inputPhone);
                        }
                    }else {
                        Toast.makeText(ForgetPasswordActivity.this, error, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, inputPhone, "2");
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
                        handler.sendMessageDelayed(message, 1000);      // send message

                    } else {
                        text_send_captcha.setTextColor(getResources().getColor(R.color.text_color_red));
                        text_send_captcha.setText("重新获取验证码");
                        text_send_captcha.setBackgroundResource(R.drawable.captcha_enabled);
                        isSendCaptcha = true;
                    }
            }
            super.handleMessage(msg);
        }
    };

    private void sendPhone(String s) {
        recLen = 60;
        initSetTime();//设置倒计时
    }

    //设置倒计时
    private void initSetTime() {
        text_send_captcha.setText("重新发送(60s)");
        text_send_captcha.setTextColor(getResources().getColor(R.color.white));
        text_send_captcha.setBackgroundColor(getResources().getColor(R.color.text_hint));
        Message message = handler.obtainMessage(1);     // Message
        handler.sendMessageDelayed(message, 1000);
    }
}
