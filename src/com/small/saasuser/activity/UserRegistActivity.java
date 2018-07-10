package com.small.saasuser.activity;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import com.google.gson.Gson;
import com.small.saasuser.entity.ChengPassWord;
import com.small.saasuser.entity.ModifyPassward;
import com.small.saasuser.global.HttpConstant;
import com.small.saasuser.utils.AnimationUtil;
import com.small.saasuser.utils.DialogTools;
import com.small.saasuser.utils.JudgeUtils;
import com.small.saasuser.utils.NetUtil;
import com.small.saasuser.utils.SharePreferenceUtils;
import com.small.saasuser.utils.ToastUtil;

public class UserRegistActivity extends BaseActivity {
	private Button bt, regist_btn;
	private ImageButton deleteuser, deletepassword, deletepassword2;
	private int time = 60;
	private String userstring, verificationstring, passwordstring,
			passwordstring1, passwordstring2;
	private EditText user, verification, password, email, password2;
	public InputMethodManager manager;
	private boolean obtain;
	private yanzhengmathread myanzhengmathread;
	private SharePreferenceUtils sp;
	private long f = 0;
	private int time2;
	private RequestParams params;
	private ModifyPassward mModifyPassward;
	private ChengPassWord mChengPassWord;
	private Dialog mDialog;
	Handler timehandler2 = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			if (msg.what == 1) {
				bt.setText(msg.arg1 + "s后重新获取");
			}
			if (msg.what == 2) {
				bt.setText("获取验证码");
				bt.setEnabled(true);
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_regist);
		initView();
		setListener();
		long oldtime = (Long) sp.getLong(UserRegistActivity.this,
				"DriverRegistTime", f);
		Log.i("oldtime", "onSuccess result:2" + oldtime);
		time2 = Integer
				.parseInt(String.valueOf((System.currentTimeMillis() - oldtime) / 1000));
		if (time2 <= 60) {
			time = 60 - time2;
			bt.setEnabled(false);
			myanzhengmathread = new yanzhengmathread(time, timehandler2);
			myanzhengmathread.start();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		onHideSoftInput(event);
		return super.onTouchEvent(event);

	}

