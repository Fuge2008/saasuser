package com.small.saasuser.utils;



import com.small.saasuser.activity.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;


public class LoadDialog extends Dialog {

	private ImageView imgload;
	private AnimationDrawable animationDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.t_loading);
		imgload = (ImageView) findViewById(R.id.loadimg); // 通过ImageView对象拿到背景显示的AnimationDrawable
		animationDrawable = (AnimationDrawable) imgload.getBackground();
		setCancelable(isShowing());

	}

	public LoadDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	public LoadDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	public LoadDialog(Context context) {
		super(context);

		// TODO Auto-generated constructor stub
	}

	public void startAnim() {
		if (animationDrawable != null) {
			animationDrawable.start();
		}

	}

	@Override
	public void show() {
		super.show();
		startAnim();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			
			dismiss();
		}
		return false;
	}
}
