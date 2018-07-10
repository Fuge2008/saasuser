package com.small.saasuser.activity;

import com.small.saasuser.activity.map.SaasLocationActivity;
import com.small.saasuser.utils.CommonUtils;
import com.small.saasuser.utils.PreferenceUtils;
import com.small.saasuser.utils.SharePreferenceUtils;
import com.small.saasuser.utils.StringUtils;
import com.small.saasuser.utils.ToastUtil;
import com.small.saasuser.view.ToggleView;
import com.small.saasuser.view.ToggleView.OnSwitchStateUpdateListener;
import com.small.saasuser.view.dialog.SweetAlertDialog;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 到通讯录获取乘客姓名、电话号码
 * @author admin
 *
 */
public class SelectPassengerActivity extends BaseActivity implements View.OnClickListener {
	private ToggleView toggleView;
	private TextView tv_contact;
	private EditText et_name;
	private EditText et_telephone;
	private TextView tv_myself;
	private Button btn_back;
	private ImageView iv_back;
	private String name;
	private String phones;

	public static final String NAME_STR = "DATA_STR_NAME";
	public static final String PHONE_STR = "DATA_STR_PHONE";

	/*
	 * 跳转联系人列表的回调函数
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (data == null) {
				return;
			}
			// 处理返回的data,获取选择的联系人信息
			Uri uri = data.getData();
			String[] contacts = get_telephoneContacts(uri);
			name = contacts[0];
			phones = contacts[1];
			if (StringUtils.isEmpty(name) || StringUtils.isEmpty(phones)) {
				ToastUtil.showShort(SelectPassengerActivity.this, "请把信息补充完整！");
				// new
				// SweetAlertDialog(this).setContentText("请补充完整乘车人姓名及联系方式!").show();
				return;
			}
			String phone=StringUtils.getCorrectPhone(phones);
			if (StringUtils.isPhone(phone)) {
				et_name.setText(name);
				et_telephone.setText(phone);

			} else {
				ToastUtil.showShort(SelectPassengerActivity.this, "您输入的号码有误！");
			}

			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selet_passenger);
		initView();
		setListeners();
		// toggleView.setSwitchBackgroundResource(R.drawable.switch_background);
		// toggleView.setSlideButtonResource(R.drawable.slide_button);
		// toggleView.setSwitchState(true);

		// 暂时设置数据，防止空指针，以后删除
		// TODO
		SharePreferenceUtils.putString(SelectPassengerActivity.this, "myName", "老司机");
		SharePreferenceUtils.putString(SelectPassengerActivity.this, "myPhone", "188 8888 8888");
	}

	private void setListeners() {
		iv_back.setOnClickListener(this);
		btn_back.setOnClickListener(this);
		tv_myself.setOnClickListener(this);
		// 设置跳转到通讯录联系人
		tv_contact.setOnClickListener(this);
		// 设置开关更新监听
		toggleView.setOnSwitchStateUpdateListener(new OnSwitchStateUpdateListener() {

			@Override
			public void onStateUpdate(boolean state) {
				if (state) {
					ToastUtil.showShort(SelectPassengerActivity.this, "您已开启通知!");
					// CommonUtils.toMessageChat(SelectPassengerActivity.this,
					// phone);
				} else {
					ToastUtil.showShort(SelectPassengerActivity.this, "您已关闭通知!");
				}
			}
		});
	}

	private void initView() {
		toggleView = (ToggleView) findViewById(R.id.toggleView);
		tv_contact = (TextView) findViewById(R.id.tv_contact);
		tv_myself = (TextView) findViewById(R.id.tv_myself);
		et_name = (EditText) findViewById(R.id.et_name);
		et_telephone = (EditText) findViewById(R.id.et_telephone);
		btn_back = (Button) findViewById(R.id.btn_back);
		iv_back = (ImageView) findViewById(R.id.iv_back);
	}

	/*
	 * 获取联系人姓名和电话号码
	 */
	private String[] get_telephoneContacts(Uri uri) {
		String[] contact = new String[2];
		// 得到ContentResolver对象
		ContentResolver cr = getContentResolver();
		// 取得电话本中开始一项的光标
		Cursor cursor = cr.query(uri, null, null, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			// 取得联系人姓名
			int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
			contact[0] = cursor.getString(nameFieldColumnIndex);
			// 取得电话号码
			String ContactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			Cursor phone = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactId, null, null);
			if (phone != null) {
				phone.moveToFirst();
				contact[1] = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			}
			phone.close();
			cursor.close();
		} else {
			return null;
		}
		return contact;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_contact:
			Uri uri = Uri.parse("content://contacts/people");
			Intent intent = new Intent(Intent.ACTION_PICK, uri);
			startActivityForResult(intent, 0);
			break;
		case R.id.btn_back:
			String  strPhone= et_telephone.getText().toString();
			String strName = et_name.getText().toString();
			if (StringUtils.isEmpty(strName) || StringUtils.isEmpty(strPhone)) {
				ToastUtil.showShort(SelectPassengerActivity.this, "请把信息补充完整！");
				return;
			} else {
				Intent intent1 = new Intent(SelectPassengerActivity.this, SaasLocationActivity.class);
				intent1.putExtra(NAME_STR, strName);
				intent1.putExtra(PHONE_STR, strPhone);
				setResult(RESULT_OK, intent1);
			}

			finish();
			System.exit(0);
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_myself:
			strName = SharePreferenceUtils.getString(SelectPassengerActivity.this, "myName", "老司机");
			strPhone = SharePreferenceUtils.getString(SelectPassengerActivity.this, "myPhone", "188 8888 8888");
			et_name.setText(strName);
			et_telephone.setText(strPhone);
			break;

		}

	}

}
