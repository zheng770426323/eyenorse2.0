package com.eyenorse.mine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eyenorse.R;
import com.eyenorse.VisionTrain.VisionTrainActivity;
import com.eyenorse.base.BaseActivity;
import com.eyenorse.bean.MyselfCheckData;
import com.eyenorse.dialog.LogOutDialog;
import com.eyenorse.http.IOAuthReturnCallBack;
import com.eyenorse.utils.DateTimeHelper;
import com.eyenorse.utils.TextUtil;
import com.eyenorse.view.MyLineChart;
import com.eyenorse.view.MyMarkView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by zhengkq on 2016/12/30.
 */

public class MyselfCheckActivity extends BaseActivity {
    @BindView(R.id.image_back)
    LinearLayout image_back;
    @BindView(R.id.spread_line_chart)
    MyLineChart mLineChart;
    @BindView(R.id.textView_left)
    EditText textView_left;
    @BindView(R.id.textView_right)
    EditText textView_right;
    @BindView(R.id.textView_confirm)
    TextView textView_confirm;
    @BindView(R.id.textView_date)
    TextView textView_date;
    @BindView(R.id.tv_left)
    TextView tv_left;
    @BindView(R.id.tv_right)
    TextView tv_right;
    @BindView(R.id.tv_before_left)
    TextView tv_before_left;
    @BindView(R.id.tv_before_right)
    TextView tv_before_right;

    //x轴数据
    final ArrayList xValues = new ArrayList();
    // y轴的数据
    private ArrayList yValues = new ArrayList();
    private ArrayList yValues1 = new ArrayList();
    private ArrayList yValues2 = new ArrayList();
    private ArrayList yValues3 = new ArrayList();
    private ArrayList yValues4 = new ArrayList();
    private MyMarkView mv;
    private int memberId;
    private String token;
    private JSONObject jsonObject;
    private float beforelefteye;
    private float beforerighteye;
    private List<MyselfCheckData.AfterCheckData> detectionrecords;
    private LineData lineData;
    private String datetime;
    private String beforedatetime;
    private long firstClick;
    private MyselfCheckData myselfCheckData;
    private LineDataSet lineDataSet;

    public static void startActivity(Context context, int memberId, String token) {
        Intent intent = new Intent(context, MyselfCheckActivity.class);
        intent.putExtra("memberId", memberId);
        intent.putExtra("token", token);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_check);
        setTop(R.color.black);

