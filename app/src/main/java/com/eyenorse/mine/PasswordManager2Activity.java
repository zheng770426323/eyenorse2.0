package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.VisionTrain.SearchActivity;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.StringSHA1;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.utils.ValidatorUtil;
import com.eyenorse.welcome.LoginActivity;
import com.eyenorse.welcome.RegisterCaptchaActivity;
import com.google.gson.Gson;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/1/3.
 */

public class PasswordManager2Activity extends BaseActivity {
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.textView_confirm)
    TextView textView_confirm;
    @BindView(R.id.edit_old_password)
    EditText edit_old_password;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.edit_password_confirm)
    EditText edit_password_confirm;

    private int memberId;
    private String token;
    private JSONObject jsonObject;
    private String error;

    public static void startActivity(Context context, int memberId,String token) {
        Intent intent = new Intent(context, PasswordManager2Activity.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_manager2);
        setTop(R.color.black);
        setCentreText("登录密码修改",getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
    }

    @OnClick({R.id.image_back, R.id.textView_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.textView_confirm:
                String inputOldPassword = edit_old_password.getText().toString();
                String inputNewPassword = edit_password.getText().toString();
                String inputRepeatPassword = edit_password_confirm.getText().toString();
                if (inputOldPassword.length() == 0) {
                    Toast.makeText(PasswordManager2Activity.this, "原密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (inputNewPassword.length() == 0) {
                    Toast.makeText(PasswordManager2Activity.this, "新密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (inputRepeatPassword.length() == 0) {
                    Toast.makeText(PasswordManager2Activity.this, "确认新密码不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (inputOldPassword.length() < 6) {
                    Toast.makeText(PasswordManager2Activity.this, "密码长度不能小于6位！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (inputNewPassword.length() < 6) {
                    Toast.makeText(PasswordManager2Activity.this, "新密码长度不能小于6位！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!ValidatorUtil.isPassword(inputNewPassword)){
                    Toast.makeText(PasswordManager2Activity.this, "登录密码不能为纯数字或字母！", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!inputNewPassword.equals(inputRepeatPassword)) {
                    Toast.makeText(PasswordManager2Activity.this, "两次输入的密码不同！", Toast.LENGTH_SHORT).show();
                    return;
                }else if(inputOldPassword.equals(inputNewPassword)){
                    Toast.makeText(PasswordManager2Activity.this, "新密码不能与原密码相同！", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    getApiRequestData().getChangePassword(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                error = new JSONObject(result).getString("error");
                                if (error.equals("") || TextUtils.isEmpty(error)) {
                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                    if (jsonObject != null) {

                                        int issuccess = jsonObject.getInt("issuccess");
                                        if (issuccess == 1) {
                                            Toast.makeText(PasswordManager2Activity.this, "密码修改成功！", Toast.LENGTH_SHORT).show();
                                            SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sp.edit();
                                            editor.putInt("memberid", 0);
                                            editor.putString("token","");
                                            editor.commit();
                                            LoginActivity.startActivity(PasswordManager2Activity.this);
                                            LoginInfo info = new LoginInfo();
                                            info.setMemberid(0);
                                            info.setToken("");
                                            EventBus.getDefault().post(info);
                                            finish();
                                        }else {
                                            //Toast.makeText(PasswordManager2Activity.this, "新密码不能与原密码相同！", Toast.LENGTH_SHORT).show();
                                            //Toast.makeText(PasswordManager2Activity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, memberId + "", StringSHA1.encryptToSHA(inputNewPassword), StringSHA1.encryptToSHA(inputOldPassword),token);
                }
                break;
        }
    }
}
