package com.diyo.activity.desktop.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.FocusFinder;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import com.diyo.activity.desktop.util.Log;

public class SmoothHorizontalScrollView extends HorizontalScrollView {
	final String TAG = "SmoothHorizontalScrollView";

	/** 传入整体布局  */
	private View ll_content;
	/** 传入更多栏目选择布局 */
	//private View ll_more;
	/** 传入拖动栏布局 */
	private View rl_column;
	/** 左阴影图片 */
	private ImageView leftImage;
	/** 右阴影图片 */
	private ImageView rightImage;
	/** 屏幕宽度 */
	private int mScreenWidth = 0;
	/** 父类的活动activity */
	private Activity activity;

	public SmoothHorizontalScrollView(Context context) {
		this(context, null, 0);
	}

	public SmoothHorizontalScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SmoothHorizontalScrollView(Context context, AttributeSet attrs,
									  int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * 在拖动的时候执行
	 */
	/*@Override
	protected void onScrollChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
		super.onScrollChanged(paramInt1, paramInt2, paramInt3, paramInt4);
		shade_ShowOrHide();
		if(!activity.isFinishing() && ll_content !=null && leftImage!=null
				&& rightImage!=null && rl_column !=null){
			if(ll_content.getWidth() <= mScreenWidth){
				//Log.d("getMeasuredWidth():ll_content.getWidth()");
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
			}
		}else{
			return;
		}
		if(paramInt1 ==0){
			Log.d("getMeasuredWidth():paramInt1 ==0");
			leftImage.setVisibility(View.GONE);
			rightImage.setVisibility(View.VISIBLE);
			return;
		}
		if(ll_content.getWidth() - paramInt1 + rl_column.getLeft() == mScreenWidth){
			leftImage.setVisibility(View.GONE);
			rightImage.setVisibility(View.GONE);
			return;
		}
		leftImage.setVisibility(View.GONE);
		rightImage.setVisibility(View.VISIBLE);
	}*/

	/**
	 * 传入父类布局中的资源文件
	 * */
	public void setParam(Activity activity, int mScreenWidth,View paramView1,
						 ImageView paramView2, ImageView paramView3 ,
						 View paramView5){
		this.activity = activity;
		this.mScreenWidth = mScreenWidth;
		ll_content = paramView1;
		leftImage = paramView2;
		rightImage = paramView3;
		//ll_more = paramView4;
		rl_column = paramView5;
	}
	/**
	 * 判断左右阴影的显示隐藏效果
	 * */
	public void shade_ShowOrHide() {
		if (!activity.isFinishing() && ll_content != null) {
			measure(0, 0);
			//如果整体宽度小于屏幕宽度的话，那左右阴影都隐藏
			//Log.d("getMeasuredWidth():"+getMeasuredWidth()+"mScreenWidth:"+mScreenWidth);
			if (mScreenWidth >= getMeasuredWidth()) {
				Log.d("getMeasuredWidth()2:"+getMeasuredWidth());
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
			}
		} else {
			return;
		}
		//如果滑动在最左边时候，左边阴影隐藏，右边显示
		if (getLeft() == 0) {
			Log.d("getMeasuredWidth()getLeft() == 0:");
			leftImage.setVisibility(View.GONE);
			rightImage.setVisibility(View.VISIBLE);
			return;
		}
		//如果滑动在最右边时候，左边阴影显示，右边隐藏
		//Log.d("Width-getRight():"+getRight());
		if (getRight() == 0) {
			Log.d("getMeasuredWidth()getRight()");
			leftImage.setVisibility(View.GONE);
			rightImage.setVisibility(View.GONE);
			return;
		}
		//否则，说明在中间位置，左、右阴影都显示
		leftImage.setVisibility(View.GONE);
		rightImage.setVisibility(View.VISIBLE);
	}

	@Override
	protected int computeScrollDeltaToGetChildRectOnScreen(Rect rect) {
		if (getChildCount() == 0)
			return 0;
		int width = getWidth();
		int screenLeft = getScrollX();
		int screenRight = screenLeft + width;
		int fadingEdge = 200;   //80
		// leave room for left fading edge as long as rect isn't at very left
		if (rect.left > 0) {
			Log.d("Smooth-rect.left");
			screenLeft += fadingEdge+50;
		}
		// leave room for right fading edge as long as rect isn't at very right
		if (rect.right < getChildAt(0).getWidth()) {
			Log.d("Smooth-rect.right");
			screenRight -= fadingEdge-28;
		}
		int scrollXDelta = 0;
		if (rect.right > screenRight && rect.left > screenLeft) {
			// need to move right to get it in view: move right just enough so
			// that the entire rectangle is in view (or at least the first
			// screen size chunk).

			if (rect.width() > width) {
				Log.d("Smooth-rect.width() > width");
				// just enough to get screen size chunk on
				scrollXDelta += (rect.left - screenLeft);
			} else {
				// get entire rect at right of screen
				scrollXDelta += (rect.right - screenRight);
				Log.d("Smooth-scrollXDelta:"+scrollXDelta);
			}

			// make sure we aren't scrolling beyond the end of our content
			int right = getChildAt(0).getRight();
			int distanceToRight = right - screenRight;
			scrollXDelta = Math.min(scrollXDelta, distanceToRight);

		} else if (rect.left < screenLeft && rect.right < screenRight) {
			// need to move right to get it in view: move right just enough so
			// that
			// entire rectangle is in view (or at least the first screen
			// size chunk of it).

			if (rect.width() > width) {
				// screen size chunk
				scrollXDelta -= (screenRight - rect.right);
				Log.d("Smooth-scrollXDelta");
			} else {
				// entire rect at left
				scrollXDelta -= (screenLeft - rect.left);
			}

			// make sure we aren't scrolling any further than the left our
			// content
			scrollXDelta = Math.max(scrollXDelta, -getScrollX());
		}
		return scrollXDelta;
	}

	@Override
	protected boolean onRequestFocusInDescendants(int direction,
			Rect previouslyFocusedRect) {

		// convert from forward / backward notation to up / down / left / right
		// (ugh).

		if (previouslyFocusedRect != null) {
			if (direction == View.FOCUS_FORWARD) {
				direction = View.FOCUS_RIGHT;
				Log.d("Smooth-direction = View.FOCUS_RIGHT");
			} else if (direction == View.FOCUS_BACKWARD) {
				direction = View.FOCUS_LEFT;
				Log.d("Smooth-direction = View.FOCUS_LEFT");
			}
			View nextFocus = FocusFinder.getInstance().findNextFocusFromRect(
					this, previouslyFocusedRect, direction);
			if (nextFocus == null) {
				return false;
			}
			return nextFocus.requestFocus(direction, previouslyFocusedRect);
		} else {
			int index;
			int increment;
			int end;
			int count = this.getChildCount();
			if ((direction & FOCUS_FORWARD) != 0) {
				index = 0;
				increment = 1;
				end = count;
				Log.d("Smooth-(direction & FOCUS_FORWARD) != 0");
			} else {
				index = count - 1;
				increment = -1;
				end = -1;
			}
			for (int i = index; i != end; i += increment) {
				View child = this.getChildAt(i);
				if (child.getVisibility() == View.VISIBLE) {
					if (child.requestFocus(direction, previouslyFocusedRect)) {
						Log.d("Smooth-previouslyFocusedRect");
						return true;
					}
				}
			}
			return false;
		}
	}
}
