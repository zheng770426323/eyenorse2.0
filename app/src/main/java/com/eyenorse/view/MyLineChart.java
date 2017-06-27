package com.eyenorse.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.AttributeSet;

import com.eyenorse.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.List;


/**
 * Created by zhengkq on 2017/1/5.
 */

public class MyLineChart extends LineChart {
    public MyLineChart(Context context) {
        super(context);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    // 获取高亮点坐标
    public float[] getHighLightPos(Entry e, Highlight highlight) {
        return getMarkerPosition(e, highlight);
    }

    // 重写这个方法, 绘制出多个marker
    @Override
    protected void drawMarkers(Canvas canvas) {
        // if there is no marker view or drawing marker is disabled
        if (mMarkerView == null|| !mDrawMarkerViews || !valuesToHighlight())
            return;
        Rect newRect = canvas.getClipBounds();
        newRect.inset(-80, 0);  //make the rect larger

        canvas.clipRect(newRect, Region.Op.REPLACE);
        for (int i = 0; i < mIndicesToHighlight.length; i++) {
            Highlight highlight = mIndicesToHighlight[i];
            int xIndex = highlight.getXIndex();
            int dataSetIndex = highlight.getDataSetIndex();
            if ((float) xIndex <= this.mDeltaX && (float) xIndex <= this.mDeltaX * this.mAnimator.getPhaseX()) {
                List<LineDataSet> dataSets = this.mData.getDataSets();
                for (int j= 0;j<dataSets.size();j++){
                    Entry e = dataSets.get(j).getYVals().get(xIndex);
                    if (e != null && e.getXIndex() == this.mIndicesToHighlight[i].getXIndex()) {
                        float[] pos = this.getMarkerPosition(e, highlight);
                        if (this.mViewPortHandler.isInBounds(pos[0], pos[1])) {
                            this.mMarkerView.refreshContent(e, highlight);
                            this.mMarkerView.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
                            this.mMarkerView.layout(0, 0, this.mMarkerView.getMeasuredWidth(), this.mMarkerView.getMeasuredHeight());
                            if (pos[1] - (float) this.mMarkerView.getHeight() <= 0.0F) {
                                float y = (float) this.mMarkerView.getHeight() - pos[1];
                                this.mMarkerView.draw(canvas, pos[0], pos[1] + y);
                            } else {
                                this.mMarkerView.draw(canvas, pos[0], pos[1]);
                            }
                        }
                    }
                }
            }
        }

    }
}
