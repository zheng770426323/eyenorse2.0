package com.eyenorse.VisionTrain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.adapter.SearchListAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.BaseBean;
import com.eyenorse.bean.ClassifyVideoList;
import com.eyenorse.bean.LoginInfo;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.view.pullableview.PullToRefreshLayout;
import com.eyenorse.view.pullableview.PullableListView;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ypy.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2017/3/29.
 */

public class SearchListActivity extends BaseActivity{
    @BindView(R.id.listView)
    PullableListView listView;
    @BindView(R.id.pullToRefreshLayout_store_list)
    PullToRefreshLayout pullToRefreshLayout_store_list;
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.image_del)
    ImageView image_del;
    @BindView(R.id.edit_search)
    EditText edit_search;
    @BindView(R.id.tv_search)
    TextView tv_search;
    private int memberId;
    private String token;
    private int from;
    private String value;
    private List<ClassifyVideoList.ClassifyVideoInfo> goodslist = new ArrayList<>();
    private SearchListAdapter adapter;
    private View headerView;
    private boolean isrecommend;
    private int lastVisiblePosition;
    private int count;


    public static void startActivity(Context context, int memberId, String token,String value){
        Intent intent = new Intent(context, SearchListActivity.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("token",token);
        intent.putExtra("value",value);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_search_list);
        EventBus.getDefault().register(this);
        setTop(R.color.black);

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
        value = intent.getStringExtra("value");
        initView();
    }

    private void initView() {
        edit_search.setText(value);
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            SearchListActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(SearchListActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    String searchTxt = edit_search.getText().toString().trim();
                    if (searchTxt.length()==0){
                        Toast.makeText(SearchListActivity.this, "请输入要搜索的文字！", Toast.LENGTH_SHORT).show();
                    }else {
                        value = searchTxt;
                        from = 0;
                        goodslist.clear();
                        initRequest();
                    }
                }
                return false;
            }
        });
        edit_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int index = edit_search.getSelectionStart() - 1;
                if (index > 0) {
                    if (isEmojiCharacter(s.charAt(index))) {
                        Editable edit = edit_search.getText();
                        edit.delete(s.length() - 2, s.length());
                        Toast.makeText(SearchListActivity.this, "不支持输入表情符号",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        LayoutInflater lif = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = lif.inflate(R.layout.header_search_list, null);
        listView.addHeaderView(headerView);
        initRequest();
        initRefresh();
    }

    private void initRefresh() {
        pullToRefreshLayout_store_list.setOnRefreshListener(new PullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
                from = 0;
                lastVisiblePosition =0;
                goodslist.clear();
                initRequest();
                pullToRefreshLayout_store_list.refreshFinish(PullToRefreshLayout.SUCCEED);
            }

            @Override
            public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
                from += 5;
                lastVisiblePosition = listView.getLastVisiblePosition();
                if (from>=count){
                    pullToRefreshLayout_store_list.loadmoreFinish(PullToRefreshLayout.NOMORE);
                }else {
                    if (isrecommend){
                        initRequestRecommend();
                    }else {
                        initRequest();
                    }
                    pullToRefreshLayout_store_list.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                }
            }
        });
    }

    private void initRequest() {
        getApiRequestData().ShowDialog(null);
        getApiRequestData().getSearchVideoList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                BaseBean<ClassifyVideoList> bean = gson.fromJson(result,new TypeToken<BaseBean<ClassifyVideoList>>(){}.getType());
                ClassifyVideoList data = bean.getData();
                if (data!=null){
                    List<ClassifyVideoList.ClassifyVideoInfo> goods = data.getGoods();
                    count = data.getCount();
                    goodslist.addAll(goods);
                    if (goodslist.size()==0){
                        initRequestRecommend();
                        isrecommend = true;
                    }else {
                        initAdapter();
                        ((TextView) headerView.findViewById(R.id.tv_name)).setText("搜索结果");
                        headerView.findViewById(R.id.rl_none_bg).setVisibility(View.GONE);
                    }
                }
            }
        },from+"","5",value);
    }

    private void initRequestRecommend() {
        getApiRequestData().getOrderVideoList(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                BaseBean<ClassifyVideoList> bean = gson.fromJson(result,new TypeToken<BaseBean<ClassifyVideoList>>(){}.getType());
                ClassifyVideoList data = bean.getData();
                if (data!=null){
                    List<ClassifyVideoList.ClassifyVideoInfo> goods = data.getGoods();
                    count = data.getCount();
                    goodslist.addAll(goods);
                    initAdapter();
                    ((TextView) headerView.findViewById(R.id.tv_name)).setText("大家都在看");
                    headerView.findViewById(R.id.rl_none_bg).setVisibility(View.VISIBLE);
                }
            }
        },from+"","5","6");
    }


    private void initAdapter() {
        adapter = new SearchListAdapter(SearchListActivity.this,goodslist);
        listView.setAdapter(adapter);
        listView.setSelection(lastVisiblePosition);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoInfoActivity.startActivity(SearchListActivity.this,goodslist.get(position-1).getGoodsid()+"");
            }
        });
    }

    @OnClick({R.id.image_back,R.id.image_del,R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.image_del:
                edit_search.setText("");
                break;
            case R.id.tv_search:
                String s = edit_search.getText().toString();
                if (s.length()==0){
                    Toast.makeText(SearchListActivity.this, "请输入要搜索的文字！", Toast.LENGTH_SHORT).show();
                }else {
                    value = s;
                    from = 0;
                    goodslist.clear();
                    initRequest();
                }
                break;
        }
    }
    public boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    public void onEventMainThread(LoginInfo info) {
        this.memberId = info.getMemberid();
        this.token = info.getToken();
        from = 0;
        lastVisiblePosition =0;
        goodslist.clear();
        initRequest();
    }
}
