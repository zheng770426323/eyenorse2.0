package com.eyenorse.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.adapter.MyLowerLevelAdapter;
import com.eyenorse.bean.InvitationInfo;
import com.eyenorse.bean.MyInvitationList;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.mine.MyInvitationActivity;
import com.eyenorse.mine.MyLowerInfoActivity;
import com.eyenorse.mine.MyMessageActivity;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.view.pullableview.PullableListView;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhengkq on 2016/12/26.
 */

public class MyTeamFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @BindView(R.id.pullToRefreshLayout)
    PullToRefreshLayout pullToRefreshLayout;
    @BindView(R.id.listView_partner)
    PullableListView listView_partner;
    @BindView(R.id.textView_null)
    TextView textView_null;
    @BindView(R.id.textView_count)
    TextView textView_count;
    private List<String>list;
    private int memberId;
    private String token;
    private int from;
    private JSONObject jsonObject;
    private MyInvitationList myInvitationList;
    private List<InvitationInfo> invite = new ArrayList<>();
    private MyLowerLevelAdapter myLowerLevelAdapter;
    private int count;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setContentView(inflater, container, R.layout.fragment_mine_team);
        //simpleViewFragmentAdvertisement.setAspectRatio(15.92f);
        ((MyInvitationActivity) getActivity()).getApiRequestData().ShowDialog(null);
        initView();
        initRequest();
        return view;
    }

    private void initView() {
        pullToRefreshLayout.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                from = 0;
                invite.clear();
                initRequest();
                pullToRefreshLayout.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                from += 15;
                initRequest();
                if (from>=count){
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.NOMORE);
                }else {
                    pullToRefreshLayout.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        });
    }
    private void initRequest() {
        this.memberId = ((MyInvitationActivity)getActivity()).memberId;
        this.token = ((MyInvitationActivity)getActivity()).token;
        ((MyInvitationActivity)getActivity()).getApiRequestData().getInvitationList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    String error = new JSONObject(result).getString("error");
                    if (TextUtil.isNull(error) || error.length() == 0) {
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        myInvitationList = gson.fromJson(jsonObject.toString(), MyInvitationList.class);
                        count = myInvitationList.getCount();
                        invite.addAll(myInvitationList.getInvite());
                        textView_count.setText(myInvitationList.getCount() + "人");
                        myLowerLevelAdapter = new MyLowerLevelAdapter(getActivity(), invite);
                        if (myInvitationList != null && invite.size() > 0) {
                            listView_partner.setAdapter(myLowerLevelAdapter);
                        } else {
                            textView_null.setText("暂无直接下级");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },from+"","15",memberId+"",token);
        listView_partner.setOnItemClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        InvitationInfo invitationInfo = invite.get(i);
        MyLowerInfoActivity.startActivity(getActivity(),invitationInfo);

    }
}
