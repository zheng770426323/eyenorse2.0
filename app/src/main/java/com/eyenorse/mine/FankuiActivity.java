package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.ValidatorUtil;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/4/11.
 */

public class FankuiActivity extends BaseActivity {

    @BindView(R.id.et_problem)
    EditText et_problem;
    @BindView(R.id.et_number)
    EditText et_number;
    @BindView(R.id.tv_send)
    TextView tv_send;

    private String token;
    private int memberId;
    private String number;
    private String content;
    private long firstClick;

    public static void startActivity(Context context,int memberId,String token){
        Intent intent = new Intent(context,FankuiActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fankui);
        setTop(R.color.black);
        setCentreText("问题反馈",getResources().getColor(R.color.text_color_2));

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");

        et_problem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if (et_number.getText().toString().length()>0&&et_problem.getText().toString().length()>0){
                   tv_send.setBackground(getResources().getDrawable(R.drawable.login_confirm_bg));
               }
            }
        });

        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_number.getText().toString().length()>0&&et_problem.getText().toString().length()>0){
                    tv_send.setBackground(getResources().getDrawable(R.drawable.login_confirm_bg));
                }
            }
        });
    }

    @OnClick({R.id.image_back,R.id.tv_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.tv_send:
                number = et_number.getText().toString();
                content = et_problem.getText().toString();
                long secondClick = System.currentTimeMillis();
                if (secondClick - firstClick < 1000) {
                    firstClick = secondClick;
                    return;
                }
                if (number.length()==0||content.length()==0){
                    return;
                }
                if (ValidatorUtil.isMobile(number)||ValidatorUtil.isEmail(number)||ValidatorUtil.isQQ(number)){
                    getApiRequestData().getAddFeedBack(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            String error = null;
                            try {
                                error = new JSONObject(result).getString("error");
                                if (error.equals("") || TextUtils.isEmpty(error)) {
                                    JSONObject data = new JSONObject(result).getJSONObject("data");
                                    int issuccess = data.getInt("issuccess");
                                    if (issuccess>0){
                                        Toast.makeText(FankuiActivity.this, "提交成功！", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },number,content,memberId+"",token);
                }else {
                    Toast.makeText(FankuiActivity.this, "请输入正确的QQ、邮箱或电话！", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
