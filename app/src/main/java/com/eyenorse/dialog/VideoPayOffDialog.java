package com.eyenorse.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.VideoAllInfos;
import com.eyenorse.view.FlowLayout;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhengkq on 2017/3/6.
 */

public class VideoPayOffDialog extends Dialog implements View.OnClickListener {
    private VideoAllInfos videoAllInfos;
    private Context context;
    private LinearLayout lin_category;
    private ImageView image_cancel;
    private Map<String, Squbody> selectTextList = new HashMap<>();
    private VideoPayOffDialog.OnClickChoose onClickChoose;
    private String strsqu = "";
    private String p_price;
    private TextView tv_none_select;
    private TextView tv_confirm;
    private boolean isStoreOk = true;
    private boolean isSkuOk = false;
    public TextView tv_price;
    public String prop;

    public VideoPayOffDialog(Context context, VideoAllInfos videoAllInfos) {
        super(context, R.style.dialog_style);
        this.videoAllInfos = videoAllInfos;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_video_payoff);

        lin_category = (LinearLayout) findViewById(R.id.lin_category);
        image_cancel = (ImageView) findViewById(R.id.image_cancel);
        image_cancel.setOnClickListener(this);

        tv_confirm = (TextView) findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(this);

        tv_none_select = (TextView) findViewById(R.id.tv_time);
        tv_price = (TextView) findViewById(R.id.tv_price);
        SimpleDraweeView video_icon = (SimpleDraweeView) findViewById(R.id.video_icon);
        video_icon.setImageURI(videoAllInfos.getImages().get(0));
        selectAddAttribute();//Squ属性展示
        checkstore();//初始属性是否有库存
    }

    private void selectAddAttribute() {
        lin_category.removeAllViews();
        if (videoAllInfos.getPropkey() != null && videoAllInfos.getPropkey().size() > 0) {
            for (final VideoAllInfos.PropKey propKey : videoAllInfos.getPropkey()) {
                final Squbody sqb = new Squbody();
                selectTextList.put(propKey.getName(), sqb);
                View view = LayoutInflater.from(context).inflate(R.layout.item_category, null);
                ((TextView) view.findViewById(R.id.tv_title)).setText(propKey.getName());
                FlowLayout flowlayout = (FlowLayout) view.findViewById(R.id.flowlayout);
                for (final String itemValue : propKey.getData()) {
                    int left, top, right, bottom;
                    left = top = right = bottom = 12;
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(left, top, right, bottom);
                    final TextView mView = new TextView(context);

                    mView.setText(itemValue);
                    mView.setTextSize(14);
                    mView.setTextColor(context.getResources().getColor(R.color.text_color_1));
                    mView.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.flower_item_bg));

                    mView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (mView.getTag() == "0" || mView.getTag() == "1") {
                                String title = propKey.name;
                                Squbody squbody = selectTextList.get(title);
                                if (squbody.selected.equals(itemValue)) {
                                    squbody.selected = "";

                                } else {
                                    squbody.selected = itemValue;
                                }
                                for (int i = 0; i < sqb.squ.size(); i++) {
                                    TextView txts = sqb.listtv.get(i);
                                    if (squbody.selected.equals(squbody.squ.get(i))) {
                                        txts.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sku_item_select_bg));
                                        txts.setTextColor(context.getResources().getColor(R.color.white));
                                        txts.setTag("1");//选中的标记
                                    } else {
                                        txts.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sku_item_bg));
                                        txts.setTextColor(context.getResources().getColor(R.color.text_color_1));
                                        txts.setTag("0");//未选中的标记
                                    }
                                    checkstore();
                                }
                            }
                        }
                    });

                    mView.setPadding(24, 12, 24, 12);
                    mView.setLayoutParams(params);
                    mView.setTag("0");
                    LinearLayout ll = new LinearLayout(context);
                    ll.setOrientation(LinearLayout.VERTICAL);
                    ll.addView(mView);
                    sqb.listtv.add(mView);
                    sqb.squ.add(itemValue);
                    flowlayout.addView(ll);
                }
                lin_category.addView(view);
            }
        }
    }

    private void checkstore() {
        List<String> proparr = new ArrayList<>();
        Map<String, Integer> unselectprop = new LinkedHashMap();
        Map<String, Integer> selectprop = new HashMap<>();
        if (videoAllInfos.propkey != null) {
            for (int j = 0; j < selectTextList.size(); j++) {
                VideoAllInfos.PropKey prokey = videoAllInfos.propkey.get(j);
                List<String> skutype = prokey.data;
                if (skutype.size() == 0) {
                    proparr.add("-");
                }
                if (selectTextList.get(prokey.name) == null) {
                    continue;
                }
                List<String> lispropname = selectTextList.get(prokey.name).squ;
                if (lispropname != null && lispropname.size() != 0) {
                    String propname = selectTextList.get(prokey.name).selected;
                    if (propname.length() == 0) {
                        unselectprop.put(prokey.name, j);
                        proparr.add("-");
                    } else {
                        selectprop.put(prokey.name, j);
                        proparr.add(propname);
                    }
                    Log.e("checkstore: proparr",proparr.toString());
                }
            }
        }
        prop = "";
        String proptxt = "";
        for (String pro : proparr) {
            prop += "_" + pro;
            proptxt +=" \""+pro+"\"";
        }
        if (prop.length() > 1) {
            prop = prop.substring(1);
        }
        int store = 0;
        String minprice = null;
        String maxprice = null;
        for (String squkey : videoAllInfos.portattr.keySet()) {
            Map<String, String> mappor = videoAllInfos.portattr.get(squkey);
            if (prop.equals(squkey)){
                minprice = mappor.get("price");
                maxprice = minprice;
                store += Integer.parseInt(mappor.get("stock"));
            }
        }
        p_price = minprice;
        if (minprice != maxprice) {
            p_price += "-" + maxprice;
        }
        Log.e("prop",prop);
        if (prop.equals("-")){
            if (videoAllInfos.getMinprice().equals(videoAllInfos.getMaxprice())){
                tv_price.setText(videoAllInfos.getMinprice());
            }else {
                tv_price.setText(videoAllInfos.getMinprice()+"-"+videoAllInfos.getMaxprice());
            }
        }else {
            tv_price.setText(p_price);
        }
        for (Squbody sbd : selectTextList.values()) {
            for (TextView txts : sbd.listtv) {
                if (txts.getTag().toString().equals("2")) {
                    txts.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sku_nosel_item_bg));
//                        txts.setTextColor(android.graphics.Color.BLACK);
                    txts.setTextColor(context.getResources().getColor(R.color.white));
                    txts.setEnabled(true);
                }
            }
        }
        checkbtn(selectprop, unselectprop, proparr, null);
        if (unselectprop.size() != 0) {
            strsqu = "";
            //有未选中sku
            StringBuilder sb  = new StringBuilder();
            for (String key: unselectprop.keySet()){
                sb.append(" \""+key+"\"");
                Log.e("key",key);
                Log.e("value",unselectprop.get(key)+"");

            }
            tv_none_select.setText("未选:"+sb.toString());
            tv_confirm.setEnabled(false);
            tv_confirm.setBackgroundColor(0xffD9D9D9);
        } else {
            //strsqu = prop;
            strsqu = proptxt;
            tv_none_select.setText("已选 "+strsqu);
            isSkuOk = true;
            if (isStoreOk){
                tv_confirm.setEnabled(true);
                tv_confirm.setBackgroundColor(0xffFE7200);
            }else {
                tv_confirm.setEnabled(false);
                tv_confirm.setBackgroundColor(0xffD9D9D9);
            }
        }
    }

    private void checkbtn(Map<String, Integer> selectprop, Map<String, Integer> unselectprop, List<String> proparr, String flag) {
        if (flag == null) {
            flag = "0";
        }
        flag = getArraycn(selectprop, flag);
        Log.e("flag", flag);
        Map<String, Integer> unselectproptemps = new HashMap<String, Integer>();
        unselectproptemps.putAll(unselectprop);
        if (!flag.equals("-1")) {
            int n = 0;
            for (String m : selectprop.keySet()) {
                if (flag.substring(n, n + 1).equals("0")) {
                    unselectproptemps.put(m, selectprop.get(m));
                }
                n++;
            }
        }
        if (unselectproptemps.size() != 0) {
            for (String x : unselectproptemps.keySet()) {
                int indexs = unselectproptemps.get(x);
                String temp = proparr.get(indexs);
                List<String> prolis = selectTextList.get(x).squ;
                for (int j = 0; j < prolis.size(); j++) {
                    proparr.remove(indexs);
                    //proparr.add(indexs, getSpcreg(prolis.get(j)));
                    proparr.add(indexs, prolis.get(j));
                    String newprop = "";
                    boolean proIsNull = false;
                    for (String pro : proparr) {
                        newprop += "_" + pro;
                        if (pro.length()>0){
                            proIsNull = true;
                        }
                    }
                    if (newprop.length() >= 2) {
                        newprop = newprop.substring(1);
                    }
                    Log.e("newprop",newprop.toString());
                    int teststore = 0;
                    if (proIsNull){
                        teststore = Integer.parseInt(videoAllInfos.portattr.get(newprop).get("stock"));
                        Log.e("teststore",teststore+"");
                    }

                    TextView txts = selectTextList.get(x).listtv.get(j);
                    if (teststore == 0) {
                        txts.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.sku_nosel_item_bg));
                        txts.setTextColor(context.getResources().getColor(R.color.white));
                        txts.setEnabled(false);
                        txts.setTag("2");
                    }
                    proparr.remove(indexs);
                    proparr.add(indexs, temp);
                }
            }
        }
        if (!flag.equals("-1")) {
            checkbtn(selectprop, unselectprop, proparr, flag);
        }
    }

    private String getArraycn(Map<String, Integer> data, String flag) {
        int flags = Integer.valueOf(flag, 2);
        int length = data.size();
        length = (int) (Math.pow(Double.parseDouble(2 + ""), Double.parseDouble(length + "")) - 1);
        if (flags >= length) {
            return "-1";
        } else {
            flags++;
            flag = Integer.toBinaryString(flags);
            length = data.size() - flag.length();
            if (length > 0) {
                for (int j = 0; j < length; j++) {
                    flag = "0" + flag;
                }
            }
            return flag;
        }
    }

    public void setOnClickChoose(VideoPayOffDialog.OnClickChoose onClickChoose) {
        this.onClickChoose = onClickChoose;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image_cancel:
                onClickChoose.onClick(false);
                dismiss();
                break;
            case R.id.tv_confirm:
                onClickChoose.onClick(true);

        }
    }

    public interface OnClickChoose {
        public void onClick(boolean f);
    }

    static class Squbody {
        public List<TextView> listtv = new ArrayList<TextView>();
        public List<String> squ = new ArrayList<String>();
        public String selected = "";//当前选中哪一个
    }
}
