package com.small.saasuser.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PersonalDetailsActivity extends BaseActivity {
	private ImageView imageViewBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_details);
		initViews();
	}

	private void initViews() {
		// TODO Auto-generated method stub

	}

	// public void doClick(View view){ //报错，暂时注销
	// switch (view.getId()) {
	// case R.id.ivBack:
	// finish();
	// break;
	//
	// }
	// }

}
