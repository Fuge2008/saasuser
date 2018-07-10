package com.small.saasuser.utils;




import com.small.saasuser.activity.R;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;



/**
 * 动画工具类
 * 
 * @author C's
 * @date 2014-4-25 上午10:57:29
 * @version V1.0
 */
public class AnimationUtil {
	/**
	 * 设置控件左右晃动效果
	 * 
	 * @param activity
	 *            上下文
	 * @param view
	 *            控件
	 */
	public static void setShakeAnimation(Activity activity, View view) {
		try {
			Animation animation = AnimationUtils.loadAnimation(activity,
					R.anim.shake);
			view.startAnimation(animation);
		} catch (Exception e) {
		}
	}
}
