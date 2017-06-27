package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.google.gson.Gson;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/4/5.
 */

public class ActivateAgencyActivity extends BaseActivity {
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
        Intent intent = new Intent(context,ActivateAgencyActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_agency);

        setTop(R.color.black);
        setCentreText("代理激活",getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");

    }

    @OnClick({R.id.textView_confirm, R.id.image_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textView_confirm:
                String inputNumber = edit_activate_number.getText().toString();
                String inputPassword = edit_activate_password.getText().toString();
                if (inputNumber.length() == 0) {
                    Toast.makeText(this, "请输入激活码!", Toast.LENGTH_SHORT).show();
                    return;
                } else if (inputPassword.length() == 0) {
                    Toast.makeText(this, "请输入激活码的密码!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    getApiRequestData().getActivateAgency(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                String error = new JSONObject(result).getString("error");
                                if (error.length() != 0) {
                                    Toast.makeText(ActivateAgencyActivity.this, error, Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                int issuccess = jsonObject.getInt("issuccess");
                                if (issuccess == 1) {
                                    Toast.makeText(ActivateAgencyActivity.this, "代理商激活成功!", Toast.LENGTH_SHORT).show();
                                    LoginInfo loginInfo = new LoginInfo();
                                    loginInfo.setMemberid(memberId);
                                    loginInfo.setIsload(true);
                                    loginInfo.setToken(token);
                                    EventBus.getDefault().post(loginInfo);
                                    finish();
                                } else {
                                    Toast.makeText(ActivateAgencyActivity.this, "激活失败,请稍后再试!", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, inputNumber, memberId + "", inputPassword, token);
                }
                break;
            case R.id.image_back:
                finish();
                break;
        }
    }
}
