/*
    ShengDao Android Client, CommonUtils
    Copyright (c) 2014 ShengDao Tech Company Limited
 */

package com.small.saasuser.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import com.small.saasuser.activity.R;
import com.small.saasuser.application.MyApplication;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

public class CommonUtils {

	private static final String TAG = CommonUtils.class.getSimpleName();

	public CommonUtils() {
		/* 不能实例化 **/}

	/** 网络类型 **/
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	/**
	 * 根据key获取config.properties里面的值
	 * 
	 * @param context
	 * @param key
	 * @return
	 */
	public static String getProperty(Context context, String key) {
		try {
			Properties props = new Properties();
			InputStream input = context.getAssets().open("config.properties");
			if (input != null) {
				props.load(input);
				return props.getProperty(key);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/**
	 * 获取当前网络类型
	 * 
	 * @return 0：没有网络 1：WIFI网络 2：WAP网络 3：NET网络
	 */
	public static int getNetworkType(Context context) {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!TextUtils.isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase(Locale.getDefault()).equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	/**
	 * 判断SDCard是否存在,并可写
	 * 
	 * @return
	 */
	public static boolean checkSDCard() {
		String flag = Environment.getExternalStorageState();
		if (android.os.Environment.MEDIA_MOUNTED.equals(flag)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取屏幕宽度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.widthPixels;
	}

	/**
	 * 获取屏幕高度
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm.heightPixels;
	}

	/**
	 * 获取屏幕显示信息对象
	 * 
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getDisplayMetrics(Context context) {
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		return dm;
	}

	/**
	 * dp转pixel
	 */
	public static float dpToPixel(float dp, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return dp * (metrics.densityDpi / 160f);
	}

	/**
	 * pixel转dp
	 */
	public static float pixelsToDp(float px, Context context) {
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		return px / (metrics.densityDpi / 160f);
	}

	/**
	 * 短信分享
	 * 
	 * @param mContext
	 * @param smstext
	 *            短信分享内容
	 * @return
	 */
	public static Boolean sendSms(Context mContext, String smstext) {
		Uri smsToUri = Uri.parse("smsto:");
		Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
		mIntent.putExtra("sms_body", smstext);
		mContext.startActivity(mIntent);
		return null;
	}

	/**
	 * 邮件分享
	 * 
	 * @param mContext
	 * @param title
	 *            邮件的标题
	 * @param text
	 *            邮件的内容
	 * @return
	 */
	public static void sendMail(Context mContext, String title, String text) {
		// 调用系统发邮件
		Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
		// 设置文本格式
		emailIntent.setType("text/plain");
		// 设置对方邮件地址
		emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, "");
		// 设置标题内容
		emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, title);
		// 设置邮件文本内容
		emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
		mContext.startActivity(Intent.createChooser(emailIntent, "Choose Email Client"));
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param activity
	 */
	public static void hideKeyboard(Activity activity) {
		if (activity != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
			}
		}
	}

	/**
	 * 显示软键盘
	 * 
	 * @param activity
	 */
	public static void showKeyboard(Activity activity) {
		if (activity != null) {
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (!imm.isActive()) {
				imm.showSoftInputFromInputMethod(activity.getCurrentFocus().getWindowToken(), 0);
			}
		}
	}

	/**
	 * 是否横屏
	 * 
	 * @param context
	 * @return true为横屏，false为竖屏
	 */
	public static boolean isLandscape(Context context) {
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否是平板 这个方法是从 Google I/O App for Android 的源码里找来的，非常准确。
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isTablet(Context context) {
		return (context.getResources().getConfiguration().screenLayout
				& Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
	}

	public static boolean isFirst() {

		SharedPreferences sharedPreferences = MyApplication.getmContext().getSharedPreferences("share",
				Context.MODE_PRIVATE);
		boolean isFirstRun = sharedPreferences.getBoolean("isFirstRun", true);

		SharedPreferences.Editor editor = sharedPreferences.edit();
		if (isFirstRun) {

			editor.putBoolean("isFirstRun", false);
			editor.commit();
			return true;

		} else {

			return false;
		}

	}

	// 电话<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**
	 * 打电话
	 * 
	 * @param context
	 * @param phone
	 */
	public static void call(Activity context, String phone) {
		if (StringUtils.isNotEmpty(phone, true)) {
			Uri uri = Uri.parse("tel:" + phone.trim());
			Intent intent = new Intent(Intent.ACTION_CALL, uri);
			toActivity(context, intent);
			return;
		}
		ToastUtil.showShort(context, "请先选择号码哦~");
	}

	// 电话>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

	// 信息<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
	/**
	 * 发送信息，多号码
	 * 
	 * @param context
	 * @param phoneList
	 */
	public static void toMessageChat(Activity context, List<String> phoneList) {
		if (context == null || phoneList == null || phoneList.size() <= 0) {
			Log.e(TAG, "sendMessage context == null || phoneList == null || phoneList.size() <= 0 "
					+ ">> showShortToast(context, 请先选择号码哦~); return; ");
			ToastUtil.showShort(context, "请先选择号码哦~");
			return;
		}

		String phones = "";
		for (int i = 0; i < phoneList.size(); i++) {
			phones += phoneList.get(i) + ";";
		}
		toMessageChat(context, phones);
	}

	/**
	 * 发送信息，单个号码
	 * 
	 * @param context
	 * @param phone
	 */
	public static void toMessageChat(Activity context, String phone) {
		if (context == null || StringUtils.isNotEmpty(phone, true) == false) {
			Log.e(TAG, "sendMessage  context == null || StringUtil.isNotEmpty(phone, true) == false) >> return;");
			return;
		}

		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.putExtra("address", phone);
		intent.setType("vnd.android-dir/mms-sms");
		toActivity(context, intent);

	}

	/**
	 * 发送邮件
	 * 
	 * @param context
	 * @param emailAddress
	 */
	public static void sendEmail(Activity context, String emailAddress) {
		if (context == null || StringUtils.isNotEmpty(emailAddress, true) == false) {
			Log.e(TAG, "sendEmail  context == null || StringUtil.isNotEmpty(emailAddress, true) == false >> return;");
			return;
		}

		Intent intent = new Intent(Intent.ACTION_SENDTO);
		intent.setData(Uri.parse("mailto:" + emailAddress));// 缺少"mailto:"前缀导致找不到应用崩溃
		intent.putExtra(Intent.EXTRA_TEXT, "内容"); // 最近在MIUI7上无内容导致无法跳到编辑邮箱界面
		toActivity(context, intent, -1);
	}

	/**
	 * 打开网站
	 * 
	 * @param context
	 * @param webSite
	 */
	public static void openWebSite(Activity context, String webSite) {
		if (context == null || StringUtils.isNotEmpty(webSite, true) == false) {
			Log.e(TAG, "openWebSite  context == null || StringUtil.isNotEmpty(webSite, true) == false >> return;");
			return;
		}

		if (!webSite.startsWith("http://") && !webSite.startsWith("https://")) {
			webSite = "http://" + webSite;
		}
		final Uri uri = Uri.parse(webSite);
		Intent intent = new Intent(Intent.ACTION_VIEW, uri);
		toActivity(context, intent, -1);
	}

	/**
	 * 复制文字
	 * 
	 * @param context
	 * @param value
	 */
	public static void copyText(Context context, String value) {
		if (context == null || StringUtils.isNotEmpty(value, true) == false) {
			Log.e(TAG, "copyText  context == null || StringUtil.isNotEmpty(value, true) == false >> return;");
			return;
		}

		ClipData cD = ClipData.newPlainText("simple text", value);
		ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
		clipboardManager.setPrimaryClip(cD);
		ToastUtil.showShort(context, "已复制\n" + value);
	}

	/**
	 * 照片裁剪
	 * 
	 * @param context
	 * @param requestCode
	 * @param fromFile
	 * @param width
	 * @param height
	 */
	public static void startPhotoZoom(Activity context, int requestCode, Uri fileUri, int width, int height) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(fileUri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);
		intent.putExtra("return-data", true);
		Log.i(TAG, "startPhotoZoom" + fileUri + " uri");
		toActivity(context, intent, requestCode);
	}

	/**
	 * 保存照片到SD卡上面
	 * 
	 * @param path
	 * @param photoName
	 * @param formSuffix
	 * @param photoBitmap
	 */
	public static String savePhotoToSDCard(String path, String photoName, String formSuffix, Bitmap photoBitmap) {
		if (photoBitmap == null || StringUtils.isNotEmpty(path, true) == false
				|| StringUtils.isNotEmpty(
						StringUtils.getTrimedString(photoName) + StringUtils.getTrimedString(formSuffix),
						true) == false) {
			Log.e(TAG, "savePhotoToSDCard photoBitmap == null || StringUtil.isNotEmpty(path, true) == false"
					+ "|| StringUtil.isNotEmpty(photoName, true) == false) >> return null");
			return null;
		}

		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			File photoFile = new File(path, photoName + "." + formSuffix); // 在指定路径下创建文件
			FileOutputStream fileOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(photoFile);
				if (photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)) {
					fileOutputStream.flush();
					Log.i(TAG,
							"savePhotoToSDCard<<<<<<<<<<<<<<\n" + photoFile.getAbsolutePath() + "\n>>>>>>>>> succeed!");
				}
			} catch (FileNotFoundException e) {
				Log.e(TAG, "savePhotoToSDCard catch (FileNotFoundException e) {\n " + e.getMessage());
				photoFile.delete();
				// e.printStackTrace();
			} catch (IOException e) {
				Log.e(TAG, "savePhotoToSDCard catch (IOException e) {\n " + e.getMessage());
				photoFile.delete();
				// e.printStackTrace();
			} finally {
				try {
					if (fileOutputStream != null) {
						fileOutputStream.close();
					}
				} catch (IOException e) {
					Log.e(TAG, "savePhotoToSDCard } catch (IOException e) {\n " + e.getMessage());
					// e.printStackTrace();
				}
			}

			return photoFile.getAbsolutePath();
		}

		return null;
	}

	/**
	 * 检测网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}

		return false;
	}

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取顶层 Activity
	 * 
	 * @param context
	 * @return
	 */
	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		@SuppressWarnings("deprecation")
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		return runningTaskInfos == null ? "" : runningTaskInfos.get(0).topActivity.getClassName();
	}

