package com.eyenorse.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyenorse.R;


/**
 * Created by zhengkq on 2016/12/16.
 */

public class LogOutDialog extends Dialog implements View.OnClickListener {

    private OnClickChoose onClickChoose;
    private String title;
    private String cancel;
    private String confirm;
    public ImageView image_mis;

    public LogOutDialog(Context context,String title,String cancel,String confirm) {
        super(context, R.style.EditDialog);
        this.title = title;
        this.cancel = cancel;
        this.confirm = confirm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_log_out);

        TextView textView_name = (TextView) findViewById(R.id.textView_name);
        TextView textView_cancel = (TextView) findViewById(R.id.textView_cancel);
        TextView textView_affirm = (TextView) findViewById(R.id.textView_affirm);
        image_mis = (ImageView) findViewById(R.id.image_mis);
        textView_name.setText(title);
        textView_cancel.setText(cancel);
        textView_affirm.setText(confirm);
        textView_cancel.setOnClickListener(this);
        textView_affirm.setOnClickListener(this);
    }

    public void setOnClickChoose(LogOutDialog.OnClickChoose onClickChoose){
        this.onClickChoose = onClickChoose;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.textView_cancel:
                onClickChoose.onClick(false);
                dismiss();
                break;
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