        Intent intent = getIntent();
        memberId = intent.getIntExtra("memberId", 0);
        token = intent.getStringExtra("token");
        initView();
    }

    private void initView() {

        textView_left.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (textView_left.getText().toString().length() > 0 && textView_right.getText().toString().length() > 0) {
                    textView_confirm.setBackground(getResources().getDrawable(R.drawable.login_confirm_bg));
                }
            }
        });
        textView_right.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (textView_left.getText().toString().length() > 0 && textView_right.getText().toString().length() > 0) {
                    textView_confirm.setBackground(getResources().getDrawable(R.drawable.login_confirm_bg));
                }
            }
        });
        setCentreText("我的自检", getResources().getColor(R.color.text_color_2));
        textView_date.setText(DateTimeHelper.getDateTime());
        mLineChart.setNoDataTextDescription("还未录入数据，去录入数据吧！");
        getApiRequestData().ShowDialog(null);
        getApiRequestData().getCheckData(new IOAuthReturnCallBack() {
            @Override
            public void onSuccess(String result, Gson gson) {
                try {
                    jsonObject = new JSONObject(result).getJSONObject("data");
                    myselfCheckData = gson.fromJson(jsonObject.toString(), MyselfCheckData.class);
                    if (myselfCheckData != null) {
                        if (TextUtil.isNull(myselfCheckData.getBeforelefteye()) || TextUtil.isNull(myselfCheckData.getBeforerighteye()) || TextUtil.isNull(myselfCheckData.getBeforedatetime())) {
                            initDialog();
                        } else {
                            getLineData();
                        }
                    } else {
                        initDialog();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, memberId + "", token);
    }

    private void showChart(final MyLineChart lineChart, LineData lineData, int color) {
        lineChart.setDrawBorders(false); //在折线图上添加边框
        lineChart.setDescription(""); //数据描述
        lineChart.setDrawGridBackground(false); //表格颜色
        lineChart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); //表格的颜色，设置一个透明度

        lineChart.setTouchEnabled(true); //可点击
        lineChart.setDragEnabled(true);  //可拖拽
        lineChart.setScaleEnabled(false);  //可缩放
        lineChart.setPinchZoom(false);
        lineChart.setBackgroundColor(color); //设置背景颜色
        lineChart.setData(lineData);  //填充数据

        Legend mLegend = lineChart.getLegend(); //设置标示，就是那个一组y的value的
        mLegend.setForm(Legend.LegendForm.CIRCLE); //样式
        mLegend.setFormSize(0f); //字体及标示大小
        mLegend.setTextColor(Color.WHITE); //字体及表示颜色
        mLegend.setXOffset(20);

        lineChart.setVisibleXRangeMaximum(5); //x轴可显示的坐标范围
        final XAxis xAxis = lineChart.getXAxis();  //x轴的标示
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); //x轴位置
        xAxis.setTextColor(getResources().getColor(R.color.text_hint));    //字体的颜色
        xAxis.setTextSize(14f); //字体大小
        xAxis.setYOffset(10f); //字体离x轴的距离
        xAxis.setSpaceBetweenLabels(1);//坑爹啊，这是设置相邻两根y线之间的个数，2表示两根之间而已隐藏一根
        xAxis.setGridColor(getResources().getColor(R.color.dark_line1));//网格线颜色
        xAxis.setDrawGridLines(true); //显示网格线
        //xAxis.setTypeface(mTf);

        YAxis axisLeft = lineChart.getAxisLeft(); //y轴左边标示
        YAxis axisRight = lineChart.getAxisRight(); //y轴右边标示
        axisLeft.setTextColor(getResources().getColor(R.color.transparent)); //字体颜色
        axisLeft.setTextSize(12f); //字体大小
        //axisLeft.setAxisMaxValue(10000f); //最大值
        axisLeft.setLabelCount(5, true); //显示格数
        axisLeft.setXOffset(6f);
        axisLeft.setDrawLabels(true);
        axisLeft.setDrawAxisLine(true);
        axisLeft.setDrawGridLines(true);
        axisLeft.setGridColor(getResources().getColor(R.color.dark_line1)); //网格线颜色
        //axisLeft.setTypeface(mTf);
        axisRight.setTextColor(getResources().getColor(R.color.transparent)); //字体颜色
        axisRight.setTextSize(12f); //字体大小
        //axisRight.setAxisMaxValue(10000f); //最大值
        axisRight.setDrawAxisLine(false);
        axisRight.setDrawGridLines(false);
        axisRight.setDrawLabels(true);

        lineChart.animateX(1000, Easing.EasingOption.Linear);  //立即执行动画
        lineChart.animateY(1000, Easing.EasingOption.Linear);  //立即执行动画
        lineChart.invalidate();

        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry entry, int i, Highlight highlight) {
                mv = new MyMarkView(MyselfCheckActivity.this, R.layout.view_chart_markerview);
                lineChart.setMarkerView(mv);
                lineChart.invalidate();
            }

            @Override
            public void onNothingSelected() {
                Toast.makeText(MyselfCheckActivity.this, "没选中~！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setLeftText(String text){
        tv_left.setText(text);
    }
    public void setRightText(String text){
        tv_right.setText(text);
    }

    private void getLineData() {
        xValues.clear();
        yValues.clear();
        yValues1.clear();
        yValues2.clear();
        yValues3.clear();
        yValues4.clear();

        tv_before_left.setText(Float.parseFloat(myselfCheckData.getBeforelefteye())+"°");
        tv_before_right.setText(Float.parseFloat(myselfCheckData.getBeforerighteye())+"°");
        beforelefteye = Float.parseFloat(myselfCheckData.getBeforelefteye());
        beforerighteye = Float.parseFloat(myselfCheckData.getBeforerighteye());
        beforedatetime = myselfCheckData.getBeforedatetime();
        detectionrecords = myselfCheckData.getDetectionrecords();

        yValues.add(new Entry(beforerighteye, 0));
        yValues1.add(new Entry(beforelefteye, 0));
        yValues2.add(new Entry(0, 0));
        yValues3.add(new Entry(beforerighteye, 0));
        yValues4.add(new Entry(beforelefteye, 0));
        xValues.add(beforedatetime.substring(5, 7) + "/" + beforedatetime.substring(8, 10));
        DecimalFormat df=new DecimalFormat("#0.0");
        for (int i = 0; i < detectionrecords.size(); i++) {
            float v = Float.parseFloat(detectionrecords.get(i).getRighteye());
            float v1 = Float.parseFloat(detectionrecords.get(i).getLefteye());
            Log.e("v",v+"");
            Log.e("v1",v1+"");
            yValues.add(new Entry(v, i + 1));
            yValues1.add(new Entry(v1, i + 1));
            yValues2.add(new Entry(0, i + 1));
            yValues3.add(new Entry(beforerighteye, i + 1));
            yValues4.add(new Entry(beforelefteye, i + 1));
            datetime = detectionrecords.get(i).getDatetime();
            xValues.add(datetime.substring(5, 7) + "/" + datetime.substring(8, 10));
        }
        lineData = initLineDataSet(xValues);
        showChart(mLineChart, lineData, getResources().getColor(R.color.white));

    }

    private LineData initLineDataSet(ArrayList xValues) {
        // y轴的数据集合
        lineDataSet = new LineDataSet(yValues, "");
        //用y轴的集合来设置参数
        lineDataSet.setLineWidth(2.0f); // 线宽
        lineDataSet.setCircleSize(3.0f);// 显示的圆形大小
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setCircleColor(getResources().getColor(R.color.top_bg_color));
        lineDataSet.setCircleColorHole(getResources().getColor(R.color.top_bg_color));
        lineDataSet.setColor(getResources().getColor(R.color.top_bg_color));// 显示颜色
        //lineDataSet.setCircleColor(getResources().getColor(R.color.top_bg_color));// 圆形的颜色
        lineDataSet.setHighLightColor(getResources().getColor(R.color.text_color_little_red)); // 高亮的线的颜色
        lineDataSet.setDrawVerticalHighlightIndicator(true);
        lineDataSet.setDrawHorizontalHighlightIndicator(false);
        lineDataSet.setHighlightEnabled(true);

        lineDataSet.setValueTextColor(getResources().getColor(R.color.transparent)); //数值显示的颜色
        lineDataSet.setValueTextSize(18f);     //数值显示的大小
        //lineDataSet.enableDashedLine(10f,10f,0);
        lineDataSet.setDrawCubic(true);

        // y轴的数据集合1
        LineDataSet lineDataSet1 = new LineDataSet(yValues1, "");
        lineDataSet1.setLineWidth(2.0f); // 线宽
        lineDataSet1.setCircleSize(3.0f);// 显示的圆形大小
        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setCircleColor(getResources().getColor(R.color.text_color_green));
        lineDataSet1.setCircleColorHole(getResources().getColor(R.color.text_color_green));
        lineDataSet1.setColor(getResources().getColor(R.color.text_color_green));// 显示颜色
        //lineDataSet1.setCircleColor(getResources().getColor(R.color.text_color_green));// 圆形的颜色
        lineDataSet1.setHighLightColor(getResources().getColor(R.color.text_color_little_red)); // 高亮的线的颜色
        lineDataSet1.setDrawVerticalHighlightIndicator(true);// 竖直方向可高亮
        lineDataSet1.setDrawHorizontalHighlightIndicator(false);// 水平方向不可高亮
        lineDataSet1.setHighlightEnabled(true);
        lineDataSet1.setValueTextColor(getResources().getColor(R.color.transparent)); //数值显示的颜色
        lineDataSet1.setValueTextSize(18f);     //数值显示的大小
        //lineDataSet1.enableDashedLine(10f,10f,0);
        lineDataSet1.setDrawCubic(true);

        // y轴的数据集合2（制造假的第三组数据 作为x轴上的游标）
        LineDataSet lineDataSet2 = new LineDataSet(yValues2, "");
        lineDataSet2.setLineWidth(0.0f); // 线宽
        lineDataSet2.setCircleSize(0f);// 显示的圆形大小
        //lineDataSet1.setDrawCircleHole(true);
        lineDataSet2.setDrawCircles(false);
        lineDataSet2.setColor(getResources().getColor(R.color.dark_line1));// 显示颜色
        //lineDataSet1.setCircleColor(getResources().getColor(R.color.text_color_green));// 圆形的颜色
        lineDataSet2.setHighLightColor(getResources().getColor(R.color.text_color_little_red)); // 高亮的线的颜色
        lineDataSet2.setDrawVerticalHighlightIndicator(true);// 竖直方向可高亮
        lineDataSet2.setDrawHorizontalHighlightIndicator(false);// 水平方向不可高亮
        lineDataSet2.setHighlightEnabled(true);
        lineDataSet2.setValueTextColor(getResources().getColor(R.color.transparent)); //数值显示的颜色
        lineDataSet2.setValueTextSize(18f);     //数值显示的大小
        //lineDataSet1.enableDashedLine(10f,10f,0);
        lineDataSet2.setDrawCubic(true);

        // y轴的数据集合3（制造假的第四组数据 作为第一次右眼数据）
        LineDataSet lineDataSet3 = new LineDataSet(yValues3, "");
        lineDataSet3.setLineWidth(1.0f); // 线宽
        lineDataSet3.enableDashedLine(16, 8, 0);
        lineDataSet3.setCircleSize(0f);// 显示的圆形大小
        //lineDataSet1.setDrawCircleHole(true);
        lineDataSet3.setDrawCircles(false);
        lineDataSet3.setColor(getResources().getColor(R.color.top_bg_color));// 显示颜色
        //lineDataSet1.setCircleColor(getResources().getColor(R.color.text_color_green));// 圆形的颜色
        lineDataSet3.setHighLightColor(getResources().getColor(R.color.text_color_little_red)); // 高亮的线的颜色
        lineDataSet3.setDrawVerticalHighlightIndicator(true);// 竖直方向可高亮
        lineDataSet3.setDrawHorizontalHighlightIndicator(false);// 水平方向不可高亮
        lineDataSet3.setHighlightEnabled(true);
        lineDataSet3.setValueTextColor(getResources().getColor(R.color.transparent)); //数值显示的颜色
        lineDataSet3.setValueTextSize(18f);     //数值显示的大小
        //lineDataSet1.enableDashedLine(10f,10f,0);
        lineDataSet3.setDrawCubic(true);

        // y轴的数据集合4（制造假的第五组数据 作为第一次右眼数据）
        LineDataSet lineDataSet4 = new LineDataSet(yValues4, "");
        lineDataSet4.setLineWidth(1.0f); // 线宽
        lineDataSet4.enableDashedLine(16, 8, 0);
        lineDataSet4.setCircleSize(0f);// 显示的圆形大小
        //lineDataSet1.setDrawCircleHole(true);
        lineDataSet4.setDrawCircles(false);
        lineDataSet4.setColor(getResources().getColor(R.color.text_color_green));// 显示颜色
        //lineDataSet1.setCircleColor(getResources().getColor(R.color.text_color_green));// 圆形的颜色
        lineDataSet4.setHighLightColor(getResources().getColor(R.color.text_color_little_red)); // 高亮的线的颜色
        lineDataSet4.setDrawVerticalHighlightIndicator(true);// 竖直方向可高亮
        lineDataSet4.setDrawHorizontalHighlightIndicator(false);// 水平方向不可高亮
        lineDataSet4.setHighlightEnabled(true);
        lineDataSet4.setValueTextColor(getResources().getColor(R.color.transparent)); //数值显示的颜色
        lineDataSet4.setValueTextSize(18f);     //数值显示的大小
        //lineDataSet1.enableDashedLine(10f,10f,0);
        lineDataSet4.setDrawCubic(true);

        ArrayList lineDataSets = new ArrayList();
        lineDataSets.add(lineDataSet); // 添加数据集合
        lineDataSets.add(lineDataSet1);
        lineDataSets.add(lineDataSet2);
        lineDataSets.add(lineDataSet3);
        lineDataSets.add(lineDataSet4);
        //创建lineData
        LineData lineData = new LineData(xValues, lineDataSets);
        return lineData;
    }

    @OnClick({R.id.image_back, R.id.textView_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.rl_message:
                MyMessageActivity.startActivity(MyselfCheckActivity.this);
                break;
            case R.id.textView_confirm:
                long secondClick = System.currentTimeMillis();
                if (secondClick - firstClick < 1000) {
                    firstClick = secondClick;
                    return;
                }
                final String inputLeft = textView_left.getText().toString();
                final String inputRight = textView_right.getText().toString();
                if (inputLeft.length() == 0) {
                    Toast.makeText(MyselfCheckActivity.this, "左眼视力不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                } else if (inputRight.length() == 0) {
                    Toast.makeText(MyselfCheckActivity.this, "右眼视力不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }else if(Float.parseFloat(inputLeft)<=0||Float.parseFloat(inputRight)<=0){
                    Toast.makeText(MyselfCheckActivity.this, "请输入正确的数字！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (myselfCheckData != null) {
                        if (TextUtil.isNull(myselfCheckData.getBeforelefteye()) || TextUtil.isNull(myselfCheckData.getBeforerighteye()) || TextUtil.isNull(myselfCheckData.getBeforedatetime())) {
                            initDialog();
                        } else {
                            getApiRequestData().getInputCheckData(new IOAuthReturnCallBack() {
                                @Override
                                public void onSuccess(String result, Gson gson) {
                                    try {
                                        String error = new JSONObject(result).getString("error");
                                        if (TextUtil.isNull(error) || "".equals(error)) {
                                            jsonObject = new JSONObject(result).getJSONObject("data");
                                            int issuccess = jsonObject.getInt("issuccess");
                                            if (issuccess > 0) {
                                                Toast.makeText(MyselfCheckActivity.this, "数据录入成功！", Toast.LENGTH_SHORT).show();
                                                textView_left.setText("");
                                                textView_right.setText("");
                                                //getLineData();
                                                initView();
                                            } else {
                                                Toast.makeText(MyselfCheckActivity.this, "数据录入失败！", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, inputLeft, memberId + "", inputRight, "2", token);
                        }
                    } else {
                        initDialog();
                    }
                }
                break;
        }
    }

    private void initDialog() {
        LogOutDialog dialog = new LogOutDialog(MyselfCheckActivity.this, "请先录入测试前视力，再查看", "取消", "去录入");
        dialog.setOnClickChoose(new LogOutDialog.OnClickChoose() {
            @Override
            public void onClick(boolean f) {
                if (f) {
                    VisionTrainActivity.startActivity(MyselfCheckActivity.this, memberId, token);
                    finish();
                }
            }
        });
        dialog.show();
    }
}
