package com.eyenorse.VisionTrain;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.adapter.SearchHistoryAdapter;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.ErrorInfo;
import com.eyenorse.bean.SearchRecord;
import com.eyenorse.bean.SearchRecordItem;
import com.eyenorse.dialog.LogOutDialog;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.view.FlowLayout;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/28.
 */

public class SearchActivity extends BaseActivity {
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.image_del)
    ImageView image_del;
    @BindView(R.id.edit_search)
    EditText edit_search;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_clear_history)
    TextView tv_clear_history;
    @BindView(R.id.tv_search)
    TextView tv_search;
    private JSONObject jsonObject;
    private SearchRecord searchRecord;
    private List<SearchRecordItem> historySearchList = new ArrayList<>();
    public int memberId;
    public String token;
    private boolean isAdd = true;
    private SearchHistoryAdapter adapter;

    public static void startActivity(Context context, int memberId,String token) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("token",token);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTop(R.color.black);

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
        initView();
    }

    private void initView() {

        initRequest();
    }

    private void initRequest() {
        edit_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager inputMethodManager = (InputMethodManager)
                            SearchActivity.this.getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(SearchActivity.this.getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                    String searchTxt = edit_search.getText().toString().trim();
                    if (searchTxt.length()==0){
                        Toast.makeText(SearchActivity.this, "请输入要搜索的文字！", Toast.LENGTH_SHORT).show();
                    }else {
                        SearchListActivity.startActivity(SearchActivity.this,memberId,token,searchTxt);
                        initAddSearchRequest(searchTxt);
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
                        Toast.makeText(SearchActivity.this, "不支持输入表情符号",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
        if (memberId>0&&token.length()>0){
            getApiRequestData().ShowDialog(null);
            //历史搜索
            getApiRequestData().getHistorySearchRecord(new IOAuthReturnCallBack() {
                @Override
                public void onSuccess(String result, Gson gson) {
                    try {
                        jsonObject = new JSONObject(result).getJSONObject("data");
                        searchRecord = gson.fromJson(jsonObject.toString(), SearchRecord.class);
                        historySearchList = searchRecord.getSearch();
                        if (historySearchList==null||historySearchList.size()==0){
                            tv_clear_history.setVisibility(View.GONE);
                        }else {
                            adapter = new SearchHistoryAdapter(SearchActivity.this,historySearchList);
                            lv.setAdapter(adapter);
                            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String searchvalue = historySearchList.get(position).getSearchvalue();
                                    Log.e("searchvalue",searchvalue);
                                    SearchListActivity.startActivity(SearchActivity.this,memberId,token,searchvalue);
                                }
                            });
                            tv_clear_history.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, memberId + "",token);
        }
    }

    @OnClick({R.id.image_del,R.id.tv_clear_history,R.id.image_back,R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.image_del:
                edit_search.setText("");
                break;
            case R.id.tv_clear_history:
                getApiRequestData().getDeleteHistoryData(new IOAuthReturnCallBack() {
                    @Override
                    public void onSuccess(String result, Gson gson) {
                        try {
                            String error = new JSONObject(result).getString("error");
                            if (TextUtil.isNull(error) || "".equals(error)) {
                                jsonObject = new JSONObject(result).getJSONObject("data");
                                int issuccess = jsonObject.getInt("issuccess");
                                if (issuccess != 0) {
                                    Toast.makeText(SearchActivity.this, "历史记录清空成功！", Toast.LENGTH_SHORT).show();
                                    historySearchList.clear();
                                    adapter.notifyDataSetChanged();
                                    tv_clear_history.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, memberId + "",token);
                break;
            case R.id.tv_search:
                String s = edit_search.getText().toString();
                if (s.length()==0){
                    Toast.makeText(SearchActivity.this, "请输入要搜索的文字！", Toast.LENGTH_SHORT).show();
                }else {
                    SearchListActivity.startActivity(SearchActivity.this,memberId,token,s);
                    initAddSearchRequest(s);
                }
                break;
        }
    }

    private void initAddSearchRequest(String text) {
        if (memberId>0&&token.length()>0){
            if (historySearchList!=null&&historySearchList.size()>0){
                for (int i = 0;i<historySearchList.size();i++){
                    if (historySearchList.get(i).getSearchvalue().equals(text)){
                        isAdd = false;
                    }
                }
            }
            if (isAdd){
                getApiRequestData().ShowDialog(null);
                getApiRequestData().getAddSearchData(new IOAuthReturnCallBack() {
                    @Override
                    public void onSuccess(String result, Gson gson) {
                        try {
                            jsonObject = new JSONObject(result).getJSONObject("data");
                            int issuccess = jsonObject.getInt("issuccess");
                            if (issuccess != 0) {
                                initRequest();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, memberId + "", text,token);
            }
        }
    }

    public boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) || ((codePoint >= 0x20) && codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) || ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }
}
