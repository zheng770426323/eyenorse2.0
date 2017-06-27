package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.eyenorse.R;
import com.eyenorse.adapter.ScanNumberAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.MyAgencyList;
import com.eyenorse.bean.NumberStatus;
import com.eyenorse.dialog.DeleteDialog;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.zxing.activity.CaptureActivity;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/4/6.
 */

public class MyLowerAgencyActivity extends BaseActivity {
    @BindView(R.id.centre_text)
    TextView centre_text;
    @BindView(R.id.ll_scan)
    LinearLayout ll_scan;
    @BindView(R.id.ll_input)
    LinearLayout ll_input;
    @BindView(R.id.tv_edit)
    TextView tv_edit;
    @BindView(R.id.tv_fahuo)
    TextView tv_fahuo;
    @BindView(R.id.ll_main)
    LinearLayout ll_main;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.ll_waybill_list)
    LinearLayout ll_waybill_list;

    private MyAgencyList.AgencyInfo agencyInfo;
    public boolean isOneEditItem = true;
    private ArrayList<String> numberList = new ArrayList<>();
    private boolean isSelectAll;
    public PopupWindow popupWindow;
    private LinearLayout ll_sel_all;
    private ImageView iv_sel_all;
    public String token;
    public int memberId;
    public int type;
    public LinkedHashMap<Integer,NumberStatus> hashMap = new LinkedHashMap<>();
    private LinkedHashMap<Integer,NumberStatus> hashMaptemp = new LinkedHashMap<>();
    private int k =0;
    private ScanNumberAdapter adapter;
    public boolean isAnim;
    public int t;
    public int width;
    private boolean hasSelect;

    public static void startActivity(Context context, MyAgencyList.AgencyInfo agencyInfo, int memberId, String token) {
        Intent intent = new Intent(context, MyLowerAgencyActivity.class);
        intent.putExtra("agencyInfo", (Serializable) agencyInfo);
        intent.putExtra("memberId", memberId);
        intent.putExtra("token", token);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lower_agency1);
        setTop(R.color.black);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        agencyInfo = (MyAgencyList.AgencyInfo) intent.getSerializableExtra("agencyInfo");
        token = intent.getStringExtra("token");
        memberId = intent.getIntExtra("memberId", 0);
        setCentreText(agencyInfo.getUsername(),getResources().getColor(R.color.text_color_2));
        adapter = new ScanNumberAdapter(MyLowerAgencyActivity.this);
        lv.setAdapter(adapter);
        initPopupWindow();
    }

    @OnClick({R.id.image_back, R.id.ll_input, R.id.tv_edit, R.id.ll_scan, R.id.tv_fahuo})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.ll_scan:
                if (tv_edit.getText().toString().equals("批量编辑")){
                    Intent intent = new Intent(MyLowerAgencyActivity.this,
                            CaptureActivity.class);
                    for (Integer i:hashMap.keySet()){
                        String number = hashMap.get(i).getNumber();
                        if (number.length()>0){
                            numberList.add(number);
                        }
                    }
                    intent.putStringArrayListExtra("numberList",numberList);
                    intent.putExtra("memberId",memberId);
                    intent.putExtra("token",token);
                    startActivityForResult(intent, 99);
                }
                break;
            case R.id.ll_input:
                if (isOneEditItem&&tv_edit.getText().toString().equals("批量编辑")){
                    ll_waybill_list.setVisibility(View.VISIBLE);
                    NumberStatus numberStatusInfo = new NumberStatus();
                    numberStatusInfo.setId(k);
                    numberStatusInfo.setSelect(false);
                    numberStatusInfo.setEdit(false);
                    numberStatusInfo.setNumber("");
                    hashMap.put(k,numberStatusInfo);
                    isOneEditItem = false;
                    isSelectAll = false;
                    type = 0;
                    t =0;
                    k++;
                    adapter.notifyDataSetChanged();
                    initBottomStatus();
                }
                break;
            case R.id.tv_edit:
                if (tv_edit.getText().toString().equals("批量编辑")) {
                    for (Object key : hashMap.keySet().toArray())
                    {
                        NumberStatus staus = hashMap.get(key);
                        if (staus.getNumber().equals(""))
                        {
                            hashMap.remove(key);
                            k--;
                            break;
                        }
                    }

                    tv_edit.setText("取消");
                    tv_fahuo.setVisibility(View.GONE);
                    if (hashMap.size() > 0) {
                        type = 1;
                        t = 1;
                        isOneEditItem = true;
                        //为popWindow添加动画效果
                        popupWindow.setAnimationStyle(R.style.dialog_style);
                        // 点击弹出泡泡窗口
                        popupWindow.showAtLocation(ll_main, Gravity.BOTTOM, 0, 0);
                    }

                } else {
                    tv_edit.setText("批量编辑");

                    for (Object key : hashMap.keySet().toArray())
                    {
                        NumberStatus staus = hashMap.get(key);
                        if (staus.getNumber().equals(""))
                        {
                            hashMap.remove(key);
                            break;
                        }
                    }
                    if (!isOneEditItem){
                        ll_waybill_list.setVisibility(View.VISIBLE);
                        NumberStatus numberStatusInfo = new NumberStatus();
                        numberStatusInfo.setId(k);
                        numberStatusInfo.setSelect(false);
                        numberStatusInfo.setEdit(false);
                        numberStatusInfo.setNumber("");
                        hashMap.put(k,numberStatusInfo);
                        isOneEditItem = false;
                        k++;
                    }


                    isOneEditItem = true;

                    tv_fahuo.setVisibility(View.VISIBLE);
                    if (hashMap.size() > 0) {
                        type = 2;
                        t = 2;
                    }
                    //为popWindow添加动画效果
                    popupWindow.setAnimationStyle(R.style.dialog_style);
                    // 点击弹出泡泡窗口
                    popupWindow.dismiss();
                }
                isAnim = false;
