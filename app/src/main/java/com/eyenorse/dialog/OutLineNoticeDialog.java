package com.eyenorse.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.ErrorInfo;

/**
 * Created by zhengkq on 2017/3/17.
 */

public class OutLineNoticeDialog extends Dialog implements View.OnClickListener {
    private ImageView image_mis;
    private ErrorInfo errorInfo;
    private TextView tv_title;
    private OnClickChoose onClickChoose;
    public OutLineNoticeDialog(Context context, ErrorInfo errorInfo) {
        super(context, R.style.EditDialog);
        this.errorInfo = errorInfo;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_outline_notice);

        TextView textView_name = (TextView) findViewById(R.id.textView_name);
        TextView textView_affirm = (TextView) findViewById(R.id.textView_affirm);
        image_mis = (ImageView) findViewById(R.id.image_mis);
        tv_title = (TextView) findViewById(R.id.tv_title);
        if (errorInfo.getStatus().equals("101")){
            SpannableString spanString = new SpannableString("您的账号于"+errorInfo.getLogintime()+"在另外一个地点登录，如非本人操作，则密码可能已泄露，建议前往 个人中心-设置 修改密码");
            ForegroundColorSpan span = new ForegroundColorSpan(getContext().getResources().getColor(R.color.confirm_bg_color));
            spanString.setSpan(span, spanString.length()-12, spanString.length()-5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView_name.setText(spanString);
            textView_name.setHeight(R.dimen.space_v_120);
            textView_affirm.setText("重新登录");
            tv_title.setText("下线通知");
        }else if (errorInfo.getStatus().equals("102")){
            textView_name.setText("您还未登录，请先登录");
            textView_name.setHeight(R.dimen.space_v_40);
            textView_affirm.setText("登录");
            tv_title.setText("登录通知");
        }else if (errorInfo.getStatus().equals("100")){
            textView_name.setText("登录已失效，请重新登录");
            textView_name.setHeight(R.dimen.space_v_40);
            textView_affirm.setText("重新登录");
            tv_title.setText("登录通知");
        }
        textView_affirm.setOnClickListener(this);
    }

    public void setOnClickChoose(OutLineNoticeDialog.OnClickChoose onClickChoose){
        this.onClickChoose = onClickChoose;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView_affirm:
                onClickChoose.onClick(true);
                dismiss();
                break;
        }
    }

    public interface OnClickChoose {
        public void onClick(boolean f);
    }

}
