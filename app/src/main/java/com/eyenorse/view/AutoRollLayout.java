package com.eyenorse.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eyenorse.R;
import com.eyenorse.VisionTrain.VideoInfoActivity;
import com.eyenorse.bean.VideoInfo;
import com.eyenorse.welcome.LoginActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2016/5/5.
 */
public class AutoRollLayout extends FrameLayout implements ViewPager.OnPageChangeListener {

    private List<VideoInfo> items ;
    private DecoratorViewPager arl_arl_vp;
    private LinearLayout arl_arl_dot_container;
    private TextView arl_arl_tv_title;
    private int position;
    private GestureDetector gestureDetector;
    private Context context;
    private List<ImageView> imageList = new ArrayList<>();
    private ImageView iv;
    private int nextIndex;
    private int memberid;

    public AutoRollLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public AutoRollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public AutoRollLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.arl_arl_activity, this);
        arl_arl_dot_container = (LinearLayout) findViewById(R.id.arl_arl_dot_container);
        arl_arl_tv_title = (TextView) findViewById(R.id.arl_arl_tv_title);
        arl_arl_vp = (DecoratorViewPager) findViewById(R.id.arl_arl_vp);
        //arl_arl_vp.setNestedpParent((ViewGroup)arl_arl_vp.getParent());
        arl_arl_vp.setOnPageChangeListener(this);
        arl_arl_vp.setOnTouchListener(touchListener);
        gestureDetector = new GestureDetector(getContext(),gestureListener);
    }
    public int getCurrentItem(){
        return arl_arl_vp.getCurrentItem();
    }
    public void setItems(List<VideoInfo> items){
        this.items = items;
        arl_arl_vp.setAdapter(pagerAdapter);
        arl_arl_tv_title.setText(null);
        arl_arl_dot_container.removeAllViews();
        addDots();
        onPageSelected(0);//文字及点的初始化
    }

    static Handler handler = new Handler();
    public void setAntoRoll(boolean roll){
        if (roll){
            handler.postDelayed(runnable,5000);
        }else{
            handler.removeCallbacks(runnable);
        }
    }
    private boolean isTouching = false;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.removeCallbacks(this);
            isTouching=false;
            if (!isTouching){
                 goNextPage();
                handler.postDelayed(this,5000);
            }
        }
    };
    private boolean toRight = true;
    private void goNextPage() {
        if (pagerAdapter.getCount()==1){
            return;
        }
        int currentItem = arl_arl_vp.getCurrentItem();
        if (currentItem==0){
            toRight = true;
        }else if(currentItem ==pagerAdapter.getCount()-1){
            toRight = false;
        }
        //int nextIndex = toRight?currentItem+1:currentItem-1;
        if (currentItem ==pagerAdapter.getCount()-1){
            nextIndex = 0;
        }else {
            nextIndex = currentItem+1;
        }

        arl_arl_vp.setCurrentItem(nextIndex);

    }

    private void addDots() {
        if (items==null||items.isEmpty()){
            return;
        }
        int pxFor6Dp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,6,getResources().getDisplayMetrics());
        arl_arl_dot_container.removeAllViews();
        for (VideoInfo item:items){
            View dot = new View(getContext());
            dot.setBackgroundResource(R.drawable.arl_dot_selector);//设置点的背景
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(pxFor6Dp,pxFor6Dp);
            layoutParams.rightMargin = pxFor6Dp;
            arl_arl_dot_container.addView(dot, layoutParams);
            dot.setOnClickListener(goThisPositionCir);
        }
    }

    public PagerAdapter pagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return items==null?0:items.size();
        }
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            iv = new ImageView(getContext());
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Picasso.with(getContext())
                    .load(items.get(position).getImg())
                    .placeholder(R.mipmap.default_750_250)
                    .error(R.mipmap.default_750_250)
                    .into(iv);
            iv.setTag(position);
            container.removeView(iv);
            container.addView(iv);
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (items.get(position).getVideo_id()>0){
                        VideoInfoActivity.startActivity(context,items.get(position).getVideo_id()+"");
                    }
                   /* if (position==2){
                        VideoInfoActivity.startActivity(context,"7");
                    }else if (position==3){
                        VideoInfoActivity.startActivity(context,"39");
                    }else if (position==4){
                        VideoInfoActivity.startActivity(context,"22");
                    }*/
                }
            });
            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((View) object));
        }
    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(final int position) {
         if (items==null||items.isEmpty()){
             return;
         }
        //String title = items.get(position).getTitle();
        //arl_arl_tv_title.setText(title);

        for (int i = 0;i<pagerAdapter.getCount();i++){
            arl_arl_dot_container.getChildAt(i).setEnabled(i!=position);
        }
     }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private OnClickListener goThisPositionCir = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int index = arl_arl_dot_container.indexOfChild(v);
            arl_arl_vp.setCurrentItem(index);
        }
    };


    private OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            gestureDetector.onTouchEvent(event);
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    isTouching = true;
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    break;
                case MotionEvent.ACTION_UP:
                    isTouching = false;
                    break;
            }
            return false;
        }
    };
    private GestureDetector.OnGestureListener gestureListener = new GestureDetector.OnGestureListener(){

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            AutoRollLayout.this.performClick();
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //AutoRollLayout.this.performClick();
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    };

}
