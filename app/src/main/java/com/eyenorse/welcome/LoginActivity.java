package com.eyenorse.welcome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.bean.UserInfo;
import com.eyenorse.dialog.LogOutDialog;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.main.MainActivity;
import com.eyenorse.utils.StringSHA1;
import com.eyenorse.utils.ValidatorUtil;
import com.google.gson.Gson;
import com.mob.tools.utils.UIHandler;
import com.ypy.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by zhengkq on 2016/12/22.
 */

public class LoginActivity extends BaseActivity implements PlatformActionListener, Handler.Callback {
    @BindView(R.id.edit_username)
    EditText edit_username;
    @BindView(R.id.edit_password)
    EditText edit_password;
    @BindView(R.id.text_login)
    TextView text_login;
    @BindView(R.id.image_qq)
    ImageView image_qq;
    @BindView(R.id.image_weixin)
    ImageView image_weixin;
    @BindView(R.id.image_weibo)
    ImageView image_weibo;
    @BindView(R.id.text_forget_password)
    TextView text_forget_password;
    @BindView(R.id.text_register)
    TextView text_register;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    private String inputUserName;
    private String inputPassword;
    private JSONObject jsonObject = null;
    private static final int MSG_USERID_FOUND = 1;
    private static final int MSG_LOGIN = 2;
    private static final int MSG_AUTH_CANCEL = 3;
    private static final int MSG_AUTH_ERROR= 4;
    private static final int MSG_AUTH_COMPLETE = 5;
    private OnLoginListener signupListener;
    private String error;
    private String userId;
    private int memberid;
    private LoginInfo loginInfo;
    private String token;

    public static void startActivity(Context context){
        Intent intent = new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_login);
        setTop(R.color.black);
        setCentreText("登录",getResources().getColor(R.color.text_color_2));

