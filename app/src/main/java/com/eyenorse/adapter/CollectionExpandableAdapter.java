package com.eyenorse.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.bean.MyCollectionGroup;
import com.eyenorse.bean.MyCollectionVideoList;
import com.eyenorse.dialog.LogOutDialog;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.main.MainActivity;
import com.eyenorse.mine.MyCollectionActivity;
import com.eyenorse.mine.PersonalInfoActivity;
import com.eyenorse.utils.TextUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

/**
 * Created by zhengkq on 2016/12/27.
 */

public class CollectionExpandableAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<MyCollectionGroup> groupList;
    private JSONObject jsonObject;
    private String invitationurl;
    private OnekeyShare oks;

    public CollectionExpandableAdapter(Context context) {
        this.context = context;
        this.groupList = ((MyCollectionActivity) context).groupList;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return groupList.get(i).getChildList().size();
    }

    @Override
    public Object getGroup(int i) {
        return groupList.get(i);
    }

    @Override
    public Object getChild(int i, int j) {
        return groupList.get(i).getChildList().get(j);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int j) {
        return j;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_my_collection_group, null);
        }
        TextView textView_title = (TextView) convertView.findViewById(R.id.textView_title);
        textView_title.setText(groupList.get(i).getDateTime());
        return convertView;
    }

    @Override
    public View getChildView(final int i, final int i1, boolean b, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_my_collection_child, null);
        }
        final List<MyCollectionVideoList.MyCollectionVideo> childList = groupList.get(i).getChildList();
        LinearLayout ll_delete = (LinearLayout) convertView.findViewById(R.id.ll_delete);
        LinearLayout ll_share = (LinearLayout) convertView.findViewById(R.id.ll_share);
        TextView textView_title1 = (TextView) convertView.findViewById(R.id.textView_title);
        textView_title1.setText(childList.get(i1).getGoodstitle());
        TextView textView_look_count = (TextView) convertView.findViewById(R.id.textView_look_count);
        textView_look_count.setText(childList.get(i1).getVideopageviews()+"");
        ImageView image_icon = (ImageView) convertView.findViewById(R.id.image_icon);
        if (childList.get(i1).getGoodsimages()!=null&&childList.get(i1).getGoodsimages().size()>0){
            Picasso.with(context)
                    .load(childList.get(i1).getGoodsimages().get(0))
                    .placeholder(R.mipmap.default_254_144)
                    .error(R.mipmap.default_254_144)
                    .into(image_icon);
        }
        ll_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LogOutDialog logOutDialog = new LogOutDialog(context, "您确定要删除这条记录吗？", "取消", "确定");
                logOutDialog.setOnClickChoose(new LogOutDialog.OnClickChoose() {
                    @Override
                    public void onClick(boolean f) {
                        if (f) {
                            ((MyCollectionActivity) context).getApiRequestData().getMyCollectionsDelete(new IOAuthReturnCallBack() {
                                @Override
                                public void onSuccess(String result, Gson gson) {
                                    try {
                                        String error = new JSONObject(result).getString("error");
                                        if (TextUtil.isNull(error) || "".equals(error)) {
                                            jsonObject = new JSONObject(result).getJSONObject("data");
                                            int issuccess = jsonObject.getInt("issuccess");
                                            if (issuccess == 1) {
                                                childList.remove(i1);
                                                ((MyCollectionActivity) context).count--;
                                                if (childList.size() == 0) {
                                                    groupList.remove(i);
                                                }
                                                if (((MyCollectionActivity) context).count == 0) {
                                                    ((MyCollectionActivity) context).initNoneData();
                                                    ((MyCollectionActivity) context).initSetText();
                                                } else {
                                                    ((MyCollectionActivity) context).initSetText();
                                                }
                                                notifyDataSetChanged();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, ((MyCollectionActivity) context).memberId + "", childList.get(i1).getGoodsid() + "",((MyCollectionActivity) context).token);
                        }
                    }
                });
                logOutDialog.show();
            }
        });
        ll_share.setOnClickListener(new View.OnClickListener() {
            private Bitmap imageData;
            @Override
            public void onClick(View view) {
                ShareSDK.initSDK(context);
                ((MyCollectionActivity) context).getApiRequestData().getInvitationAdress(new IOAuthReturnCallBack() {
                    @Override
                    public void onSuccess(String result, Gson gson) {
                        try {
                            String error = new JSONObject(result).getString("error");
                            if (TextUtil.isNull(error) || "".equals(error)) {
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                invitationurl = jsonObject.getString("invitationurl");
                                oks = new OnekeyShare();
                                //关闭sso授权
                                oks.disableSSOWhenAuthorize();
                                // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
                                oks.setTitle("眼护士");
                                // comment是我对这条分享的评论，仅在人人网和QQ空间使用
                                oks.setComment("眼护士VR眼保健操");
                                // site是分享此内容的网站名称，仅在QQ空间使用
                                oks.setSite(context.getString(R.string.app_name));

                                oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
                                    @Override
                                    public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                                        if ("QZone".equals(platform.getName())) {
                                            paramsToShare.setText(childList.get(i1).getGoodstitle());
                                            paramsToShare.setTitle("眼护士-\"每天给我一点时间 让你拥有清晰光明\"");
                                            paramsToShare.setTitleUrl(invitationurl);
                                            paramsToShare.setImageUrl(childList.get(i1).getGoodsimages().get(0));

                                        }
                                        if ("QQ".equals(platform.getName())) {
                                            paramsToShare.setText(childList.get(i1).getGoodstitle());
                                            paramsToShare.setTitle("眼护士-\"每天给我一点时间 让你拥有清晰光明\"");
                                            paramsToShare.setImageUrl(childList.get(i1).getGoodsimages().get(0));
                                            paramsToShare.setTitleUrl(invitationurl);
                                        }
                                        if ("SinaWeibo".equals(platform.getName())) {
                                            paramsToShare.setUrl(null);
                                            paramsToShare.setText("眼护士-\"每天给我一点时间 让你拥有清晰光明\"" + childList.get(i1).getGoodstitle() + invitationurl);
                                            paramsToShare.setImageUrl(childList.get(i1).getGoodsimages().get(0));
                                        }
                                        if ("Wechat".equals(platform.getName())) {
                                            paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                                            paramsToShare.setText(childList.get(i1).getGoodstitle());
                                            paramsToShare.setTitle("眼护士-\"每天给我一点时间 让你拥有清晰光明\"");
                                            // url仅在微信（包括好友和朋友圈）中使用
                                            paramsToShare.setUrl(invitationurl);
                                            //paramsToShare.setTitleUrl("http://sweetystory.com/");
                                            paramsToShare.setImageUrl(childList.get(i1).getGoodsimages().get(0));
                                        }
                                        if ("WechatMoments".equals(platform.getName())) {
                                            paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                                            //paramsToShare.setText("一起来做眼护士VR眼保健操吧...");
                                            paramsToShare.setTitle("眼护士-\"每天给我一点时间 让你拥有清晰光明\"" + childList.get(i1).getGoodstitle());
                                            // url仅在微信（包括好友和朋友圈）中使用
                                            paramsToShare.setUrl(invitationurl);
                                            paramsToShare.setImageUrl(childList.get(i1).getGoodsimages().get(0));
                                        }
                                    }
                                });
                                // 启动分享GUI
                                oks.show(context);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },((MyCollectionActivity) context).memberId+"",((MyCollectionActivity) context).token);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
