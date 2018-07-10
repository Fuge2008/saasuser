package com.small.saasuser.activity;

import java.util.ArrayList;
import java.util.List;

import com.small.saasuser.application.MyApplication;
import com.small.saasuser.utils.AnFQNumEditText;
import com.small.saasuser.utils.MessageCommit;
import com.small.saasuser.utils.SharePreferenceUtils;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Created by tanyl on 2016/11/9 14:09
 */
public class MessageCommitActivity extends BaseActivity
		implements View.OnClickListener, AdapterView.OnItemSelectedListener {
	private Button mButton;
	private AnFQNumEditText anFQNumEditText;
	private Spinner spinner;
	private List<String> booksList;
	private ArrayAdapter arrayAdapter;
	private String index;
	public static final String PERCENTAGE = "Percentage";// 类型2(百分比类型)
	private ImageButton btn_left;
	// private Retrofit retrofit;

	private SharePreferenceUtils sp;

	private AutoCompleteTextView autotext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messagecommit);
		sp = SharePreferenceUtils.getIntance();

		mButton = (Button) findViewById(R.id.comit);
		btn_left = (ImageButton) findViewById(R.id.feed_btn_left);
		anFQNumEditText = (AnFQNumEditText) findViewById(R.id.anetDemo);
		autotext = (AutoCompleteTextView) findViewById(R.id.receiver_message);

		autotext.setThreshold(1);

		String[] autoStrings = new String[] { "小猫科技", "小狗科技", "小猫科技有限公司", "大猫科技", "小猫支付公司", "小熊科技公司" };

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(MessageCommitActivity.this,
				R.layout.autocompletetextview, autoStrings);

		autotext.setAdapter(adapter);
		btn_left.setOnClickListener(this);
		mButton.setOnClickListener(this);

		anFQNumEditText.setEtMinHeight(160)// 设置最小高度，单位px
				.setLength(60)// 设置总字数
				.setType(AnFQNumEditText.PERCENTAGE)// TextView显示类型(SINGULAR单数类型)(PERCENTAGE百分比类型)
				.setLineColor("#3F51B5")// 设置横线颜色
				.show();

		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setOnItemSelectedListener(this);

		// 1. 设置数据源
		booksList = new ArrayList<String>();
		booksList.add("政企后台");
		booksList.add("租赁平台");

		// 2. 新建ArrayAdapter
		arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, booksList);
		// 3. 设置下拉菜单的样式
		arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 4. 为Spinner加载适配器
		spinner.setAdapter(arrayAdapter);
		// 5. 为Spinner设置监听器

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.comit:

			break;

		case R.id.feed_btn_left:
			finish();
			break;

		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
		index = String.valueOf(position);
		Log.i("lin", index);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}
