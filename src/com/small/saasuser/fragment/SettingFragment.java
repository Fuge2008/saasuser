package com.small.saasuser.fragment;

import org.apache.http.message.BasicNameValuePair;

import com.saas.chat.Constants;
import com.saas.chat.common.Utils;
import com.saas.chat.view.activity.WebViewActivity;
import com.small.saasuser.activity.AppSettingActivity;
import com.small.saasuser.activity.UserLoginActivity;
import com.small.saasuser.global.HttpConstant;
import com.small.saasuser.activity.FAQActivity;
import com.small.saasuser.activity.R;
import com.small.saasuser.activity.SAASActivity;
import com.small.saasuser.activity.SystemSettingActivity;
import com.small.saasuser.activity.UserGuideActivity;
import com.small.saasuser.utils.CacheUtil;
import com.small.saasuser.utils.LogUtil;
import com.small.saasuser.utils.SharePreferenceUtils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SettingFragment extends BaseFragment implements OnClickListener {

	private TextView tv_app_setting;
	private TextView tv_contact_customer_service;
	private TextView tv_about_SAAS;
	private TextView tv_use_guide;
	private TextView faq;
	private Button btn_logout2;
	private SharePreferenceUtils sp;

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_system_setting, null);
		tv_app_setting = (TextView) view.findViewById(R.id.app_setting);
		tv_contact_customer_service = (TextView) view.findViewById(R.id.contact_customer_service);
		tv_about_SAAS = (TextView) view.findViewById(R.id.about_SAAS);
		tv_use_guide = (TextView) view.findViewById(R.id.use_guide);
		faq = (TextView) view.findViewById(R.id.faq);
		sp = SharePreferenceUtils.getIntance();
		btn_logout2 = (Button) view.findViewById(R.id.unlogin);
		setListener();
		return view;
	}

	

	private void setListener() {
		tv_app_setting.setOnClickListener(this);
		tv_contact_customer_service.setOnClickListener(this);
		tv_about_SAAS.setOnClickListener(this);
		tv_use_guide.setOnClickListener(this);
		btn_logout2.setOnClickListener(this);
		faq.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.app_setting:

			Intent intent = new Intent(getActivity(), AppSettingActivity.class);
			startActivity(intent);
			break;
		case R.id.contact_customer_service:
			showDialog();
			break;
		case R.id.about_SAAS:

			Utils.start_Activity(mActivity, WebViewActivity.class, new BasicNameValuePair(Constants.Title, "关于SAAS"),
					new BasicNameValuePair(Constants.URL, "http://www.easemob.com/"));

			break;
		case R.id.use_guide:
			Utils.start_Activity(mActivity, WebViewActivity.class, new BasicNameValuePair(Constants.Title, "使用指南"),
					new BasicNameValuePair(Constants.URL,
							"http://wiki.lbsyun.baidu.com/cms/androidsdk/doc/1025v4.1.1/index.html"));

			break;
		case R.id.faq:
			Utils.start_Activity(mActivity, WebViewActivity.class, new BasicNameValuePair(Constants.Title, "FAQ"),
					new BasicNameValuePair(Constants.URL, "http://www.imgeek.org/help/"));

			break;
		case R.id.unlogin:
			sp.putString(getActivity(), "ischeck2", "false");
			Intent inent = new Intent(getActivity(), UserLoginActivity.class);
			startActivity(inent);
			getActivity().finish();
			break;
		}
	}

	public void showDialog() {

		new AlertDialog.Builder(getActivity()).setTitle("系统提示").setMessage("拨打客服电话 (400-6800-235)")

				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						Intent intent = new Intent(Intent.ACTION_CALL);
						Uri data = Uri.parse("tel:" + "4006800235");
						intent.setData(data);
						startActivity(intent);
						arg0.dismiss();

					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						arg0.dismiss();
					}
				}).create().show();

	}

}