        ShareSDK.initSDK(this);
        initView();
    }



    private void initView() {
        closeInputMethod();//页面刚显示时edittext失去焦点

        edit_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = edit_password.getText().length();
                if (length>=6){
                    text_login.setBackground(getResources().getDrawable(R.drawable.login_confirm_bg));
                }
            }
        });
    }

    private void closeInputMethod() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(edit_username.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(edit_password.getWindowToken(), 0);

    }

    @OnClick({R.id.image_back,R.id.text_login,R.id.image_qq,R.id.image_weixin,R.id.image_weibo,R.id.text_forget_password,R.id.text_register})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.text_login:
                inputUserName = edit_username.getText().toString();
                inputPassword = edit_password.getText().toString();

                boolean isMobilen = ValidatorUtil.isMobile(inputUserName);
                boolean isEmail = ValidatorUtil.isEmail(inputUserName);
                if (inputUserName.length()==0){
                    Toast.makeText(LoginActivity.this,"请输入登录名",Toast.LENGTH_SHORT).show();
                    return;
                }else if (inputPassword.length()==0){
                    Toast.makeText(LoginActivity.this,"请输入密码",Toast.LENGTH_SHORT).show();
                    return;
                }else if (!isMobilen&&!isEmail){
                    Toast.makeText(LoginActivity.this,"请输入正确的手机号码或邮箱",Toast.LENGTH_SHORT).show();
                    return;
                }else if ((isMobilen||isEmail)&&inputPassword.length()>=6){
                    //验证是否注册
                    getApiRequestData().getMemberMobileCheck(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                boolean isexist = jsonObject.getBoolean("isexist");
                                if (!isexist){
                                    Toast.makeText(LoginActivity.this,"该手机号不存在！",Toast.LENGTH_SHORT).show();
                                    return;
                                }else {
                                    loginRequest();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },inputUserName);
                }
                break;
            case R.id.image_qq:
                authorize(new QQ(this));
                break;
            case R.id.image_weixin:
                authorize(new Wechat(this));
                break;
            case R.id.image_weibo:
                authorize(new SinaWeibo(this));
                break;
            case R.id.text_forget_password:
                ForgetPasswordActivity.startActivity(LoginActivity.this);
                break;
            case R.id.text_register:
                RegisterCaptchaActivity.startActivity(LoginActivity.this);
                break;
            case R.id.image_back:
                finish();
                MainActivity.startActivity(LoginActivity.this);
                break;
        }
    }

    private void authorize(Platform plat) {
        if(plat.isValid()) {
            String userId = plat.getDb().getUserId();
            if (!TextUtils.isEmpty(userId)) {
                UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
                login(plat.getName(), userId, null);
                //Log.e("headerImage",plat.getDb().getUserIcon());
                return;
            }
        }
        plat.setPlatformActionListener(this);
        plat.SSOSetting(false);
        if(!plat.isClientValid()&&plat.getName().equals("Wechat")) {
            Toast.makeText(this,"该应用未安装,请先安装",Toast.LENGTH_LONG).show();
            return;
        }
        plat.showUser(null);
        setOnLoginListener(new OnLoginListener() {
            @Override
            public boolean onSignin(String platform, HashMap<String, Object> res) {
                return false;
            }

            @Override
            public boolean onSignUp(UserInfo info) {
                return false;
            }
        });
    }

    private void login(String plat, String userId, HashMap<String, Object> userInfo) {
        Message msg = new Message();
        msg.what = MSG_LOGIN;
        msg.obj = userId;
        if (plat.equals("QQ")){
            msg.arg1 = 1;
        }else if (plat.equals("SinaWeibo")){
            msg.arg1 = 3;
        }else if (plat.equals("Wechat")){
            msg.arg1 = 2;
        }
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg) {
        userId = (String)msg.obj;
        int arg1 = msg.arg1;
        switch (msg.what) {
            case MSG_USERID_FOUND: {
                Toast.makeText(this, R.string.userid_found, Toast.LENGTH_SHORT).show();
            }
            break;
            case MSG_LOGIN: {
                thirdPartyLogin(arg1);
            }
            break;
            case MSG_AUTH_CANCEL: {
                Toast.makeText(this, R.string.auth_cancel, Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_CANCEL--------");
            }
            break;
            case MSG_AUTH_ERROR: {
                Toast.makeText(this, R.string.auth_error, Toast.LENGTH_SHORT).show();
                System.out.println("-------MSG_AUTH_ERROR--------");
            }
            break;
            case MSG_AUTH_COMPLETE: {
                Toast.makeText(this, R.string.auth_complete, Toast.LENGTH_SHORT).show();
                System.out.println("--------MSG_AUTH_COMPLETE-------");
            }
            break;
        }
        return false;
    }

    private void thirdPartyLogin(final int arg1) {
        getApiRequestData().getThirdPartyIsRegister(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    int isexist = jsonObject.getInt("isexist");
                    if (isexist>0){
                        getApiRequestData().getThirdPartyLogin(new IOAuthReturnCallBack() {
                            @Override
                            public void onSuccess(String result, Gson gson) {
                                try {
                                    jsonObject = new JSONObject(result).getJSONObject("data");
                                    memberid = jsonObject.getInt("memberid");
                                    token = jsonObject.getString("token");
                                    if (memberid>0&&token.length()>0){
                                        SharedPreferences sp = getSharedPreferences("loginUser", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putBoolean("isload", true);
                                        editor.putInt("memberid", memberid);
                                        editor.putString("token",token);
                                        editor.commit();
                                        LoginInfo loginInfo = new LoginInfo();
                                        loginInfo.setMemberid(memberid);
                                        loginInfo.setIsload(true);
                                        loginInfo.setToken(token);
                                        EventBus.getDefault().post(loginInfo);
                                        MainActivity.startActivity(LoginActivity.this);
                                        finish();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },userId,arg1+"");
                    }else {
                        LogOutDialog logOutDialog = new LogOutDialog(LoginActivity.this,"你是否已注册过平台账户？","去注册","去绑定");
                        logOutDialog.setOnClickChoose(new LogOutDialog.OnClickChoose() {
                            @Override
                            public void onClick(boolean f) {
                                if (f){
                                    BindPhoneActivity.startActivity(LoginActivity.this,userId,arg1);
                                }else{
                                    RegisterCaptchaActivity.startActivity(LoginActivity.this,userId,arg1);
                                }
                            }
                        });
                        logOutDialog.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },userId,arg1+"");
    }

    private void loginRequest() {
        getApiRequestData().getMemberLogin(new IOAuthReturnCallBack() {

            private String token;

            @Override
            public void onSuccess(String result, Gson gson) {

                try {
                    error = new JSONObject(result).getString("error");
                    if (error.equals("")||TextUtils.isEmpty(error)){
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        LoginActivity.this.error = new JSONObject(result).getString("error");
                        memberid = jsonObject.getInt("memberid");
                        token = jsonObject.getString("token");
                        if (memberid >0&&token.length()>0){
                            SharedPreferences sp = getSharedPreferences("loginUser", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isload", true);
                            editor.putInt("memberid", memberid);
                            editor.putString("token",token);
                            editor.commit();
                            loginInfo = new LoginInfo();
                            loginInfo.setMemberid(memberid);
                            loginInfo.setIsload(true);
                            loginInfo.setToken(token);
                            EventBus.getDefault().post(loginInfo);
                            MainActivity.startActivity(LoginActivity.this);
                            finish();
                        }
                    }else {
                        Toast.makeText(LoginActivity.this,error,Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String arg0, String arg1) {
                super.onFailure(arg0, arg1);

                Toast.makeText(LoginActivity.this,arg0,Toast.LENGTH_SHORT).show();
            }

        },inputUserName, StringSHA1.encryptToSHA(inputPassword));
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (i == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_COMPLETE, this);
            login(platform.getName(), platform.getDb().getUserId(), hashMap);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        if (i == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_ERROR, this);
        }
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(Platform platform, int i) {
        if (i == Platform.ACTION_USER_INFOR) {
            UIHandler.sendEmptyMessage(MSG_AUTH_CANCEL, this);
        }
    }
    public void setOnLoginListener(OnLoginListener l) {
        this.signupListener = l;
    }
    public interface OnLoginListener {
        /** 授权完成调用此接口，返回授权数据，如果需要注册，则返回true */
        public boolean onSignin(String platform, HashMap<String, Object> res);

        /** 填写完注册信息后调用此接口，返回true表示数据合法，注册页面可以关闭 */
        public boolean onSignUp(UserInfo info) ;

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            MainActivity.startActivity(LoginActivity.this);
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
