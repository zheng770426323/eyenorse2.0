package com.eyenorse.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by yiwang on 2016/11/7.
 */

public class ChangeSizeUtil {

    public static void ChangeIcon(Context context, SimpleDraweeView simpleDraweeView){
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) simpleDraweeView.getLayoutParams();
        int width = SettingUtil.getWidth(context);
        params.height = width * 902 /1052 / 3;
        params.width = width * 902 /1052 / 3;
        simpleDraweeView.setLayoutParams(params);
    }

    public static void ChangeIconBackground(Context context, RelativeLayout relativeLayout){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
        int width = SettingUtil.getWidth(context);
        params.height = width * 589 / 902;
        relativeLayout.setLayoutParams(params);
    }

    public static void ChangeCharge(Context context, ImageView relativeLayout){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) relativeLayout.getLayoutParams();
        int width = SettingUtil.getWidth(context);
        params.height = width * 310 / 1242;
        relativeLayout.setLayoutParams(params);
    }

}
