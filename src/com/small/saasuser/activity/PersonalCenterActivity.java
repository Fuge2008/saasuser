package com.small.saasuser.activity;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;

import com.google.gson.Gson;
import com.small.saasuser.entity.UserCenterData;
import com.small.saasuser.entity.UserCenterRoot;
import com.small.saasuser.entity.UserID;
import com.small.saasuser.global.HttpConstant;
import com.small.saasuser.utils.DialogTools;
import com.small.saasuser.utils.SharePreferenceUtils;
import com.small.saasuser.utils.ToastUtil;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class PersonalCenterActivity extends BaseActivity {

	private TextView query_tv_name, query_tv_Sex, query_tv_Age, query_tv_Heitht, query_tv_Weitht,
			query_tv_Identification, query_tv_PhoneNum, query_tv_EnterpriseName, query_tv_UserPhoto, query_tv_Number,
			query_tv_WorkAge, query_tv_Department, query_tv_Position, query_tv_Level, query_tv_UpLevelStaffName,
			query_tv_UpLevelStaffNumber, query_tv_EntryTime, query_tv_InJobState;
	private UserCenterRoot mUserCenterRoot;
	private UserCenterData mUserCenterData;
	private ImageView query_tv_touxiang;
	private StringBuilder sb;
	private ImageOptions options;
	private RequestParams params;
	private Dialog mDialog;
	private SharePreferenceUtils sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_details);
		mDialog = DialogTools.createLoadingDialog(PersonalCenterActivity.this, "正在加载.....");

		mDialog.show();
		initView();
		getData();
	}

	private void getData() {
		// TODO Auto-generated method stub
		Resources resources = PersonalCenterActivity.this.getResources();
		Drawable drawable = resources.getDrawable(R.drawable.userlogo);
		options = new ImageOptions.Builder().setFadeIn(true).setSquare(true).setFailureDrawable(drawable).build(); // 淡入效果
		params = new RequestParams(HttpConstant.USER_CENTER);
		UserID mDriverID = UserID.getLoginData();
		params.addParameter("LoginID", sp.getString(PersonalCenterActivity.this, "LoginID", null));
		Log.i("JAVA", params.toString());
		Callback.Cancelable cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {

				mUserCenterRoot = new Gson().fromJson(result, UserCenterRoot.class);

				if (mUserCenterRoot.getErrCode() == 1) {

					mUserCenterData = mUserCenterRoot.getData();
					Log.i("JAVA", "onSuccess result:1" + mUserCenterData.toString());
					query_tv_name.setText(mUserCenterData.getName() + "");
					query_tv_Sex.setText(mUserCenterData.getSex() + "");
					query_tv_Age.setText(mUserCenterData.getAge() + "");
					query_tv_Heitht.setText(mUserCenterData.getHeitht() + "");
					query_tv_Weitht.setText(mUserCenterData.getWeitht() + "");
					query_tv_Identification.setText(mUserCenterData.getIdentification() + "");
					query_tv_PhoneNum.setText(mUserCenterData.getPhoneNum() + "");
					query_tv_EnterpriseName.setText(mUserCenterData.getEnterpriseName() + "");

					query_tv_Number.setText(mUserCenterData.getNumber() + "");
					query_tv_WorkAge.setText(mUserCenterData.getWorkAge() + "");
					query_tv_Department.setText(mUserCenterData.getDepartment() + "");
					query_tv_Position.setText(mUserCenterData.getPosition() + "");
					query_tv_Level.setText(mUserCenterData.getLevel() + "");
					query_tv_UpLevelStaffName.setText(mUserCenterData.getUpLevelStaffName() + "");
					query_tv_UpLevelStaffNumber.setText(mUserCenterData.getUpLevelStaffNumber() + "");
					query_tv_EntryTime.setText(mUserCenterData.getEntryTime() + "");
					query_tv_InJobState.setText(mUserCenterData.getInJobState() + "");
					Log.i("ddddddddddd111111111", HttpConstant.BASEURL + mUserCenterData.getUserPhotoPath());
					x.image().loadDrawable(HttpConstant.BASEURL + mUserCenterData.getUserPhotoPath(), options,
							new Callback.CommonCallback<Drawable>() {
								@Override
								public void onSuccess(Drawable result) {
									// if(result==null)
									// query_tv_touxiang.setBackground(R.drawable.R);
									// result.setBounds(0, 0,
									// result.getMinimumWidth(),
									// result.getMinimumHeight());
									query_tv_touxiang.setBackgroundDrawable(result);

								}

								@Override
								public void onError(Throwable ex, boolean isOnCallback) {
								}

								@Override
								public void onCancelled(CancelledException cex) {
								}

								@Override
								public void onFinished() {
								}
							});
					mDialog.dismiss();
				} else {

					DialogTools.createsystemDialog(PersonalCenterActivity.this, "" + mUserCenterRoot.getErrMsg())
							.show();
				}
			}

			// 请求异常后的回调方法
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				Log.i("JAVA", "onSuccess result:2" + ex.toString());
				DialogTools.createsystemDialog(PersonalCenterActivity.this, "服务器故障!").show();
			}

			// 主动调用取消请求的回调方法
			@Override
			public void onCancelled(CancelledException cex) {
				Log.i("JAVA", "onSuccess result:3");
			}

			@Override
			public void onFinished() {
				Log.i("JAVA", "onSuccess result:4");
				mDialog.dismiss();
			}
		});
	}

	private void initView() {
		// TODO Auto-generated method stub
		sp = SharePreferenceUtils.getIntance();
		query_tv_name = (TextView) findViewById(R.id.query_tv_name);
		query_tv_Sex = (TextView) findViewById(R.id.query_tv_Sex);
		query_tv_Age = (TextView) findViewById(R.id.query_tv_Age);
		query_tv_Heitht = (TextView) findViewById(R.id.query_tv_Heitht);
		query_tv_Weitht = (TextView) findViewById(R.id.query_tv_Weitht);
		query_tv_Identification = (TextView) findViewById(R.id.query_tv_Identification);
		query_tv_PhoneNum = (TextView) findViewById(R.id.query_tv_PhoneNum);
		query_tv_EnterpriseName = (TextView) findViewById(R.id.query_tv_EnterpriseName);
		query_tv_Number = (TextView) findViewById(R.id.query_tv_Number);
		query_tv_WorkAge = (TextView) findViewById(R.id.query_tv_WorkAge);
		query_tv_Department = (TextView) findViewById(R.id.query_tv_Department);
		query_tv_Position = (TextView) findViewById(R.id.query_tv_Position);
		query_tv_Level = (TextView) findViewById(R.id.query_tv_Level);
		query_tv_UpLevelStaffName = (TextView) findViewById(R.id.query_tv_UpLevelStaffName);
		query_tv_UpLevelStaffNumber = (TextView) findViewById(R.id.query_tv_UpLevelStaffNumber);
		query_tv_EntryTime = (TextView) findViewById(R.id.query_tv_EntryTime);
		query_tv_InJobState = (TextView) findViewById(R.id.query_tv_InJobState);
		query_tv_touxiang = (ImageView) findViewById(R.id.query_tv_touxiang);
	}

}
