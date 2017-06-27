package com.eyenorse.view.pullableview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

public class PullableScrollView extends ScrollView implements Pullable
{

	private int lastX;
	private int lastY;

	public PullableScrollView(Context context)
	{
		super(context);

	}

	public PullableScrollView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

	}

	public PullableScrollView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	@Override
	public boolean canPullDown()
	{
		if (getScrollY() == 0)
			return true;
		else
			return false;
	}

	@Override
	public boolean canPullUp()
	{
		if (getScrollY() >= (getChildAt(0).getHeight() - getMeasuredHeight()))
			return true;
		else
			return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()){
			case MotionEvent.ACTION_DOWN:
				lastX = (int)ev.getX();
				lastY = (int)ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				int x = (int)ev.getX();
				int y = (int)ev.getY();
				Log.e("11111",Math.abs(y-lastY)+"");
				Log.e("22222",Math.abs(x-lastX)+"");
				if (Math.abs(y-lastY)<Math.abs(x-lastX)){
					return false;
				}
				break;
			case MotionEvent.ACTION_UP:
				/*int x = (int)ev.getX();
				int y = (int)ev.getY();
				if (Math.abs(y-lastY)>Math.abs(x-lastX)){
					return true;
				}*/
				break;
		}
		return super.onInterceptTouchEvent(ev);
	}
}
