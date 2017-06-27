package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.eyenorse.bean.PersonInfoReset;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.google.gson.Gson;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/26.
 */

public class ActivateAccountActivity extends BaseActivity {
    @BindView(R.id.edit_activate_number)
    EditText edit_activate_number;
    @BindView(R.id.edit_activate_password)
    EditText edit_activate_password;
    @BindView(R.id.textView_confirm)
    TextView textView_confirm;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    private int memberId;
    private String token;
    private JSONObject jsonObject;

    public static void startActivity(Context context,int memberId,String token){
        Intent intent = new Intent(context,ActivateAccountActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_activate);

        setTop(R.color.black);
        setCentreText("账号激活",getResources().getColor(R.color.black));

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
    }
    @OnClick({R.id.textView_confirm,R.id.image_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.textView_confirm:
                String inputNumber = edit_activate_number.getText().toString();
                String inputPassword = edit_activate_password.getText().toString();
                if (inputNumber.length()==0){
                    Toast.makeText(this,"卡号不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }else if (inputPassword.length()==0){
                    Toast.makeText(this,"密码不能为空!",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    getApiRequestData().getActivateAccount(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                String error = new JSONObject(result).getString("error");
                                if (error.length()!=0){
                                    Toast.makeText(ActivateAccountActivity.this,error,Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                int issuccess = jsonObject.getInt("issuccess");
                                if (issuccess!=0){
                                    Toast.makeText(ActivateAccountActivity.this,"账号激活成功!",Toast.LENGTH_SHORT).show();
                                    EventBus.getDefault().post(new PersonInfoReset("已激活",4));
                                    LoginInfo loginInfo = new LoginInfo();
                                    loginInfo.setMemberid(memberId);
                                    loginInfo.setIsload(true);
                                    loginInfo.setToken(token);
                                    EventBus.getDefault().post(loginInfo);
                                    finish();
                                }else {
                                    Toast.makeText(ActivateAccountActivity.this,"账号激活失败,请稍后再试!",Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },inputNumber,memberId+"",inputPassword,token);
                }
                break;
            case R.id.image_back:
                finish();
                break;
            case R.id.rl_message:
                MyMessageActivity.startActivity(ActivateAccountActivity.this);
                break;
        }
    }
}
