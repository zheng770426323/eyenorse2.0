package com.eyenorse.base;

import android.app.Activity;
import android.app.Application;
import android.graphics.Color;
import android.text.TextUtils;

import com.eyenorse.utils.localImages.PicassoImageLoader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;

/**
 * 自定义application
 *
 * @author Zhengkq
 */
public class SysApplication extends Application {
    private List<Activity> mList = new LinkedList<Activity>();
    private static SysApplication instance;

    public SysApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Fresco.initialize(this);
           /*GallerFinal初始化*/
        //设置主题
        ThemeConfig theme = new ThemeConfig.Builder()
                .setTitleBarBgColor(Color.parseColor("#212629"))
                .build();

        final FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableEdit(true)//开启编辑功能
                .setEnableCrop(true)//开启裁剪功能
                .setForceCrop(true)//启动强制裁剪功能,一进入编辑页面就开启图片裁剪，不需要用户手动点击裁剪，此功能只针对单选操作
                .setCropSquare(true)//裁剪正方形
                .build();
        //配置imageloader
        ImageLoader imageloader = new PicassoImageLoader();
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                // .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .setNoAnimcation(true)
                .build();
        GalleryFinal.init(coreConfig);
    }

        /**
         * 获取进程号对应的进程名
         *
         * @param pid 进程号
         * @return 进程名
         */
    private static String getProcessName(int pid) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader("/proc/" + pid + "/cmdline"));
            String processName = reader.readLine();
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim();
            }
            return processName;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return null;
    }


    public synchronized static SysApplication getInstance(){
        if (null == instance) {
            instance = new SysApplication();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }
    //关闭每一个list内的activity
    public void exit() {
        try {
            for (Activity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}