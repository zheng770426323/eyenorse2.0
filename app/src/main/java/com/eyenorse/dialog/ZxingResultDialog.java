package com.eyenorse.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyenorse.R;

/**
 * Created by zhengkq on 2017/4/5.
 */

public class ZxingResultDialog extends Dialog implements View.OnClickListener{
    private ZxingResultDialog.OnClickChoose onClickChoose;
    private String title;
    private Context context;

    public ZxingResultDialog(Context context, String title) {
        super(context, R.style.EditDialog);
        this.context = context;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_zxing_result);

        TextView textView_name = (TextView) findViewById(R.id.tv_name);
        TextView tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        ImageView image_mis = (ImageView) findViewById(R.id.iv_icon);
        textView_name.setText(title);
        if (title.equals("扫描成功!")){
            image_mis.setImageDrawable(context.getResources().getDrawable(R.mipmap.zxing_dianji));
        }else if (title.equals("重复啦~")){
            image_mis.setImageDrawable(context.getResources().getDrawable(R.mipmap.zxing_chongfu));
        }else if (title.equals("出错啦~")){
            image_mis.setImageDrawable(context.getResources().getDrawable(R.mipmap.zxing_mistake));
        }
        tv_confirm.setOnClickListener(this);
    }

    public void setOnClickChoose(ZxingResultDialog.OnClickChoose onClickChoose){
        this.onClickChoose = onClickChoose;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_confirm:
                onClickChoose.onClick(true);
                dismiss();
                break;
        }
    }

    public interface OnClickChoose {
        public void onClick(boolean f);
    }
}
