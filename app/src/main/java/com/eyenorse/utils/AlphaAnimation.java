package com.eyenorse.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.view.View;

@SuppressLint("NewApi")
public class AlphaAnimation {

	final static float AlphaRate = 0.6f;
	
	public static void down(View v){
		if (Build.VERSION.SDK_INT >= 11) {  
			v.setAlpha(AlphaRate);
        }
	}
	
	public static void up(View v){
		if (Build.VERSION.SDK_INT >= 11) {  
			v.setAlpha((float)1);
		}
	}
	
	public static void Alpha(Activity activity,int id){
		if (Build.VERSION.SDK_INT >= 11) {  
			activity.findViewById(id).setAlpha(AlphaRate);
		}
	}
}
