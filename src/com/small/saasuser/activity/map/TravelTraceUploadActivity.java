package com.small.saasuser.activity.map;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.mapapi.utils.CoordinateConverter.CoordType;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.TraceLocation;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.small.saasuser.activity.BaseActivity;
import com.small.saasuser.activity.R;
import com.small.saasuser.application.MyApplication;
import com.small.saasuser.utils.AbAppManager;
import com.small.saasuser.utils.DateUtil;
import com.small.saasuser.utils.JsonParser;
import com.small.saasuser.utils.LogUtil;
import com.small.saasuser.utils.SharePreferenceUtils;
import com.small.saasuser.view.ClearEditText;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class TravelTraceUploadActivity extends BaseActivity implements View.OnClickListener {
	private static String TAG = TravelTraceUploadActivity.class.getSimpleName();
	private MyApplication trackApp = null;

	private Button btn_start;
	private Button btn_end;
	private ImageView iv_back;
	private ImageButton iv_voice;
	private ClearEditText ce_search_upload;
	private Context context;
	int ret = 0; // 函数调用返回值
	private SharedPreferences sp;
	// private Geofence geoFence = null;
	/**
	 * 开启轨迹服务监听器
	 */
	protected static OnStartTraceListener startTraceListener = null;

	/**
	 * 停止轨迹服务监听器
	 */
	protected static OnStopTraceListener stopTraceListener = null;

	/**
	 * Entity监听器
	 */
	private static OnEntityListener entityListener = null;

	/**
	 * 采集周期（单位 : 秒）
	 */
	private int gatherInterval = 5;

	/**
	 * 打包周期（单位 : 秒）
	 */
	private int packInterval = 15;

	/**
	 * 图标
	 */
	private static BitmapDescriptor realtimeBitmap;

	private static Overlay overlay = null;

	// 覆盖物
	protected static OverlayOptions overlayOptions;

	// 路线覆盖物
	private static PolylineOptions polyline = null;

	private static List<LatLng> pointList = new ArrayList<LatLng>();

	private Intent serviceIntent = null;

	/**
	 * 刷新地图线程(获取实时点)
	 */
	protected RefreshThread refreshThread = null;

	protected static MapStatusUpdate msUpdate = null;

	private View view = null;

	private LayoutInflater mInflater = null;

	protected static boolean isInUpload = true;

	private static boolean isRegister = false;

	protected static PowerManager pm = null;

	protected static WakeLock wakeLock = null;

	private TrackReceiver trackReceiver = new TrackReceiver();

	private TrackUploadHandler mHandler = null;

	private boolean isTraceStarted = false;

	// 语音听写对象
	private SpeechRecognizer mIat;
	// 语音听写UI
	private RecognizerDialog mIatDialog;
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;
	// 初始化监听器。
	private InitListener mInitListener = new InitListener() {

		@Override
		public void onInit(int code) {
			Log.d(TAG, "SpeechRecognizer init() code = " + code);
			if (code != ErrorCode.SUCCESS) {
				Toast.makeText(context, "初始化失败，错误码：" + code, 0).show();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_travel_trace_upload);
		trackApp = (MyApplication) getApplicationContext();
		sp = SharePreferenceUtils.getIntance().getSharedPreferences(this);
		context = this;
		// 初始化
		initViews();

		// 初始化监听器
		initListener();

		// 设置采集周期
		setInterval();

		// 设置http请求协议类型
		setRequestType();

		mHandler = new TrackUploadHandler(this);

	}

	private void initViews() {
		trackApp.initBmap((MapView) findViewById(R.id.bmapView));
		btn_start = (Button) findViewById(R.id.btn_start);
		btn_end = (Button) findViewById(R.id.btn_end);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_voice = (ImageButton) findViewById(R.id.iv_voice);
		ce_search_upload = (ClearEditText) findViewById(R.id.ce_search_upload);

		// 初始化识别对象
		mIat = SpeechRecognizer.createRecognizer(this, mInitListener);
		// 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
		mIatDialog = new RecognizerDialog(this, mInitListener);

		btn_start.setOnClickListener(this);
		btn_end.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		iv_voice.setOnClickListener(this);

	}

	private void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);
		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");
		// 设置语言
		mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
		// 设置语言区域
		mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
		// 设置语音前端点
		mIat.setParameter(SpeechConstant.VAD_BOS, sp.getString("iat_vadbos_preference", "4000"));
		// 设置语音后端点
		mIat.setParameter(SpeechConstant.VAD_EOS, sp.getString("iat_vadeos_preference", "1000"));
		// 设置标点符号
		mIat.setParameter(SpeechConstant.ASR_PTT, sp.getString("iat_punc_preference", "1"));
		// 设置音频保存路径
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory() + "/iflytek/wavaudio.pcm");
		// 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
		// 注：该参数暂时只对在线听写有效
		mIat.setParameter(SpeechConstant.ASR_DWA, sp.getString("iat_dwa_preference", "0"));
	}

	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			// showTip("开始说话");
			// btn_sou.setText("搜索");
		}

		@Override
		public void onError(SpeechError error) {
			// Tips：
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			// 如果使用本地功能（语音+）需要提示用户开启语音+的录音权限。
			// showTip(error.getPlainDescription(true));
		}

		@Override
		public void onEndOfSpeech() {
			// showTip("结束说话");
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {
			Log.d(TAG, results.getResultString());
			printResult(results);

			if (isLast) {
				// TODO 最后的结果
			}
		}

		@SuppressWarnings("unused")
		public void onVolumeChanged(int volume) {
			// showTip("当前正在说话，音量大小：" + volume);
		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}

		@Override
		public void onVolumeChanged(int arg0, byte[] arg1) {
			// TODO Auto-generated method stub

		}
	};

	private RecognizerDialogListener recognizerDialogListener = new RecognizerDialogListener() {
		public void onResult(RecognizerResult results, boolean isLast) {
			printResult(results);
		}

		/**
		 * 识别回调错误.
		 */
		public void onError(SpeechError error) {
			// showTip(error.getPlainDescription(true));
		}

	};

	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		String str = resultBuffer.toString();
		ce_search_upload.requestFocus();
		ce_search_upload.setText(str.substring(0, str.length() - 1));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_start:
			Toast.makeText(TravelTraceUploadActivity.this, "正在开启轨迹服务，请稍候", Toast.LENGTH_LONG).show();
			startTrace();
			if (!isRegister) {
				if (null == pm) {
					pm = (PowerManager) trackApp.getSystemService(Context.POWER_SERVICE);
				}
				if (null == wakeLock) {
					wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "track upload");
				}
				IntentFilter filter = new IntentFilter();
				filter.addAction(Intent.ACTION_SCREEN_OFF);
				filter.addAction(Intent.ACTION_SCREEN_ON);
				filter.addAction("com.baidu.trace.action.GPS_STATUS");
				trackApp.registerReceiver(trackReceiver, filter);
				isRegister = true;
			}

			break;
		case R.id.btn_end:
			Toast.makeText(TravelTraceUploadActivity.this, "正在停止轨迹服务，请稍候", Toast.LENGTH_SHORT).show();
			stopTrace();
			if (isRegister) {
				try {
					trackApp.unregisterReceiver(trackReceiver);
					isRegister = false;
				} catch (Exception e) {
					// TODO: handle
				}

			}
			break;
		case R.id.iv_voice:
			ce_search_upload.setText(null);// 清空显示内容
			mIatResults.clear();
			// 设置参数
			setParam();
			boolean isShowDialog = sp.getBoolean("iat_show", true);
			if (isShowDialog) {
				// 显示听写对话框
				mIatDialog.setListener(recognizerDialogListener);
				mIatDialog.show();
				// showTip(getString(R.string.text_begin));
			} else {
				// 不显示听写对话框
				ret = mIat.startListening(recognizerListener);
				if (ret != ErrorCode.SUCCESS) {
					// showTip("听写失败,错误码：" + ret);
				} else {
					// showTip(getString(R.string.text_begin));
				}
			}

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
		super.onPause();
		trackApp.getBmapView().onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		trackApp.getClient().onDestroy();
		trackApp.getBmapView().onDestroy();
		// android.os.Process.killProcess(android.os.Process.myPid());
		MyApplication.getInstance().delActivityList(this);
		// AbAppManager.getAbAppManager().finishActivity(this);
	}

	// 启动服务，后台获取位置信息
	public void startMonitorService() {
		Toast.makeText(TravelTraceUploadActivity.this, "启动服务，后台获取位置信息", 1).show();
		serviceIntent = new Intent(trackApp, MonitorService.class);
		trackApp.startService(serviceIntent);
	}

	/**
	 * 初始化监听器
	 */
	private void initListener() {
		// 初始化开启轨迹服务监听器
		if (null == startTraceListener) {
			initOnStartTraceListener();
		}

		// 初始化停止轨迹服务监听器
		if (null == stopTraceListener) {
			initOnStopTraceListener();
		}

		// 初始化entity监听器
		if (null == entityListener) {
			initOnEntityListener();
		}
	}

	/**
	 * 开启轨迹服务
	 */
	private void startTrace() {
		// 通过轨迹服务客户端client开启轨迹服务
		trackApp.getClient().startTrace(trackApp.getTrace(), startTraceListener);

		if (!MonitorService.isRunning) {
			// 开启监听service
			MonitorService.isCheck = true;
			MonitorService.isRunning = true;
			startMonitorService();
		}
	}

	/**
	 * 停止轨迹服务
	 */
	private void stopTrace() {

		// 停止监听service
		MonitorService.isCheck = false;
		MonitorService.isRunning = false;

		// 通过轨迹服务客户端client停止轨迹服务
		trackApp.getClient().stopTrace(trackApp.getTrace(), stopTraceListener);

		if (null != serviceIntent) {
			trackApp.stopService(serviceIntent);
		}
	}

	/**
	 * 设置采集周期和打包周期
	 */
	private void setInterval() {
		trackApp.getClient().setInterval(gatherInterval, packInterval);
	}

	/**
	 * 设置请求协议
	 */
	protected void setRequestType() {
		int type = 0;
		trackApp.getClient().setProtocolType(type);
	}

	/**
	 * 查询实时轨迹
	 */
	private void queryRealtimeLoc() {
		trackApp.getClient().queryRealtimeLoc(trackApp.getServiceId(), entityListener);
	}

	/**
	 * 查询entityList
	 */
	private void queryEntityList() {
		// entity标识列表（多个entityName，以英文逗号"," 分割）
		String entityNames = trackApp.getEntityName();
		// 属性名称（格式为 : "key1=value1,key2=value2,....."）
		String columnKey = "";
		// 返回结果的类型（0 : 返回全部结果，1 : 只返回entityName的列表）
		int returnType = 0;
		// 活跃时间（指定该字段时，返回从该时间点之后仍有位置变动的entity的实时点集合）
		int activeTime = (int) (System.currentTimeMillis() / 1000 - packInterval);
		// 分页大小
		int pageSize = 10;
		// 分页索引
		int pageIndex = 1;

		trackApp.getClient().queryEntityList(trackApp.getServiceId(), entityNames, columnKey, returnType, activeTime,
				pageSize, pageIndex, entityListener);
	}

	/**
	 * 初始化OnStartTraceListener
	 */
	private void initOnStartTraceListener() {
		// 初始化startTraceListener
		startTraceListener = new OnStartTraceListener() {

			// 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
			public void onTraceCallback(int arg0, String arg1) {
				// TODO Auto-generated method stub
				mHandler.obtainMessage(arg0, "开启轨迹服务回调接口消息 [消息编码 : " + arg0 + "，消息内容 : " + arg1 + "]").sendToTarget();
			}

			// 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
			public void onTracePushCallback(byte arg0, String arg1) {
				// TODO Auto-generated method stub
				if (0x03 == arg0 || 0x04 == arg0) {
					try {
						JSONObject dataJson = new JSONObject(arg1);
						if (null != dataJson) {
							String mPerson = dataJson.getString("monitored_person");
							String action = dataJson.getInt("action") == 1 ? "进入" : "离开";
							String date = DateUtil.getDate(dataJson.getInt("time"));
							long fenceId = dataJson.getLong("fence_id");
							mHandler.obtainMessage(-1,
									"监控对象[" + mPerson + "]于" + date + " [" + action + "][" + fenceId + "号]围栏")
									.sendToTarget();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						mHandler.obtainMessage(-1, "轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1 + "]")
								.sendToTarget();
					}
				} else {
					mHandler.obtainMessage(-1, "轨迹服务推送接口消息 [消息类型 : " + arg0 + "，消息内容 : " + arg1 + "]").sendToTarget();
				}
			}

		};
	}

	/**
	 * 初始化OnStopTraceListener
	 */
	private void initOnStopTraceListener() {
		// 初始化stopTraceListener
		stopTraceListener = new OnStopTraceListener() {

			// 轨迹服务停止成功
			public void onStopTraceSuccess() {
				// TODO Auto-generated method stub
				mHandler.obtainMessage(1, "停止轨迹服务成功").sendToTarget();
				startRefreshThread(false);
				trackApp.getClient().onDestroy();
			}

			// 轨迹服务停止失败（arg0 : 错误编码，arg1 : 消息内容，详情查看类参考）
			public void onStopTraceFailed(int arg0, String arg1) {
				// TODO Auto-generated method stub
				mHandler.obtainMessage(-1, "停止轨迹服务接口消息 [错误编码 : " + arg0 + "，消息内容 : " + arg1 + "]").sendToTarget();
				startRefreshThread(false);
			}
		};
	}

	/**
	 * 初始化OnEntityListener
	 */
	private void initOnEntityListener() {
		entityListener = new OnEntityListener() {

			// 请求失败回调接口
			@Override
			public void onRequestFailedCallback(String arg0) {
				// TODO Auto-generated method stub
				trackApp.getmyHandler().obtainMessage(0, "entity请求失败回调接口消息 : " + arg0).sendToTarget();
			}

			// 添加entity回调接口
			public void onAddEntityCallback(String arg0) {
				// TODO Auto-generated method stub
				trackApp.getmyHandler().obtainMessage(0, "添加entity回调接口消息 : " + arg0).sendToTarget();
			}

			// 查询entity列表回调接口
			@Override
			public void onQueryEntityListCallback(String message) {
				// TODO Auto-generated method stub
				TraceLocation entityLocation = new TraceLocation();
				try {
					JSONObject dataJson = new JSONObject(message);
					if (null != dataJson && dataJson.has("status") && dataJson.getInt("status") == 0
							&& dataJson.has("size") && dataJson.getInt("size") > 0) {
						JSONArray entities = dataJson.getJSONArray("entities");
						JSONObject entity = entities.getJSONObject(0);
						JSONObject point = entity.getJSONObject("realtime_point");
						JSONArray location = point.getJSONArray("location");
						entityLocation.setLongitude(location.getDouble(0));
						entityLocation.setLatitude(location.getDouble(1));
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					trackApp.getmyHandler().obtainMessage(0, "解析entityList回调消息失败").sendToTarget();
					return;
				}
				showRealtimeTrack(entityLocation);
			}

			@Override
			public void onReceiveLocation(TraceLocation location) {
				// TODO Auto-generated method stub
				showRealtimeTrack(location);
			}

		};
	}

	protected class RefreshThread extends Thread {

		protected boolean refresh = true;

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Looper.prepare();
			while (refresh) {
				// 轨迹服务开启成功后，调用queryEntityList()查询最新轨迹；
				// 未开启轨迹服务时，调用queryRealtimeLoc()进行实时定位。
				if (isTraceStarted) {
					queryEntityList();
				} else {
					queryRealtimeLoc();
				}

				try {
					Thread.sleep(gatherInterval * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("线程休眠失败");
				}
			}
			Looper.loop();
		}
	}

	/**
	 * 显示实时轨迹
	 * 
	 * @param location
	 */
	protected void showRealtimeTrack(TraceLocation location) {

		if (null == refreshThread || !refreshThread.refresh) {
			return;
		}

		double latitude = location.getLatitude();
		double longitude = location.getLongitude();
		LogUtil.i(TAG, "实时经纬度-----------》" + "经度：" + longitude + "纬度：" + latitude);
		if (Math.abs(latitude - 0.0) < 0.000001 && Math.abs(longitude - 0.0) < 0.000001) {
			mHandler.obtainMessage(-1, "当前查询无轨迹点").sendToTarget();
		} else {
			LatLng latLng = new LatLng(latitude, longitude);
			if (1 == location.getCoordType()) {
				LatLng sourceLatLng = latLng;
				CoordinateConverter converter = new CoordinateConverter();
				converter.from(CoordType.GPS);
				converter.coord(sourceLatLng);
				latLng = converter.convert();
			}
			pointList.add(latLng);

			if (isInUpload) {
				// 绘制实时点
				drawRealtimePoint(latLng);
				LogUtil.i(TAG, "绘点-----------》" + "经度：纬度：" + latLng.toString());
			}
		}
	}

	/**
	 * 绘制实时点
	 * 
	 * @param point
	 */
	private void drawRealtimePoint(LatLng point) {

		if (null != overlay) {
			overlay.remove();
		}

		MapStatus mMapStatus = new MapStatus.Builder().target(point).zoom(19).build();

		msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

		if (null == realtimeBitmap) {
			realtimeBitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
		}
		LogUtil.i(TAG, "添加图标-----------》");
		overlayOptions = new MarkerOptions().position(point).icon(realtimeBitmap).zIndex(9).draggable(true);

		if (pointList.size() >= 2 && pointList.size() <= 10000) {
			// 添加路线（轨迹）
			polyline = new PolylineOptions().width(10).color(Color.RED).points(pointList);
		}

		addMarker();

	}

	/**
	 * 添加地图覆盖物
	 */
	protected void addMarker() {

		if (null != msUpdate) {
			trackApp.getmBaiduMap().setMapStatus(msUpdate);
		}

		// 路线覆盖物
		if (null != polyline) {
			trackApp.getmBaiduMap().addOverlay(polyline);
		}

		// 实时点覆盖物
		if (null != overlayOptions) {
			overlay = trackApp.getmBaiduMap().addOverlay(overlayOptions);
		}

	}

	protected void startRefreshThread(boolean isStart) {
		if (null == refreshThread) {
			refreshThread = new RefreshThread();
		}
		refreshThread.refresh = isStart;
		if (isStart) {
			if (!refreshThread.isAlive()) {
				refreshThread.start();
			}
		} else {
			refreshThread = null;
		}
	}

	static class TrackUploadHandler extends Handler {
		WeakReference<TravelTraceUploadActivity> trackUpload;

		TrackUploadHandler(TravelTraceUploadActivity uploadActivity) {
			trackUpload = new WeakReference<TravelTraceUploadActivity>(uploadActivity);
		}

		@Override
		public void handleMessage(Message msg) {
			TravelTraceUploadActivity uploadActivity = trackUpload.get();
			Toast.makeText(uploadActivity.trackApp, (String) msg.obj, Toast.LENGTH_LONG).show();

			switch (msg.what) {
			case 0:
			case 10006:
			case 10008:
			case 10009:
				uploadActivity.isTraceStarted = true;
				uploadActivity.btn_start.setBackgroundColor(Color.rgb(0x99, 0xcc, 0xff));
				uploadActivity.btn_start.setTextColor(Color.rgb(0x00, 0x00, 0xd8));
				break;

			case 1:
			case 10004:
				uploadActivity.isTraceStarted = false;
				uploadActivity.btn_end.setBackgroundColor(Color.rgb(0xff, 0xff, 0xff));
				uploadActivity.btn_end.setTextColor(Color.rgb(0x00, 0x00, 0x00));
				break;

			default:
				break;
			}
		}
	}

}
