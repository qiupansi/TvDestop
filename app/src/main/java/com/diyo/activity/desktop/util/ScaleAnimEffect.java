package com.diyo.activity.desktop.util;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

/**
 * 伸缩动画效果
 */
public class ScaleAnimEffect {
	private float fromXScale;
	private float toXScale;
	private float fromYScale;
	private float toYScale;
	private long duration;
	private float fromAlpha;
	private float toAlpha;
	// private long offSetDuration;

	/**
	 * 设置缩放参数
	 *
	 * @param fromXScale
	 *            初始X轴缩放比例
	 * @param toXScale
	 *            目标X轴缩放比例
	 * @param fromYScale
	 *            初始Y轴缩放比例
	 * @param toYScale
	 *            目标Y轴缩放比例
	 * @param duration
	 *            动画持续时间
	 */
	public void setAttributes(float fromXScale, float toXScale,
							  float fromYScale, float toYScale, long duration) {
		this.fromXScale = fromXScale;
		this.fromYScale = fromYScale;
		this.toXScale = toXScale;
		this.toYScale = toYScale;
		this.duration = duration;
	}

	/**
	 * 伸缩动画特效
	 * @return
	 */
	public Animation createAnimation() {
		ScaleAnimation anim = new ScaleAnimation(fromXScale, toXScale,
				fromYScale, toYScale, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		anim.setFillAfter(true);
		anim.setInterpolator(new AccelerateInterpolator());
		anim.setDuration(duration);
		return anim;
	}

	/**
	 *透明度动画特效
	 * @param fromAlpha
	 * @param toAlpha
	 * @param duration
	 * @param offsetDuration
	 * @return
	 */
	public Animation alphaAnimation(float fromAlpha, float toAlpha,
			long duration, long offsetDuration) {
		AlphaAnimation anim = new AlphaAnimation(fromAlpha, toAlpha);
		anim.setDuration(duration);
		anim.setStartOffset(offsetDuration);
		anim.setInterpolator(new AccelerateInterpolator());
		return anim;
	}

}
