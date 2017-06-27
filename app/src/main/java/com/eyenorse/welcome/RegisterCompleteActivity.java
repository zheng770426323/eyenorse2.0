package com.eyenorse.welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.main.MainActivity;
import com.ypy.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/23.
 */

public class RegisterCompleteActivity extends BaseActivity {
    @BindView(R.id.textView_experience)
    TextView textView_experience;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    private int memberId;
    private String token;

    public static void startActivity(Context context,int memberId,String token){
        Intent intent = new Intent(context,RegisterCompleteActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_complete);

        setTop(R.color.black);
        setCentreText("注册",getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
    }

    @OnClick({R.id.textView_experience,R.id.image_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.textView_experience:
                SharedPreferences sp = getSharedPreferences("loginUser", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isload", true);
                editor.putInt("memberid", memberId);
                editor.putString("token",token);
                editor.commit();
                LoginInfo loginInfo = new LoginInfo();
                loginInfo.setMemberid(memberId);
                loginInfo.setToken(token);
                EventBus.getDefault().post(loginInfo);
                //LoginActivity.startActivity(RegisterCompleteActivity.this);
                MainActivity.startActivity(RegisterCompleteActivity.this);
                finish();
                break;
            case R.id.image_back:
                LoginActivity.startActivity(RegisterCompleteActivity.this);
                finish();
                break;
        }
    }
}
