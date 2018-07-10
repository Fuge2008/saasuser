package com.small.saasuser.activity;

import com.small.saasuser.utils.LogUtil;
import com.small.saasuser.utils.ToastUtil;

import android.content.Intent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 未来计划详情
 * @author admin
 *
 */
public class TravelPlanDetailActivity extends BaseActivity implements View.OnClickListener {
	private TextView tv_car_model;
	private TextView tv_passager_id;
	private TextView tv_passager_name;
	private TextView tv_passager_phone;
	private TextView tv_start_address;
	private TextView tv_end_address;
	private TextView tv_passager_num;
	private TextView tv_user_car_time;
	private TextView tv_end_time;
	private TextView tv_make_order_time;
	private TextView tv_pay_type;
	private TextView tv_other_describe;
	private Button btn_begin;
	private Button btn_edit;
	private ImageView iv_back;
	private Bundle bun;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_plan_detail);
		initView();
		initData();
		SetListener();
	}
	/**
	 * 初始化控件
	 */
	private void initView() {
		tv_car_model = (TextView) findViewById(R.id.tv_car_model);
		tv_passager_id = (TextView) findViewById(R.id.tv_passager_id);
		tv_passager_name = (TextView) findViewById(R.id.tv_passager_name);
		tv_passager_phone = (TextView) findViewById(R.id.tv_passager_phone);
		tv_start_address = (TextView) findViewById(R.id.tv_start_address);
		tv_end_address = (TextView) findViewById(R.id.tv_end_address);
		tv_passager_num = (TextView) findViewById(R.id.tv_passager_num);
		tv_user_car_time = (TextView) findViewById(R.id.tv_user_car_time);
		tv_end_time = (TextView) findViewById(R.id.tv_end_time);
		tv_make_order_time = (TextView) findViewById(R.id.tv_make_order_time);
		tv_pay_type = (TextView) findViewById(R.id.tv_pay_type);
		tv_other_describe = (TextView) findViewById(R.id.tv_other_describe);
		btn_begin = (Button) findViewById(R.id.btn_begin);
		btn_edit = (Button) findViewById(R.id.btn_edit);
		iv_back = (ImageView) findViewById(R.id.iv_back);
	}
	/**
	 * 出奢华控件
	 */
	private void initData() {
		bun = getIntent().getExtras();
		String car_appointment = bun.getString("car_appointment");
		int passager_id = bun.getInt("passager_id");
		String passager_name = bun.getString("passager_name");
		String passager_phone = bun.getString("passager_phone");
		String start_address = bun.getString("start_address");
		String end_address = bun.getString("end_address");
		int passager_num = bun.getInt("passager_num");
		String user_car_time = bun.getString("user_car_time");
		String end_time = bun.getString("end_time");
		String make_order_time = bun.getString("make_order_time");
		int pay_type = bun.getInt("pay_type");
		String other_describe = bun.getString("other_describe");

		tv_car_model.setText(car_appointment.toString());
		tv_passager_id.setText(String.valueOf(passager_id));
		tv_passager_name.setText(passager_name.toString());
		tv_passager_phone.setText(passager_phone.toString());
		tv_start_address.setText(start_address.toString());
		tv_end_address.setText(end_address.toString());
		tv_passager_num.setText(String.valueOf(passager_num));
		tv_user_car_time.setText(user_car_time.toString());
		tv_end_time.setText(end_time.toString());
		tv_make_order_time.setText(make_order_time.toString());
		tv_other_describe.setText(other_describe.toString());
		String PAY_TYPE;
		if (1 == pay_type) {
			PAY_TYPE = "现    结";
		} else if (0 == pay_type) {
			PAY_TYPE = "自定义周期结算";
		} else {
			PAY_TYPE = "未     知";
		}
		tv_pay_type.setText(PAY_TYPE);
		// tv_pay_type.setText(String.valueOf(pay_type));
	}

	private void SetListener() {
		iv_back.setOnClickListener(this);
		btn_begin.setOnClickListener(this);
		btn_edit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_begin:// 开启订单，执行订单逻辑
			// TODO 提交服务器，更改订单状态
			Intent intent1 = new Intent(TravelPlanDetailActivity.this, MainActivity.class);
			intent1.putExtra("BEGIN_ORDER", 1);
			startActivity(intent1);
			ToastUtil.showShort(getApplicationContext(), "已经成功开启订单！");
			break;
		case R.id.btn_edit:// 前往修改订单
			// TODO 提交服务器，修改订单主体信息
			Intent intent2 = new Intent(TravelPlanDetailActivity.this, TravelPlanEditActivity.class);
			intent2.putExtras(bun);
			LogUtil.i("info", bun.toString());
			startActivity(intent2);
			break;

		}
	}
}
