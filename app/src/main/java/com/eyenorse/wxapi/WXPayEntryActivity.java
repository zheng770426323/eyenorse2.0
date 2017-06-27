package com.eyenorse.wxapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.mine.MyVideoActivity;
import com.eyenorse.utils.ParameterConfig;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.ypy.eventbus.EventBus;

/**
 * Created by zhengkq on 2017/1/17.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;
    // private TextView reulttv;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wx_pay_result);
        api = WXAPIFactory.createWXAPI(this, ParameterConfig.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            //builder.setMessage(getString(R.string.pay_result_callback_msg, String.valueOf(resp.errCode)));
            builder.show();
            Intent intent;
            int code = resp.errCode;
            switch (code) {
                case 0:
                    Toast.makeText(this, "支付成功",Toast.LENGTH_SHORT).show();
                    /*intent=new Intent(this,PayOffSelectActivity.class);
                    intent.putExtra("result", 0);
                    startActivity(intent);*/
                    SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
                    int memberid = sp.getInt("memberid", 0);
                    String token = sp.getString("token","");
                    LoginInfo loginInfo = new LoginInfo();
                    loginInfo.setMemberid(memberid);
                    loginInfo.setToken(token);
                    loginInfo.setVip(true);
                    EventBus.getDefault().post(loginInfo);
                    MyVideoActivity.startActivity(WXPayEntryActivity.this,memberid,token);
                    finish();
                    finish();
                    break;
                case -1:
                    Toast.makeText(this, "支付失败",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case -2:
                    Toast.makeText(this, "支付取消",Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                default:
                    break;
            }
        }
    }
}
