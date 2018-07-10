package com.small.saasuser.application;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.xutils.x;

import com.baidu.frontia.FrontiaApplication;
import com.baidu.lbsapi.BMapManager;
import com.baidu.lbsapi.MKGeneralListener;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.Trace;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.easemob.chat.EMMessage.ChatType;
import com.saas.chat.MainActivity;
import com.saas.chat.chat.ChatActivity;
import com.saas.chat.chat.VoiceCallActivity;
import com.small.saasuser.activity.UserLoginActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

public class MyApplication extends FrontiaApplication {
	public static Context applicationContext;
	public static Context mContext;
	public static MyApplication instance = null;
	private List<Activity> activityList = new ArrayList<Activity>();
	public static boolean ISAPPUPDATE = false;
	/**
	 * 轨迹服务
	 */
	private Trace trace = null;

	/**
	 * 轨迹服务客户端
	 */
	private LBSTraceClient client = null;

	/**
	 * 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
	 */
	private int serviceId = 129168;

	/**
	 * entity标识
	 */
	private String entityName = "myTrace";

	/**
	 * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
	 */
	private int traceType = 2;

	private MapView bmapView = null;

	private BaiduMap mBaiduMap = null;

	private TrackHandler myHandler = null;
	// public BMapManager mBMapManager;

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = getApplicationContext();
		applicationContext = this;
		SDKInitializer.initialize(mContext);
		// 初始化轨迹服务客户端
		client = new LBSTraceClient(mContext);

		// 初始化轨迹服务
		trace = new Trace(mContext, serviceId, entityName, traceType);

		// 设置定位模式
		client.setLocationMode(LocationMode.High_Accuracy);

