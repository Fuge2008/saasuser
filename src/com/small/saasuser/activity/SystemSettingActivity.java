package com.small.saasuser.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class SystemSettingActivity extends Activity {
	private ImageView imageViewTitleBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_system_setting);
		initViews();
		setListeners();
		setAdapters();

	}

	private void initViews() {
		imageViewTitleBack = (ImageView) findViewById(R.id.systemsetting_title_back);

	}

	private void setListeners() {
		imageViewTitleBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

	private void setAdapters() {
		// TODO Auto-generated method stub

	}
}
