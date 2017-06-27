package com.eyenorse.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.eyenorse.base.Config;
import com.eyenorse.dialog.LogOutDialog;
import com.eyenorse.http.ApiRequestData;
import com.eyenorse.http.AsyncHttpUtils;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.google.gson.Gson;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

public class ApkUpdate {
    private Context context;

    private String apkurl;

    public String getApkurl() {
        return apkurl;
    }

    public ApkUpdate(Context context) {
        this.context = context;
    }

    public void getJanUpdate(final boolean bolVersion) {// 检测更新
        if (bolVersion) {
            FactoryTools.setToastShow(context, "版本检测中....");
        }
        AsyncHttpUtils.getInstance(context).doGet(Config.Base + "eyenorse.txt", null, new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    Log.e("httpresult", "result=" + result);
                    final String[] jsons = result.split(",");
                    int newver = Integer.parseInt(jsons[0].trim().replaceAll(
                            "\\.", ""));
                    int ver = Integer.parseInt(FactoryTools.getVersion(context)
                            .trim().replaceAll("\\.", ""));
                    Log.e("apk", "newver=" + newver + ",ver=" + ver);
                    if (newver > ver) {
                        setStart(jsons[0], jsons[1]);
                        return;
                    }
                    if (bolVersion) {
                        FactoryTools.setToastShow(context, "暂无新版本");
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    FactoryTools.setToastShow(context, "请求异常，请稍后再试");
                }
            }

            @Override
            public void onFailure(String arg0, String arg1) {


            }
        });
    }

    private void setStart(final String newVersion, String apkurl) {
        this.apkurl = apkurl;
        final LogOutDialog dialog = new LogOutDialog(context, "检测到新版本V" + newVersion + "，立刻下载","取消","下载");
        dialog.show();
        dialog.setOnClickChoose(new LogOutDialog.OnClickChoose() {
            @Override
            public void onClick(boolean f) {
                if (f){
                    //应对6.0以上版本申请权限
                    AndPermission.with((Activity) context)
                            .requestCode(101)
                            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .rationale(mRationaleListener)
                            .send();
                    dialog.dismiss();
                }
            }
        });
    }

    private RationaleListener mRationaleListener = new RationaleListener() {
        @Override
        public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
            new AlertDialog.Builder(context)
                    .setTitle("友好提醒")
                    .setMessage("没有sd卡的写入权限将不能为您下载更新包，请把sd卡的写入权限赐给我吧！")
                    .setPositiveButton("好，给你", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.resume();// 用户同意继续申请。
                        }
                    })
                    .setNegativeButton("我拒绝", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            rationale.cancel(); // 用户拒绝申请。
                        }
                    }).show();
        }
    };

}
