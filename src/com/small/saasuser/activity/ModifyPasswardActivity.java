package com.small.saasuser.activity;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.common.Callback.CancelledException;
import org.xutils.http.RequestParams;

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

public class ModifyPasswardActivity extends BaseActivity {
	private Button bt, ModifyPassward_logins;
	private int time = 10;
	private ImageButton mdelete_password2, delete_ModifyPassward_password, delete_ModifyPassward_password2;
	private boolean obtain;
	private String userstring, verificationstring, userpasswordString, userpasswordString1, userpasswordString2;
	private EditText user, verification, ModifyPassward_password, ModifyPassward_password2;
	public InputMethodManager manager;
	private yanzhengmathread myanzhengmathread;
	private SharePreferenceUtils sp;
	private long f = 0;
	private int time2;
	private RequestParams params;
	private ModifyPassward mModifyPassward;
	private ChengPassWord mChengPassWord;
	private Dialog mDailog;
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
		setContentView(R.layout.activity_modify_password);
		initView();
		setListener();
		long oldtime = (Long) sp.getLong(ModifyPasswardActivity.this, "ModifyPasswardActivityTime", f);
		Log.i("oldtime", "onSuccess result:2" + oldtime);
		time2 = Integer.parseInt(String.valueOf((System.currentTimeMillis() - oldtime) / 1000));
		if (time2 <= 60) {
			time = 60 - time2;
			bt.setEnabled(false);
			myanzhengmathread = new yanzhengmathread(time, timehandler2);
			myanzhengmathread.start();
		}
	}

	private void initView() {
		// TODO Auto-generated method stub
		user = (EditText) findViewById(R.id.ModifyPassward_user);
		verification = (EditText) findViewById(R.id.ModifyPassward_Verification);
		ModifyPassward_logins = (Button) findViewById(R.id.ModifyPassward_login);
		bt = (Button) findViewById(R.id.ModifyPassward_verification_btn);
		mdelete_password2 = (ImageButton) findViewById(R.id.delete_password2);
		manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		ModifyPassward_password = (EditText) findViewById(R.id.ModifyPassward_password);
		ModifyPassward_password2 = (EditText) findViewById(R.id.ModifyPassward_password2);
		delete_ModifyPassward_password = (ImageButton) findViewById(R.id.delete_ModifyPassward_password);
		delete_ModifyPassward_password2 = (ImageButton) findViewById(R.id.delete_ModifyPassward_password2);

		sp = SharePreferenceUtils.getIntance();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		onHideSoftInput(event);
		return super.onTouchEvent(event);

	}

	public void onHideSoftInput(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null && getCurrentFocus().getWindowToken() != null) {
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}

		}

	}

	private void setListener() {
		// TODO Auto-generated method stub
		bt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!(getphone().equals(""))) {
					bt.setEnabled(false);
					bt.setText("正在获取中...");
					params = new RequestParams(HttpConstant.USER_LOGIN_VERIFICATION);
					params.addBodyParameter("PhoneNum", getphone());
					if (NetUtil.isNetworkConnected(ModifyPasswardActivity.this)) {
						x.http().post(params, new Callback.CommonCallback<String>() {
							@Override
							public void onSuccess(String result) {
								Log.i("33333", result);
								mModifyPassward = new Gson().fromJson(result, ModifyPassward.class);
								if (mModifyPassward.getErrCode() == 1) {
									time = 60;
									myanzhengmathread = new yanzhengmathread(time, timehandler2);
									myanzhengmathread.start();
									sp.putLong(ModifyPasswardActivity.this, "ModifyPasswardActivityTime",
											System.currentTimeMillis());
								} else {
									mDailog = DialogTools.createDialog_onebutton(ModifyPasswardActivity.this, "系统提示！",
											mModifyPassward.getErrMsg(), "确定", new OnClickListener() {

												@Override
												public void onClick(View arg0) {
													// TODO
													// Auto-generated
													// method
													// stub
													mDailog.dismiss();
												}
											});
									mDailog.show();
									Message m = timehandler2.obtainMessage();
									m.what = 2;
									m.arg1 = time;
									timehandler2.sendMessage(m);
								}
							}

							@Override
							public void onError(Throwable ex, boolean isOnCallback) {
								Message m = timehandler2.obtainMessage();
								m.what = 2;
								m.arg1 = time;
								timehandler2.sendMessage(m);

								mDailog = DialogTools.createDialog_onebutton(ModifyPasswardActivity.this, "系统提示！",
										"系统故障", "确定", new OnClickListener() {

											@Override
											public void onClick(View arg0) {
												// TODO
												// Auto-generated
												// method stub
												mDailog.dismiss();
											}
										});
								mDailog.show();
							}

							@Override
							public void onCancelled(CancelledException cex) {
							}

							@Override
							public void onFinished() {

							}
						});
					} else {

						DialogTools.showNoNetWork(ModifyPasswardActivity.this);
					}

				}
			}
		});

		ModifyPassward_logins.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (!(getphone().equals("")) && !(getCaptcha().equals(""))) {
					userpasswordString1 = getPassword(ModifyPassward_password);
					userpasswordString2 = getPassword(ModifyPassward_password2);
					if (userpasswordString1.equals(userpasswordString2)) {
						params = new RequestParams(HttpConstant.USER_CHANGE_PASSWORD);
						params.addBodyParameter("PhoneNum", getphone());
						params.addBodyParameter("LoginPwd", userpasswordString1);
						params.addBodyParameter("ForgetCode", getCaptcha());
						Log.i("ModifyPasswardActivity", "onSuccess result:2" + params);
						if (NetUtil.isNetworkConnected(ModifyPasswardActivity.this)) {
							x.http().post(params, new Callback.CommonCallback<String>() {
								@Override
								public void onSuccess(String result) {
									mChengPassWord = new Gson().fromJson(result, ChengPassWord.class);
									if (mChengPassWord.getErrCode() == 1) {
										mDailog = DialogTools.createDialog_onebutton(ModifyPasswardActivity.this,
												"系统提示！", mChengPassWord.getErrMsg() + ",并返回到登录界面", "确定",
												new OnClickListener() {

													@Override
													public void onClick(View arg0) {
														// TODO
														// Auto-generated
														// method
														// stub
														mDailog.dismiss();
														ModifyPasswardActivity.this.finish();
													}
												});
										mDailog.show();
									} else if (mChengPassWord.getErrCode() == 0) {
										mDailog = DialogTools.createDialog_onebutton(ModifyPasswardActivity.this,
												"系统提示！", mChengPassWord.getErrMsg(), "确定", new OnClickListener() {

													@Override
													public void onClick(View arg0) {
														// TODO
														// Auto-generated
														// method
														// stub
														mDailog.dismiss();

													}
												});
										mDailog.show();
									}

								}

								@Override
								public void onError(Throwable ex, boolean isOnCallback) {
									mDailog = DialogTools.createDialog_onebutton(ModifyPasswardActivity.this, "系统提示！",
											"系统故障", "确定", new OnClickListener() {

												@Override
												public void onClick(View arg0) {
													// TODO
													// Auto-generated
													// method
													// stub
													mDailog.dismiss();
												}
											});
									mDailog.show();
								}

								@Override
								public void onCancelled(CancelledException cex) {
								}

								@Override
								public void onFinished() {

								}
							});
						} else {

							DialogTools.showNoNetWork(ModifyPasswardActivity.this);
						}
					} else {
						AnimationUtil.setShakeAnimation(ModifyPasswardActivity.this, ModifyPassward_password);
						AnimationUtil.setShakeAnimation(ModifyPasswardActivity.this, ModifyPassward_password2);
						ToastUtil.show(ModifyPasswardActivity.this, "密码不一致", 0);
					}

				}
			}
		});
		mdelete_password2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				user.setText("");
			}
		});
		delete_ModifyPassward_password.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ModifyPassward_password.setText("");
			}
		});
		delete_ModifyPassward_password2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ModifyPassward_password2.setText("");
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
			ToastUtil.show(ModifyPasswardActivity.this, "请输入手机号!", 0);
			userstring = "";
			AnimationUtil.setShakeAnimation(ModifyPasswardActivity.this, user);
		} else if (!(JudgeUtils.isMobileNO(userstring))) {
			ToastUtil.show(ModifyPasswardActivity.this, "请输入11位数的手机号码!", 0);
			userstring = "";
			AnimationUtil.setShakeAnimation(ModifyPasswardActivity.this, user);
		}
		return userstring;
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
			ToastUtil.show(ModifyPasswardActivity.this, "请输入验证码!", 0);
			verificationstring = "";
			AnimationUtil.setShakeAnimation(ModifyPasswardActivity.this, verification);
		} else if (verificationstring.length() != 6) {
			ToastUtil.show(ModifyPasswardActivity.this, "请输入6位数的验证码!", 0);
			verificationstring = "";
			AnimationUtil.setShakeAnimation(ModifyPasswardActivity.this, verification);
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
		userpasswordString = userpassword.getText().toString().trim();
		// 判断密码是否为空
		if (TextUtils.isEmpty(userpasswordString)) {
			ToastUtil.show(ModifyPasswardActivity.this, "请输入密码!", 0);
			userpasswordString = "";
			AnimationUtil.setShakeAnimation(ModifyPasswardActivity.this, userpassword);
		} else if (userpasswordString.length() > 16 || userpasswordString.length() < 6) {
			ToastUtil.show(ModifyPasswardActivity.this, "请输入6-16位密码!", 0);
			userpasswordString = "";
			AnimationUtil.setShakeAnimation(ModifyPasswardActivity.this, userpassword);
		}
		return userpasswordString;
	}
}
