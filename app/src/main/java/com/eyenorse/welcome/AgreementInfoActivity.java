package com.eyenorse.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.MediaMetadataCompat;
import android.view.View;
import android.webkit.WebView;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.base.Config;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.google.gson.Gson;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/1/16.
 */

public class AgreementInfoActivity extends BaseActivity {
    @BindView(R.id.webapp)
    WebView webapp;
    public static void startActivity(Context context){
        Intent intent = new Intent(context,AgreementInfoActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_info);

        setTop(R.color.black);
        setCentreText("用户协议",getResources().getColor(R.color.text_color_2));
        initView();
    }

    private void initView() {
        String URL = Config.Base_Url + Config.Xieyi;
        webapp.loadUrl(URL);
       /* getApiRequestData().getAgreementInfo(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                webapp
            }
        });*/
    }
    @OnClick({R.id.image_back})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_back:
                finish();
        }
    }
}
