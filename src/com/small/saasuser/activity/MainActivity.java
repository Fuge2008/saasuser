package com.small.saasuser.activity;

import com.baidu.lbsapi.auth.LBSAuthManagerListener;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BNaviEngineManager.NaviEngineInitListener;
import com.baidu.navisdk.util.verify.BNKeyVerifyListener;
import com.small.saasuser.entity.PassagerUseInfoEntity;
import com.small.saasuser.fragment.SettingFragment;
import com.small.saasuser.fragment.HomePageFragment;
import com.small.saasuser.fragment.TravelPlanFragment;
import com.small.saasuser.fragment.TravelWantFragment;
import com.small.saasuser.fragment.UserCenterFragment;
import com.small.saasuser.utils.SharePreferenceUtils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends BaseActivity implements OnCheckedChangeListener {

	private RadioGroup rg_home;
	private RadioButton rb_home_page, rb_user_center, rb_setting;
	private boolean mIsEngineInitSuccess = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// getWindow().setFormat(PixelFormat.TRANSLUCENT);
		// 初始化导航引擎
		BaiduNaviManager.getInstance().initEngine(this, getSdcardDir(), mNaviEngineInitListener,
				new LBSAuthManagerListener() {
					@Override
					public void onAuthResult(int status, String msg) {
						String str = null;
						if (0 == status) {
							str = "key校验成功!";
						} else {
							str = "key校验失败, " + msg;
						}
						//Toast.makeText(MainActivity.this, str, Toast.LENGTH_LONG).show();
					}
				});
		// 创建Demo视图
		iniViews();
	}

	public static final String TAG_EXIT = "exit";

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null) {
			boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
			if (isExit) {
				this.finish();
			}
		}
	}

	private void iniViews() {
		rg_home = (RadioGroup) findViewById(R.id.rg_home);
		rb_home_page = (RadioButton) findViewById(R.id.rb_home_page);
		rb_user_center = (RadioButton) findViewById(R.id.rb_user_center);
		rb_setting = (RadioButton) findViewById(R.id.rb_setting);
		rg_home.setOnCheckedChangeListener(this);
		rb_home_page.setChecked(true);

	}

	private void changeFragment(Fragment targetFragment) {
		getSupportFragmentManager().beginTransaction().replace(R.id.main_fragment, targetFragment, "fragment")
				.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE).commit();
	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		if (rb_home_page.getId() == checkedId) {
			changeFragment(new HomePageFragment());
		} else if (rb_user_center.getId() == checkedId) {
			changeFragment(new UserCenterFragment());
		} else if (rb_setting.getId() == checkedId) {
			changeFragment(new SettingFragment());
		}
	}

	/**
	 * 获取sd卡路径
	 * 
	 * @return
	 */
	private String getSdcardDir() {
		if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().toString();
		}
		return null;
	}

	/**
	 * 导航引擎初始化监听器
	 */
	private NaviEngineInitListener mNaviEngineInitListener = new NaviEngineInitListener() {
		// 引擎初始化成功
		public void engineInitSuccess() {
			mIsEngineInitSuccess = true;
			// ToastUtil.showShort(mContext, "导航引擎初始化成功！");
		}

		// 引擎开始初始化
		public void engineInitStart() {
		}

		// 引擎初始化失败
		public void engineInitFail() {
			// ToastUtil.showShort(mContext, "导航引擎初始化失败！");
		}
	};
	/**
	 * 校验APP-KEY监听器
	 */
	private BNKeyVerifyListener mKeyVerifyListener = new BNKeyVerifyListener() {
		@Override
		public void onVerifySucc() {
		}

		@Override
		public void onVerifyFailed(int arg0, String arg1) {
		}
	};
	// 定义是否已退出应用
	private static boolean isExit = false;

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			isExit = false;
		}
	};

	/**
	 * 返回事件
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	/** 双击返回键退出应用 */
	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(this, "再按一次退出应用", Toast.LENGTH_SHORT).show();
			// 返回键双击延迟
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		System.exit(0);
		android.os.Process.killProcess(android.os.Process.myUid());
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

}
