package com.eyenorse.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.eyenorse.utils.StringSHA1;
import com.eyenorse.utils.ValidatorUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/23.
 */

public class PassworgResetActivity extends BaseActivity {
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.edit_password_confirm)
    EditText edit_password_confirm;
    @BindView(R.id.textView_confirm)
    TextView textView_confirm;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    private String inputCaptcha;
    private String inputPhone;
    private JSONObject jsonObject;

    public static void startActivity(Context context,String inputCaptcha,String inputPhone){
        Intent intent = new Intent(context,PassworgResetActivity.class);
        intent.putExtra("inputCaptcha",inputCaptcha);
        intent.putExtra("inputPhone",inputPhone);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);

        setTop(R.color.black);
        setCentreText("重置密码",getResources().getColor(R.color.text_color_2));
        Intent intent = getIntent();
        inputCaptcha = intent.getStringExtra("inputCaptcha");
        inputPhone = intent.getStringExtra("inputPhone");
    }

    @OnClick({R.id.image_back,R.id.textView_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.textView_confirm:
                String password1 = edit_password.getText().toString();
                String password2 = edit_password_confirm.getText().toString();
                if (password1.length()==0){
                    Toast.makeText(PassworgResetActivity.this,"登录密码不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }else if (password1.length()<6){
                    Toast.makeText(PassworgResetActivity.this,"登录密码不能少于6位！",Toast.LENGTH_SHORT).show();
                    return;
                }else if (!ValidatorUtil.isPassword(password1)){
                    Toast.makeText(PassworgResetActivity.this, "登录密码不能位纯数字或字母！", Toast.LENGTH_SHORT).show();
                    return;
                }else if (!password1.equals(password2)){
                    Toast.makeText(PassworgResetActivity.this,"两次输入的密码不同，请确认！",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    getApiRequestData().getMemberPassword(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                int issuccess = jsonObject.getInt("issuccess");
                                if (issuccess==1){
                                    LoginActivity.startActivity(PassworgResetActivity.this);
                                    Toast.makeText(PassworgResetActivity.this,"密码修改成功！",Toast.LENGTH_SHORT).show();
                                    finish();
                                }else if (issuccess==0){
                                    Toast.makeText(PassworgResetActivity.this,"操作失败！",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },inputCaptcha,inputPhone, StringSHA1.encryptToSHA(password1));
                }
                break;
            case R.id.image_back:
                finish();
                break;
        }
    }
}