//                resetAdpt();
                adapter.notifyDataSetChanged();
                /*adapter = new ScanNumberAdapter(MyLowerAgencyActivity.this);
                lv.setAdapter(adapter);*/
                break;
            case R.id.tv_fahuo:
                if (hashMap.size() > 0) {
                    numberList.clear();
                    for (Integer i:hashMap.keySet()) {
                        NumberStatus numberStatus = hashMap.get(i);
                        numberList.add(numberStatus.getNumber());
                    }
                }
                getApiRequestData().getCheckAgencyCodes(new IOAuthReturnCallBack() {
                    @Override
                    public void onSuccess(String result, Gson gson) {
                        try {
                            String error = new JSONObject(result).getString("error");
                            if (TextUtil.isNull(error) || error.length() == 0) {
                                JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                                int success = new JSONObject(jsonObject.toString()).getInt("issuccess");
                                if (success == 1) {
                                    Toast.makeText(MyLowerAgencyActivity.this, "发货成功", Toast.LENGTH_SHORT).show();
                                    numberList.clear();
                                    hashMap.clear();
                                    lv.setAdapter(adapter);
                                } else {
                                    Toast.makeText(MyLowerAgencyActivity.this, "发货失败", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, numberList, numberList.size() + "", memberId + "", agencyInfo.getId() + "", token);
                break;
        }
    }

    public void initBottomStatus() {
        isSelectAll = checkIsSelectAll();
        if (isSelectAll) {
            iv_sel_all.setImageDrawable(getResources().getDrawable(R.mipmap.selected));
        } else {
            iv_sel_all.setImageDrawable(getResources().getDrawable(R.mipmap.unselect));
        }
    }

    public  void resetAdpt(){
        lv.setAdapter(adapter);
    }


    public boolean checkIsSelectAll() {
        if (hashMap.size()>0){
            for (Integer i : hashMap.keySet()) {
                NumberStatus numberStatus = hashMap.get(i);
                if (numberStatus.isEdit()){
                    if (!numberStatus.isSelect()) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * 初始化popupWindow
     */
    private void initPopupWindow() {
        View popView = getLayoutInflater().inflate(R.layout.pop_select_all, null);
        popupWindow = new PopupWindow(popView,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(false);
        ll_sel_all = (LinearLayout) popView.findViewById(R.id.ll_sel_all);
        iv_sel_all = (ImageView) popView.findViewById(R.id.iv_sel_all);
        LinearLayout ll_del = (LinearLayout) popView.findViewById(R.id.ll_del);
        ll_sel_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hashMap.size() > 0) {
                    isSelectAll = !isSelectAll;
                    if (isSelectAll) {
                        iv_sel_all.setImageDrawable(getResources().getDrawable(R.mipmap.selected));
                        for (Integer i:hashMap.keySet()) {
                            if (hashMap.get(i).isEdit()){
                                hashMap.get(i).setSelect(true);
                            }
                        }
                    } else {
                        iv_sel_all.setImageDrawable(getResources().getDrawable(R.mipmap.unselect));
                        for (Integer i:hashMap.keySet()) {
                            hashMap.get(i).setSelect(false);
                        }
                    }
                    isAnim = true;
                    adapter.notifyDataSetChanged();
                }
            }
        });
        ll_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hashMap.size() > 0) {
                    for (Integer i:hashMap.keySet()){
                        if (hashMap.get(i).isSelect()){
                            hasSelect = true;
                        }
                    }
                    /*Iterator it=hashMap.keySet().iterator();
                    while(it.hasNext())
                    {
                        int i=(int)it.next();
                        if (!hashMap.get(i).isSelect()||!hashMap.get(i).isEdit()){
                            hashMaptemp.put(i,hashMap.get(i));
                        }
                        it.remove();

                    }*/
                    if (hasSelect){
                        DeleteDialog dialog = new DeleteDialog(MyLowerAgencyActivity.this);
                        WindowManager.LayoutParams attributes = dialog.getWindow().getAttributes();
                        attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
                        attributes.gravity = Gravity.BOTTOM;
                        dialog.setCanceledOnTouchOutside(true);
                        dialog.setOnClickChoose(new DeleteDialog.OnClickChoose() {
                            @Override
                            public void onClick(boolean f) {
                                if (f){
                                    for (Integer i:hashMap.keySet()){
                                        if (!hashMap.get(i).isSelect()||!hashMap.get(i).isEdit()){
                                            hashMaptemp.put(i,hashMap.get(i));
                                        }
                                    }
                                    isAnim = true;
                                    hashMap.clear();
                                    for (Integer i:hashMaptemp.keySet()){
                                        hashMap.put(i,hashMaptemp.get(i));
                                    }
                                    hashMaptemp.clear();
                                    t = 1;
                                    type = 1;
                                    resetAdpt();
                                    //hashMap = hashMaptemp;
                                    Log.e("size",hashMap.size()+"");
                                    checkIsSelectAll();
                                    if (hashMap.size()==0) {
                                        tv_edit.setText("批量编辑");
                                        tv_fahuo.setVisibility(View.VISIBLE);
                                        type = 0;
                                        t = 0;
                                        //为popWindow添加动画效果
                                        popupWindow.setAnimationStyle(R.style.dialog_style);
                                        // 点击弹出泡泡窗口
                                        popupWindow.dismiss();
                                        isSelectAll= false;
                                        isOneEditItem = true;
                                        initBottomStatus();
                                    }
                                }
                            }
                        });
                        dialog.show();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 扫描二维码/条码回传
        if (requestCode == 99 && resultCode == RESULT_OK) {
            if (data != null) {
                String result = data.getExtras().getString("result");
                Log.e("result",result);
                if (hashMap.size()>0){
                    for (Integer i:hashMap.keySet()){
                        if (hashMap.get(i).getNumber().equals(result)){
                            Toast.makeText(MyLowerAgencyActivity.this,"已添加该代理!",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (hashMap.get(i).getNumber().equals("")){
                            hashMap.get(i).setNumber(result);
                            hashMap.get(i).setEdit(true);
                            hashMap.get(i).setNumber(result);
                            isOneEditItem = true;
                            adapter.notifyDataSetChanged();
                            return;
                        }
                    }
                }

                NumberStatus numberStatusInfo = new NumberStatus();
                numberStatusInfo.setId(k);
                numberStatusInfo.setSelect(false);
                numberStatusInfo.setEdit(true);
                numberStatusInfo.setNumber(result);
                hashMap.put(k,numberStatusInfo);
                isOneEditItem = true;
                k++;
                if (hashMap.size()==0){
                    adapter = new ScanNumberAdapter(MyLowerAgencyActivity.this);
                    lv.setAdapter(adapter);
                }else {
                    adapter.notifyDataSetChanged();
                }
                ll_waybill_list.setVisibility(View.VISIBLE);
            }
        }
    }
}