	/**
	 * 检查是否有位置权限
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isHasLocationPermission(Context context) {
		return isHasPermission(context, "android.permission.ACCESS_COARSE_LOCATION")
				|| isHasPermission(context, "android.permission.ACCESS_FINE_LOCATION");
	}

	/**
	 * 检查是否有权限
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static boolean isHasPermission(Context context, String name) {
		try {
			return PackageManager.PERMISSION_GRANTED == context.getPackageManager().checkPermission(name,
					context.getPackageName());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	}
	// 启动新Activity方法<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

	/**
	 * 打开新的Activity，向左滑入效果
	 * 
	 * @param intent
	 */
	public static void toActivity(final Activity context, final Intent intent) {
		toActivity(context, intent, true);
	}

	/**
	 * 打开新的Activity
	 * 
	 * @param intent
	 * @param showAnimation
	 */
	public static void toActivity(final Activity context, final Intent intent, final boolean showAnimation) {
		toActivity(context, intent, -1, showAnimation);
	}

	/**
	 * 打开新的Activity，向左滑入效果
	 * 
	 * @param intent
	 * @param requestCode
	 */
	public static void toActivity(final Activity context, final Intent intent, final int requestCode) {
		toActivity(context, intent, requestCode, true);
	}

	/**
	 * 打开新的Activity
	 * 
	 * @param intent
	 * @param requestCode
	 * @param showAnimation
	 */
	public static void toActivity(final Activity context, final Intent intent, final int requestCode,
			final boolean showAnimation) {
		if (context == null || intent == null) {
			return;
		}
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if (requestCode < 0) {
					context.startActivity(intent);
				} else {
					context.startActivityForResult(intent, requestCode);
				}
				if (showAnimation) {
					context.overridePendingTransition(R.anim.right_push_in, R.anim.hold);
				} else {
					context.overridePendingTransition(R.anim.null_anim, R.anim.null_anim);
				}
			}
		});
	}
	
	 /** 
     * 设置隐藏标题栏 
     *  
     * @param activity 
     */  
    public static void setNoTitleBar(Activity activity) {  
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);  
    }  
  
    /** 
     * 设置全屏 
     *  
     * @param activity 
     */  
    public static void setFullScreen(Activity activity) {  
        activity.getWindow().setFlags(  
                WindowManager.LayoutParams.FLAG_FULLSCREEN,  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
    }  
  
    /** 
     * 取消全屏 
     *  
     * @param activity 
     */  
    public static void cancelFullScreen(Activity activity) {  
        activity.getWindow().clearFlags(  
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
    }  
    
   


}
