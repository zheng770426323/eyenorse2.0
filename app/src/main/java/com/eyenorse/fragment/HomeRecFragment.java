package com.eyenorse.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.eyenorse.R;
import com.eyenorse.VisionTrain.RecentlyVideoListActivity;
import com.eyenorse.VisionTrain.VideoInfoActivity;
import com.eyenorse.adapter.HomeListAdapter;
import com.eyenorse.bean.AllCategaryItem;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.ClassifyVideoList;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.view.pullableview.PullableListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by zhengkq on 2017/3/31.
 */

public class HomeRecFragment extends BaseFragment {
    @BindView(R.id.pullToRefreshLayout_store_list)
    PullToRefreshLayout pullToRefreshLayout_store_list;
    @BindView(R.id.listView_message)
    PullableListView listView_message;
    @BindView(R.id.textView_null)
    TextView textView_null;
    private int from;
    public static final String ARG_PAGE = "ARG_PAGE";
    private AllCategaryItem.Categary mPage;
    private int count;
    private List<ClassifyVideoList.ClassifyVideoInfo> allGoods = new ArrayList<>();
    private int lastVisiblePosition;
    private HomeListAdapter adapter;

    public static HomeRecFragment newInstance(AllCategaryItem.Categary page) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_PAGE, page);
        HomeRecFragment pageFragment = new HomeRecFragment();
        pageFragment.setArguments(args);
        return pageFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = (AllCategaryItem.Categary) getArguments().getSerializable(ARG_PAGE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setContentView(inflater, container, R.layout.view_listview);
        //((RecentlyVideoListActivity) getActivity()).getApiRequestData().ShowDialog(null);
        allGoods.clear();
        from = 0;
        initView();
        initRequest();
        return view;
    }

    private void initView() {
        pullToRefreshLayout_store_list.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                from = 0;
                allGoods.clear();
                lastVisiblePosition = 0;
                initRequest();
                pullToRefreshLayout_store_list.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                from += 10;
                lastVisiblePosition = listView_message.getLastVisiblePosition();
                if (from>=count){
                    pullToRefreshLayout_store_list.loadmoreFinish(PullToRefreshLayout.NOMORE);
                }else {
                    pullToRefreshLayout_store_list.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                    initRequest();
                }
            }
        });
    }

    private void initRequest() {
        ((RecentlyVideoListActivity)getActivity()).getApiRequestData().getCategaryVideoList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                BaseBean<ClassifyVideoList> bean = gson.fromJson(result, new TypeToken<BaseBean<ClassifyVideoList>>(){}.getType());
                ClassifyVideoList classifyVideoList = bean.getData();
                List<ClassifyVideoList.ClassifyVideoInfo> goods = classifyVideoList.getGoods();
                count = classifyVideoList.getCount();
                if (goods!=null){
                    allGoods.addAll(goods);
                }
                if (allGoods.size()>0){
                    adapter = new HomeListAdapter(getActivity(),allGoods);
                    listView_message.setAdapter(adapter);
                    listView_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            VideoInfoActivity.startActivity(getActivity(),allGoods.get(position).getGoodsid()+"");
                        }
                    });
                    listView_message.setSelection(lastVisiblePosition);
                    textView_null.setText("");
                }else {
                    textView_null.setText("暂无视频");
                }
            }
        },from+"","10",mPage.getCategoryid()+"");
    }
}
