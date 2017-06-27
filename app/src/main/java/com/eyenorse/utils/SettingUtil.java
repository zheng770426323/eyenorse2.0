package com.eyenorse.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.eyenorse.base.Config;


/**
 * Created by yiwang on 2016/11/3.
 */

public class SettingUtil {

    public static int getWidth(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Config.SharedPreferencesData, 0);
        return preferences.getInt("width", 0);
    }

    public static int getHeight(Context context){
        SharedPreferences preferences = context.getSharedPreferences(Config.SharedPreferencesData, 0);
        return preferences.getInt("height", 0);
    }
}
