package com.eyenorse.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.mine.MyselfCheckActivity;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * Created by zhengkq on 2017/1/2.
 */

public class MyMarkView extends MarkerView {
    private Context context;
    private int x;
    private int y;
    private boolean firstXpos = true;
    private boolean secondXpos = false;
    private boolean thirdXpos = false;
    private boolean fourthXpox = false;
    private boolean fifthXpox = false;
    private boolean firstYpos = true;
    private boolean secondYpos = false;
    private boolean thirdYpos = false;
    private boolean fourthYpox = false;
    private boolean fifthYpox = false;
    private boolean firstEntry = true;
    private boolean secondEntry = false;
    private boolean thirdEntry = false;
    private boolean fourthEntry = false;
    private boolean fifthEntry = false;

    public MyMarkView (Context context, int layoutResource) {
        super(context, layoutResource);
        this.context = context;
        // this markerview only displays a textview
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        TextView textView_markerView = (TextView) findViewById(R.id.textView_markerView);
        ImageView image_icon = (ImageView) findViewById(R.id.image_icon);
        // tvContent.setText("" + e.getVal()); // set the entry-value as the display text
        if (firstEntry){
            textView_markerView.setBackground(getResources().getDrawable(R.drawable.check_icon_right_bg));
            textView_markerView.setVisibility(VISIBLE);
            image_icon.setVisibility(GONE);
            textView_markerView.setText(e.getVal()+"°");
            ((MyselfCheckActivity)context).setRightText(e.getVal()+"°");
            this.firstEntry = !firstEntry;
            this.secondEntry = !secondEntry;
        }else if (secondEntry){
            textView_markerView.setBackground(getResources().getDrawable(R.drawable.check_icon_left_bg));
            textView_markerView.setVisibility(VISIBLE);
            image_icon.setVisibility(GONE);
            textView_markerView.setText(e.getVal()+"°");
            ((MyselfCheckActivity)context).setLeftText(e.getVal()+"°");
            this.secondEntry = !secondEntry;
            this.thirdEntry = !thirdEntry;
        }else if (thirdEntry){
            image_icon.setVisibility(VISIBLE);
            textView_markerView.setVisibility(GONE);
            this.thirdEntry = !thirdEntry;
            this.fourthEntry = !fourthEntry;
        }else if(fourthEntry){
            image_icon.setVisibility(GONE);
            textView_markerView.setVisibility(GONE);
            this.fourthEntry = !fourthEntry;
            this.fifthEntry = !fifthEntry;
        }else if (fifthEntry){
            image_icon.setVisibility(GONE);
            textView_markerView.setVisibility(GONE);
            this.fifthEntry = !fifthEntry;
            this.firstEntry = !firstEntry;
        }
    }

    @Override
    public int getXOffset(float xpos) {
        // this will center the marker-view horizontally
        if (firstXpos){
            x = 15;
            this.firstXpos = !firstXpos;
            this.secondXpos = !secondXpos;
        }else if(secondXpos){
            x = -getWidth()-15;
            this.secondXpos = !secondXpos;
            this.thirdXpos = !thirdXpos;
        }else if(thirdXpos){
            x = -getWidth()/2;
            this.thirdXpos = !thirdXpos;
            this.fourthXpox = !fourthXpox;
        }else if (fourthXpox){
            x = 0;
            this.fourthXpox = !fourthXpox;
            this.fifthXpox = !fifthXpox;
        }else if (fifthXpox){
            x = 0;
            this.fifthXpox = !fifthXpox;
            this.firstXpos = !firstXpos;
        }
        return x;
    }

    @Override
    public int getYOffset(float ypos) {
        // this will cause the marker-view to be above the selected value
        if (firstYpos){
            y = -(getHeight()/2);
            this.firstYpos = !firstYpos;
            this.secondYpos = !secondYpos;
        } else if (secondYpos) {
            y = -(getHeight()/2);
            this.secondYpos = !secondYpos;
            this.thirdYpos = !thirdYpos;
        }else if (thirdYpos){
            y = 0;
            this.thirdYpos = !thirdYpos;
            this.fourthYpox = !fourthYpox;
        }else if (fourthYpox){
            y = 0;
            this.fourthYpox = !fourthYpox;
            this.fifthYpox = !fifthYpox;
        }else if (fifthYpox){
            y = 0;
            this.fifthYpox = !fifthYpox;
            this.firstYpos = !firstYpos;
        }
        return y;
    }
}
