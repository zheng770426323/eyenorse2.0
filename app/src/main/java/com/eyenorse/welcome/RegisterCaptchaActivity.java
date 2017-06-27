package com.eyenorse.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.http.ApiRequestData;
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

public class RegisterCaptchaActivity extends BaseActivity {
    @BindView(R.id.edit_phone)
    EditText edit_phone;
    @BindView(R.id.text_send_captcha)
    TextView text_send_captcha;
    @BindView(R.id.edit_captcha)
    EditText edit_captcha;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.textView_next)
    TextView textView_next;
    @BindView(R.id.ll_select)
    LinearLayout ll_select;
    @BindView(R.id.image_select)
    ImageView image_select;
    @BindView(R.id.ll_xieyi)
    LinearLayout ll_xieyi;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    private int recLen = 60;
    private boolean isSelect = true;
    private int length;
    private String inputPhone;
    private String inputCaptcha;
    private String inputPassword;
    private JSONObject jsonObject;
    private boolean isSendCaptcha = true;
    private String thirdpartyaccount;
    private int thirdpartytype;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, RegisterCaptchaActivity.class);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String thirdpartyaccount, int thirdpartytype) {
        Intent intent = new Intent(context, RegisterCaptchaActivity.class);
        intent.putExtra("thirdpartyaccount", thirdpartyaccount);
        intent.putExtra("thirdpartytype", thirdpartytype);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_captcha);

        setTop(R.color.black);
        setCentreText("注册",getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        thirdpartyaccount = intent.getStringExtra("thirdpartyaccount");
        thirdpartytype = intent.getIntExtra("thirdpartytype", 0);

        initView();
    }

    private void initView() {

        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                length = edit_password.getText().length();
                if (length >= 6 && isSelect) {
                    textView_next.setBackground(getResources().getDrawable(R.drawable.login_confirm_bg));
                }
            }
        });
    }

    @OnClick({R.id.image_back, R.id.text_send_captcha, R.id.textView_next, R.id.ll_select, R.id.ll_xieyi})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_send_captcha:
                if (!isSendCaptcha) {
                    return;
                }
                inputPhone = edit_phone.getText().toString();
                if (inputPhone.length() == 0) {
                    Toast.makeText(RegisterCaptchaActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(RegisterCaptchaActivity.this, "该手机号已注册！", Toast.LENGTH_SHORT).show();
                                } else {
                                    requestSendCaptcha();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, inputPhone);

                } else {
                    Toast.makeText(RegisterCaptchaActivity.this, "请输入正确的手机号码！", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.textView_next:
                inputCaptcha = edit_captcha.getText().toString();
                inputPassword = edit_password.getText().toString();
                inputPhone = edit_phone.getText().toString();
                if (inputCaptcha.length() == 0) {
                    Toast.makeText(RegisterCaptchaActivity.this, "验证码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (inputCaptcha.length() != 0 && inputCaptcha.length() != 6) {
                    Toast.makeText(RegisterCaptchaActivity.this, "请输入6位验证码！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (inputPassword.length() == 0) {
                    Toast.makeText(RegisterCaptchaActivity.this, "登录密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (inputPassword.length() <6){
                    Toast.makeText(RegisterCaptchaActivity.this, "登录密码不能少于6位！", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!isSelect) {
                    Toast.makeText(RegisterCaptchaActivity.this, "请同意协议！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!ValidatorUtil.isPassword(inputPassword)){
                    Toast.makeText(RegisterCaptchaActivity.this, "登录密码不能为纯数字或字母！", Toast.LENGTH_SHORT).show();
                    return;
                }else if (inputCaptcha.length() == 6 && ValidatorUtil.isPassword(inputPassword)) {
                    getApiRequestData().getCaptchaCheckMobile(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                int istrue = jsonObject.getInt("istrue");
                                if (istrue == 1) {
                                    if (thirdpartytype > 0 && !TextUtil.isNull(thirdpartyaccount)) {
                                        RegisterSettingNameActivity.startActivity(RegisterCaptchaActivity.this, inputCaptcha, inputPassword, inputPhone, thirdpartyaccount, thirdpartytype);
                                    } else {
                                        RegisterSettingNameActivity.startActivity(RegisterCaptchaActivity.this, inputCaptcha, inputPassword, inputPhone);
                                    }
                                } else if (istrue == 0) {
                                    Toast.makeText(RegisterCaptchaActivity.this, "验证码输入错误！", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, inputCaptcha, inputPhone, "1");
                }
                break;
            case R.id.ll_select:
                if (isSelect) {
                    image_select.setBackground(getResources().getDrawable(R.mipmap.unselected));
                } else {
                    image_select.setBackground(getResources().getDrawable(R.mipmap.select));
                    if (length >= 6) {
                        textView_next.setBackground(getResources().getDrawable(R.drawable.button_next_bg));
                    }
                }
                isSelect = !isSelect;
                break;
            case R.id.ll_xieyi:
                AgreementInfoActivity.startActivity(RegisterCaptchaActivity.this);
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
                //Log.e("result------","result------:"+result);
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    boolean issuccess = jsonObject.getBoolean("issuccess");
                    if (issuccess) {
                        Toast.makeText(RegisterCaptchaActivity.this, "验证码已发送！", Toast.LENGTH_SHORT).show();
                        isSendCaptcha = false;// send message
                        sendPhone(inputPhone);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, inputPhone, "1");
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
