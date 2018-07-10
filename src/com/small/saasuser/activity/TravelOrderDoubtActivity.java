package com.small.saasuser.activity;


import com.small.saasuser.utils.ToastUtil;
import com.small.saasuser.view.dialog.SweetAlertDialog;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 存在疑问提交类
 * @author admin
 *
 */
public class TravelOrderDoubtActivity extends BaseActivity implements View.OnClickListener {
	private EditText et_content = null;
	private TextView tv_count = null;
	private Button btn_sumbit;

	private static final int MAX_COUNT = 100;
	private int editStart;
	private int editEnd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_order_doubt);
		initViews();

		setLeftCount();
	}

	private void initViews() {
		et_content = (EditText) findViewById(R.id.et_content);
		et_content.addTextChangedListener(mTextWatcher);
		et_content.setSelection(et_content.length()); // 将光标移动最后一个字符后面
		tv_count = (TextView) findViewById(R.id.tv_count);
		btn_sumbit = (Button) findViewById(R.id.btn_sumbit);
		btn_sumbit.setOnClickListener(this);

	}
	/**
	 * 监听统计字数
	 */
	private TextWatcher mTextWatcher = new TextWatcher() {

		public void afterTextChanged(Editable s) {
			editStart = et_content.getSelectionStart();
			editEnd = et_content.getSelectionEnd();

			// 先去掉监听器，否则会出现栈溢出
			et_content.removeTextChangedListener(mTextWatcher);

			// 注意这里只能每次都对整个EditText的内容求长度，不能对删除的单个字符求长度
			// 因为是中英文混合，单个字符而言，calculateLength函数都会返回1
			if(calculateLength(s.toString())>=MAX_COUNT){
				ToastUtil.showShort(getApplicationContext(), "字数已满");
			}

			while (calculateLength(s.toString()) > MAX_COUNT) { // 当输入字符个数超过限制的大小时，进行截断操作
				s.delete(editStart - 1, editEnd);
				editStart--;
				editEnd--;
							}
			et_content.setText(s);
			et_content.setSelection(editStart);

			// 恢复监听器
			et_content.addTextChangedListener(mTextWatcher);

			setLeftCount();
		}

		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

		}

		public void onTextChanged(CharSequence s, int start, int before, int count) {

		}

	};

	/**
	 * 计算分享内容的字数，一个汉字=两个英文字母，一个中文标点=两个英文标点 注意：该函数的不适用于对单个字符进行计算，因为单个字符四舍五入后都是1
	 * 
	 * @param c
	 * @return
	 */
	private long calculateLength(CharSequence c) {
		double len = 0;
		for (int i = 0; i < c.length(); i++) {
			int tmp = (int) c.charAt(i);
			if (tmp > 0 && tmp < 127) {
				len += 0.5;
			} else {
				len++;
			}
		}
		return Math.round(len);
	}

	/**
	 * 刷新剩余输入字数,最大值100个字
	 */
	private void setLeftCount() {
		tv_count.setText(String.valueOf((MAX_COUNT - getInputCount())));
	}

	/**
	 * 获取用户输入的内容字数
	 * 
	 * @return
	 */
	private long getInputCount() {
		return calculateLength(et_content.getText().toString());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sumbit://请求服务器，发送投诉建议
			//TODO    请求网络
			 new SweetAlertDialog(this, SweetAlertDialog.CUSTOM_IMAGE_TYPE)  
             .setTitleText("提示!")  
             .setContentText("谢谢您的投诉（建议），我们会尽快处理！")  
             .setCustomImage(R.drawable.saas)  
             .show();  
//			finish();
			break;

		}

	}

}
