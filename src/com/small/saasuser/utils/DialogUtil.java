package com.small.saasuser.utils;





import com.small.saasuser.activity.R;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * 退出对话框(系统类型)
 */
public class DialogUtil {

	/**
	 * 创建普通单按钮对话框
	 * 
	 * @param ctx
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon 必填
	 * @param title
	 *            标题 必填
	 * @param message
	 *            显示内容 必填
	 * @param btnName
	 *            按钮名称 必填
	 * @param listener
	 *            监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return
	 */
	public static Dialog createDialog(Context ctx, int iconId, String title,
			String message, String btnName, View.OnClickListener listener) {
		final Dialog loadingDialog = new Dialog(ctx, R.style.loading_dialog);
		View view = View.inflate(ctx, R.layout.dialog_alter, null);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
		ImageView img_log = (ImageView) view.findViewById(R.id.img_log);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		tv_title.setText(title);
		tv_msg.setText(message);
		img_log.setImageResource(iconId);
		btn_cancel.setText(btnName);
		btn_cancel.setOnClickListener(listener);

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(view, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;

	}


	/**
	 * 创建普通双按钮对话框
	 * 
	 * @param ctx
	 *            上下文 必填
	 * @param iconId
	 *            图标，如：R.drawable.icon[不想显示就写0] 必填
	 * @param title
	 *            标题 必填
	 * @param message
	 *            显示内容 必填
	 * @param btnPositiveName
	 *            第一个按钮名称 必填
	 * @param listener_Positive
	 *            第一个监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @param btnNegativeName
	 *            第二个按钮名称 必填
	 * @param listener_Negative
	 *            第二个监听器，需实现android.content.DialogInterface.OnClickListener接口 必填
	 * @return 对话框实例
	 */
	public static Dialog createDialog(Context ctx, int iconId, String title,
			String message, String btnPositiveName,
			View.OnClickListener listener_Positive, String btnNegativeName,
			View.OnClickListener listener_Negative) {

		final Dialog loadingDialog = new Dialog(ctx, R.style.loading_dialog);
		View view = View.inflate(ctx, R.layout.dialog_alter2, null);
		TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
		TextView tv_msg = (TextView) view.findViewById(R.id.tv_msg);
		ImageView img_log = (ImageView) view.findViewById(R.id.img_log);
		Button btn_sure = (Button) view.findViewById(R.id.btn_sure);
		Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);

		tv_title.setText(title);
		tv_msg.setText(message);
		img_log.setImageResource(iconId);
		btn_sure.setText(btnPositiveName);
		btn_sure.setOnClickListener(listener_Positive);
		btn_cancel.setText(btnNegativeName);
		btn_cancel.setOnClickListener(listener_Negative);

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(view, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
		return loadingDialog;
	}



	/**
	 * 加载对话框
	 * 
	 * @param context
	 *            上下文
	 * @param msg
	 *            内容
	 * @return
	 */
	public static Dialog createLoadDialog(Context context, String msg) {

	

		Dialog loadingDialog = new LoadDialog(context, R.style.myDialogTheme);
		return loadingDialog;
	}

	public static Dialog createLoaDialog(Context context) {

		Dialog loadingDialog = new LoadDialog(context, R.style.myDialogTheme);
		return loadingDialog;
	}

	static Dialog dialog;

	public static void showProgressDialog(Context context, String msg) {

		dialog = createLoadDialog(context, msg);
		dialog.show();

	}

	public static void closeProgressDialog() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

	/**
	 * 当判断当前手机没有网络时使用
	 * 
	 * @param context
	 */
	public static void showNoNetWork(final Context context) {
		AlertDialog.Builder builder = new Builder(context);
		builder.setIcon(R.drawable.ic_launcher)
				//
				.setTitle(R.string.app_name)
				//
				.setMessage("当前无网络")
				.setPositiveButton("设置", new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// 跳转到系统的网络设置界面
						dialog.dismiss();
						Intent intent = new Intent(
								Settings.ACTION_WIFI_SETTINGS);
						context.startActivity(intent);

					}
				}).setNegativeButton("知道了", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();
	}
	
	/**
	 * 系统单选列表对话框
	 * 
	 * @param context
	 *            上下文
	 * @param IconId
	 *            标题栏图标
	 * @param title
	 *            标题
	 * @param str
	 *            列表内容
	 * @param listener
	 *            回调
	 */
	public void creadDialog(Context context, int IconId, String title,
			String[] str, OnClickListener listener) {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		// 设置图片
		builder.setIcon(IconId);
		// 设置标题
		builder.setTitle(title);
		// 设置单选
		builder.setSingleChoiceItems(str, -1, listener);
		// 显示
		builder.show();

	}


}
