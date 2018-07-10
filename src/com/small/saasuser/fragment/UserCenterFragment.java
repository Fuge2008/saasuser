package com.small.saasuser.fragment;

import com.saas.chat.SplashActivity;
import com.small.saasuser.activity.MessageCenterActivity;
import com.small.saasuser.activity.MessageCommitActivity;
import com.small.saasuser.activity.PersonalCenterActivity;
import com.small.saasuser.activity.R;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class UserCenterFragment extends BaseFragment implements View.OnClickListener {

	private TextView relativeLayoutPersonal;
	private TextView relativeLayoutSetting;
	private TextView relativeLayoutFankui;

	@Override
	public View initView() {

		View view = View.inflate(mActivity, R.layout.fragment_user_center, null);
		relativeLayoutPersonal = (TextView) view.findViewById(R.id.relativeLayout_personal);
		relativeLayoutSetting = (TextView) view.findViewById(R.id.relativeLayout_setting);
		relativeLayoutFankui = (TextView) view.findViewById(R.id.relativeLayout_fankui);
		setListeners();
		return view;
	}

	private void setListeners() {
		relativeLayoutPersonal.setOnClickListener(this);
		relativeLayoutSetting.setOnClickListener(this);
		relativeLayoutFankui.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.relativeLayout_personal:
			Intent intent = new Intent(getActivity(), PersonalCenterActivity.class);
			startActivity(intent);
			break;
		case R.id.relativeLayout_setting:
			Intent mintent = new Intent(getActivity(), SplashActivity.class);
			startActivity(mintent);
			break;
		case R.id.relativeLayout_fankui:
			Intent mintent1 = new Intent(getActivity(), MessageCommitActivity.class);
			startActivity(mintent1);
			break;

		}

	}

}
