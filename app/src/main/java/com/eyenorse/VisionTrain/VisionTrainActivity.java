package com.eyenorse.VisionTrain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.mine.ActivateAccountActivity;
import com.eyenorse.mine.LookHistoryActivity;
import com.eyenorse.mine.MyMessageActivity;
import com.eyenorse.mine.MyVideoActivity;
import com.eyenorse.mine.MyselfCheckActivity;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.welcome.LoginActivity;
import com.google.gson.Gson;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/29.
 */

public class VisionTrainActivity extends BaseActivity {
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.textView_next)
    TextView textView_next;
    @BindView(R.id.edit_left)
    EditText edit_left;
    @BindView(R.id.edit_right)
    EditText edit_right;
    private JSONObject jsonObject;
    private int memberId;
    private String token;

    public static void startActivity(Context context,int memberId,String token){
        Intent intent = new Intent(context,VisionTrainActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision_train);
        EventBus.getDefault().register(this);
        setTop(R.color.black);

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
        setCentreText("视力记录",getResources().getColor(R.color.text_color_2));
    }

    @OnClick({R.id.image_back,R.id.textView_next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.textView_next:
                String left = edit_left.getText().toString();
                String right = edit_right.getText().toString();
                if (left.length()==0){
                    Toast.makeText(VisionTrainActivity.this,"请输入左眼的视力！",Toast.LENGTH_SHORT).show();
                    return;
                }else if (right.length()==0){
                    Toast.makeText(VisionTrainActivity.this,"请输入右眼的视力！",Toast.LENGTH_SHORT).show();
                    return;
                }else if (left.length()>4||right.length()>4){
                    Toast.makeText(VisionTrainActivity.this,"请输入正确的数据！",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    getApiRequestData().getInputCheckData(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                String error = new JSONObject(result).getString("error");
                                if (TextUtil.isNull(error) || "".equals(error)) {
                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                    int issuccess = jsonObject.getInt("issuccess");
                                    if (issuccess > 0) {
                                        Toast.makeText(VisionTrainActivity.this, "数据录入成功！", Toast.LENGTH_SHORT).show();
                                        VisionPlayActivity.startActivity(VisionTrainActivity.this, memberId, token);
                                        finish();
                                    } else {
                                        Toast.makeText(VisionTrainActivity.this, "数据录入失败！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },left,memberId+"",right,"1",token);
                }
                break;
        }
    }
    public void onEventMainThread(LoginInfo info) {
        this.memberId = info.getMemberid();
        this.token = info.getToken();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
