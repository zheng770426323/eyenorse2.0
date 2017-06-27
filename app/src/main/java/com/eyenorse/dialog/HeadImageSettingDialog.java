package com.eyenorse.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.eyenorse.R;
import com.eyenorse.fragment.FragmentConstants;

import java.io.File;

/**
 * Created by zhengkq on 2017/1/7.
 */

public class HeadImageSettingDialog extends Dialog implements View.OnClickListener {
    private HeadImageSettingDialog.OnClickChoose onClickChoose;

    public HeadImageSettingDialog(Context context) {
        super(context, R.style.HeadSelectDialog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_head_setting);


        findViewById(R.id.textView_cancel).setOnClickListener(this);
        findViewById(R.id.textView_local).setOnClickListener(this);
        findViewById(R.id.textView_camera).setOnClickListener(this);
    }

    public void setOnClickChoose(HeadImageSettingDialog.OnClickChoose onClickChoose){
        this.onClickChoose = onClickChoose;
    }
    public interface OnClickChoose {
        public void onClick(int id);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.textView_cancel:
                onClickChoose.onClick(3);
                dismiss();
                break;
            case R.id.textView_local:
                onClickChoose.onClick(1);
                dismiss();

                break;
            case R.id.textView_camera:
                onClickChoose.onClick(2);
                dismiss();

                break;
        }
    }
}
