package com.eyenorse.utils;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.Toast;
/**
 * Created by zhengkq on 2017/2/9.
 */

public class GetLocalPicture {
    /**
     * PopupMenu打开本地相册.
     */
    public static boolean openPhotosNormal(Activity activity,int actResultCode) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                "image/*");
        try {
            activity.startActivityForResult(intent, actResultCode);
        } catch (android.content.ActivityNotFoundException e) {
            return true;
        }
        return false;
    }

    /**
     * 打开其他的一文件浏览器,如果没有本地相册的话
     */
    public static boolean openPhotosBrowser(Activity activity,int requestCode) {
        Toast.makeText(activity, "没有相册软件，运行文件浏览器", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // "android.intent.action.GET_CONTENT"
        intent.setType("image/*"); // 查看类型 String IMAGE_UNSPECIFIED =
        // "image/*";
        Intent wrapperIntent = Intent.createChooser(intent, null);
        try {
            activity.startActivityForResult(wrapperIntent, requestCode);
        } catch (android.content.ActivityNotFoundException e1) {
            return true;
        }
        return false;
    }

    /**
     * 这个是找不到相关的图片浏览器,或者相册
     */
    public static boolean openPhotosFinally(Activity activity) {
        Toast.makeText(activity, "您的系统没有文件浏览器或则相册支持,请安装！", Toast.LENGTH_LONG).show();
        return false;
    }

}
