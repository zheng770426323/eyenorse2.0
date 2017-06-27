package com.eyenorse.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.eyenorse.alipay.AuthResult;
import com.eyenorse.alipay.PayResult;
import com.eyenorse.alipay.ShoppingInfo;
import com.eyenorse.alipay.SignUtils;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.mine.MyVideoActivity;
import com.ypy.eventbus.EventBus;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by zhengkq on 2017/3/8.
 */

public class AliPayUtil {
    private static final int SDK_PAY_FLAG = 100;
    private static final int SDK_AUTH_FLAG = 200;
    private ShoppingInfo info;
    private Context context;
    private String tradenumber;
    public AliPayUtil(final Context context, ShoppingInfo info, String tradenumber) {
        this.context= context;
        this.info = info;
        this.tradenumber = tradenumber;
        if (TextUtils.isEmpty(ParameterConfig.RSA_PRIVATE)) {
            new AlertDialog.Builder(context).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            ((Activity)context).finish();
                        }
                    }).show();
            return;
        }

        String orderInfo = getOrderInfo(info.getName(), info.getIntro(), info.getCount());   // 订单信息
        android.util.Log.i("orderInfo", orderInfo);
        String sign1 = sign(orderInfo);
        android.util.Log.i("sign1", sign1);
        String encodedSign = "";
        try {
            encodedSign = URLEncoder.encode(sign1, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        final String payInfo = orderInfo + "&sign=\"" + encodedSign + "\"&" + getSignType();
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask((Activity) context);
                Map<String, String> result = alipay.payV2(payInfo, true);
                android.util.Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);


            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(context, "支付成功", Toast.LENGTH_SHORT).show();
                        LoginInfo loginInfo = new LoginInfo();
                        loginInfo.setMemberid(info.getMemberId());
                        loginInfo.setToken(info.getToken());
                        loginInfo.setVip(true);
                        EventBus.getDefault().post(loginInfo);
                        MyVideoActivity.startActivity(context,info.getMemberId(),info.getToken());
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(context, "支付失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Toast.makeText(context,
                                "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        // 其他状态值则为授权失败
                        Toast.makeText(context,
                                "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

                    }
                    break;
                }
            }
        }
    };

    private String getOrderInfo(String subject, String body, String price) {
        //合作者身份ID  之前：2088901234476712
        String orderInfo = "partner=" + "\"" + ParameterConfig.PID + "\"";
        //卖家支付宝账号 之前：migougongsi@163.com
        orderInfo += "&seller_id=" + "\"" + ParameterConfig.TARGET_ID + "\"";
        //商户网站唯一订单
        //orderInfo += "&out_trade_no=" + "\"" + OrderInfoUtil2_0.getOutTradeNo() + "\"";
        orderInfo += "&out_trade_no=" + "\"" + tradenumber + "\"";
        //商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";
        //商品详情
        orderInfo += "&body=" + "\"" + body + "\"";
        //商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";
        //服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + ParameterConfig.ALI_notifyUrl + "\"";
        //接口名称,固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        //支付类型，固定值
        orderInfo += "&payment_type=\"1\"";
        //参数编码，固定值
        orderInfo += "&_input_charset=\"utf-8\"";
        //设置未支付时间，默认30分钟，一旦超过，交易自动关闭，取值范围1m-15d（1分钟至15天），不接受小数
        orderInfo += "&it_b_pay=\"30m\"";
        //支付完成当前页面跳转到指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        //调用银行卡支付，需配置此参数，参与签名，固定值
        //orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }

    private String getSignType() {
        return "sign_type=\"RSA\"";
    }

    private String sign(String orderInfo) {
        android.util.Log.i("orderInfo1", orderInfo);
        return SignUtils.sign(orderInfo.toString(), ParameterConfig.RSA_PRIVATE);
    }
}
