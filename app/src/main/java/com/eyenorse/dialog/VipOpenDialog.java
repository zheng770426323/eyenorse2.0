package com.eyenorse.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.eyenorse.R;

/**
 * Created by zhengkq on 2016/12/29.
 */

public class VipOpenDialog extends Dialog implements View.OnClickListener {
    private VipOpenDialog.OnClickChoose onClickChoose;
    private String title;
    private String content;
    private String confirm;

    public VipOpenDialog(Context context,String title,String content,String confirm) {
        super(context, R.style.EditDialog);
        this.title = title;
        this.content = content;
        this.confirm = confirm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vip_open);

        TextView textView_title = (TextView) findViewById(R.id.textView_title);
        textView_title.setText(title);
        TextView textView_content = (TextView) findViewById(R.id.textView_content);
        textView_content.setText(content);
        TextView textView_affirm = (TextView) findViewById(R.id.textView_affirm);
        textView_affirm.setText(confirm);

        findViewById(R.id.textView_cancel).setOnClickListener(this);
        textView_affirm.setOnClickListener(this);
    }

    public void setOnClickChoose(VipOpenDialog.OnClickChoose onClickChoose){
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
