package com.small.saasuser.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.small.saasuser.entity.OderOption;
import com.small.saasuser.entity.OderOption.Data;
import com.small.saasuser.entity.TravelPlanEntity;
import com.small.saasuser.global.HttpConstant;
import com.small.saasuser.utils.DateUtil;
import com.small.saasuser.utils.LogUtil;
import com.small.saasuser.utils.SharePreferenceUtils;
import com.small.saasuser.utils.StringUtils;
import com.small.saasuser.utils.ToastUtil;
import com.small.saasuser.view.dialog.ItemDialog;
import com.small.saasuser.view.dialog.ItemDialog.OnDialogItemClickListener;
import com.small.saasuser.view.dialog.SweetAlertDialog;
import com.small.saasuser.view.wheel.WheelMainActivity;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ConfirmOrderActivity extends BaseActivity implements View.OnClickListener, OnDialogItemClickListener {
	protected static final String TAG = ConfirmOrderActivity.class.getSimpleName();

	private TextView tv_passager_other;
	private TextView tv_passager_me;
	private TextView tv_start_location;
	private TextView tv_end_location;
	private TextView tv_start_time;
	private TextView tv_chose_time;
	private TextView tv_close_account;
	private TextView tv_user_car_model;
	private TextView tv_chose_car_model;
	private TextView tv_chose_close_account;
	// private TextView tv_chose_order_model;
	// private TextView tv_order_model;

	private Button btn_sumbit;
	// private RadioButton rb_small_car;
	private RadioButton rb_big_car;
	private ImageView iv_back;

	private EditText et_number_people;
	private EditText et_descript;
	private EditText et_number_seat;

	private ListView lvchoseCloseAccount;
	private EditText et_close_account;
	private ArrayList<String> mList;
	private PopupWindow mPopup;
	private ImageView iv_drop;

	public static final int REQUSET = 1;
	public static final int REQUSET2 = 2;

	public final static int CAR_MODEL_ZIJIA = 1;
	public final static int CAR_MODEL_PEIJIA = 2;
	public final static int CAR_MODEL_DAIJIA = 3;
	public final static int CAR_MODEL_BASHI = 4;
	public final static int CAR_MODEL_HANDLER = 0;
	private static final String[] CAR_MODEL_NAMES = { "自驾", "代驾", "配驾", "巴士" };
	// private int CAR_MODEL;
	// private static final int[] CAR_MODEL_CODES = { CAR_MODEL_ZIJIA,
	// CAR_MODEL_PEIJIA, CAR_MODEL_DAIJIA,
	// CAR_MODEL_BASHI };
	public static final int CAR_MODEL_DEFALUT = 1;
	private int s = -1;
	private String strUseCarModel = "自驾";
	private String strPassagerName;
	private String strPassagerPhone;
	// private String strStartTime;
	// private String strStartLocation;
	// private String strEndLocation;
	private String strDistance = "60km";
	private String strTimeConsuming = "50分钟";
	private String money = "60¥";

	private String startLocation;
	private String endLocation;
	private String startLocationDetail;
	private String endLocationDetail;
	private double startLat;
	private double startLng;
	private double endLat;
	private double endLng;

	// private String carType;
	// private String userId;// 用户名

	private String LoginID;// 登录名
	private int UseVehicleMode;// 用车方式
	private String Origin;// 初始地
	private String Destination;// 目的地
	private String UseVehicleSeatingCapacity;// 座位数
	private String UseVehicleTime;// 开始时间
	private String UseVehicleType;// 用车类型
	private String OccTime;// 下单时间
	private String SettlementType;// 结算类型
	private String CustomCycleLength;// 自定义周期时长
	private String OtherCmd;// 其他描述
	private String OrderMode;// 订单模式
	private String TotalPersonNum;// 坐车人数

	private String userCarPerson;// 用车人姓名
	private String userCarPersonPhone;// 用车人联系方式

	/**
	 * 接收返回数据
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// requestCode标示请求的标示 resultCode表示有数据
		switch (requestCode) {
		case REQUSET:
			if (requestCode == ConfirmOrderActivity.REQUSET && resultCode == RESULT_OK) {
				UseVehicleTime = data.getStringExtra(WheelMainActivity.DATA_STR);
				Log.i("info", "返回时间" + UseVehicleTime);
				tv_start_time.setText(UseVehicleTime.toString());
			}
			break;
		case REQUSET2:
			if (requestCode == ConfirmOrderActivity.REQUSET2 && resultCode == RESULT_OK) {
				strPassagerName = data.getStringExtra(SelectPassengerActivity.NAME_STR);
				strPassagerPhone = data.getStringExtra(SelectPassengerActivity.PHONE_STR);
				tv_passager_me.setText("姓名：" + strPassagerName.toString() + "  电话：" + strPassagerPhone.toString());
			}
			break;

		}

	}

	/**
	 * 更新UI界面
	 */
	private Handler handle = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CAR_MODEL_HANDLER:
				tv_user_car_model.setText(strUseCarModel);
				break;

			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_order);
		iniViews();

		SharePreferenceUtils.putString(this, "LoginID", "1122");
		SharePreferenceUtils.putString(this, "RealName", "老司机");
		SharePreferenceUtils.putString(this, "TelePhone", "18888888888"); // TODO
																			// 三个参数，暂时设定，后需删除

		Intent intent = getIntent();
		startLocation = intent.getStringExtra("start_address");
		endLocation = intent.getStringExtra("end_address");
		// TODO 待添加数据
		 startLocationDetail = intent.getStringExtra("start_address_detail");
		 endLocationDetail = intent.getStringExtra("end_address__detail");
		startLat = intent.getDoubleExtra("startLat", -0.001);
		startLng = intent.getDoubleExtra("startLng", -0.001);
		endLat = intent.getDoubleExtra("endLat", -0.001);
		endLng = intent.getDoubleExtra("endLng", -0.001);
		startLocationDetail = intent.getStringExtra("start_address_detail");
		endLocationDetail = intent.getStringExtra("end_address_detail");
		tv_start_location.setText(startLocation);
		tv_end_location.setText(endLocation);
		LogUtil.i(TAG, "接收地图传过来信息：\n"+"起点名称："+startLocation+"\n起点地址："+startLocationDetail+"\n终点名称："+endLocation+"\n起点地址："+endLocationDetail+"\n起点经纬度："
		+"("+startLng+","+startLng+")"+"\n终点经纬度："+"("+endLng+","+endLng+")");
		//RequestToNetForOption();//获取参数可选项

		setListeners();

	}

	/**
	 * 请求网络，获取订单可选项
	 */
	private void RequestToNetForOption() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("LoginID", "1122");// 登录名
		sendToNet(HttpConstant.LOAD_ORDER_OPTIONS, params);
	}

	/**
	 * 初始化控件
	 */
	private void iniViews() {
		tv_close_account = (TextView) findViewById(R.id.tv_close_account);
		tv_passager_me = (TextView) findViewById(R.id.tv_passager_me);
		tv_passager_other = (TextView) findViewById(R.id.tv_passager_other);
		tv_start_location = (TextView) findViewById(R.id.tv_start_location);
		tv_end_location = (TextView) findViewById(R.id.tv_end_location);
		tv_start_time = (TextView) findViewById(R.id.tv_start_time);
		tv_chose_time = (TextView) findViewById(R.id.tv_chose_time);
		tv_user_car_model = (TextView) findViewById(R.id.tv_user_car_model);
		tv_chose_car_model = (TextView) findViewById(R.id.tv_chose_car_model);
		tv_chose_close_account = (TextView) findViewById(R.id.tv_chose_close_account);
		// tv_chose_order_model= (TextView)
		// findViewById(R.id.tv_chose_order_model);
		// tv_order_model= (TextView) findViewById(R.id.tv_order_model);

		et_descript = (EditText) findViewById(R.id.et_descript);
		et_number_people = (EditText) findViewById(R.id.et_number_people);
		et_number_seat = (EditText) findViewById(R.id.et_number_seat);

		btn_sumbit = (Button) findViewById(R.id.btn_sumbit);
		rb_big_car = (RadioButton) findViewById(R.id.rb_big_car);
		// rb_small_car = (RadioButton) findViewById(R.id.rb_small_car);
		iv_back = (ImageView) findViewById(R.id.iv_back);

		iv_drop = (ImageView) findViewById(R.id.iv_drop);
		et_close_account = (EditText) findViewById(R.id.et_close_account);
		lvchoseCloseAccount = new ListView(this);
		initData();

	}

	@SuppressLint("ResourceAsColor")
	private void initData() {
		mList = new ArrayList<String>();

		for (int i = 1; i <= 30; i++) {
			if (i == 1) {
				mList.add("每日结");
			} else if (i == 7) {
				mList.add("每周结");
			} else if (i == 30) {
				mList.add("每月结");
			} else {
				mList.add("" + i);
			}

		}
		lvchoseCloseAccount.setAdapter(new MyAdapter());
		lvchoseCloseAccount.setBackgroundColor(R.color.crimson);
		lvchoseCloseAccount.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (0 == position || 6 == position || 29 == position) {
					et_close_account.setText(mList.get(position));
				} else {

					et_close_account.setText(mList.get(position) + " 天");
				}
				mPopup.dismiss();
			}
		});

	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public String getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.item_close_account, null);
				holder = new ViewHolder();
				holder.tvContent = (TextView) convertView.findViewById(R.id.tv_chose_day);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tvContent.setText(getItem(position));

			return convertView;
		}

	}

	static class ViewHolder {
		public TextView tvContent;
	}

	/**
	 * 设置监听
	 */
	private void setListeners() {
		btn_sumbit.setOnClickListener(this);
		tv_passager_other.setOnClickListener(this);
		tv_start_time.setOnClickListener(this);
		tv_chose_time.setOnClickListener(this);
		tv_chose_car_model.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		tv_chose_close_account.setOnClickListener(this);
		// tv_chose_order_model.setOnClickListener(this);
		iv_drop.setOnClickListener(this);
	}

	/**
	 * 封装订单数据包
	 */
	private void sendOrderToNet() {
		// TODO 发送订单----》下单
		// 封装订单数据
		RequestParams params = new RequestParams();
		params.addBodyParameter("LoginID", "1122");// 登录名
		params.addBodyParameter("UseVehicleMode", Integer.toString(UseVehicleMode));// 用车方式:
																					// ENUM1-自驾，2-代驾，3-
																					// 配驾，4-巴士
		params.addBodyParameter("Origin", Origin);// 初始地
		params.addBodyParameter("Destination", Destination);// 目的地

		 params.addBodyParameter("OriginAddress", startLocationDetail);// 初始详细地址
		 params.addBodyParameter("DestinationAddress", endLocationDetail);// 目的详细地址
		//TODO数据已经传过来，未添加
		params.addBodyParameter("OriginLatitude", String.valueOf(startLat));// 初始地纬度
		params.addBodyParameter("OriginLongitude", String.valueOf(startLng));// 初始地经度
		params.addBodyParameter("DestinationLatitude", String.valueOf(endLat));// 目的地纬度
		params.addBodyParameter("DestinationLongitude", String.valueOf(endLng));// 目的地经度

		params.addBodyParameter("PredictEndTime", "2016-11-30 12:00");// 预计结束时间
		params.addBodyParameter("StateUpdateTime", "2016-11-30 12:00");// 订单状态更新时间

		params.addBodyParameter("UseVehicleSeatingCapacity", UseVehicleSeatingCapacity);// 座位数
		params.addBodyParameter("UseVehicleTime", UseVehicleTime);// 开始时间
		params.addBodyParameter("UseVehicleType", UseVehicleType);// 用车类型
		params.addBodyParameter("OccTime", OccTime);// 下单时间
		params.addBodyParameter("SettlementType", "1");// 结算类型
		params.addBodyParameter("CustomCycleLength", "0");// 自定义周期时长
		params.addBodyParameter("OtherCmd", OtherCmd);// 其他描述
		params.addBodyParameter("OrderMode", "1");// 订单模式
		params.addBodyParameter("TotalPersonNum", TotalPersonNum);// 坐车人数
		sendToNet(HttpConstant.USER_ROUTE_REPLY, params);

	}

	/**
	 * 请求网络
	 * 
	 * @param params
	 */
	private void sendToNet(final String url, final RequestParams params) {

		HttpUtils http = new HttpUtils();
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onStart() {
				Toast.makeText(ConfirmOrderActivity.this, "连接中...", Toast.LENGTH_SHORT).show();
				LogUtil.i(TAG, "onStart()" + params.toString());
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if (isUploading) {
					Toast.makeText(ConfirmOrderActivity.this, "提交中..." + current + "/" + total, Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(ConfirmOrderActivity.this, "响应中..." + current + "/" + total, Toast.LENGTH_SHORT)
							.show();
				}
				LogUtil.i(TAG, "onLoading" + params.toString());
			}

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				Toast.makeText(ConfirmOrderActivity.this, "成功" + responseInfo.result, Toast.LENGTH_SHORT).show();
				LogUtil.i(TAG, "成功" + responseInfo.result.toString());
				if (StringUtils.equals(HttpConstant.LOAD_ORDER_OPTIONS, url)) {
					String result = responseInfo.result;
					LogUtil.i(TAG, "请求网络数据" + result);
					Gson gson = new Gson();
					OderOption reData = gson.fromJson(result, OderOption.class);
					// String[] strings = gson.fromJson(reData.,
					// String[].class);
//					LogUtil.i(TAG, "解析数据" + reData);
//					//String UseVehicleLevelList=reData.Data.UseVehicleLevelList;
//					//TODO 对可选数据的处理
//						String [] OrderModeList=new String[10];
//						OrderModeList=StringUtils.doSplit(reData.Data.OrderModeList, ",");
//						 List list = Arrays.asList(OrderModeList);
//						for(int i=0;i<OrderModeList.length;i++){
//							LogUtil.i(TAG, "检测解析后的数据：\n"+list.get(i));
//							LogUtil.i(TAG, "检测解析后的数据2：\n"+list.toArray());
//							//LogUtil.i(TAG, "检测解析后的数据：\n"+OrderModeList[i].toString());
//						}
					ToastUtil.showShort(getApplicationContext(), "获取订单可选项成功！"+reData);
				} else if (StringUtils.equals(HttpConstant.USER_ROUTE_REPLY, url)) {
					ToastUtil.showShort(getApplicationContext(), "提交订单成功:"+responseInfo.result.toString());
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Toast.makeText(ConfirmOrderActivity.this, "失败" + error.getExceptionCode() + ":" + msg,
						Toast.LENGTH_SHORT).show();
				LogUtil.i(TAG, "失败" + error.getExceptionCode() + ":" + msg.toString());
			}
		});

	}

	// 拿到订单数据
	private void getFormData() {
		// 1.登录名，作为身份标识
		LoginID = SharePreferenceUtils.getString(this, "LoginID", "1122");
		// 2.起点名称
		Origin = tv_start_location.getText().toString();
		// 3.终点名称
		Destination = tv_end_location.getText().toString();
		// 4.起、终点经纬度 //(e_lat,e_lng),(s_lat,s_lng)
		// 5.起、终点详细地址 //
		// 6.用车人,联系方式 //userCarPerson , userCarPersonPhone

		if (StringUtils.equals(tv_passager_me.getText().toString().substring(4), "本人乘车")) {
			userCarPerson = SharePreferenceUtils.getString(ConfirmOrderActivity.this, "RealName", "张三");
			userCarPersonPhone = SharePreferenceUtils.getString(ConfirmOrderActivity.this, "Telephone", "18888888888");
		} else {
			userCarPerson = strPassagerName;
			userCarPersonPhone = strPassagerPhone;
		}

		// 7.用车方式；1 - 自驾，2 - 代驾，3 - 配驾，4 - 巴士 //UseVehicleMode

		// 8.乘车人数//TotalPersonNum
		// 9.车类型；0 - 大车，1 - 小车 //UseVehicleType
		UseVehicleType = rb_big_car.isChecked() ? "0" : "1";
		// 10.出发时间

		// 11.结算类型：1）即时结算；2）自定周期结算、周期； 0（自定义周期结算）/1 （现结） 单位：天（结算类型为现结 ，默认为零）
		// 12.其他描述//OtherCmd
		OtherCmd = et_descript.getText().toString();
		// 13.发送订单模式；0（即时）/1（预约）/2 (全部) //暂时不考虑
		// 14.下单时间 //OccTime
		OccTime = DateUtil.getSystemTime().toString();
		// 14.座位数 //UseVehicleSeatingCapacity

		if (StringUtils.isNotEmpty(et_number_people, true) && StringUtils.isNotEmpty(et_number_seat, true)) {
			TotalPersonNum = StringUtils.isNumber(et_number_people.getText().toString())
					? et_number_people.getText().toString() : "geshi";

			UseVehicleSeatingCapacity = StringUtils.isNumber(et_number_seat.getText().toString())
					? et_number_seat.getText().toString() : "geshi";
		} else {
			ToastUtil.showShort(ConfirmOrderActivity.this, "请补充完整信息！");
		}

		// private int SettlementType;// 结算类型
		// private String CustomCycleLength;// 自定义周期时长
		// private int OrderMode;// 订单模式
		
		
		
		
//		OderOption option=new OderOption();
//		String OrderModeList=option.Data.getOrderModeList();
//		String UseVehicleModeList=option.get(0).UseVehicleModeList;
//		String UseVehicleTypeList=option.get(0).UseVehicleTypeList;
//		String UseVehicleLevelList=option.get(0).UseVehicleLevelList;
//		String UseVehicleSeatingCapacityList=option.get(0).UseVehicleSeatingCapacityList;
//		String SettlementTypeList=option.get(0).SettlementTypeList;
//		String CustomCycleLengthMax=option.get(0).CustomCycleLengthMax;
		
//		LogUtil.i(TAG, "可选信息*****************************************************"+OrderModeList);
		
		
	
	}

	private void showFailDialog() {
		new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE).setTitleText("提交失败").setContentText("网络错误!").show();

	}

	// 展示进度条对话框
	private void showSuccessProgress() {

		final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
				.setTitleText("提交中...");
		pDialog.show();
		pDialog.setCancelable(false);
		new CountDownTimer(800 * 7, 800) {
			public void onTick(long millisUntilFinished) {

				s++;
				switch (s) {
				case 0:
					pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.blue_btn_bg_color));
					break;
				case 1:
					pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_50));
					break;
				case 2:
					pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
					break;
				case 3:
					pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_deep_teal_20));
					break;
				case 4:
					pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.material_blue_grey_80));
					break;
				case 5:
					pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.warning_stroke_color));
					break;
				case 6:
					pDialog.getProgressHelper().setBarColor(getResources().getColor(R.color.success_stroke_color));
					break;
				}
			}

			public void onFinish() {
				s = -1;
				pDialog.setTitleText("提交订单成功！").setConfirmText("确定").changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
			}
		}.start();

	}

	// 自定义结算，提示输入选择框
	protected void showDropDown() {
		if (mPopup == null) {
			mPopup = new PopupWindow(lvchoseCloseAccount, et_close_account.getWidth(), 300, true);
			mPopup.setBackgroundDrawable(new ColorDrawable());
		}

		mPopup.showAsDropDown(et_close_account);
	}

	// 选择用车模式
	private void choseCarModel() {

		new ItemDialog(ConfirmOrderActivity.this, CAR_MODEL_NAMES, "用车模式", CAR_MODEL_DEFALUT, this).show();
	}

	@Override
	public void onDialogItemClick(int requestCode, int position, String item) {

		strUseCarModel = CAR_MODEL_NAMES[position];
		if (CAR_MODEL_NAMES[0].equals(strUseCarModel)) {
			UseVehicleMode = CAR_MODEL_ZIJIA;
		} else if (CAR_MODEL_NAMES[1].equals(strUseCarModel)) {
			UseVehicleMode = CAR_MODEL_PEIJIA;
		} else if (CAR_MODEL_NAMES[2].equals(strUseCarModel)) {
			UseVehicleMode = CAR_MODEL_DAIJIA;
		} else if (CAR_MODEL_NAMES[3].equals(strUseCarModel)) {
			UseVehicleMode = CAR_MODEL_BASHI;
		}
		Message msg = new Message();
		msg.what = CAR_MODEL_HANDLER;
		msg.obj = strUseCarModel;
		handle.sendMessage(msg);
		// 关闭对话框
		// dialog.dismiss();

	}

	/**
	 * 点击事件
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sumbit:
			getFormData();
			if (StringUtils.isEmpty(Origin) || StringUtils.isEmpty(Destination) || StringUtils.isEmpty(TotalPersonNum)
					|| StringUtils.isEmpty(UseVehicleSeatingCapacity)) {
				ToastUtil.showShort(ConfirmOrderActivity.this, "请补充完整信息");
				showFailDialog();
			} else {

				if ("geshi".equals(TotalPersonNum) || "geshi".equals(UseVehicleSeatingCapacity)) {
					ToastUtil.showShort(ConfirmOrderActivity.this, "输入的信息格式错误！");
					return;
				} else {

					sendOrderToNet();
					showSuccessProgress();
				}
				// Intent intent = new Intent(ConfirmOrderActivity.this,
				// MainActivity.class);
				// Bundle b = new Bundle();
				// OrderInfo info = new OrderInfo();
				// info.setStartLocation(Origin);
				// info.setEndLocation(Destination);
				// info.setDistance(strDistance);
				// info.setTimeConsuming(strTimeConsuming);
				// info.setStartTime(UseVehicleTime);
				// info.setPassagerName(strName);
				// info.setPassagerPhone(strPhone);
				// info.setMoney(money);
				// b.putParcelable("orderData", info);
				// Log.i("info", "订单信息----->>" + info.toString());
				// intent.putExtras(b);
				// intent.putExtra("RESULT", 11);
				// startActivity(intent);
				// finish();

			}
			// showSuccessDialog();
			break;
		case R.id.tv_passager_other:
			Intent intent2 = new Intent(ConfirmOrderActivity.this, SelectPassengerActivity.class);
			startActivityForResult(intent2, REQUSET2);
			break;
		case R.id.tv_chose_time:
			Intent intent3 = new Intent(ConfirmOrderActivity.this, WheelMainActivity.class);
			startActivityForResult(intent3, REQUSET);
			break;
		case R.id.tv_chose_car_model:
			choseCarModel();
			break;
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_chose_close_account:
			if (StringUtils.equals("即时结算", tv_close_account.getText().toString())) {
				new SweetAlertDialog(this).setContentText("您选择了自行定义周期结算，周期可为1~30天").show();
				tv_close_account.setText("自定义结算");
				iv_drop.setVisibility(View.VISIBLE);
				et_close_account.setVisibility(View.VISIBLE);
				et_close_account.setText("每日结");
			} else {
				new SweetAlertDialog(this).setContentText("您选择了即时结算").show();
				tv_close_account.setText("即时结算");
				iv_drop.setVisibility(View.INVISIBLE);
				et_close_account.setVisibility(View.INVISIBLE);
			}

			break;
		case R.id.iv_drop:
			showDropDown();
			break;

		}

	}

	/**
	 * 选择用车方式
	 */
	// private void choseCarModel() {
	// DialogUtil dialog = new DialogUtil();
	// final String[] strModel = new String[] { "自驾", "代驾", "配驾", "巴士" };
	// dialog.creadDialog(ConfirmOrderActivity.this, R.drawable.ic_launcher,
	// "用车模式", strModel,
	// new android.content.DialogInterface.OnClickListener() {
	//
	// @Override
	// public void onClick(DialogInterface dialog, int index) {
	// strUseCarModel = strModel[index];
	// if (strModel[0].equals(strUseCarModel)) {
	// UseVehicleMode = CAR_MODEL_ZIJIA;
	//
	// } else if (strModel[1].equals(strUseCarModel)) {
	// UseVehicleMode = CAR_MODEL_PEIJIA;
	// } else if (strModel[2].equals(strUseCarModel)) {
	// UseVehicleMode = CAR_MODEL_DAIJIA;
	// } else if (strModel[3].equals(strUseCarModel)) {
	// UseVehicleMode = CAR_MODEL_BASHI;
	// }
	// Message msg = new Message();
	// msg.what = CAR_MODEL_HANDLER;
	// msg.obj = strUseCarModel;
	// handle.sendMessage(msg);
	// // 关闭对话框
	// dialog.dismiss();
	// }
	// });
	//
	// }

}
