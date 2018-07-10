package com.small.saasuser.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 配置文件操作类
 * 
 */
public class SharePreferenceUtils {

	/** 单例对象 */
	private static SharePreferenceUtils intance = null;
	/** 配置文件对象 */
	private static SharedPreferences mSharedPreferences=null;
	/**名称*/
	private static final String SHARE_PREFS_NAME = "saasUser";

	
	/**
	 * 获取单例对象
	 * 
	 * @return
	 */
	public static SharePreferenceUtils getIntance() {
		if (intance == null) {
			intance = new SharePreferenceUtils();
		}
		return intance;
	}

	
	public static void putBoolean(Context ctx, String key, boolean value) {
		if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}

		mSharedPreferences.edit().putBoolean(key, value).commit();
	}

	public static boolean getBoolean(Context ctx, String key,
			boolean defaultValue) {
		if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}

		return mSharedPreferences.getBoolean(key, defaultValue);
	}

	public static void putString(Context ctx, String key, String value) {
		if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}

		mSharedPreferences.edit().putString(key, value).commit();
	}

	public static String getString(Context ctx, String key, String defaultValue) {
		if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}

		return mSharedPreferences.getString(key, defaultValue);
	}
	
	public static void putLong(Context ctx, String key, Long value) {
		if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}

		mSharedPreferences.edit().putLong(key, value).commit();
	}

	public static Long getLong(Context ctx, String key, Long defaultValue) {
		if (mSharedPreferences == null) {
			mSharedPreferences = ctx.getSharedPreferences(SHARE_PREFS_NAME,
					Context.MODE_PRIVATE);
		}

		return mSharedPreferences.getLong(key, defaultValue);
	}
	/**
	 * 获取配置文件对象
	 * 
	 * @param context
	 *            上下文
	 * @return
	 */
	public SharedPreferences getSharedPreferences(Context context) {
		try {
			if (mSharedPreferences == null) {
				mSharedPreferences = context.getSharedPreferences(SHARE_PREFS_NAME, Context.MODE_PRIVATE);
			}
		} catch (Exception e) {
		}
		return mSharedPreferences;
	}

}
