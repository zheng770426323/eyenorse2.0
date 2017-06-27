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
import com.eyenorse.bean.PersonInfoReset;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.utils.ValidatorUtil;
import com.google.gson.Gson;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/1/7.
 */

public class MailboxResetActivity extends BaseActivity {
    @BindView(R.id.textView_confirm)
    TextView textView_confirm;
    @BindView(R.id.edit_new_mailbox)
    EditText edit_new_mailbox;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    private int memberId;
    private String token;
    private JSONObject jsonObject;
    private long firstClick;

    public static void startActivity(Context context,int memberId,String token){
        Intent intent = new Intent(context,MailboxResetActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mailbox_reset);

        setTop(R.color.black);
        setCentreText("绑定邮箱",getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
    }

    @OnClick({R.id.image_back,R.id.textView_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.textView_confirm:
                long secondClick = System.currentTimeMillis();
                if (secondClick - firstClick < 1000) {
                    firstClick = secondClick;
                    return;
                }
                final String inputEmail = edit_new_mailbox.getText().toString();
                if (inputEmail.length()==0){
                    Toast.makeText(MailboxResetActivity.this,"邮箱不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }else if (!ValidatorUtil.isEmail(inputEmail)){
                    Toast.makeText(MailboxResetActivity.this,"请输入正确的邮箱格式！",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    getApiRequestData().getChangeUserInfo(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                String error = new JSONObject(result).getString("error");
                                if (TextUtil.isNull(error) || "".equals(error)) {
                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                    int issuccess = jsonObject.getInt("issuccess");
                                    if (issuccess == 1) {
                                        EventBus.getDefault().post(new PersonInfoReset(inputEmail, 3));
                                        Toast.makeText(MailboxResetActivity.this, "邮箱修改成功！", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(MailboxResetActivity.this, "邮箱修改失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },inputEmail,null,memberId+"",null,token);
                }
                break;
            case R.id.image_back:
                finish();
                break;
        }
    }
}
