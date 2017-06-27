package com.eyenorse.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.VisionTrain.VisionPlayActivity;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.mine.MyInvitationActivity;
import com.eyenorse.utils.DisPlayUtil;
import com.eyenorse.utils.TextUtil;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;

import butterknife.BindView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by zhengkq on 2016/12/26.
 */

public class StartInvitationFragment extends BaseFragment {
    @BindView(R.id.image_qr_code)
    ImageView image_qr_code;
    @BindView(R.id.textView_start)
    TextView textView_start;
    @BindView(R.id.editView_input_website)
    TextView editView_input_website;
    @BindView(R.id.textView_copy_start)
    TextView textView_copy_start;
    @BindView(R.id.textView_intro)
    TextView textView_intro;
    private JSONObject jsonObject;
    private int memberId;
    private String token;
    private String invitationurl;
    private Bitmap imageData;
    private OnekeyShare oks;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setContentView(inflater, container, R.layout.fragment_start_invitation);
        //simpleViewFragmentAdvertisement.setAspectRatio(15.92f);
        initView();
        return view;
    }

    private void initView() {
        //((MyInvitationActivity)getActivity()).getApiRequestData().ShowDialog(null);
        this.memberId = ((MyInvitationActivity)getActivity()).memberId;
        this.token = ((MyInvitationActivity)getActivity()).token;

        ((MyInvitationActivity)getActivity()).getApiRequestData().getInvitationInfo(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    String invitetext = jsonObject.getString("invitetext");
                    textView_intro.setText(invitetext);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        ((MyInvitationActivity)getActivity()).getApiRequestData().getInvitationAdress(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    invitationurl = jsonObject.getString("invitationurl");
                    editView_input_website.setText(invitationurl);
                    Bitmap qrImage = createQRImage(invitationurl, image_qr_code.getWidth(), image_qr_code.getHeight());
                    image_qr_code.setImageBitmap(qrImage);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },memberId+"",token);

    }
    //Edited by mythou
    //http://www.cnblogs.com/mythou/
    //要转换的地址或字符串,可以是中文
    public Bitmap  createQRImage(String url, final int width, final int height)
    {
        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return null;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < height; y++)
            {
                for (int x = 0; x < width; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * width + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * width + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @OnClick({R.id.textView_copy_start})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.textView_copy_start:
                initShare();
                break;
        }
    }

    private void initShare() {
        ShareSDK.initSDK(getActivity());
        ((MyInvitationActivity)getActivity()).getApiRequestData().getInvitationAdress(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    String error = new JSONObject(result).getString("error");
                    if (TextUtil.isNull(error) || "".equals(error)) {
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        invitationurl = jsonObject.getString("invitationurl");
                        oks = new OnekeyShare();
                        //关闭sso授权
                        oks.disableSSOWhenAuthorize();
                        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                        oks.setTitle("眼护士");
                        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                        oks.setComment("眼护士眼保健操");
                        // site是分享此内容的网站名称，仅在QQ空间使用
                        oks.setSite(getContext().getString(R.string.app_name));

                        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
                            @Override
                            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                                if ("QZone".equals(platform.getName())) {
                                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                                    paramsToShare.setText("特约邀请");
                                    paramsToShare.setTitle("眼护士");
                                    paramsToShare.setTitleUrl(invitationurl);
                                }
                                if ("QQ".equals(platform.getName())) {
                                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                                    paramsToShare.setText("特约邀请");
                                    paramsToShare.setTitle("眼护士");
                                    //paramsToShare.setImageUrl(videoimage);
                                    paramsToShare.setTitleUrl(invitationurl);
                                }
                                if ("SinaWeibo".equals(platform.getName())) {
                                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                                    paramsToShare.setUrl(null);
                                    paramsToShare.setText("眼护士-特约邀请:" + invitationurl);
                                    imageData = BitmapFactory.decodeResource(getResources(), R.mipmap.icon);
                                    paramsToShare.setImageData(imageData);
                                }
                                if ("Wechat".equals(platform.getName())) {
                                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                                    paramsToShare.setText("特约邀请");
                                    paramsToShare.setTitle("眼护士");
                                    // url仅在微信（包括好友和朋友圈）中使用
                                    paramsToShare.setUrl(invitationurl);
                                }
                                if ("WechatMoments".equals(platform.getName())) {
                                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                                    //paramsToShare.setText("一起来做眼护士VR眼保健操吧...");
                                    paramsToShare.setTitle("眼护士-特约邀请");
                                    // url仅在微信（包括好友和朋友圈）中使用
                                    paramsToShare.setUrl(invitationurl);
                                }
                            }
                        });
                        // 启动分享GUI
                        oks.show(getActivity());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },((MyInvitationActivity)getActivity()).memberId+"",token);
    }
}
