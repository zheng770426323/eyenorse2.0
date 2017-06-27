package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/1/3.
 */

public class LoginPasswordManagerActivity extends BaseActivity{
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.rl_password_manager)
    RelativeLayout rl_password_manager;

    private int memberId;
    private String token;
    private JSONObject jsonObject;

    public static void startActivity(Context context,int memberId,String token){
        Intent intent = new Intent(context,LoginPasswordManagerActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_manager);

        setTop(R.color.black);
        setCentreText("登录密码管理",getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
    }

   /* private void initMessage() {
        getApiRequestData().getNoneReadMessage(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    String error = new JSONObject(result).getString("error");
                    if (TextUtil.isNull(error)||"".equals(error)) {
                        JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                        int noticenum = jsonObject.getInt("noticenum");
                        if (noticenum == 0) {
                            image_message_icon.setVisibility(View.INVISIBLE);
                        } else {
                            image_message_icon.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },memberId+"",token);
    }*/

    @OnClick({R.id.image_back,R.id.rl_password_manager})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.rl_password_manager:
                PasswordManager2Activity.startActivity(LoginPasswordManagerActivity.this,memberId,token);
                break;
        }
    }
}
