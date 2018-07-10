package com.small.saasuser.activity;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.small.saasuser.entity.UserID;
import com.small.saasuser.global.HttpConstant;
import com.small.saasuser.entity.LoginData;
import com.small.saasuser.entity.LoginrRoot;
import com.small.saasuser.utils.AnimationUtil;
import com.small.saasuser.utils.DialogTools;
import com.small.saasuser.utils.JudgeUtils;
import com.small.saasuser.utils.NetUtil;
import com.small.saasuser.utils.SharePreferenceUtils;
import com.small.saasuser.utils.ToastUtil;

public class UserLoginActivity extends BaseActivity {
	private static final String TAG = "DriverLoginActivity";
	private TextView t1, t2, t3;
	private String userString, userpasswordString;
	private EditText user, userpassword;
	private CheckBox remindpassword, remindpassword2;
	private Button login;
	private ImageButton deleteueser, deletepassword;
	public InputMethodManager manager;
	private SharePreferenceUtils sp;
	private Intent inent;
	private LoginrRoot mLoginrRoot;
	private LoginData mLoginData;
	private RequestParams params;
	private Dialog mDialog, mDialog2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_login);
		initView();
		setListener();

	}

	// 点击事件
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		onHideSoftInput(event);
		return super.onTouchEvent(event);

	}

	// 点击Activity 空白处隐藏键盘
	public void onHideSoftInput(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}

		}

	}

	// 登录时保持账号密码
	private void remind() {
		// TODO Auto-generated method stub
		//
		if (!(user.getText().toString().equals(""))
				&& !(userpassword.getText().toString().equals(""))) {
			userString = user.getText().toString().trim();
			userpasswordString = userpassword.getText().toString().trim();
			sp.putString(UserLoginActivity.this, "user", userString);
			sp.putString(UserLoginActivity.this, "userpasswordString",
					userpasswordString);
		}
	}

	private void remind2() {
		// TODO Auto-generated method stub
		sp.putString(UserLoginActivity.this, "ischeck2", "true");
	}

	private void initView() {
		// TODO Auto-generated method stub
		sp = SharePreferenceUtils.getIntance();
		t1 = (TextView) findViewById(R.id.wangle);
		// t2= (TextView) findViewById(R.id.wangle2);
		t3 = (TextView) findViewById(R.id.wangle3);
		user = (EditText) findViewById(R.id.User);
		userpassword = (EditText) findViewById(R.id.UserPassword);
		remindpassword = (CheckBox) findViewById(R.id.remindpassword);
		remindpassword2 = (CheckBox) findViewById(R.id.remindpassword2);
		deleteueser = (ImageButton) findViewById(R.id.delete_user);
		deletepassword = (ImageButton) findViewById(R.id.delete_password);
		login = (Button) findViewById(R.id.login);
		// 获取上次登录的账号或者密码
		user.setText(sp.getString(UserLoginActivity.this, "user", "")
				.toString());
		if (sp.getString(UserLoginActivity.this, "ischeck1", "flase")
				.toString().equals("true")) {
			userpassword.setText(sp.getString(UserLoginActivity.this,
					"userpasswordString", "").toString());
			remindpassword.setChecked(true);
		}
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (sp.getString(UserLoginActivity.this, "ischeck2", "flase")
				.toString().equals("true")) {
			login();
		} else {
			remindpassword2.setChecked(false);
			sp.putString(UserLoginActivity.this, "ischeck2", "false");
		}
	}

	private void setListener() {
		// TODO Auto-generated method stub
		// 记住密码
		remindpassword
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						sp.putString(UserLoginActivity.this, "ischeck1", "true");
					}
				});
		remindpassword2
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						sp.putString(UserLoginActivity.this, "ischeck2", "true");
					}
				});
		deleteueser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				user.setText("");
			}
		});
		deletepassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				userpassword.setText("");
			}
		});
		login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				login();
			}
		});
		t1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 跳转到修改密码界面

				Intent inent = new Intent(UserLoginActivity.this,
						ModifyPasswardActivity.class);
				startActivity(inent);
			}
		});
		// t2.setOnClickListener(new View.OnClickListener() {
		// @Override
		// public void onClick(View arg0) {
		// //跳转到验证码登录
		//
		// Intent inent=new
		// Intent(DriverLoginActivity.this,DriverVerificationLogin.class);
		// startActivity(inent);
		// }
		// });
		t3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// 跳转到用户注册界面

				Intent inent = new Intent(UserLoginActivity.this,
						UserRegistActivity.class);
				startActivity(inent);
			}
		});

	}

	/**
	 * 获取输入的手机号
	 * 
	 * @return 手机号(如果不符合条件,反悔空)
	 */
	public String getphone() {
		// 手机号
		userString = user.getText().toString().trim();
		// 判断手机号是否为空
		if (TextUtils.isEmpty(userString)) {
			ToastUtil.show(UserLoginActivity.this, "请输入手机号!", 0);
			userString = "";
			AnimationUtil.setShakeAnimation(UserLoginActivity.this, user);
		} else if (!(JudgeUtils.isMobileNO(userString))) {
			ToastUtil.show(UserLoginActivity.this, "请输入11位数的手机号码!", 0);
			userString = "";
			AnimationUtil.setShakeAnimation(UserLoginActivity.this, user);
		}
		return userString;
	}

	/**
	 * 获取输入的密码
	 * 
	 * @return 密码(如果不符合条件,返回空)
	 */
	public String getPassword() {
		// 密码
		userpasswordString = userpassword.getText().toString().trim();
		// 判断密码是否为空
		if (TextUtils.isEmpty(userpasswordString)) {
			ToastUtil.show(UserLoginActivity.this, "请输入密码!", 0);
			userpasswordString = "";
			AnimationUtil.setShakeAnimation(UserLoginActivity.this,
					userpassword);
		} else if (userpasswordString.length() > 16
				|| userpasswordString.length() < 6) {
			ToastUtil.show(UserLoginActivity.this, "请输入6-16位密码!", 0);
			userpasswordString = "";
			AnimationUtil.setShakeAnimation(UserLoginActivity.this,
					userpassword);
		}
		return userpasswordString;
	}

	public void login() {

		mDialog = DialogTools.createLoadingDialog(UserLoginActivity.this,
				"正在登陆.....");

		if (!(getphone().equals("")) && !(getPassword().equals(""))) {
			params = new RequestParams(HttpConstant.USER_LOGIN);
			params.addBodyParameter("PhoneNum", getphone());
			params.addBodyParameter("LoginPwd", getPassword());
			params.addBodyParameter("MobilePlatform", 0 + "");
			params.addBodyParameter("IMEICode", getDvired_ID() + "");
			params.addBodyParameter("Alias", getphone());
			Log.i("PersonalinformationAcitivity", params.toString());
			if (NetUtil.isNetworkConnected(UserLoginActivity.this)) {
				mDialog.show();
				x.http().post(params, new Callback.CommonCallback<String>() {
					@Override
					public void onSuccess(String result) {
						mLoginrRoot = new Gson().fromJson(result,
								LoginrRoot.class);
						if (mLoginrRoot.getErrCode() == 1) {
							Log.i("PersonalinformationAcitivity",
									"onSuccess result:2" + 0);
							if (sp.getString(UserLoginActivity.this,
									"ischeck1", "flase").toString()
									.equals("true")) {
								remind();
							}
							if (sp.getString(UserLoginActivity.this,
									"ischeck2", "flase").toString()
									.equals("true")) {
								remind2();
							}
							sp.putString(UserLoginActivity.this, "UserID", mLoginrRoot.getData()
									.getUserID()+"");
							sp.putString(UserLoginActivity.this, "Identify", mLoginrRoot.getData()
									.getIdentify()+"");
							sp.putString(UserLoginActivity.this, "LoginID", mLoginrRoot.getData()
									.getLoginID()+"");
							Log.i("1", mLoginrRoot.getData()
									.getLoginID()+"");
							Log.i("1", sp.getString(UserLoginActivity.this, "LoginID", null));
							inent = new Intent(UserLoginActivity.this,
									MainActivity.class);
							startActivity(inent);
							finish();
						} else {
							mDialog2 = DialogTools.createDialog_onebutton(
									UserLoginActivity.this, "系统提示！", ""
											+ mLoginrRoot.getErrMsg(), "确定",
									new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO
											// Auto-generated
											// method
											// stub
											mDialog2.dismiss();
										}
									});
							mDialog2.show();
						}
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						mDialog2 = DialogTools.createDialog_onebutton(
								UserLoginActivity.this, "系统提示！", "服务器故障!",
								"确定", new OnClickListener() {

									@Override
									public void onClick(View arg0) {
										// TODO
										// Auto-generated
										// method
										// stub
										mDialog2.dismiss();
									}
								});
						mDialog2.show();
					}

					@Override
					public void onCancelled(CancelledException cex) {

					}

					@Override
					public void onFinished() {
						mDialog.dismiss();
					}
				});
			} else {
				mDialog.dismiss();
				DialogTools.showNoNetWork(UserLoginActivity.this);
			}

		}
	}

	public String getDvired_ID() {
		TelephonyManager tm = (TelephonyManager) this
				.getSystemService(Context.TELEPHONY_SERVICE);
		String DEVICE_ID = tm.getDeviceId();
		return DEVICE_ID;
	}
}
