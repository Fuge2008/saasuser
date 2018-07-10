package com.small.saasuser.activity;

import com.small.saasuser.application.MyApplication;
import com.small.saasuser.receiver.ConnectionChangeReceiver;
import com.small.saasuser.utils.AbAppManager;
import com.small.saasuser.view.SystemBarTintManager;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

/**
 * activity基类
 */
public class BaseActivity extends FragmentActivity {
	public InputMethodManager manager;

	private ConnectionChangeReceiver mReceiver;// 网络连接状态监听广播
	protected ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			Window window = getWindow();
			// Translucent status bar
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
					WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			SystemBarTintManager tintManager = new SystemBarTintManager(this);
			tintManager.setStatusBarTintEnabled(true);
			tintManager.setStatusBarTintResource(R.color.title_color);// 通知栏所需颜色
		}
		AbAppManager.getAbAppManager().addActivity(this);
		// 注册网络监听广播
		registerReceiver();
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 添加到Application的界面集合
		MyApplication.getInstance().addActivity(this);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);// 注销网络广播监听
		// MyApplication.getInstance().delActivityList(this);//
		// 删除Application的界面集合
		AbAppManager.getAbAppManager().finishActivity(this);
	}

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		mReceiver = new ConnectionChangeReceiver();
		this.registerReceiver(mReceiver, filter);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {// 点击监听
		onHideSoftInput(event);
		return super.onTouchEvent(event);

	}

	public void onHideSoftInput(MotionEvent event) {// 点击空白处,关闭输入法软键盘
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}

		}

	}

}
