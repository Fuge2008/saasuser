 package com.small.saasuser.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by tanyl on 2016/10/11 09:54
 */
public class PreferenceUtils {

    public static void writeString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tanlin", Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key,value).commit();


    }
    public static void writeBoolean(Context context, String key, Boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tanlin", Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key,value).commit();


    }

    public static Boolean readBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tanlin", Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key,false);

    }
    public static String readString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("tanlin", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, null);

    }
}