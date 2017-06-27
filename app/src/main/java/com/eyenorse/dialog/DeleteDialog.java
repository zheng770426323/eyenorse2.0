package com.eyenorse.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.eyenorse.R;

/**
 * Created by zhengkq on 2017/4/17.
 */

public class DeleteDialog extends Dialog implements View.OnClickListener{
    private DeleteDialog.OnClickChoose onClickChoose;

    public DeleteDialog(Context context) {
        super(context, R.style.HeadSelectDialog);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_delete);
        findViewById(R.id.tv_cancel).setOnClickListener(this);
        findViewById(R.id.tv_del).setOnClickListener(this);
    }

    public void setOnClickChoose(DeleteDialog.OnClickChoose onClickChoose){
        this.onClickChoose = onClickChoose;
    }
    public interface OnClickChoose {
        public void onClick(boolean f);
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_cancel:
                onClickChoose.onClick(false);
                dismiss();
                break;
            case R.id.tv_del:
                onClickChoose.onClick(true);
                dismiss();
                break;
        }
    }
}
