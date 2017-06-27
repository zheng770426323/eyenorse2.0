package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.bean.PersonInfoReset;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.welcome.LoginActivity;
import com.eyenorse.welcome.PassworgResetActivity;
import com.eyenorse.welcome.RegisterSettingNameActivity;
import com.google.gson.Gson;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/1/7.
 */

public class NickNameResetActivity extends BaseActivity {
    @BindView(R.id.textView_confirm)
    TextView textView_confirm;
    @BindView(R.id.edit_new_nickname)
    EditText edit_new_nickname;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    private int memberId;
    private String token;
    private JSONObject jsonObject;

    public static void startActivity(Context context,int memberId,String token){
        Intent intent = new Intent(context,NickNameResetActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nick_name_reset);
        setTop(R.color.black);
        setCentreText("重置昵称",getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
    }

    @OnClick({R.id.image_back,R.id.textView_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.textView_confirm:
                final String inputNickname = edit_new_nickname.getText().toString();
                if (inputNickname.length()==0){
                    Toast.makeText(NickNameResetActivity.this,"昵称不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }else if (inputNickname.length()<2){
                    Toast.makeText(NickNameResetActivity.this,"昵称长度不能小于2位！",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    getApiRequestData().getMemberNameCheck(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                String error = new JSONObject(result).getString("error");
                                if (TextUtil.isNull(error) || "".equals(error)) {
                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                    boolean isexist = jsonObject.getBoolean("isexist");
                                    if (isexist) {
                                        Toast.makeText(NickNameResetActivity.this, "该昵称已存在！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        getApiRequestData().getChangeUserInfo(new IOAuthReturnCallBack() {
                                            @Override
                                            public void onSuccess(String result, Gson gson) {
                                                try {
                                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                                    int issuccess = jsonObject.getInt("issuccess");
                                                    if (issuccess == 1) {
                                                        EventBus.getDefault().post(new PersonInfoReset(inputNickname, 1));
                                                        Toast.makeText(NickNameResetActivity.this, "昵称修改成功！", Toast.LENGTH_SHORT).show();
                                                        LoginInfo loginInfo = new LoginInfo();
                                                        loginInfo.setMemberid(memberId);
                                                        loginInfo.setIsload(true);
                                                        loginInfo.setToken(token);
                                                        EventBus.getDefault().post(loginInfo);
                                                        finish();
                                                    } else {
                                                        Toast.makeText(NickNameResetActivity.this, "昵称修改失败！", Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }, null, null, memberId + "", inputNickname, token);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, inputNickname);
                }
                break;
            case R.id.image_back:
                finish();
                break;
        }
    }
}
