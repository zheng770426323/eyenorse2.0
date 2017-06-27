package com.eyenorse.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * Created by zhengkq on 2017/1/13.
 */

public class WifiAndNetworkUtil {
    public static boolean isNetworkConnection(Context context) {
        ConnectivityManager mConnectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //检查网络类型
        NetworkInfo info = mConnectivity.getActiveNetworkInfo();

        int netType = info.getType();
        int netSubtype = info.getSubtype();

        if (netType == ConnectivityManager.TYPE_WIFI) {  //WIFI
            return info.isConnected();
        } else if (netType == ConnectivityManager.TYPE_MOBILE && netSubtype == TelephonyManager.NETWORK_TYPE_UMTS && !mTelephony.isNetworkRoaming()) {   //MOBILE
            return info.isConnected();
        } else {
            return false;
        }
    }

    //判断WiFi是否已连接
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
    //判断WiFi和移动流量是否已连接
    public static boolean checkNetworkConnection(Context context) {
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        final android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable())  //getState()方法是查询是否连接了数据网络
            return true;
        else
            return false;
    }

    //只判断移动网络连接是否正常
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);   //获取移动网络信息
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();    //getState()方法是查询是否连接了数据网络
            }
        }
        return false;
    }
}