	public void onHideSoftInput(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}

		}

	}

	private void initView() {
		// TODO Auto-generated method stub
		sp = SharePreferenceUtils.getIntance();
		user = (EditText) findViewById(R.id.DriverRegist_user);
		verification = (EditText) findViewById(R.id.DriverRegist_Verification);
		password = (EditText) findViewById(R.id.DriverRegist_password);
		password2 = (EditText) findViewById(R.id.DriverRegist_password2);
		regist_btn = (Button) findViewById(R.id.regist);
		bt = (Button) findViewById(R.id.activity_user_registered_btn);
		
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		deleteuser = (ImageButton) findViewById(R.id.delete_password);
		deletepassword = (ImageButton) findViewById(R.id.delete_password2);
		deletepassword2 = (ImageButton) findViewById(R.id.delete_DriverRegist_password2);
	}

	private void setListener() {
		// TODO Auto-generated method stub
		bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(getphone().equals(""))) {
					bt.setEnabled(false);
					bt.setText("正在获取中...");
					params = new RequestParams(
							HttpConstant.USER_RESIGITER_VERIFICATION);
					params.addBodyParameter("PhoneNum", getphone());
					if (NetUtil.isNetworkConnected(UserRegistActivity.this)) {
						x.http().post(params,
								new Callback.CommonCallback<String>() {
									@Override
									public void onSuccess(String result) {
										Log.i("ssssss", result);
										mModifyPassward = new Gson().fromJson(
												result, ModifyPassward.class);
										if (mModifyPassward.getErrCode() == 1) {
											time = 60;
											myanzhengmathread = new yanzhengmathread(
													time, timehandler2);
											myanzhengmathread.start();
											sp.putLong(UserRegistActivity.this,
													"DriverRegistActivityTime",
													System.currentTimeMillis());
										} else {
											mDialog = DialogTools
													.createDialog_onebutton(
															UserRegistActivity.this,
															"系统提示！",
															mModifyPassward
																	.getErrMsg(),
															"确定",
															new OnClickListener() {

																@Override
																public void onClick(
																		View arg0) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	mDialog.dismiss();
																}
															});
											mDialog.show();
											Message m = timehandler2
													.obtainMessage();
											m.what = 2;
											m.arg1 = time;
											timehandler2.sendMessage(m);
										}
									}

									@Override
									public void onError(Throwable ex,
											boolean isOnCallback) {

										Message m = timehandler2
												.obtainMessage();
										m.what = 2;
										m.arg1 = time;
										timehandler2.sendMessage(m);
										mDialog = DialogTools
												.createDialog_onebutton(
														UserRegistActivity.this,
														"系统提示！", "系统故障", "确定",
														new OnClickListener() {

															@Override
															public void onClick(
																	View arg0) {
																// TODO
																// Auto-generated
																// method stub
																mDialog.dismiss();
															}
														});
										mDialog.show();
									}

									@Override
									public void onCancelled(
											CancelledException cex) {
									}

									@Override
									public void onFinished() {

									}
								});
					} else {

						DialogTools.showNoNetWork(UserRegistActivity.this);
					}

				}

			}
		});

		deleteuser.setOnClickListener(new View.OnClickListener() {

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
				password.setText("");
			}
		});
		deletepassword2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				password2.setText("");
			}
		});
		regist_btn.setOnClickListener(new View.OnClickListener() {
			// 确认注册
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub

				if (!(getphone().equals("")) && !(getCaptcha()).equals("")) {
					passwordstring1 = getPassword(password);
					passwordstring2 = getPassword(password2);
					if (passwordstring1.equals(passwordstring2)) {
						params = new RequestParams(HttpConstant.USER_RESIGITER);
						params.addBodyParameter("PhoneNum", getphone());
						params.addParameter("LoginPwd", passwordstring1);
						params.addParameter("RegisterCode", getCaptcha());

						if (NetUtil.isNetworkConnected(UserRegistActivity.this)) {
							x.http().post(params,
									new Callback.CommonCallback<String>() {
										@Override
										public void onSuccess(String result) {
											mChengPassWord = new Gson()
													.fromJson(result,
															ChengPassWord.class);
											if (mChengPassWord.getErrCode() == 1) {
												mDialog = DialogTools
														.createDialog_onebutton(
																UserRegistActivity.this,
																"系统提示！", mChengPassWord.getErrMsg()+"，并返回到登录界面", "确定",
																new OnClickListener() {

																	@Override
																	public void onClick(
																			View arg0) {
																		// TODO
																		// Auto-generated
																		// method stub
																		mDialog.dismiss();
																		UserRegistActivity.this.finish();
																	}
																});
												mDialog.show();
											} else {
												mDialog = DialogTools
														.createDialog_onebutton(
																UserRegistActivity.this,
																"系统提示！", mChengPassWord.getErrMsg(), "确定",
																new OnClickListener() {

																	@Override
																	public void onClick(
																			View arg0) {
																		// TODO
																		// Auto-generated
																		// method stub
																		mDialog.dismiss();
																	}
																});
												mDialog.show();

											}

										}

										@Override
										public void onError(Throwable ex,
												boolean isOnCallback) {
											mDialog = DialogTools
													.createDialog_onebutton(
															UserRegistActivity.this,
															"系统提示！", "系统故障", "确定",
															new OnClickListener() {

																@Override
																public void onClick(
																		View arg0) {
																	// TODO
																	// Auto-generated
																	// method stub
																	mDialog.dismiss();
																}
															});
											mDialog.show();
										}

										@Override
										public void onCancelled(
												CancelledException cex) {
										}

										@Override
										public void onFinished() {

										}
									});
						} else {

							DialogTools.showNoNetWork(UserRegistActivity.this);
						}
					} else {
						AnimationUtil.setShakeAnimation(
								UserRegistActivity.this, password);
						AnimationUtil.setShakeAnimation(
								UserRegistActivity.this, password2);
						ToastUtil.show(UserRegistActivity.this, "密码不一致", 0);
					}

				}

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
		userstring = user.getText().toString().trim();
		// 判断手机号是否为空
		if (TextUtils.isEmpty(userstring)) {
			ToastUtil.show(UserRegistActivity.this, "请输入手机号!", 0);
			userstring = "";
			AnimationUtil.setShakeAnimation(UserRegistActivity.this, user);
		} else if (!(JudgeUtils.isMobileNO(userstring))) {
			ToastUtil.show(UserRegistActivity.this, "请输入11位数的手机号码!", 0);
			userstring = "";
			AnimationUtil.setShakeAnimation(UserRegistActivity.this, user);
		}
		return userstring;
	}

	/**
	 * 获取输入的密码
	 * 
	 * @return 密码(如果不符合条件,反悔空)
	 */
	public String getPassword() {
		// 密码
		passwordstring = password.getText().toString().trim();
		// 判断密码是否为空
		if (TextUtils.isEmpty(passwordstring)) {
			ToastUtil.show(UserRegistActivity.this, "请输入密码!", 0);
			passwordstring = "";
			AnimationUtil.setShakeAnimation(UserRegistActivity.this, password);
		} else if (passwordstring.length() > 16 || passwordstring.length() < 6) {
			ToastUtil.show(UserRegistActivity.this, "请输入6-16位密码!", 0);
			passwordstring = "";
			AnimationUtil.setShakeAnimation(UserRegistActivity.this, password);
		}
		return passwordstring;
	}

	/**
	 * 获取输入的验证码
	 * 
	 * @return 验证码(如果不符合条件,反悔空)
	 */
	public String getCaptcha() {
		// 验证码
		verificationstring = verification.getText().toString().trim();
		// 判断手机号是否为空
		if (TextUtils.isEmpty(verificationstring)) {
			ToastUtil.show(UserRegistActivity.this, "请输入验证码!", 0);
			verificationstring = "";
			AnimationUtil.setShakeAnimation(UserRegistActivity.this,
					verification);
		} else if (verificationstring.length() != 6) {
			ToastUtil.show(UserRegistActivity.this, "请输入6位数的验证码!", 0);
			verificationstring = "";
			AnimationUtil.setShakeAnimation(UserRegistActivity.this,
					verification);
		}
		return verificationstring;
	}

	/**
	 * 获取输入的密码
	 * 
	 * @return 密码(如果不符合条件,返回空)
	 */
	public String getPassword(EditText userpassword) {
		// 密码
		passwordstring = userpassword.getText().toString().trim();
		// 判断密码是否为空
		if (TextUtils.isEmpty(passwordstring)) {
			ToastUtil.show(UserRegistActivity.this, "请输入密码!", 0);
			passwordstring = "";
			AnimationUtil.setShakeAnimation(UserRegistActivity.this,
					userpassword);
		} else if (passwordstring.length() > 16 || passwordstring.length() < 6) {
			ToastUtil.show(UserRegistActivity.this, "请输入6-16位密码!", 0);
			passwordstring = "";
			AnimationUtil.setShakeAnimation(UserRegistActivity.this,
					userpassword);
		}
		return passwordstring;
	}
}
