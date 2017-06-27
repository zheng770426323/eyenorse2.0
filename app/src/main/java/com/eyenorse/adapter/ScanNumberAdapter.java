package com.eyenorse.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.bean.NumberStatus;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.mine.MyLowerAgencyActivity;
import com.eyenorse.utils.TextUtil;
import com.google.gson.Gson;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by zhengkq on 2017/4/6.
 */

public class ScanNumberAdapter extends BaseAdapter {
    private Context context;
    private LinkedHashMap<Integer,NumberStatus> hashMap = new LinkedHashMap<>();
    private List<NumberStatus>list = new ArrayList<>();
    private LayoutInflater inflater;
    private int memberId;
    private String token;
    private int type;
    private boolean isAnim;
    private ListView lv;
    public ScanNumberAdapter(Context context) {
        this.context = context;
        this.hashMap = ((MyLowerAgencyActivity)context).hashMap;
       /* for (Integer i:hashMap.keySet()){
            list.add(hashMap.get(i));
        }*/
        this.memberId = ((MyLowerAgencyActivity)context).memberId;
        this.token = ((MyLowerAgencyActivity)context).token;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return hashMap.size();
    }

    @Override
    public Object getItem(int i) {
        return hashMap.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        this.type = ((MyLowerAgencyActivity)context).type;
        this.isAnim = ((MyLowerAgencyActivity)context).isAnim;
        final ScanNumberAdapter.ViewHolder viewHolder;

        Object[] keyList = hashMap.keySet().toArray();

        final NumberStatus numberStatus = hashMap.get(keyList[i]);

        if (view==null){
            view =inflater.inflate(R.layout.item_waybill,null);
            viewHolder = new ScanNumberAdapter.ViewHolder();
            viewHolder.rl = (LinearLayout) view.findViewById(R.id.rl);
            viewHolder.image_sel = (ImageView) view.findViewById(R.id.image_sel);
            viewHolder.et_number = (EditText) view.findViewById(R.id.et_number);
            viewHolder.tv_edit_end = (TextView) view.findViewById(R.id.tv_edit_end);
            viewHolder.tv_del_end = (TextView) view.findViewById(R.id.tv_del_end);
            viewHolder.ll_kongbai = (LinearLayout) view.findViewById(R.id.ll_kongbai);
            view.setTag(viewHolder);
        }else
        {
            viewHolder = (ScanNumberAdapter.ViewHolder) view.getTag();
        }


        if (numberStatus!=null){
            //final int t = numberStatus.getId();
            if (numberStatus.isSelect()){
                viewHolder.image_sel.setImageDrawable(context.getResources().getDrawable(R.mipmap.selected));
            }else {
                viewHolder.image_sel.setImageDrawable(context.getResources().getDrawable(R.mipmap.unselect));
            }

            if (numberStatus.isEdit()){
                viewHolder.et_number.setFocusable(false);
                viewHolder.et_number.setText(numberStatus.getNumber());
                viewHolder.tv_edit_end.setVisibility(View.GONE);

                ((SwipeMenuLayout) view).setIos(false).setLeftSwipe(true).setSwipeEnable(true);
//                final View finalView = view;
                final View finalView = view;
                viewHolder.tv_del_end.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("id-----",numberStatus.getId()+"");
                        hashMap.remove(numberStatus.getId());
                        ((MyLowerAgencyActivity)context).hashMap.remove(numberStatus.getId());
                        ((MyLowerAgencyActivity)context).resetAdpt();

//                        notifyDataSetChanged();
//                        notifyDataSetInvalidated();
                    }
                });
            }else {
                viewHolder.et_number.setText("");
                Log.e("id-----2222:::::",numberStatus.getId()+"");
                viewHolder.et_number.setFocusable(true);
                viewHolder.tv_edit_end.setVisibility(View.VISIBLE);

                ((SwipeMenuLayout) view).setIos(false).setLeftSwipe(false).setSwipeEnable(false);
                viewHolder.tv_edit_end.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String input = viewHolder.et_number.getText().toString();
                        if (input.length() != 13){
                            Toast.makeText(context, "请输入13位激活码", Toast.LENGTH_SHORT).show();
                        }else {
                            if (hashMap.size() > 0 ) {
                                for (Integer i : hashMap.keySet()) {
                                    NumberStatus numberStatus = hashMap.get(i);
                                    if (numberStatus.getNumber().toUpperCase().equals(input.toUpperCase())) {
                                        Toast.makeText(context, "重复了~", Toast.LENGTH_SHORT).show();
                                        return;
                                    }
                                }
                            }
                            ((MyLowerAgencyActivity)context).getApiRequestData().getCheckCodes(new IOAuthReturnCallBack() {
                                @Override
                                public void onSuccess(String result, Gson gson) {
                                    try {
                                        String error = new JSONObject(result).getString("error");
                                        if (TextUtil.isNull(error) || error.length() == 0) {
                                            JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                                            int success = new JSONObject(jsonObject.toString()).getInt("issuccess");
                                            if (success == 1) {
                                                viewHolder.et_number.setText(input);
                                                viewHolder.tv_edit_end.setVisibility(View.GONE);
                                                viewHolder.et_number.setFocusable(false);
                                                hashMap.get(numberStatus.getId()).setEdit(true);
                                                ((MyLowerAgencyActivity)context).hashMap.get(numberStatus.getId()).setEdit(true);
                                                ((MyLowerAgencyActivity)context).isOneEditItem = true;
                                                hashMap.get(numberStatus.getId()).setNumber(input);
                                                ((MyLowerAgencyActivity)context).hashMap.get(numberStatus.getId()).setNumber(input);
                                                notifyDataSetChanged();
                                                notifyDataSetInvalidated();
                                            } else {
                                                Toast.makeText(context, "激活码校验失败", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, input, memberId + "", token);
                        }
                    }
                });

            }
            int width = viewHolder.image_sel.getWidth();
            if (type==1&&!isAnim){
                Log.e("animation1","动画出来"+i);
                ((MyLowerAgencyActivity)context).width = width;
                viewHolder.et_number.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (numberStatus.isSelect()){
                            viewHolder.image_sel.setImageDrawable(context.getResources().getDrawable(R.mipmap.unselect));
                            hashMap.get(numberStatus.getId()).setSelect(false);
                            ((MyLowerAgencyActivity)context).hashMap.get(numberStatus.getId()).setSelect(false);
                        }else {
                            viewHolder.image_sel.setImageDrawable(context.getResources().getDrawable(R.mipmap.selected));
                            hashMap.get(numberStatus.getId()).setSelect(true);
                            ((MyLowerAgencyActivity)context).hashMap.get(numberStatus.getId()).setSelect(true);
                        }
                        ((MyLowerAgencyActivity)context).initBottomStatus();
                    }
                });
            }else if(type ==2&&!isAnim){
                viewHolder.rl.setLeft(0);
            }
            if (((MyLowerAgencyActivity) context).t==1){
                int width1 = ((MyLowerAgencyActivity)context).width;
                viewHolder.rl.setLeft(width1);
                Log.e("width1",width1+"");
                Log.e("fresh","刷新了-----"+i);
            }else if (((MyLowerAgencyActivity) context).t==2){
                viewHolder.rl.setLeft(0);
                Log.e("fresh","-----刷新了"+i);
            }
        }
        return view;
    }

    public class ViewHolder{
        private LinearLayout rl;
        private ImageView image_sel;
        private EditText et_number;
        private TextView tv_edit_end;
        private TextView tv_del_end;
        private LinearLayout ll_kongbai;
    }
}
