package com.eyenorse.VisionTrain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.main.MainActivity;
import com.eyenorse.mine.MyMessageActivity;
import com.eyenorse.mine.MyselfCheckActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/29.
 */

public class VisionCheckEndActivity extends BaseActivity {
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.textView_check)
    TextView textView_check;
    @BindView(R.id.textView_continue)
    TextView textView_continue;
    private int memberId;
    private String token;

    public static void startActivity(Context context,int memberId,String token){
        Intent intent = new Intent(context,VisionCheckEndActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_check_end);
        setTop(R.color.black);
        setCentreText("测试", getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
    }

    @OnClick({R.id.image_back,R.id.textView_check,R.id.textView_continue})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.textView_check:
                MyselfCheckActivity.startActivity(VisionCheckEndActivity.this,memberId,token);
                finish();
                break;
            case R.id.textView_continue:
                MainActivity.startActivity(VisionCheckEndActivity.this);
                finish();
                break;
        }
    }
}
