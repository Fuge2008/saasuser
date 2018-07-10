package com.small.saasuser.activity;

import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.navisdk.BaiduNaviManager;
import com.baidu.navisdk.BaiduNaviManager.OnStartNavigationListener;
import com.baidu.navisdk.comapi.routeplan.RoutePlanParams.NE_RoutePlan_Mode;
import com.small.saasuser.activity.map.MapUtil;
import com.small.saasuser.activity.map.TravelTraceQueryActivity;
import com.small.saasuser.activity.map.TravelTraceUploadActivity;
import com.small.saasuser.activity.map.bnav.BNavigatorActivity;
import com.small.saasuser.application.MyApplication;
import com.small.saasuser.utils.ToastUtil;
import com.small.saasuser.utils.XUtilsImageLoader;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TravelNowDetailActivity extends BaseActivity
		implements OnGetRoutePlanResultListener, View.OnClickListener {
	protected static final String TAG = TravelNowDetailActivity.class.getSimpleName();
	// 行程结束，确认行程字段
	private TextView tv_order_number;
	private TextView tv_start_time;
	private TextView tv_end_time;
	private TextView tv_passager_number;
	private TextView tv_order_bill;
	private Button btn_submit;
	private Button btn_doubt;
	
	private RelativeLayout ll1;
	private RelativeLayout ll2;
	

	// 行程未结束，正在进行字段
	private TextView tv_start_location;
	private TextView tv_predict_time;
	private TextView tv_end_loacatin;
	// private TextView tv_spend_time;
	private TextView tv_my_location;
	private TextView tv_driver_name;
	private TextView tv_car_type;
	private TextView tv_car_plate;

	private RatingBar rb_star_level;
	private ImageView iv_driver_head;
	private ImageView iv_phone;
	private ImageView iv_back;
	private CheckBox cb_route_plan;
	private CheckBox cb_navigation;
	private CheckBox cb_route_again;
	private CheckBox cb_satellite;

	private int order_id;
	private String start_location;
	private double start_lat;
	private double start_lng;
	private String end_loacatin;
	private double end_lat;
	private double end_lng;
	private String driver_name;
	private String car_type;
	private String car_plate;
	private String car_color;
	private String star_level;
	private String driver_head;
	private String tel_number;
	private Bitmap driver_image;

	protected RoutePlanSearch routePlanSearch;// 路线搜索相关
	private BaiduMap baiduMap;
	private MapView mapView;
	private LinearLayout ll_baidumap;
	private LatLng start = new LatLng(22.529535, 113.947441);
	private LatLng end = new LatLng(22.591334, 114.122088);
	private String startAddress = null;// 起点
	private String endAddress = null;// 终点
	private boolean FLAG = true;// 判断是否第一次路线规划

	private RouteLine route = null;// 路线

	private MyLocationListenner myListener;
	private LocationClient mLocClient;// 定位相关
	private LocationMode mCurrentMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		LatLng defaultLatLng = new LatLng(22.555133, 114.066218);
		mapView = new MapView(this,
				new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(defaultLatLng).zoom(16).build())
						.scaleControlEnabled(true).zoomControlsEnabled(false));
		setContentView(R.layout.activity_order_now_detail);
		initViews();
		initData();
		setListeners();
		getPicturefromNet();
		//float maxZoomLevel = baiduMap.getMaxZoomLevel();
		//float minZoomLevel = baiduMap.getMinZoomLevel();

		MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(start);
		baiduMap.setMapStatus(mapStatusUpdate);

		mapStatusUpdate = MapStatusUpdateFactory.zoomTo(15);
		baiduMap.setMapStatus(mapStatusUpdate);
		routePlanSearch = RoutePlanSearch.newInstance();// 初始化路线查询对象
		routePlanSearch.setOnGetRoutePlanResultListener(this);// 设置路线查询结果监听
		routePlanSearchInit();
		
	}

	
	/**
	 * 加载头像,已进行了压缩处理和优化缓存机制，无需考虑oom
	 */
	private void getPicturefromNet() {
		XUtilsImageLoader image_head=new XUtilsImageLoader(TravelNowDetailActivity.this);
		image_head.display(iv_driver_head, "http://p3.so.qhmsg.com/t0130092dd6c3ea991d.jpg");
		
	}



	private void initViews() {
		ll_baidumap = (LinearLayout) findViewById(R.id.ll_baidumap);
		tv_start_location = (TextView) findViewById(R.id.tv_start_location);
		tv_predict_time = (TextView) findViewById(R.id.tv_predict_time);
		tv_end_loacatin = (TextView) findViewById(R.id.tv_end_loacatin);
		tv_my_location = (TextView) findViewById(R.id.tv_my_location);
		tv_driver_name = (TextView) findViewById(R.id.tv_driver_name);
		tv_car_type = (TextView) findViewById(R.id.tv_car_type);
		tv_car_plate = (TextView) findViewById(R.id.tv_car_plate);

		tv_order_number = (TextView) findViewById(R.id.tv_order_number);
		tv_start_time = (TextView) findViewById(R.id.tv_start_time);
		tv_end_time = (TextView) findViewById(R.id.tv_end_time);
		tv_passager_number = (TextView) findViewById(R.id.tv_passager_number);
		tv_order_bill = (TextView) findViewById(R.id.tv_order_bill);

		btn_submit = (Button) findViewById(R.id.btn_submit);
		btn_doubt = (Button) findViewById(R.id.btn_doubt);
		cb_route_plan = (CheckBox) findViewById(R.id.cb_route_plan);
		cb_navigation = (CheckBox) findViewById(R.id.cb_navigation);
		cb_route_again = (CheckBox) findViewById(R.id.cb_route_again);
		cb_satellite = (CheckBox) findViewById(R.id.cb_satellite);
		
		ll1=(RelativeLayout) findViewById(R.id.ll1);
		ll2=(RelativeLayout) findViewById(R.id.ll2);

		rb_star_level = (RatingBar) findViewById(R.id.rb_star_level);

		iv_driver_head = (ImageView) findViewById(R.id.iv_driver_head);
		iv_phone = (ImageView) findViewById(R.id.iv_phone);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		ll_baidumap.addView(mapView);
		baiduMap = mapView.getMap();
		myListener = new MyLocationListenner();
	}

	private void initData() {
		Bundle bun = getIntent().getExtras();

		order_id = bun.getInt("order_id");
		start_location = bun.getString("start_location");
		end_loacatin = bun.getString("end_loacatin");
		driver_name = bun.getString("driver_name");
		car_type = bun.getString("car_type");
		car_plate = bun.getString("car_plate");
		car_color = bun.getString("car_color");
		star_level = bun.getString("star_level");
		driver_head = bun.getString("driver_head");
		tel_number = bun.getString("tel_number");
		start_lat = Double.valueOf(bun.getString("start_lat").toString());
		start_lng = Double.valueOf(bun.getString("start_lng").toString());
		end_lat = Double.valueOf(bun.getString("end_lat").toString());
		end_lng = Double.valueOf(bun.getString("end_lng").toString());
		start = new LatLng(start_lat, start_lng);
		end = new LatLng(end_lat, end_lng);
		
		tv_start_location.setText(start_location.toString());
		tv_end_loacatin.setText(end_loacatin.toString());
		tv_driver_name.setText(driver_name.toString());
		tv_car_type.setText(car_type + "(" + car_color + ")");
		tv_car_plate.setText(car_plate.toString());

	}

	private void setListeners() {
		iv_phone.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		btn_submit.setOnClickListener(this);
		btn_doubt.setOnClickListener(this);
		cb_route_plan.setOnClickListener(this);
		cb_navigation.setOnClickListener(this);
		cb_route_again.setOnClickListener(this);
		cb_satellite.setOnClickListener(this);
		iv_driver_head.setOnClickListener(this);
		tv_end_loacatin.setOnClickListener(this);

	}


	public void routePlanSearchInit() {
		routePlanSearch.drivingSearch(getSearchParams());

	}

	/**
	 * 乘车路线规划
	 * 
	 * @return
	 */
	private DrivingRoutePlanOption getSearchParams() {
		DrivingRoutePlanOption params = new DrivingRoutePlanOption();
		// List<PlanNode> nodes = new ArrayList<PlanNode>();
		// nodes.add(PlanNode.withCityNameAndPlaceName("深圳市", "布吉联检站"));//添加点
		// params.passBy(nodes); // 设置途经点
		params.from(PlanNode.withLocation(start)); // 设置起点
		params.to(PlanNode.withLocation(end)); // 设置终点
		return params;
	}

	/** 获取驾车搜索结果的回调方法 */
	@Override
	public void onGetDrivingRouteResult(DrivingRouteResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			ToastUtil.showShort(getApplicationContext(), "抱歉，未找到结果");
		}
		if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {

			return;
		}

		route = result.getRouteLines().get(0);
		DrivingRouteOverlay overlay = new DrivingRouteOverlay(baiduMap);
		baiduMap.setOnMarkerClickListener(overlay);
		List<DrivingRouteLine> routeLines = result.getRouteLines(); // 获取到所有的搜索路线，最优化的路线会在集合的前面
		overlay.setData(routeLines.get(0)); // 把搜索结果设置到覆盖物
		overlay.addToMap(); // 把搜索结果添加到地图
		overlay.zoomToSpan(); // 把搜索结果在一个屏幕内显示完

		

		List<DrivingRouteLine> rLines = result.getRouteLines();
		List<DrivingStep> steps = rLines.get(0).getAllStep();
		int distance = rLines.get(0).getDistance(); // 路程
		int time = rLines.get(0).getDuration() / 60; // 耗时
		if (FLAG) {

			tv_predict_time.setText("全程：" + MapUtil.distanceFormatter(distance).toString() + ", 约需要："
					+ MapUtil.timeFormatter(time).toString());
		} else {
			tv_predict_time.setText("还剩：" + MapUtil.distanceFormatter(distance).toString() + ", 仍需要："
					+ MapUtil.timeFormatter(time).toString());
		}
	}

	/** 获取换乘（公交、地铁）搜索结果的回调方法 */
	@Override
	public void onGetTransitRouteResult(TransitRouteResult result) {
	}

	/** 获取步行搜索结果的回调方法 */
	@Override
	public void onGetWalkingRouteResult(WalkingRouteResult result) {
	}

	/**
	 * 启动GPS导航. 前置条件：导航引擎初始化成功
	 */
	private void launchNavigator() {
		// 这里给出一个起终点示例，实际应用中可以通过POI检索、外部POI来源等方式获取起终点坐标
		BaiduNaviManager.getInstance().launchNavigator(this, start.latitude, start.longitude, start_location,
				end.latitude, end.longitude, end_loacatin, NE_RoutePlan_Mode.ROUTE_PLAN_MOD_MIN_TIME, // 算路方式
				true, // 真实导航
				BaiduNaviManager.STRATEGY_FORCE_ONLINE_PRIORITY, // 在离线策略
				new OnStartNavigationListener() { // 跳转监听

					@Override
					public void onJumpToNavigator(Bundle configParams) {
						Intent intent = new Intent(TravelNowDetailActivity.this, BNavigatorActivity.class);
						intent.putExtras(configParams);
						startActivity(intent);
					}

					@Override
					public void onJumpToDownloader() {
					}
				});
	}

	/**
	 * 开始定位
	 */
	private void beginLocation() {
		mCurrentMode = LocationMode.Hight_Accuracy;
		// 开启定位图层
		baiduMap.setMyLocationEnabled(false);

		// 定位初始化
		mLocClient = new LocationClient(this);
		// 设置地图缩放级别为15
		baiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		// 设置是否返回定位结果 包括地址信息
		option.setIsNeedAddress(true);
		// 设置返回结果是否包含手机机头的方向
		option.setNeedDeviceDirect(true);
		option.setOpenGps(false);// 打开gpss
		option.setCoorType("bd09ll"); // 设置坐标类型 取值有3个： 返回国测局经纬度坐标系：gcj02
		// 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
		option.setScanSpan(1000);// 扫描间隔 单位毫秒
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		mLocClient.setLocOption(option);
		mLocClient.start();

	}

	/**
	 * 定位SDK监听函数
	 */

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// mapview 销毁后不在处理新接收的位置
			if (location == null || mapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			baiduMap.setMyLocationData(locData);
			double Latitude = location.getLatitude(); // 获取经度
			double Longitude = location.getLongitude(); // 获取纬度
			String address = location.getAddrStr();
			start = new LatLng(Latitude, Longitude);
			tv_my_location.setText(address.toString());
			// LogUtil.i("info", Latitude + Longitude + address);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.iv_phone:
			Intent intent = new Intent(Intent.ACTION_CALL);
			Uri data = Uri.parse("tel:" + tel_number);
			intent.setData(data);
			startActivity(intent);
			break;
		case R.id.btn_submit:// 确认行程
			// TODO 请求服务器，确认行程
			//ToastUtil.showShort(getApplicationContext(), "正在确认行程");
			Intent intent2 = new Intent(TravelNowDetailActivity.this, TravelTraceQueryActivity.class);
			startActivity(intent2);

			break;
		case R.id.btn_doubt:// 对行程有疑问
//			Intent intent2 = new Intent(TravelNowDetailActivity.this, TravelOrderDoubtActivity.class);
//			startActivity(intent2);
			Intent intent8 = new Intent(TravelNowDetailActivity.this, TravelTraceUploadActivity.class);
			startActivity(intent8);
			break;
		case R.id.cb_route_plan:// 轨迹追踪
			// beginLocation();
			// Intent intent3 = new Intent(TravelNowDetailActivity.this,
			// TravelTraceUploadActivity.class);
			// startActivity(intent3);
			if (baiduMap.isTrafficEnabled()) {
				// 开启交通图
				baiduMap.setTrafficEnabled(false);
			} else {
				baiduMap.setTrafficEnabled(true);
			}
			break;
		case R.id.cb_navigation:// 导航
			baiduMap.clear();
			beginLocation();
			launchNavigator();
			break;
		case R.id.cb_route_again:// 重新规划路线
			FLAG = false;
			baiduMap.clear();
			if (cb_route_again.isChecked()) {
				// start = new LatLng(22.555133, 114.066218);
				routePlanSearch = RoutePlanSearch.newInstance();
				routePlanSearch.setOnGetRoutePlanResultListener(this);
				routePlanSearchInit();
			} else {
				beginLocation();
			}

			break;
		case R.id.cb_satellite:// 轨迹回放
			// Intent intent4 = new Intent(TravelNowDetailActivity.this,
			// TravelTraceQueryActivity.class);
			// startActivity(intent4);
			if (baiduMap.getMapType() == BaiduMap.MAP_TYPE_NORMAL) {
				// 卫星地图
				baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			} else if (baiduMap.getMapType() == BaiduMap.MAP_TYPE_SATELLITE) {
				// 普通地图
				baiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			}

			break;
		case R.id.iv_driver_head:// 轨迹回放
			Intent intent6=new Intent(TravelNowDetailActivity.this,PersonalCenterActivity.class);
			startActivity(intent6);
			finish();
			break;
		case R.id.tv_end_loacatin:
			ll2.setVisibility(View.GONE);
			ll1.setVisibility(View.VISIBLE);
			break;

		}

	}

}
