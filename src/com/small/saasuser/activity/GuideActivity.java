package com.small.saasuser.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.small.saasuser.adapter.ViewPagerAdapter;
import com.small.saasuser.utils.SharePreferenceUtils;

public class GuideActivity extends BaseActivity {
	private android.support.v4.view.ViewPager ViewPager;
	private ViewPagerAdapter mViewPagerAdapter;
	// 引导图片资源

	private List<View> pageList;
	private Bitmap b1;
	private Bitmap b2;
	private Bitmap b3;
	private Bitmap b4;
	private SharePreferenceUtils sharedPreferences;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setContentView(R.layout.guide);
		sharedPreferences=SharePreferenceUtils.getIntance();
		if(sharedPreferences.getString(GuideActivity.this, "isfristlogin", "false").equals("ture")){
			Intent intent= new Intent(GuideActivity.this,UserLoginActivity.class);
			startActivity(intent);
			this.finish();
		}
		initView();
		setListener();
		getdata();
	}

	private void getdata() {
		// TODO Auto-generated method stub

	}

	private void setListener() {
		// TODO Auto-generated method stub
		initViewPager();
		mViewPagerAdapter= new ViewPagerAdapter(this,pageList);
		ViewPager.setAdapter(mViewPagerAdapter);
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		ViewPager = (android.support.v4.view.ViewPager) findViewById(R.id.viewpager);
	}

	private void initViewPager() {
		pageList = new ArrayList<View>();

		ImageView image1 = new ImageView(this);
		image1.setScaleType(ScaleType.FIT_XY);
		// b1 = FileUtil.compressBitmapTest(GuidanceActivity.this,
		// R.drawable.start_img1, wm);
		b1 = BitmapFactory
				.decodeResource(getResources(),R.drawable.a);
		image1.setImageBitmap(b1);

		ImageView image2 = new ImageView(this);
		image2.setScaleType(ScaleType.FIT_XY);
		// b2 = FileUtil.compressBitmapTest(GuidanceActivity.this,
		// R.drawable.start_img2, wm);
		b2 = BitmapFactory
				.decodeResource(getResources(), R.drawable.b);
		image2.setImageBitmap(b2);
		View page3 = View.inflate(this, R.layout.start_3, null);
		ImageView guidance_page3_bg = (ImageView) page3
				.findViewById(R.id.guidance_page3_bg);
		guidance_page3_bg.setScaleType(ScaleType.FIT_XY);
		// b3 = FileUtil.compressBitmapTest(GuidanceActivity.this,
		// R.drawable.start_img3, wm);
		b4 = BitmapFactory
				.decodeResource(getResources(),R.drawable.c);
		guidance_page3_bg.setImageBitmap(b4);

		Button btn = (Button) page3.findViewById(R.id.start_btn);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sharedPreferences.putString(GuideActivity.this, "isfristlogin", "ture");
				Intent intent= new Intent(GuideActivity.this,UserLoginActivity.class);
				startActivity(intent);
				GuideActivity.this.finish();
			}
		});
		pageList.add(image1);
		pageList.add(image2);
		pageList.add(page3);
	}
}
