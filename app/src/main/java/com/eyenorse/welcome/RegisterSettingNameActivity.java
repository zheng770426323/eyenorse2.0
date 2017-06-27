package com.eyenorse.welcome;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.eyenorse.R;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.dialog.HeadImageSettingDialog;
import com.eyenorse.http.FactoryTools;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.mine.PersonalInfoActivity;
import com.eyenorse.utils.BitmapUtil;
import com.eyenorse.utils.GetRoundBitmap;
import com.eyenorse.utils.StringSHA1;
import com.eyenorse.utils.TextUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
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
 * Created by zhengkq on 2016/12/23.
 */

public class RegisterSettingNameActivity extends BaseActivity {
    @BindView(R.id.simpleDraweeView_head_icon)
    SimpleDraweeView simpleDraweeView_head_icon;
    @BindView(R.id.rl_image_bg)
    RelativeLayout rl_image_bg;
    @BindView(R.id.edit_username)
    EditText edit_username;
    @BindView(R.id.textView_confirm)
    TextView textView_confirm;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    private String inputCaptcha;
    private String inputPassword;
    private String inputPhone;
    private Uri uri;
    private JSONObject jsonObject = null;
    private String inputName;
    private String realFilePath = null;
    private Bitmap bitmap;
    private String error;
    private File file = null;
    private String thirdpartyaccount;
    private int thirdpartytype;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 6;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE2 = 7;

    public static void startActivity(Context context, String inputCaptcha, String inputPassword, String inputPhone) {
        Intent intent = new Intent(context, RegisterSettingNameActivity.class);
        intent.putExtra("inputCaptcha", inputCaptcha);
        intent.putExtra("inputPassword", inputPassword);
        intent.putExtra("inputPhone", inputPhone);
        context.startActivity(intent);
    }

    public static void startActivity(Context context, String inputCaptcha, String inputPassword, String inputPhone, String thirdpartyaccount, int thirdpartytype) {
        Intent intent = new Intent(context, RegisterSettingNameActivity.class);
        intent.putExtra("inputCaptcha", inputCaptcha);
        intent.putExtra("inputPassword", inputPassword);
        intent.putExtra("inputPhone", inputPhone);
        intent.putExtra("thirdpartyaccount", thirdpartyaccount);
        intent.putExtra("thirdpartytype", thirdpartytype);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_register_settingname);
        //ChangeSizeUtil.ChangeIconBackground(RegisterSettingNameActivity.this, rl_image_bg);
        //ChangeSizeUtil.ChangeIcon(RegisterSettingNameActivity.this, simpleDraweeView_head_icon);
        setTop(R.color.black);
        setCentreText("注册",getResources().getColor(R.color.text_color_2));

