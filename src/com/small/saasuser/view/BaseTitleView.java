package com.small.saasuser.view;

import com.small.saasuser.activity.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;



public class BaseTitleView extends RelativeLayout{

	private TextView tv_title;
	
	private ImageView iv_back;
	
	
	
	/**
	 * 初始化布局文件
	 * @param context
	 */
	private void iniView(Context context) {
		View.inflate(context,R.layout.base_title,this);
		iv_back=(ImageView) findViewById(R.id.iv_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}
	public BaseTitleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		iniView(context);
	}
	/**
	 * 带有2个参数的构造方法,布局文件的时候调用
	 * @param context
	 * @param attrs	标题内容
	 */
	public BaseTitleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		iniView(context);
		String title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.small.saasuser.activity","saasusertitle");
		setTitle(title);
	}
	public BaseTitleView(Context context) {
		super(context);
		iniView(context);
	}
	/**
	 * 设置组合控件的标题
	 */
	public void setTitle(String text){
		tv_title.setText(text);
	}
	
}
