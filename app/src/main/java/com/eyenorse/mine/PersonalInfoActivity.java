package com.eyenorse.mine;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.bean.PersonInfoReset;
import com.eyenorse.bean.PersonalInfo;
import com.eyenorse.dialog.HeadImageSettingDialog;
import com.eyenorse.http.FactoryTools;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.BitmapUtil;
import com.eyenorse.utils.GetRoundBitmap;
import com.eyenorse.utils.ImageAbsolutePath;
import com.eyenorse.utils.RoundImageView;
import com.eyenorse.utils.TextUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.ypy.eventbus.EventBus;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;

/**
 * Created by zhengkq on 2016/12/26.
 */

public class PersonalInfoActivity extends BaseActivity{
    @BindView(R.id.rl_head_icon)
    RelativeLayout rl_head_icon;
    @BindView(R.id.simpleDraweeView_head_icon)
    SimpleDraweeView simpleDraweeView_head_icon;
    @BindView(R.id.rl_nickname)
    RelativeLayout rl_nickname;
    @BindView(R.id.text_user_name)
    TextView text_user_name;
    @BindView(R.id.rl_phone)
    RelativeLayout rl_phone;
    @BindView(R.id.text_phone)
    TextView text_phone;
    @BindView(R.id.rl_mailbox)
    RelativeLayout rl_mailbox;
    @BindView(R.id.text_mailbox)
    TextView text_mailbox;
    @BindView(R.id.rl_activate_account)
    RelativeLayout rl_activate_account;
    @BindView(R.id.text_activate_account)
    TextView text_activate_account;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.rl_password_manager)
    RelativeLayout rl_password_manager;
    @BindView(R.id.image_arror4)
    ImageView image_arror4;

    private int memberid;
    private String token;
    private JSONObject jsonObject;
    private PersonalInfo personalInfo;
    private int isactivation;
    private String realFilePath;
    private Bitmap bitmap;
    private Uri uri;
    private Bitmap croppedRoundBitmap;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PersonalInfoActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personnal_info);
        EventBus.getDefault().register(this);
        setTop(R.color.black);
        setCentreText("个人信息",getResources().getColor(R.color.black));

        SharedPreferences sp = getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        memberid = sp.getInt("memberid", 0);
        token = sp.getString("token", "");

        getApiRequestData().ShowDialog(null);
        initView();
    }
    private void initView() {
        getApiRequestData().getMemberInfo(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    personalInfo = gson.fromJson(jsonObject.toString(), PersonalInfo.class);

                    simpleDraweeView_head_icon.setImageURI(personalInfo.getHeadimage());
                    text_user_name.setText(personalInfo.getName());
                    text_phone.setText(personalInfo.getMobile());
                    if (TextUtil.isNull(personalInfo.getEmail()) || personalInfo.getEmail().equals("")) {
                        text_mailbox.setText("未绑定邮箱");
                    } else {
                        text_mailbox.setText(personalInfo.getEmail());
                    }
                    isactivation = personalInfo.getIsactivation();
                    if (isactivation == 0) {
                        text_activate_account.setText("未激活");
                        image_arror4.setVisibility(View.VISIBLE);
                    } else {
                        text_activate_account.setText("已激活");
                        image_arror4.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, memberid + "", token);
    }

    @OnClick({R.id.image_back, R.id.rl_activate_account,  R.id.rl_nickname, R.id.rl_mailbox
            , R.id.rl_head_icon,R.id.rl_password_manager})
    public void onClick(View view) {
        final Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.rl_activate_account:
                if (isactivation == 0) {
                    ActivateAccountActivity.startActivity(PersonalInfoActivity.this, memberid, token);
                }
                break;
            case R.id.rl_nickname:
                NickNameResetActivity.startActivity(PersonalInfoActivity.this, memberid, token);
                break;
            case R.id.rl_mailbox:
                MailboxResetActivity.startActivity(PersonalInfoActivity.this, memberid, token);
                break;
            case R.id.rl_head_icon:
                final HeadImageSettingDialog dialog = new HeadImageSettingDialog(PersonalInfoActivity.this);
                WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
                attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
                attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
                attributes.gravity = Gravity.BOTTOM;
                dialog.setCanceledOnTouchOutside(true);

                dialog.setOnClickChoose(new HeadImageSettingDialog.OnClickChoose() {
                    @Override
                    public void onClick(int id) {
                        if (id == 1) {
                            if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(PersonalInfoActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            } else {
                                initChooseImage();
                            }
                        } else if (id == 2) {
                            if (ContextCompat.checkSelfPermission(PersonalInfoActivity.this,
                                    Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(PersonalInfoActivity.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        MY_PERMISSIONS_REQUEST_CALL_PHONE2);
                            } else {
                                initTakePhoto();
                            }
                        }
                    }
                });
                dialog.show();
                break;
            case R.id.rl_password_manager:
                LoginPasswordManagerActivity.startActivity(PersonalInfoActivity.this,memberid,token);
                break;
        }
    }

    private void initTakePhoto() {
        if (FactoryTools.isSDCard(PersonalInfoActivity.this)) {//拍照
            GalleryFinal.openCamera(3, new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    realFilePath = resultList.get(0).getPhotoPath();
                    //uri = ImageAbsolutePath.getImageContentUri(RegisterSettingNameActivity.this, realFilePath);
                    if (realFilePath != null && realFilePath.length() != 0) {
                        initImage(realFilePath);
                    }
                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {
                    FactoryTools.setToastShow(getApplicationContext(), errorMsg);
                }
            });
        }
    }

    private void initChooseImage() {
        if (FactoryTools.isSDCard(PersonalInfoActivity.this)) {
            GalleryFinal.openGallerySingle(3, new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    realFilePath = resultList.get(0).getPhotoPath();
                    Log.e("realFilePath", realFilePath);
                    if (realFilePath != null && realFilePath.length() != 0) {
                        initImage(realFilePath);
                    }
                }

                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {
                    FactoryTools.setToastShow(getApplicationContext(), errorMsg);
                }
            });
        }
    }
    /* *//**
     * 拍照
     *//*
    void takePhoto(){
        *//**
     * 最后一个参数是文件夹的名称，可以随便起
     *//*
        file = new File(Environment.getExternalStorageDirectory(),"拍照");
        if(!file.exists()){
            file.mkdir();
        }
        *//**
     * 这里将时间作为不同照片的名称
     *//*
        output=new File(file,System.currentTimeMillis()+".jpg");
        *//**
     * 如果该文件夹已经存在，则删除它，否则创建一个
     *//*
        try {
            if (output.exists()) {
                output.delete();
            }
            output.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        *//**
     * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
     *//*
        uri = Uri.fromFile(output);
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, CROP_PHOTO);
    }

    *//**
     * 从相册选取图片
     *//*
    void choosePhoto(){
        *//**
     * 打开选择图片的界面
     *//*
        Intent intent = new Intent(Intent.ACTION_PICK);
        //Intent intent = new Intent("com.android.camera.action.CROP");
        *//*intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image*//**//*");//相片类型*//*
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image*//*");

        intent.putExtra("return-data", true);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
        Toast.makeText(PersonalInfoActivity.this, "打开选择图片的界面", Toast.LENGTH_SHORT).show();
    }
    */

    /**
     * 本地照片调用
     *//*
    public void openPhotos(int requestCode) {
        if (GetLocalPicture.openPhotosNormal(this,requestCode) && GetLocalPicture.openPhotosBrowser(this,requestCode) && GetLocalPicture.openPhotosFinally(this));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
         if (resultCode == RESULT_OK) {
             Bitmap bit = null;
            switch (requestCode) {
                case CROP_PHOTO:
                    realFilePath = getRealFilePath(PersonalInfoActivity.this, uri);
                    initImage(realFilePath);
                    break;
                case REQUEST_CODE_PICK_IMAGE:
                    uri = data.getData();
                    realFilePath = ImageAbsolutePath.getImageAbsolutePath(PersonalInfoActivity.this, uri);
                    initImage(realFilePath);

                    break;
            }
        }else {
             Toast.makeText(PersonalInfoActivity.this, "resultCode="+resultCode, Toast.LENGTH_SHORT).show();
         }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                takePhoto();
            } else{
                // Permission Denied
                Toast.makeText(PersonalInfoActivity.this, "请开启应用拍照权限", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                choosePhoto();
            } else{
                // Permission Denied
                Toast.makeText(PersonalInfoActivity.this, "请开启读写权限", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }*/

    /*public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }*/
    public File operaFileData(String path, byte[] by) {
        {
            FileOutputStream fileout = null;
            String fileName = path;
            File file = new File(fileName);
            if (file.exists()) {
                file.delete();
            }
            try {
                fileout = new FileOutputStream(file);
                fileout.write(by, 0, by.length);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    fileout.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return file;
        }

    }

    private void initImage(final String realFilePath) {
        //采样压缩
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(realFilePath, options2);
        croppedRoundBitmap = GetRoundBitmap.getCroppedRoundBitmap(bitmap, simpleDraweeView_head_icon.getWidth() / 2);
        byte[] bytes = BitmapUtil.bitmapToBase64(bitmap);
        File file = operaFileData(realFilePath, bytes);

        //Log.e("base64---", base64);

        getApiRequestData().getChangeUserInfoImage(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    String error = new JSONObject(result).getString("error");
                    if (TextUtil.isNull(error) || "".equals(error)) {
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        int issuccess = jsonObject.getInt("issuccess");
                        if (issuccess == 1) {
                            Toast.makeText(PersonalInfoActivity.this, "头像修改成功！", Toast.LENGTH_SHORT).show();
                            initView();

                            LoginInfo loginInfo = new LoginInfo();
                            loginInfo.setMemberid(memberid);
                            loginInfo.setIsload(true);
                            loginInfo.setToken(token);
                            EventBus.getDefault().post(loginInfo);
                            //finish();
                        } else {
                            Toast.makeText(PersonalInfoActivity.this, "头像修改失败！", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, file, memberid + "", token);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(PersonInfoReset personInfoReset) {
        int id = personInfoReset.getId();
        String content = personInfoReset.getContent();
        if (id == 4) {
            text_activate_account.setText(content);
            isactivation = 1;
            image_arror4.setVisibility(View.INVISIBLE);
        } else if (id == 1) {
            text_user_name.setText(content);
        } else if (id == 3) {
            text_mailbox.setText(content);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initChooseImage();
            } else
            {
                // Permission Denied
                Toast.makeText(PersonalInfoActivity.this, "没有使用SD卡的权限，请在权限管理中开启", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE2)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                initTakePhoto();
            } else
            {
                // Permission Denied
                Toast.makeText(PersonalInfoActivity.this, "没有使用相机的权限，请在权限管理中开启", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
