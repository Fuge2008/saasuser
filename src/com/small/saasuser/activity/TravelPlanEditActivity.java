package com.small.saasuser.activity;

import com.small.saasuser.utils.LogUtil;
import com.small.saasuser.utils.StringUtils;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 未执行订单信息修改
 * @author admin
 *
 */
public class TravelPlanEditActivity extends BaseActivity implements View.OnClickListener {
	private ImageView iv_back;

	private EditText et_car_model;
	private EditText et_passager_id;
	private EditText et_passager_name;
	private EditText et_passager_phone;
	private EditText et_start_address;
	private EditText et_end_address;
	private EditText et_passager_num;
	private EditText et_user_car_time;
	private EditText et_end_time;
	private EditText et_make_order_time;
	private EditText et_pay_type;
	private EditText et_other_describe;

	private String car_appointment;
	private String passager_id;
	private String passager_name;
	private String passager_phone;
	private String start_address;
	private String end_address;
	private int passager_num;
	private String user_car_time;
	private String end_time;
	private String make_order_time;
	private int pay_type;
	private String other_describe;
	private Button btn_sumbit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel_plan_edit);
		initView();
		initData();
		setListener();
	}

	private void initView() {
		et_car_model = (EditText) findViewById(R.id.et_car_model);
		et_passager_id = (EditText) findViewById(R.id.et_passager_id);
		et_passager_name = (EditText) findViewById(R.id.et_passager_name);
		et_passager_phone = (EditText) findViewById(R.id.et_passager_phone);
		et_start_address = (EditText) findViewById(R.id.et_start_address);
		et_end_address = (EditText) findViewById(R.id.et_end_address);
		et_passager_num = (EditText) findViewById(R.id.et_passager_num);
		et_user_car_time = (EditText) findViewById(R.id.et_user_car_time);
		et_end_time = (EditText) findViewById(R.id.et_end_time);
		et_make_order_time = (EditText) findViewById(R.id.et_make_order_time);
		et_pay_type = (EditText) findViewById(R.id.et_pay_type);
		et_other_describe = (EditText) findViewById(R.id.et_other_describe);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		btn_sumbit = (Button) findViewById(R.id.btn_sumbit);

	}

	private void initData() {
		Bundle bun = getIntent().getExtras();
		LogUtil.i("info", bun.toString());
		String car_appointment = bun.getString("car_appointment");
		LogUtil.i("info33", bun.getString("car_appointment"));
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

		et_car_model.setText(car_appointment.toString());
		et_passager_id.setText(String.valueOf(passager_id));
		et_passager_name.setText(passager_name.toString());
		et_passager_phone.setText(passager_phone.toString());
		et_start_address.setText(start_address.toString());
		et_end_address.setText(end_address.toString());
		et_passager_num.setText(String.valueOf(passager_num));
		et_user_car_time.setText(user_car_time.toString());
		et_end_time.setText(end_time.toString());
		et_make_order_time.setText(make_order_time.toString());
		et_other_describe.setText(other_describe.toString());
		String PAY_TYPE;
		if (1 == pay_type) {
			PAY_TYPE = "现    结";
		} else if (0 == pay_type) {
			PAY_TYPE = "自定义周期结算";
		} else {
			PAY_TYPE = "未     知";
		}
		et_pay_type.setText(PAY_TYPE);
	}

	private void editData() {

		String car_model = et_car_model.getText().toString();
		String passager_id = et_passager_id.getText().toString();
		String passager_name = et_passager_name.getText().toString();
		String passager_phone = et_passager_phone.getText().toString();
		String start_address = et_start_address.getText().toString();
		String end_address = et_end_address.getText().toString();
		String passager_num = et_passager_num.getText().toString();
		String user_car_time = et_user_car_time.getText().toString();
		String end_time = et_end_time.getText().toString();
		String make_order_time = et_make_order_time.getText().toString();
		String pay_type = et_pay_type.getText().toString();
		String other_describe = et_other_describe.getText().toString();

		if (StringUtils.isEmpty(car_model) || StringUtils.isEmpty(passager_id) || StringUtils.isEmpty(passager_name)
				|| StringUtils.isEmpty(passager_phone) || StringUtils.isEmpty(start_address)
				|| StringUtils.isEmpty(end_address) || StringUtils.isEmpty(passager_num)
				|| StringUtils.isEmpty(user_car_time) || StringUtils.isEmpty(end_time)
				|| StringUtils.isEmpty(make_order_time) || StringUtils.isEmpty(pay_type)
				|| StringUtils.isEmpty(other_describe)) {
			Toast.makeText(TravelPlanEditActivity.this, "对不起，订单信息不能为空！", Toast.LENGTH_SHORT).show();

		} else {
			Toast.makeText(TravelPlanEditActivity.this, "恭喜，订单修改成功！", Toast.LENGTH_SHORT).show();
			// TODO 在此添加发送网络，请求修改订单的请求
			finish();
		}

	}

	private void setListener() {
		iv_back.setOnClickListener(this);
		btn_sumbit.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_sumbit:
			editData();
			break;

		}

	}

}