        Intent intent = getIntent();
        inputCaptcha = intent.getStringExtra("inputCaptcha");
        inputPassword = intent.getStringExtra("inputPassword");
        inputPhone = intent.getStringExtra("inputPhone");
        thirdpartyaccount = intent.getStringExtra("thirdpartyaccount");
        thirdpartytype = intent.getIntExtra("thirdpartytype", 0);
        initView();
    }

    private void initView() {
        edit_username.setInputType(InputType.TYPE_CLASS_TEXT);
        edit_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = edit_username.getText().length();
                if (length >= 2) {
                    textView_confirm.setBackground(getResources().getDrawable(R.drawable.login_confirm_bg));
                }
            }
        });
    }

    @OnClick({R.id.image_back, R.id.textView_confirm, R.id.simpleDraweeView_head_icon})
    public void onClick(View view) {
        final Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.textView_confirm:
                inputName = edit_username.getText().toString();
                if (inputName.length() == 0) {
                    Toast.makeText(RegisterSettingNameActivity.this, "输入的昵称不能为空！", Toast.LENGTH_SHORT).show();
                } else if (inputName.length() != 0 && inputName.length() < 2) {
                    Toast.makeText(RegisterSettingNameActivity.this, "输入的昵称不能小于2位！", Toast.LENGTH_SHORT).show();
                } else if (inputName.length() >= 2) {
                    getApiRequestData().getMemberNameCheck(new IOAuthReturnCallBack() {
                        @Override
                        public void onSuccess(String result, Gson gson) {
                            try {
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                boolean isexist = jsonObject.getBoolean("isexist");
                                if (isexist) {
                                    Toast.makeText(RegisterSettingNameActivity.this, "该昵称已存在！", Toast.LENGTH_SHORT).show();
                                } else {
                                    initRequest();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, inputName);
                }
                break;
            case R.id.image_back:
                finish();
                break;
            case R.id.simpleDraweeView_head_icon:
                final HeadImageSettingDialog dialog = new HeadImageSettingDialog(RegisterSettingNameActivity.this);
                WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
                attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
                attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
                attributes.gravity = Gravity.BOTTOM;
                dialog.setCanceledOnTouchOutside(true);

                dialog.setOnClickChoose(new HeadImageSettingDialog.OnClickChoose() {
                    @Override
                    public void onClick(int id) {
                        if (id == 1) {
                            if (ContextCompat.checkSelfPermission(RegisterSettingNameActivity.this,
                                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(RegisterSettingNameActivity.this,
                                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            } else {
                                initChooseImage();
                            }
                        } else if (id == 2) {
                            if (ContextCompat.checkSelfPermission(RegisterSettingNameActivity.this,
                                    Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(RegisterSettingNameActivity.this,
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
        }
    }

    private void initTakePhoto() {
        if (FactoryTools.isSDCard(RegisterSettingNameActivity.this)) {//拍照
            GalleryFinal.openCamera(3, new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    realFilePath = resultList.get(0).getPhotoPath();
                    if (realFilePath != null && realFilePath.length() != 0) {
                        bitmap = BitmapFactory.decodeFile(realFilePath);
                        Bitmap croppedRoundBitmap = GetRoundBitmap.getCroppedRoundBitmap(bitmap, simpleDraweeView_head_icon.getWidth() / 2);
                        simpleDraweeView_head_icon.setImageBitmap(croppedRoundBitmap);
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
        if (FactoryTools.isSDCard(RegisterSettingNameActivity.this)) {
            GalleryFinal.openGallerySingle(3, new GalleryFinal.OnHanlderResultCallback() {
                @Override
                public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
                    realFilePath = resultList.get(0).getPhotoPath();
                    if (realFilePath != null && realFilePath.length() != 0) {
                        bitmap = BitmapFactory.decodeFile(realFilePath);
                        Bitmap croppedRoundBitmap = GetRoundBitmap.getCroppedRoundBitmap(bitmap, simpleDraweeView_head_icon.getWidth() / 2);
                        simpleDraweeView_head_icon.setImageBitmap(croppedRoundBitmap);
                    }
                }
                @Override
                public void onHanlderFailure(int requestCode, String errorMsg) {
                    FactoryTools.setToastShow(getApplicationContext(), errorMsg);
                }
            });
        }
    }

    private void initRequest() {
        //采样压缩
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = 2;
        bitmap = BitmapFactory.decodeFile(realFilePath, options2);
        if (bitmap != null) {
            byte[] bytes = BitmapUtil.bitmapToBase64(bitmap);
            file = operaFileData(realFilePath, bytes);
        }
        //Log.e("base64---", base64);
        if (!TextUtil.isNull(thirdpartyaccount) && thirdpartytype > 0) {
            getApiRequestData().getThirdPartyRegister(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    try {
                        error = new JSONObject(result).getString("error");
                        if (TextUtil.isNull(error) || error.equals("")) {
                            jsonObject = new JSONObject(result).getJSONObject("data");
                            int memberid = jsonObject.getInt("memberid");
                            String token = jsonObject.getString("token");
                            if (memberid > 0&&token.length()>0) {
                                RegisterCompleteActivity.startActivity(RegisterSettingNameActivity.this,memberid,token);
                                finish();
                            }
                        } else {
                            Toast.makeText(RegisterSettingNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, file, inputCaptcha, inputPhone, inputName, StringSHA1.encryptToSHA(inputPassword), thirdpartyaccount, thirdpartytype + "");
        } else {
            getApiRequestData().getRegister(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    try {
                        error = new JSONObject(result).getString("error");
                        if (TextUtil.isNull(error) || error.equals("")) {
                            jsonObject = new JSONObject(result).getJSONObject("data");
                            int memberid = jsonObject.getInt("memberid");
                            String token = jsonObject.getString("token");
                            if (memberid > 0&&token.length()>0) {
                                RegisterCompleteActivity.startActivity(RegisterSettingNameActivity.this,memberid,token);
                                finish();
                            } else {
                                Toast.makeText(RegisterSettingNameActivity.this, "无用户id", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterSettingNameActivity.this, error, Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, file, inputCaptcha, inputPhone, inputName, StringSHA1.encryptToSHA(inputPassword));
        }
    }

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
                Toast.makeText(RegisterSettingNameActivity.this, "没有使用SD卡的权限，请在权限管理中开启", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(RegisterSettingNameActivity.this, "没有使用相机的权限，请在权限管理中开启", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
