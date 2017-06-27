package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.base.Config;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.dialog.LogOutDialog;
import com.eyenorse.dialog.MyProgressDialog;
import com.eyenorse.http.ApiRequestData;
import com.eyenorse.http.AsyncHttpUtils;
import com.eyenorse.http.FactoryTools;
import com.eyenorse.http.FileAuthReturnCallBack;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.ApkUpdate;
import com.eyenorse.utils.ClearMemoryUtils;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.welcome.AgreementInfoActivity;
import com.eyenorse.welcome.LoginActivity;
import com.eyenorse.welcome.RegisterCaptchaActivity;
import com.google.gson.Gson;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/26.
 */

public class SettingActivity extends BaseActivity {
    @BindView(R.id.textView_back)
    TextView textView_back;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.tv_banben)
    TextView tv_banben;
    @BindView(R.id.tv_xieyi)
    TextView tv_xieyi;
    @BindView(R.id.tv_fankui)
    TextView tv_fankui;
    @BindView(R.id.tv_update)
    TextView tv_update;
    @BindView(R.id.textView_memory)
    TextView textView_memory;
    private int memberId;
    private String token;
    private MyProgressDialog progressDialog;

    public static void startActivity(Context context,int memberId,String token){
        Intent intent = new Intent(context,SettingActivity.class);
        intent.putExtra("memberId",memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        setTop(R.color.black);
        setCentreText("系统设置",getResources().getColor(R.color.text_color_2));
        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");

        try {
            String versionName = getVersionName();
            if (Config.Base_Url.equals("http://yanhushi.caifutang.com/api/")){
                tv_banben.setText("版本号："+versionName+"(改版-测试)");
            }else {
                tv_banben.setText("版本号："+versionName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        initMemory();
    }

    private String getVersionName() throws Exception
    {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        String version = packInfo.versionName;
        return version;
    }

    @OnClick({R.id.image_back,R.id.textView_back,R.id.tv_xieyi,R.id.tv_fankui,R.id.tv_update,R.id.tv_clear_cache})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.textView_back:
                LogOutDialog logOutDialog = new LogOutDialog(SettingActivity.this,"确定要退出吗？","取消","确定");
                logOutDialog.setOnClickChoose(new LogOutDialog.OnClickChoose() {
                    @Override
                    public void onClick(boolean f) {
                        if (f){
                            SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putBoolean("isload", false);
                            editor.putInt("memberid", 0);
                            editor.putString("token","");
                            editor.commit();
                            LoginActivity.startActivity(SettingActivity.this);
                            finish();
                            LoginInfo loginInfo = new LoginInfo();
                            loginInfo.setIsload(false);
                            loginInfo.setMemberid(0);
                            loginInfo.setToken("");
                            EventBus.getDefault().post(loginInfo);
                        }
                    }
                });
                logOutDialog.show();
                break;
            case R.id.tv_xieyi:
                AgreementInfoActivity.startActivity(SettingActivity.this);
                break;
            case R.id.tv_fankui:
                if (memberId>0){
                    FankuiActivity.startActivity(SettingActivity.this,memberId,token);
                }else {
                    LoginActivity.startActivity(SettingActivity.this);
                }
                break;
            case R.id.tv_update:
                ApkUpdate apkUpdate = new ApkUpdate(this);
                apkUpdate.getJanUpdate(true);
                break;
            case R.id.tv_clear_cache:
                LogOutDialog dialog = new LogOutDialog(SettingActivity.this, "清除后一些数据需要重新加载哦~","取消","确定");
                dialog.show();
                dialog.setOnClickChoose(new LogOutDialog.OnClickChoose() {
                    @Override
                    public void onClick(boolean f) {
                        if (f){
                            try {
                                ClearMemoryUtils.clearAllCache(SettingActivity.this);
                                recLen = 5;
                                Message message = handler.obtainMessage(1);
                                handler.sendMessageDelayed(message, 1000);
                                progressDialog.show();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                break;
        }
    }

    //计算缓存
    private void initMemory() {
        progressDialog = new MyProgressDialog(SettingActivity.this, "正在清除缓存...");
        try {
            String memory = ClearMemoryUtils.getTotalCacheSize(SettingActivity.this);
            textView_memory.setText(memory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int recLen = 5;
    final Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    recLen--;
                    if (recLen > 0) {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);
                    } else {
                        progressDialog.dismiss();
                        initMemory();
                    }
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        AndPermission.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    // 成功回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionYes(101)
    private void getLocationYes() {
        // 申请权限成功，可以去做点什么了。
        downloadFile();
    }

    // 失败回调的方法，用注解即可，里面的数字是请求时的requestCode。
    @PermissionNo(101)
    private void getLocationNo() {
        // 申请权限失败，可以提醒一下用户。
        Toast.makeText(SettingActivity.this,"下载的apk文件需要存入sd卡，没有该权限无法更新",Toast.LENGTH_SHORT).show();
    }

    /**
     * @throws Exception
     */
    private void downloadFile() {

        String filesd = Environment.getExternalStorageDirectory().getPath();
        File tmpFile = new File(filesd + "/eyenorse");
        if (!tmpFile.exists()) {
            tmpFile.mkdir();
        }
        File file = new File(filesd + "/eyenorse/app-release_132_jiagu_sign.apk");
        AsyncHttpUtils.getInstance(this).ShowFileDialog(null);
        AsyncHttpUtils.getInstance(this).doFlie(Config.Base + "app-release_132_jiagu_sign.apk", file, new FileAuthReturnCallBack() {
            @Override
            public void onSuccess(File file) {
                if (file != null && file.exists()) {
                    installApk(file);
//                    install(file.getPath());
                } else {
                    FactoryTools.setToastShow(SettingActivity.this, "安装包下载失败，请重试");
                }
            }

            @Override
            public void onFailure(String arg0, String arg1) {

            }
        });
    }

    // 安装Apk：
    private void install(String fileName) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(new File(fileName)),
                "application/vnd.android.package-archive");
        startActivity(intent);
    }

    /**
     * 安装 apk 文件
     * 安卓官方为了提高私有文件的安全性，面向 Android 7.0 或更高版本的应用私有目录被限制访问
     * 分享私有文件内容的推荐方法是使用 FileProvider
     * @param apkFile
     */
    public void installApk(File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(this, "com.eyenorse", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        startActivity(intent);
    }

}
