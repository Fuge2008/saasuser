package com.small.saasuser.activity;

import com.small.saasuser.utils.LogUtil;
import com.small.saasuser.utils.StringUtils;
import com.small.saasuser.utils.ToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
/**
 * 历史记录详情
 * @author admin
 *
 */
public class TravelRecordDetailActivity extends BaseActivity implements View.OnClickListener {
	protected static final String TAG = TravelRecordDetailActivity.class.getSimpleName();
	private TextView tv_car_user;
	private TextView tv_user_phone;
	private TextView tv_order_id;
	private TextView tv_start_address;
	private TextView tv_end_address;
	private TextView tv_passager_num;
	private TextView tv_user_car_time;
	private TextView tv_end_time;
	private TextView tv_make_order_time;
	private TextView tv_oil_cost;
	private TextView tv_parking_cost;
	private TextView tv_other_cost;
	private TextView tv_discount;
	private TextView tv_total_cost;
	private TextView tv_pay_statue;
	private Button btn_pay;
	private ImageView iv_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_oder_record_detail);
		initViews();
		initData();
		setListener();
	}
	/**
	 * 初始化数据
	 */
	private void initData() {

		Bundle bun = getIntent().getExtras();
		String car_user = bun.getString("car_user");
		String user_phone = bun.getString("user_phone");
		int order_id = bun.getInt("order_id");
		String start_address = bun.getString("start_address");
		String end_address = bun.getString("end_address");
		int passager_num = bun.getInt("passager_num");
		String user_car_time = bun.getString("user_car_time");
		String end_time = bun.getString("end_time");
		String make_order_time = bun.getString("make_order_time");
		String oil_cost = bun.getString("oil_cost");
		String parking_cost = bun.getString("parking_cost");
		String other_cost = bun.getString("other_cost");
		String discount = bun.getString("discount");
		String total_cost = bun.getString("total_cost");
		String pay_statue = bun.getString("pay_statue");
		// int order_statue=bun.getInt("order_statue"); //订单状态

		tv_car_user.setText(car_user.toString());
		tv_user_phone.setText(user_phone.toString());
		tv_order_id.setText("" + order_id);
		tv_start_address.setText(start_address.toString());
		tv_end_address.setText(end_address.toString());
		tv_passager_num.setText("" + passager_num);
		tv_user_car_time.setText(user_car_time.toString());
		tv_end_time.setText(end_time.toString());
		tv_make_order_time.setText(make_order_time.toString());
		tv_oil_cost.setText(oil_cost.toString() );
		tv_parking_cost.setText(parking_cost.toString());
		tv_other_cost.setText(other_cost.toString());
		tv_discount.setText(discount.toString());
		tv_total_cost.setText(total_cost.toString());

		String STATUE;
		if (StringUtils.equals(pay_statue, "0")) {
			STATUE = "未结算";
			btn_pay.setText("支    付");
		} else if (StringUtils.equals(pay_statue, "1")) {
			STATUE = "已结算";
			btn_pay.setText("删    除");
		} else {
			STATUE = "未知";
			btn_pay.setText("未    知");
		}
		tv_pay_statue.setText(STATUE);
	}
	/**
	 * 初始化控件
	 */
	private void initViews() {
		tv_car_user = (TextView) findViewById(R.id.tv_car_user);
		tv_user_phone = (TextView) findViewById(R.id.tv_user_phone);
		tv_order_id = (TextView) findViewById(R.id.tv_order_id);
		tv_start_address = (TextView) findViewById(R.id.tv_start_address);
		tv_end_address = (TextView) findViewById(R.id.tv_end_address);
		tv_passager_num = (TextView) findViewById(R.id.tv_passager_num);
		tv_user_car_time = (TextView) findViewById(R.id.tv_user_car_time);
		tv_end_time = (TextView) findViewById(R.id.tv_end_time);
		tv_make_order_time = (TextView) findViewById(R.id.tv_make_order_time);
		tv_oil_cost = (TextView) findViewById(R.id.tv_oil_cost);
		tv_parking_cost = (TextView) findViewById(R.id.tv_parking_cost);
		tv_other_cost = (TextView) findViewById(R.id.tv_other_cost);
		tv_discount = (TextView) findViewById(R.id.tv_discount);
		tv_total_cost = (TextView) findViewById(R.id.tv_total_cost);
		tv_pay_statue = (TextView) findViewById(R.id.tv_pay_statue);
		btn_pay = (Button) findViewById(R.id.btn_pay);
		iv_back = (ImageView) findViewById(R.id.iv_back);
	}

	private void setListener() {
		iv_back.setOnClickListener(this);
		btn_pay.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_pay:
			if (StringUtils.equals(btn_pay.getText().toString(), "支    付")) {
				// TODO 前往支付
				ToastUtil.showShort(getApplicationContext(), "支付成功");
				btn_pay.setText("删    除");
				tv_pay_statue.setText("已结算");
			} else if (StringUtils.equals(btn_pay.getText().toString(), "删    除")) {
				// TODO 通知服务器，删除记录
				ToastUtil.showShort(getApplicationContext(), "抱歉！暂时无法删除");
			}
			break;

		}
	}

}
