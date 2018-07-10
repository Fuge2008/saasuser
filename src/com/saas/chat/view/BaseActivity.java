package com.saas.chat.view;

import org.apache.http.message.BasicNameValuePair;

import com.saas.chat.common.Utils;
import com.saas.chat.dialog.FlippingLoadingDialog;
import com.saas.chat.net.NetClient;
import com.small.saasuser.activity.R;
import com.small.saasuser.application.MyApplication;
import com.small.saasuser.view.SystemBarTintManager;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public abstract class BaseActivity extends Activity {
	protected Activity context;
	protected NetClient netClient;
	protected FlippingLoadingDialog mLoadingDialog;

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
		super.onCreate(savedInstanceState);
		context = this;
		MyApplication.getInstance2().addActivity(this);
		netClient = new NetClient(this);
		initControl();
		initView();
		initData();
		setListener();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	public void onPause() {
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Utils.finish(this);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 绑定控件id
	 */
	protected abstract void initControl();

	/**
	 * 初始化控件
	 */
	protected abstract void initView();

	/**
	 * 初始化数据
	 */
	protected abstract void initData();

	/**
	 * 设置监听
	 */
	protected abstract void setListener();

	/**
	 * 打开 Activity
	 * 
	 * @param activity
	 * @param cls
	 * @param name
	 */
	public void start_Activity(Activity activity, Class<?> cls, BasicNameValuePair... name) {
		Utils.start_Activity(activity, cls, name);
	}

	/**
	 * 关闭 Activity
	 * 
	 * @param activity
	 */
	public void finish(Activity activity) {
		Utils.finish(activity);
	}

	/**
	 * 判断是否有网络连接
	 */
	public boolean isNetworkAvailable(Context context) {
		return Utils.isNetworkAvailable(context);
	}

	public FlippingLoadingDialog getLoadingDialog(String msg) {
		if (mLoadingDialog == null)
			mLoadingDialog = new FlippingLoadingDialog(this, msg);
		return mLoadingDialog;
	}
}