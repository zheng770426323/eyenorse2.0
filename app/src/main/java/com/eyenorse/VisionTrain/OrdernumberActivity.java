package com.eyenorse.VisionTrain;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eyenorse.R;
import com.eyenorse.alipay.ShoppingInfo;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.WXpayforInfo;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.AliPayUtil;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.utils.WXpayUtil;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/4/17.
 */

public class OrdernumberActivity extends BaseActivity {
    @BindView(R.id.ll_radio2)
    RelativeLayout ll_radio2;
    @BindView(R.id.image_radio2)
    ImageView image_radio2;
    @BindView(R.id.ll_radio1)
    RelativeLayout ll_radio1;
    @BindView(R.id.image_radio1)
    ImageView image_radio1;
    @BindView(R.id.tv_order_number)
    TextView tv_order_number;
    @BindView(R.id.tv_order_price)
    TextView tv_order_price;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;

    private JSONObject jsonObject;
    private String ordernumber;
    private String goodsid;
    private String prop;
    private String price;
    private int memberid;
    private String token;
    private boolean radio2IsSelect = true;
    private boolean radio1IsSelect = false;
    private String tradenumber;

    public static void startActivity(Context context,String goodsid,String prop,String price){
        Intent intent = new Intent(context,OrdernumberActivity.class);
        intent.putExtra("goodsid",goodsid);
        intent.putExtra("prop",prop);
        intent.putExtra("price",price);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordernumber);

        setCentreText("支付订单",getResources().getColor(R.color.text_color_2));
        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        memberid = sp.getInt("memberid", 0);
        token = sp.getString("token","");
        Intent intent = getIntent();
        goodsid = intent.getStringExtra("goodsid");
        prop = intent.getStringExtra("prop");
        price = intent.getStringExtra("price");
        initView();
    }

    private void initView() {
        tv_order_price.setText(price);
        getApiRequestData().getOrderCreate(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    String error = new JSONObject(result).getString("error");
                    if (error.length()==0|| TextUtil.isNull(error)){
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        ordernumber = jsonObject.getString("ordernumber");
                        if (!TextUtil.isNull(ordernumber)) {
                            tv_order_number.setText(ordernumber);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, goodsid,"1",prop, memberid + "",token);
    }

    @OnClick({R.id.image_back,R.id.ll_radio2,R.id.ll_radio1,R.id.tv_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.ll_radio2:
                image_radio1.setImageResource(R.mipmap.mine_radio);
                image_radio2.setImageResource(R.mipmap.mine_radioed);
                radio1IsSelect = false;
                radio2IsSelect = true;
                break;
            case R.id.ll_radio1:
                image_radio1.setImageResource(R.mipmap.mine_radioed);
                image_radio2.setImageResource(R.mipmap.mine_radio);
                radio1IsSelect = true;
                radio2IsSelect = false;
                break;
            case R.id.tv_confirm:
                if (ordernumber.length()>0){
                    if (radio2IsSelect){
                        final WXpayforInfo info = new WXpayforInfo();
                        info.setProductname("充值");
                        info.setTotalamount(Double.parseDouble(price));
                        getApiRequestData().getThirdParty(new IOAuthReturnCallBack() {
                            @Override
                            public void onSuccess(String result, Gson gson) {
                                try {
                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                    tradenumber = jsonObject.getString("tradenumber");
                                    new WXpayUtil(OrdernumberActivity.this, info, tradenumber);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, memberid + "", ordernumber, "2");
                    }else {
                        getApiRequestData().getThirdParty(new IOAuthReturnCallBack() {
                            @Override
                            public void onSuccess(String result, Gson gson) {
                                try {
                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                    tradenumber = jsonObject.getString("tradenumber");
                                    if (!TextUtil.isNull(tradenumber)) {
                                        ShoppingInfo info = new ShoppingInfo();
                                        info.setCount(price);
                                        info.setIntro("使用支付宝进行充值");
                                        info.setName("充值");
                                        info.setMemberId(memberid);
                                        info.setToken(token);
                                        new AliPayUtil(OrdernumberActivity.this, info, tradenumber);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }, memberid + "", ordernumber, "1");
                    }
                }else {
                    Toast.makeText(OrdernumberActivity.this,"订单号不能为空！",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