		myHandler = new TrackHandler(this);

		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				SpeechUtility.createUtility(MyApplication.this,
						SpeechConstant.APPID + "=582bbff6," + SpeechConstant.FORCE_LOGIN + "=true");
			}
		}).start();

		initEMChat();
		EMChat.getInstance().init(mContext);
		EMChat.getInstance().setDebugMode(true);
		EMChat.getInstance().setAutoLogin(true);
		EMChatManager.getInstance().getChatOptions().setUseRoster(true);
		FrontiaApplication.initFrontiaApplication(this);
		// CrashHandler crashHandler = CrashHandler.getInstance();// 全局异常捕捉
		// crashHandler.init(mContext);
		// 初始化Xutill
		x.Ext.init(this);
	}

	/**
	 * 单例模式
	 * 
	 * @return Application对象
	 */

	/** Log打印List集合 */
	public List<Activity> getActivityList() {
		if (activityList != null) {
			return activityList;
		}
		return activityList;
	}

	/** 删除List集合 */
	public void delActivityList(Activity activity) {
		if (activityList != null && activity != null) {
			activity.finish();
			activityList.remove(activity);
			activity = null;
		}
	}

	/**
	 * 添加界面到集合
	 * 
	 * @param activity
	 *            界面
	 */
	public void addActivity(Activity activity) {
		// 判断传入的界面是否有效
		if (activity != null) {
			activityList.add(activity);

		}
	}

	/**
	 * 退出所有的界面
	 */
	public void exitActivity(Context context) {
		// 清除图片加载
		// 判断是否存在下载更新服务,如果存在
		if (ISAPPUPDATE) {
			try {
				for (Activity activity : activityList) {
					if (activity != null) {
						// 关闭界面
						activity.finish();
					}
				}
			} catch (Exception e) {
			} finally {

			}
		} else {
			try {
				for (Activity activity : activityList) {
					if (activity != null) {
						// 关闭界面
						activity.finish();
					}
				}
			} catch (Exception e) {
			} finally {
				// 强制GC(释放内存)
				System.gc();
				// 退出
				// System.exit(0);
			}
		}

	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetPermissionState(int iError) {
			// 非零值表示key验证未通过
			if (iError != 0) {
			} else {

			}
		}
	}

	/**
	 * 返回登录界面
	 */
	public void reture_login(Activity activity) {

		Intent intent = new Intent(activity, UserLoginActivity.class);
		activity.startActivity(intent);
		activity.finish();

	}

	public static MyApplication getInstance() {
		if (instance == null) {
			synchronized (MyApplication.class) {
				if (instance == null) {
					instance = new MyApplication();
				}
			}
		}
		return instance;

	}

	public static Context getmContext() {

		return mContext;
	}

	private void initEMChat() {
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		if (processAppName == null || !processAppName.equalsIgnoreCase("com.saas.chat")) {
			return;
		}
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 获取到EMChatOptions对象
		// 设置自定义的文字提示
		options.setNotifyText(new OnMessageNotifyListener() {

			@Override
			public String onNewMessageNotify(EMMessage message) {
				return "你的好友发来了一条消息哦";
			}

			@Override
			public String onLatestMessageNotify(EMMessage message, int fromUsersNum, int messageNum) {
				return fromUsersNum + "个好友，发来了" + messageNum + "条消息";
			}

			@Override
			public String onSetNotificationTitle(EMMessage arg0) {
				return null;
			}

			@Override
			public int onSetSmallIcon(EMMessage arg0) {
				return 0;
			}
		});
		options.setOnNotificationClickListener(new OnNotificationClickListener() {

			@Override
			public Intent onNotificationClick(EMMessage message) {
				Intent intent = new Intent(mContext, MainActivity.class);
				ChatType chatType = message.getChatType();
				if (chatType == ChatType.Chat) { // 单聊信息
					intent.putExtra("userId", message.getFrom());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_SINGLE);
				} else { // 群聊信息
					// message.getTo()为群聊id
					intent.putExtra("groupId", message.getTo());
					intent.putExtra("chatType", ChatActivity.CHATTYPE_GROUP);
				}
				return intent;
			}
		});
		// 视频通话 ----->调试
		// IntentFilter callFilter = new
		// IntentFilter(EMChatManager.getInstance()
		// .getIncomingCallBroadcastAction());
		// registerReceiver(new CallReceiver(), callFilter);
	}

	private class CallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 拨打方username
			String from = intent.getStringExtra("from");
			// call type
			String type = intent.getStringExtra("type");
			startActivity(new Intent(mContext, VoiceCallActivity.class).putExtra("username", from)
					.putExtra("isComingCall", true));
		}
	}

	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm
							.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
			}
		}
		return processName;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		try {
			deleteCacheDirFile(getHJYCacheDir(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.gc();
	}

	// public static Context getInstance() {
	// return mContext;
	// }

	// // 运用list来保存们每一个activity是关键
	// private List<Activity> mList = new LinkedList<Activity>();
	// private static App instance;

	// 构造方法
	// 实例化一次
	public synchronized static MyApplication getInstance2() {
		if (null == instance) {
			instance = new MyApplication();
		}
		return instance;
	}

	// // add Activity
	// public void addActivity(Activity activity) {
	// mList.add(activity);
	// }

	// 关闭每一个list内的activity
	public void exit() {
		try {
			for (Activity activity : activityList) {
				if (activity != null)
					activity.finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

	public static String getHJYCacheDir() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return Environment.getExternalStorageDirectory().toString() + "/Health/Cache";
		else
			return "/System/com.saas.Walk/Walk/Cache";
	}

	public static String getHJYDownLoadDir() {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
			return Environment.getExternalStorageDirectory().toString() + "/Walk/Download";
		else {
			return "/System/com.saas.Walk/Walk/Download";
		}
	}

	public static void deleteCacheDirFile(String filePath, boolean deleteThisPath) throws IOException {
		if (!TextUtils.isEmpty(filePath)) {
			File file = new File(filePath);
			if (file.isDirectory()) {// 处理目录
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteCacheDirFile(files[i].getAbsolutePath(), true);
				}
			}
			if (deleteThisPath) {
				if (!file.isDirectory()) {// 如果是文件，删除
					file.delete();
				} else {// 目录
					if (file.listFiles().length == 0) {// 目录下没有文件或者目录，删除
						file.delete();
					}
				}
			}
		}
	}
	//百度鹰眼
	public void initBmap(MapView bmapView) {
		this.bmapView = bmapView;
		this.mBaiduMap = bmapView.getMap();
		this.bmapView.showZoomControls(false);
	}

	static class TrackHandler extends Handler {
		WeakReference<MyApplication> trackApp;

		TrackHandler(MyApplication myApplication) {
			trackApp = new WeakReference<MyApplication>(myApplication);
		}

		@Override
		public void handleMessage(Message msg) {
			Toast.makeText(trackApp.get().mContext, (String) msg.obj, Toast.LENGTH_SHORT).show();
		}
	}

	public Context getmyContext() {
		return mContext;
	}

	public Trace getTrace() {
		return trace;
	}

	public LBSTraceClient getClient() {
		return client;
	}

	public int getServiceId() {
		return serviceId;
	}

	public String getEntityName() {
		return entityName;
	}

	public Handler getmyHandler() {
		return myHandler;
	}

	public MapView getBmapView() {
		return bmapView;
	}

	public BaiduMap getmBaiduMap() {
		return mBaiduMap;
	}

	/**
	 * 获取设备IMEI码，手机设备唯一标识
	 * 
	 * @param context
	 * @return
	 */
	protected static String getImei(Context context) {
		String mImei = "NULL";
		try {
			mImei = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
		} catch (Exception e) {
			System.out.println("获取IMEI码失败");
			mImei = "NULL";
		}
		return mImei;
	}

}
