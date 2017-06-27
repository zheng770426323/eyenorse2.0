package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.InvitationInfo;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;
import com.ypy.eventbus.EventBus;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/27.
 */

public class MyLowerInfoActivity extends BaseActivity {
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.simpleDraweeView_head_icon)
    SimpleDraweeView simpleDraweeView_head_icon;
    @BindView(R.id.text_phone)
    TextView text_phone;
    @BindView(R.id.text_mailbox)
    TextView text_mailbox;
    private InvitationInfo invitationInfo;

    public static void startActivity(Context context, InvitationInfo invitationInfo){
        Intent intent = new Intent(context,MyLowerInfoActivity.class);
        intent.putExtra("invitationInfo",(Serializable) invitationInfo);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_lower_info);
        setTop(R.color.black);
        setCentreText("个人资料",getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        invitationInfo = (InvitationInfo) intent.getSerializableExtra("invitationInfo");
        initView();
    }

    private void initView() {
        simpleDraweeView_head_icon.setImageURI(invitationInfo.getHeadimage());
        text_phone.setText(invitationInfo.getUsername());
    }

    @OnClick({R.id.image_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
        }
    }
}
