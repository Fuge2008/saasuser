package com.saas.chat.view.activity;

import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;

import com.small.saasuser.activity.R;
import com.saas.chat.Constants;
import com.saas.chat.GloableParams;
import com.saas.chat.adpter.ContactAdapter;
import com.saas.chat.common.Utils;
import com.saas.chat.view.BaseActivity;
import com.saas.chat.widght.SideBar;

//订阅号列表
public class PublishUserListActivity extends BaseActivity implements
		OnClickListener {
	private TextView txt_title;
	private ImageView img_back, img_right;
	private ListView lvContact;
	private SideBar indexBar;
	private TextView mDialogText;
	private WindowManager mWindowManager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.em_activity_publicmsglist);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void initControl() {
		txt_title = (TextView) findViewById(R.id.txt_title);
		txt_title.setText("公众号");
		img_back = (ImageView) findViewById(R.id.img_back);
		img_back.setVisibility(View.VISIBLE);
		img_right = (ImageView) findViewById(R.id.img_right);
		img_right.setImageResource(R.drawable.em_icon_add);
		img_right.setVisibility(View.VISIBLE);
	}

	@Override
	protected void initView() {
		mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		lvContact = (ListView) findViewById(R.id.lvContact);

		mDialogText = (TextView) LayoutInflater.from(this).inflate(
				R.layout.em_list_position, null);
		mDialogText.setVisibility(View.INVISIBLE);
		indexBar = (SideBar) findViewById(R.id.sideBar);
		indexBar.setListView(lvContact);
		WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_APPLICATION,
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
						| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);
		mWindowManager.addView(mDialogText, lp);
		indexBar.setTextView(mDialogText);
		View layout_head = getLayoutInflater().inflate(
				R.layout.em_layout_head_search, null);
		lvContact.addHeaderView(layout_head);
		lvContact.setAdapter(new ContactAdapter(this, GloableParams.UserInfos));
	}

	@Override
	protected void initData() {
	}

	@Override
	protected void setListener() {
		img_back.setOnClickListener(this);
		img_right.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.img_back:
			Utils.finish(PublishUserListActivity.this);
			break;
		case R.id.img_right:
			Utils.start_Activity(this, PublicActivity.class,
					new BasicNameValuePair(Constants.NAME, "查找公众号"));
			break;
		default:
			break;
		}
	}

}
