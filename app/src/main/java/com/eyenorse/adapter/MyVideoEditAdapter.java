package com.eyenorse.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.VisionTrain.OrdernumberActivity;
import com.eyenorse.VisionTrain.VideoInfoActivity;
import com.eyenorse.alipay.ShoppingInfo;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.MyVideoBean;
import com.eyenorse.bean.VideoAllInfos;
import com.eyenorse.bean.WXpayforInfo;
import com.eyenorse.dialog.VideoPayOffDialog;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.mine.MyVideoActivity;
import com.eyenorse.utils.AliPayUtil;
import com.eyenorse.utils.DateTimeHelper;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.utils.WXpayUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.eyenorse.R.id.tv_select_count;

/**
 * Created by zhengkq on 2017/4/1.
 */

public class MyVideoEditAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<MyVideoBean.MyVideoItem> list = new ArrayList<>();
    private int type;
    private int one;
    private VideoAllInfos videoAllInfos;
    private JSONObject jsonObject;
    private int memberid;
    private String token;
    private String price;

    public MyVideoEditAdapter(Context context,List<MyVideoBean.MyVideoItem> list) {
        this.context = context;
        this.list.addAll(list);
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final MyVideoEditAdapter.ViewHolder viewHolder;
        if (convertView==null){
            convertView =inflater.inflate(R.layout.item_video_list_edit,null);
            viewHolder = new MyVideoEditAdapter.ViewHolder();
            viewHolder.simpleDraweeView = (SimpleDraweeView) convertView.findViewById(R.id.simpleDraweeView);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_look_count = (TextView) convertView.findViewById(R.id.tv_look_count);
            viewHolder.tv_play_time = (TextView) convertView.findViewById(R.id.tv_play_time);
            viewHolder.tv_good_time = (TextView) convertView.findViewById(R.id.tv_good_time);
            //viewHolder.rl_more = (RelativeLayout) convertView.findViewById(R.id.rl_more);
            viewHolder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
            viewHolder.ll_item = (LinearLayout) convertView.findViewById(R.id.ll_item);
            viewHolder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);
            viewHolder.simpleDraweeView_bg = (SimpleDraweeView) convertView.findViewById(R.id.simpleDraweeView_bg);
            viewHolder.tv_xufei = (TextView) convertView.findViewById(R.id.tv_xufei);
            viewHolder.tv_have_time = (TextView) convertView.findViewById(R.id.tv_have_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (MyVideoEditAdapter.ViewHolder) convertView.getTag();
        }
        final MyVideoBean.MyVideoItem myVideoItem = list.get(position);
        viewHolder.simpleDraweeView.setImageURI(myVideoItem.getImages());
        viewHolder.tv_title.setText(myVideoItem.getTitle());
        viewHolder.tv_look_count.setText(myVideoItem.getVideopageviews()+"");
        viewHolder.tv_play_time.setText("时长"+DateTimeHelper.getHourMinSS(myVideoItem.getDuration()));
        viewHolder.tv_good_time.setText(myVideoItem.getCollectioncount()+"");
        String today = DateTimeHelper.getDateTime().substring(0, 11);
        try {
            int daysBetween = DateTimeHelper.daysBetween(today, list.get(position).getExpire_time(), DateTimeHelper.mFormat_chattime);
            if (daysBetween<0){
                viewHolder.tv_xufei.setVisibility(View.VISIBLE);
                viewHolder.simpleDraweeView_bg.setVisibility(View.VISIBLE);
                viewHolder.tv_have_time.setVisibility(View.VISIBLE);
                viewHolder.tv_xufei.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoInfoActivity.startActivity(context,myVideoItem.getId()+"");
                        /*SharedPreferences sp = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
                        memberid = sp.getInt("memberid", 0);
                        token = sp.getString("token","");
                        ((MyVideoActivity)context).getApiRequestData().getVideoInfo(new IOAuthReturnCallBack() {
                            @Override
                            public void onSuccess(String result, Gson gson) {
                                BaseBean<VideoAllInfos> baseBean = gson.fromJson(result,new TypeToken< BaseBean<VideoAllInfos>>(){}.getType());
                                videoAllInfos = baseBean.getData();
                                initPayOffDialog(list.get(position).getId()+"");
                            }
                        }, list.get(position).getId()+"", memberid + "", token);*/
                    }
                });
            }else {
                viewHolder.tv_xufei.setVisibility(View.GONE);
                viewHolder.simpleDraweeView_bg.setVisibility(View.GONE);
                viewHolder.tv_have_time.setVisibility(View.GONE);
                viewHolder.rl_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VideoInfoActivity.startActivity(context,myVideoItem.getId()+"");
                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (myVideoItem.isSelect()){
            viewHolder.iv_select.setImageDrawable(context.getResources().getDrawable(R.mipmap.selected));
        }else {
            viewHolder.iv_select.setImageDrawable(context.getResources().getDrawable(R.mipmap.unselect));
        }
        one = viewHolder.iv_select.getWidth()*2;
        this.type = ((MyVideoActivity)context).type;
        if (type==2){
            TranslateAnimation animation = new TranslateAnimation(0,one,0,0);
            animation.setDuration(300);
            animation.setFillAfter(true);// True:图片停在动画结束位置
            viewHolder.ll_item.startAnimation(animation);
        }else if (type ==1){
            TranslateAnimation animation = new TranslateAnimation(one,0,0,0);
            animation.setDuration(300);
            animation.setFillAfter(true);// True:图片停在动画结束位置
            viewHolder.ll_item.startAnimation(animation);
        }
        return convertView;
    }
    public class ViewHolder{
        public SimpleDraweeView simpleDraweeView;
        public SimpleDraweeView simpleDraweeView_bg;
        public TextView tv_title;
        public TextView tv_look_count;
        public TextView tv_play_time;
        public TextView tv_good_time;
        //public RelativeLayout rl_more;
        public ImageView iv_select;
        public LinearLayout ll_item;
        public RelativeLayout rl_item;
        public TextView tv_xufei;
        public TextView tv_have_time;
    }

    private void initPayOffDialog(final String goodsid) {
        if (videoAllInfos != null) {
            final VideoPayOffDialog videoPayOffDialog = new VideoPayOffDialog(context, videoAllInfos);
            WindowManager.LayoutParams attributes = videoPayOffDialog.getWindow().getAttributes();
            attributes.height = WindowManager.LayoutParams.WRAP_CONTENT;
            attributes.width = WindowManager.LayoutParams.MATCH_PARENT;
            attributes.gravity = Gravity.BOTTOM;
            videoPayOffDialog.setCanceledOnTouchOutside(true);
            videoPayOffDialog.setOnClickChoose(new VideoPayOffDialog.OnClickChoose() {
                @Override
                public void onClick(boolean f) {

                    if (f) {
                        price = videoPayOffDialog.tv_price.getText().toString();
                        OrdernumberActivity.startActivity(context,goodsid,videoPayOffDialog.prop,price);
                    } else {
                        videoPayOffDialog.dismiss();
                    }
                }
            });
            videoPayOffDialog.show();
        }else {
            Toast.makeText(context,"未请求到数据！",Toast.LENGTH_SHORT).show();
        }
    }
}
