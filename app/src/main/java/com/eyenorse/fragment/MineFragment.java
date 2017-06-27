package com.eyenorse.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.bean.PersonalInfo;
import com.eyenorse.dialog.LogOutDialog;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.main.MainActivity;
import com.eyenorse.mine.ActivateAccountActivity;
import com.eyenorse.mine.ActivateAgencyActivity;
import com.eyenorse.mine.LookHistoryActivity;
import com.eyenorse.mine.MyAgencyListActivity;
import com.eyenorse.mine.MyInvitationActivity;
import com.eyenorse.mine.MyMessageActivity;
import com.eyenorse.mine.MyVideoActivity;
import com.eyenorse.mine.MyselfCheckActivity;
import com.eyenorse.mine.PersonalInfoActivity;
import com.eyenorse.mine.SettingActivity;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.view.HeadZoomScrollView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/23.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.simpleDraweeView_head_icon)
    SimpleDraweeView simpleDraweeView_head_icon;
    @BindView(R.id.textView_mine_activate)
    TextView textView_mine_activate;
    @BindView(R.id.rl_message)
    RelativeLayout rl_message;
    @BindView(R.id.rl_setting)
    RelativeLayout rl_setting;
    @BindView(R.id.rl_mine_collect)
    RelativeLayout rl_mine_collect;
    @BindView(R.id.rl_personal)
    RelativeLayout rl_personal;
    @BindView(R.id.rl_history)
    RelativeLayout rl_history;
    @BindView(R.id.ll_mine_invite)
    LinearLayout ll_mine_invite;
    /*@BindView(R.id.ll_mine_order)
    LinearLayout ll_mine_order;*/
    @BindView(R.id.ll_mine_check)
    LinearLayout ll_mine_check;
    @BindView(R.id.rl_agency)
    RelativeLayout rl_agency;
    /*
        @BindView(R.id.rl_mine_video)
        RelativeLayout rl_mine_video;
    */
    @BindView(R.id.scrollView)
    HeadZoomScrollView scrollView;
    private PersonalInfo personalInfo;
    private int isactivation;
    private int memberId;
    private String token;
    private String error;
    private int isagent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Fresco.initialize(getActivity());
        setContentView(inflater, container, R.layout.fragment_mine);
        memberId = ((MainActivity) getActivity()).memberid;
        token = ((MainActivity) getActivity()).token;
        ((MainActivity) getActivity()).getApiRequestData().ShowDialog(null);
        initRefresh();
        if (memberId > 0) {
            initRequest();
        }
        return view;
    }

    private void initRefresh() {
        scrollView.setOnScrollListener(new HeadZoomScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
            }

            @Override
            public void onReply() { //下拉刷新
                //((MainActivity) getActivity()).getApiRequestData().ShowDialog(null);
                initRequest();
            }
        });
    }

    private void initRequest() {

        if (memberId > 0 && token.length() > 0) {
            ((MainActivity) getActivity()).getApiRequestData().getMemberInfo(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    try {
                        error = new JSONObject(result).getString("error");
                        if (TextUtil.isNull(error) || "".equals(error)) {
                            JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                            personalInfo = gson.fromJson(jsonObject.toString(), PersonalInfo.class);
                            simpleDraweeView_head_icon.setImageURI(personalInfo.getHeadimage());
                            isactivation = personalInfo.getIsactivation();
                            isagent = personalInfo.getIsagent();
                            if (isactivation == 1) {
                                textView_mine_activate.setText(personalInfo.getName());
                            } else {
                                textView_mine_activate.setVisibility(View.VISIBLE);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, memberId + "", token);
        } else {
            //Toast.makeText(getActivity(),"您还未登录！",Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick({R.id.simpleDraweeView_head_icon, R.id.rl_message, R.id.rl_setting, R.id.ll_mine_invite
            , R.id.rl_mine_collect, R.id.rl_history, R.id.ll_mine_check
            , R.id.textView_mine_activate, R.id.rl_personal, R.id.rl_agency})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.simpleDraweeView_head_icon:
                if (isactivation == 0) {
                    ActivateAccountActivity.startActivity(getActivity(), memberId, token);
                } else {
                    PersonalInfoActivity.startActivity(getActivity());
                }
                break;
            case R.id.rl_personal:
                if (isactivation == 0) {
                    ActivateAccountActivity.startActivity(getActivity(), memberId, token);
                } else {
                    PersonalInfoActivity.startActivity(getActivity());
                }
                break;
            case R.id.rl_message:
                if (isactivation == 0) {
                    ActivateAccountActivity.startActivity(getActivity(), memberId, token);
                } else {
                    MyMessageActivity.startActivity(getActivity());
                }
                break;
            case R.id.rl_setting:
                SettingActivity.startActivity(getActivity(), memberId, token);
                break;
            case R.id.ll_mine_invite:
                if (isactivation == 0) {
                    ActivateAccountActivity.startActivity(getActivity(), memberId, token);
                } else {
                    MyInvitationActivity.startActivity(getActivity(), memberId, token);
                }
                break;
            /*case R.id.ll_mine_order:
                MyCollectionActivity.startActivity(getActivity(),memberId,token);
                //MyOrderActivity.startActivity(getActivity(),memberId);
                break;*/
            case R.id.rl_mine_collect:
                if (isactivation == 0) {
                    ActivateAccountActivity.startActivity(getActivity(), memberId, token);
                } else {
                    MyVideoActivity.startActivity(getActivity(), memberId, token);
                }
                break;
            case R.id.rl_history:
                if (isactivation == 0) {
                    ActivateAccountActivity.startActivity(getActivity(), memberId, token);
                } else {
                    LookHistoryActivity.startActivity(getActivity(), memberId, token);
                }
                break;
           /* case R.id.image_vipicon:
                OpenMemberShipActivity.startActivity(getActivity());
                break;*/
            case R.id.ll_mine_check:
                if (isactivation > 0) {
                    MyselfCheckActivity.startActivity(getActivity(), memberId, token);
                } else {
                    LogOutDialog dialog = new LogOutDialog(getActivity(), "请先激活账号后，再查看！", "取消", "激活");
                    dialog.setCancelable(false);
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setOnClickChoose(new LogOutDialog.OnClickChoose() {
                        @Override
                        public void onClick(boolean f) {
                            if (f) {
                                ActivateAccountActivity.startActivity(getActivity(), memberId, token);
                            }
                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.textView_mine_activate:
                if (isactivation == 0) {
                    ActivateAccountActivity.startActivity(getActivity(), memberId, token);
                } else {
                    PersonalInfoActivity.startActivity(getActivity());
                }
                break;
            case R.id.rl_agency:
                if (isagent == 1) {
                    MyAgencyListActivity.startActivity(getActivity(), memberId, token);
                } else {
                    ActivateAgencyActivity.startActivity(getActivity(), memberId, token);
                }
                break;
           /* case R.id.rl_mine_video:
                MyVideoActivity.startActivity(getActivity(),memberId,token);
                break;*/
        }
    }
}
