package com.small.saasuser.activity.map;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.trace.OnTrackListener;
import com.small.saasuser.activity.BaseActivity;
import com.small.saasuser.activity.R;
import com.small.saasuser.application.MyApplication;
import com.small.saasuser.utils.AbAppManager;
import com.small.saasuser.utils.DateUtil;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class TravelTraceQueryActivity extends BaseActivity implements View.OnClickListener {
	private Button btn_date;
	private Button btn_excuision;
	private Button btn_distance;
	private ImageView iv_back;
	private MyApplication trackApp = null;
	private int startTime = 0;
	private int endTime = 0;

	// private int year = 0;
	// private int month = 0;
	// private int day = 0;
	// 起点图标
	private static BitmapDescriptor bmStart;
	// 终点图标
	private static BitmapDescriptor bmEnd;

	// 起点图标覆盖物
	private static MarkerOptions startMarker = null;
	// 终点图标覆盖物
	private static MarkerOptions endMarker = null;
	// 路线覆盖物
	public static PolylineOptions polyline = null;

	private static MarkerOptions markerOptions = null;

	/**
	 * Track监听器
	 */
	protected static OnTrackListener trackListener = null;

	private MapStatusUpdate msUpdate = null;

	// private TextView tvDatetime = null;

	private static int isProcessed = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_travel_trace_query);
		trackApp = (MyApplication) getApplicationContext();
		initViews();
	}

	private void initViews() {
		btn_date = (Button) findViewById(R.id.btn_date);
		btn_excuision = (Button) findViewById(R.id.btn_excuision);
		btn_distance = (Button) findViewById(R.id.btn_distance);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		trackApp.initBmap((MapView) findViewById(R.id.bmapView));

		btn_date.setOnClickListener(this);
		btn_excuision.setOnClickListener(this);
		btn_distance.setOnClickListener(this);
		iv_back.setOnClickListener(this);

		// tvDatetime = (TextView) view.findViewById(R.id.tv_datetime);
		// tvDatetime.setText(" 当前日期 : " + DateUtil.getCurrentDate() + " ");
		initData();

		// 初始化OnTrackListener
		initOnTrackListener();

	}

	private void initData() {
		String st = "2016年11月16日0时0分0秒";
		String et = "2016年11月16日23时59分59秒";
		startTime = Integer.parseInt(DateUtil.getTimeToStamp(st));
		endTime = Integer.parseInt(DateUtil.getTimeToStamp(et));

	}

	/**
	 * 初始化OnTrackListener
	 */
	private void initOnTrackListener() {

		trackListener = new OnTrackListener() {

			// 请求失败回调接口
			@Override
			public void onRequestFailedCallback(String arg0) {
				// TODO Auto-generated method stub
				trackApp.getmyHandler().obtainMessage(0, "track请求失败回调接口消息 : " + arg0).sendToTarget();
			}

			// 查询历史轨迹回调接口
			@Override
			public void onQueryHistoryTrackCallback(String arg0) {
				// TODO Auto-generated method stub
				super.onQueryHistoryTrackCallback(arg0);
				showHistoryTrack(arg0);
			}

			@Override
			public void onQueryDistanceCallback(String arg0) {
				// TODO Auto-generated method stub
				try {
					JSONObject dataJson = new JSONObject(arg0);
					if (null != dataJson && dataJson.has("status") && dataJson.getInt("status") == 0) {
						double distance = dataJson.getDouble("distance");
						DecimalFormat df = new DecimalFormat("#.0");
						trackApp.getmyHandler().obtainMessage(0, "里程 : " + df.format(distance) + "米").sendToTarget();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					trackApp.getmyHandler().obtainMessage(0, "queryDistance回调消息 : " + arg0).sendToTarget();
				}
			}

			@Override
			public Map<String, String> onTrackAttrCallback() {
				// TODO Auto-generated method stub
				System.out.println("onTrackAttrCallback");
				return null;
			}

		};
	}

	/**
	 * 查询历史轨迹
	 */
	private void queryHistoryTrack(int processed, String processOption) {

		// entity标识
		String entityName = trackApp.getEntityName();
		// 是否返回精简的结果（0 : 否，1 : 是）
		int simpleReturn = 0;
		// 是否返回纠偏后轨迹（0 : 否，1 : 是）
		int isProcessed = processed;
		// 开始时间
		if (startTime == 0) {
			startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
		}
		if (endTime == 0) {
			endTime = (int) (System.currentTimeMillis() / 1000);
		}
		// 分页大小
		int pageSize = 1000;
		// 分页索引
		int pageIndex = 1;

		trackApp.getClient().queryHistoryTrack(trackApp.getServiceId(), entityName, simpleReturn, isProcessed,
				processOption, startTime, endTime, pageSize, pageIndex, trackListener);
	}

	// 查询里程
	private void queryDistance(int processed, String processOption) {

		// entity标识
		String entityName = trackApp.getEntityName();

		// 是否返回纠偏后轨迹（0 : 否，1 : 是）
		int isProcessed = processed;

		// 里程补充
		String supplementMode = "driving";

		// 开始时间
		if (startTime == 0) {
			startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
		}
		// 结束时间
		if (endTime == 0) {
			endTime = (int) (System.currentTimeMillis() / 1000);
		}

		trackApp.getClient().queryDistance(trackApp.getServiceId(), entityName, isProcessed, processOption,
				supplementMode, startTime, endTime, trackListener);
	}

	/**
	 * 轨迹查询(先选择日期，再根据是否纠偏，发送请求)
	 */
	public void execute() {
		// tvDatetime.setText(" 当前日期 : " + year + "-" + month + "-" +
		// day + " ");
		// 选择完日期，根据是否纠偏发送轨迹查询请求
		if (0 == isProcessed) {
			Toast.makeText(TravelTraceQueryActivity.this, "正在查询历史轨迹，请稍候", Toast.LENGTH_SHORT).show();
			queryHistoryTrack(0, null);
		} else {
			Toast.makeText(TravelTraceQueryActivity.this, "正在查询纠偏后的历史轨迹，请稍候", Toast.LENGTH_SHORT).show();
			queryHistoryTrack(1, "need_denoise=1,need_vacuate=1,need_mapmatch=1");
		}
	}

	/**
	 * 显示历史轨迹
	 * 
	 * @param historyTrack
	 */
	private void showHistoryTrack(String historyTrack) {

		HistoryTrackData historyTrackData = GsonService.parseJson(historyTrack, HistoryTrackData.class);

		List<LatLng> latLngList = new ArrayList<LatLng>();
		if (historyTrackData != null && historyTrackData.getStatus() == 0) {
			if (historyTrackData.getListPoints() != null) {
				latLngList.addAll(historyTrackData.getListPoints());
			}

			// 绘制历史轨迹
			drawHistoryTrack(latLngList, historyTrackData.distance);

		}

	}

	/**
	 * 绘制历史轨迹
	 * 
	 * @param points
	 */
	private void drawHistoryTrack(final List<LatLng> points, final double distance) {
		// 绘制新覆盖物前，清空之前的覆盖物
		trackApp.getmBaiduMap().clear();

		if (points.size() == 1) {
			points.add(points.get(0));
		}

		if (points == null || points.size() == 0) {
			trackApp.getmyHandler().obtainMessage(0, "当前查询无轨迹点").sendToTarget();
			resetMarker();
		} else if (points.size() > 1) {

			LatLng llC = points.get(0);
			LatLng llD = points.get(points.size() - 1);
			LatLngBounds bounds = new LatLngBounds.Builder().include(llC).include(llD).build();

			msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);

			bmStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_start);
			bmEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_end);

			// 添加起点图标
			startMarker = new MarkerOptions().position(points.get(points.size() - 1)).icon(bmStart).zIndex(9)
					.draggable(true);

			// 添加终点图标
			endMarker = new MarkerOptions().position(points.get(0)).icon(bmEnd).zIndex(9).draggable(true);

			// 添加路线（轨迹）
			polyline = new PolylineOptions().width(10).color(Color.RED).points(points);

			markerOptions = new MarkerOptions();
			markerOptions.flat(true);
			markerOptions.anchor(0.5f, 0.5f);
			markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding));
			markerOptions.position(points.get(points.size() - 1));

			addMarker();

			trackApp.getmyHandler().obtainMessage(0, "当前轨迹里程为 : " + (int) distance + "米").sendToTarget();

		}

	}

	/**
	 * 添加覆盖物
	 */
	protected void addMarker() {

		if (null != msUpdate) {
			trackApp.getmBaiduMap().animateMapStatus(msUpdate, 2000);
		}

		if (null != startMarker) {
			trackApp.getmBaiduMap().addOverlay(startMarker);
		}

		if (null != endMarker) {
			trackApp.getmBaiduMap().addOverlay(endMarker);
		}

		if (null != polyline) {
			trackApp.getmBaiduMap().addOverlay(polyline);
		}

	}

	/**
	 * 重置覆盖物
	 */
	private void resetMarker() {
		startMarker = null;
		endMarker = null;
		polyline = null;
	}

	// public static final TrackQueryFragment newInstance(TrackApplication
	// trackApp) {
	// TrackQueryFragment fragment = new TrackQueryFragment();
	// fragment.trackApp = trackApp;
	// return fragment;
	// }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_date:
			// 查询轨迹
			execute();
			break;
		case R.id.btn_excuision:
			isProcessed = isProcessed ^ 1;
			if (0 == isProcessed) {
				btn_excuision.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
				btn_excuision.setTextColor(Color.rgb(0x00, 0x00, 0x00));
				Toast.makeText(TravelTraceQueryActivity.this, "正在查询历史轨迹，请稍候", Toast.LENGTH_SHORT).show();
				queryHistoryTrack(0, null);
			} else {
				btn_excuision.setBackgroundColor(Color.rgb(0x99, 0xcc, 0xff));
				btn_excuision.setTextColor(Color.rgb(0x00, 0x00, 0xd8));
				Toast.makeText(TravelTraceQueryActivity.this, "正在查询纠偏后的历史轨迹，请稍候", Toast.LENGTH_SHORT).show();
				queryHistoryTrack(1, "need_denoise=1,need_vacuate=1,need_mapmatch=1");
			}
			break;
		case R.id.btn_distance:
			queryDistance(0, null);
			break;
		case R.id.iv_back:
			finish();
			break;

		}

	}

	@Override
	protected void onResume() {
		trackApp.getBmapView().onResume();
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		trackApp.getBmapView().onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		trackApp.getClient().onDestroy();
		trackApp.getBmapView().onDestroy();
//		android.os.Process.killProcess(android.os.Process.myPid());//杀死进程，会直接退出
		MyApplication.getInstance().delActivityList(this);
//		AbAppManager.getAbAppManager().finishActivity(this);
	}

}
