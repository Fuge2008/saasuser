package com.small.saasuser.view.wheel;

import java.util.Calendar;

import com.small.saasuser.activity.ConfirmOrderActivity;
import com.small.saasuser.activity.R;
import com.small.saasuser.activity.R.id;
import com.small.saasuser.activity.R.layout;
import com.small.saasuser.activity.map.SaasLocationActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WheelMainActivity extends Activity {
	private LinearLayout layout = null;

	private WheelView yearWV = null;
	private WheelView monthWV = null;
	private WheelView dayWV = null;
	private WheelView hourWV = null;
	private WheelView minuteWV = null;

	private TextView time_TV = null;
	private Button reset_Btn = null;

	private Button btn_sumbit;
	private String dateStr;
	public static final String DATA_STR = "DATA_STR_CODE";

	public int year;
	public int month;

	// 滚轮上的数据，字符串数组
	String[] yearArrayString = null;
	String[] dayArrayString = null;
	String[] monthArrayString = null;
	String[] hourArrayString = null;
	String[] minuteArrayString = null;
	Calendar c = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_imitate_wheel_main);
		layout = (LinearLayout) findViewById(R.id.llayout);
		System.out.println("layout.getgetTop()-->" + layout.getTop());
		System.out.println("layout.getBottom()-->" + layout.getBottom());

		// 得到相应的数组
		yearArrayString = getYEARArray(2010, 19);
		monthArrayString = getDayArray(12);
		hourArrayString = getHMArray(24);
		minuteArrayString = getHMArray(60);

		// 获取当前系统时间
		c = Calendar.getInstance();
		initView();
	}

	public void initView() {
		time_TV = (TextView) findViewById(R.id.time_chose);
		reset_Btn = (Button) findViewById(R.id.reset_btn);
		btn_sumbit = (Button) findViewById(R.id.btn_sumbit);

		yearWV = (WheelView) findViewById(R.id.time_year);
		monthWV = (WheelView) findViewById(R.id.time_month);
		dayWV = (WheelView) findViewById(R.id.time_day);
		hourWV = (WheelView) findViewById(R.id.time_hour);
		minuteWV = (WheelView) findViewById(R.id.time_minute);

		// 设置每个滚轮的行数
		yearWV.setVisibleItems(5);
		monthWV.setVisibleItems(5);
		dayWV.setVisibleItems(5);
		hourWV.setVisibleItems(5);
		minuteWV.setVisibleItems(5);

		// 设置滚轮的标签
		yearWV.setLabel("年");
		monthWV.setLabel("月");
		dayWV.setLabel("日");
		hourWV.setLabel("时");
		minuteWV.setLabel("分");

		yearWV.setCyclic(true);
		monthWV.setCyclic(true);
		dayWV.setCyclic(true);
		hourWV.setCyclic(true);
		minuteWV.setCyclic(true);
		setData();

	}

	/**
	 * 给滚轮提供数据
	 */
	private void setData() {
		// 给滚轮提供数据
		yearWV.setAdapter(new ArrayWheelAdapter<String>(yearArrayString));
		monthWV.setAdapter(new ArrayWheelAdapter<String>(monthArrayString));
		hourWV.setAdapter(new ArrayWheelAdapter<String>(hourArrayString));
		minuteWV.setAdapter(new ArrayWheelAdapter<String>(minuteArrayString));

		yearWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				// 获取年和月
				year = Integer.parseInt(yearArrayString[yearWV.getCurrentItem()]);
				month = Integer.parseInt(monthArrayString[monthWV.getCurrentItem()]);
				// 根据年和月生成天数数组
				dayArrayString = getDayArray(getDay(year, month));
				// 给天数的滚轮设置数据
				dayWV.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
				// 防止数组越界
				if (dayWV.getCurrentItem() >= dayArrayString.length) {
					dayWV.setCurrentItem(dayArrayString.length - 1);
				}
				// 显示的时间
				showDate();
			}
		});

		// 当月变化时显示的时间
		monthWV.addChangingListener(new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				// 获取年和月
				year = Integer.parseInt(yearArrayString[yearWV.getCurrentItem()]);
				month = Integer.parseInt(monthArrayString[monthWV.getCurrentItem()]);
				// 根据年和月生成天数数组
				dayArrayString = getDayArray(getDay(year, month));
				// 给天数的滚轮设置数据
				dayWV.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
				// 防止数组越界
				if (dayWV.getCurrentItem() >= dayArrayString.length) {
					dayWV.setCurrentItem(dayArrayString.length - 1);
				}
				// 显示的时间
				showDate();
			}
		});

		// 当天变化时，显示的时间
		dayWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showDate();
			}
		});

		// 当小时变化时，显示的时间
		hourWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showDate();
			}
		});

		// 当分钟变化时，显示的时间
		minuteWV.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				showDate();
			}
		});

		// 把当前系统时间显示为滚轮默认时间
		setOriTime();

		// 点击回到系统时间
		reset_Btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 把当前系统时间显示为滚轮默认时间
				c = Calendar.getInstance();
				setOriTime();
			}
		});
		btn_sumbit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(WheelMainActivity.this, ConfirmOrderActivity.class);
				intent.putExtra(DATA_STR, dateStr);
				Log.i("info", "选择时间" + dateStr);
				setResult(RESULT_OK, intent);
				finish();// 结束之后会将结果传回MapSouSuoActivity

			}
		});

	}

	// 设定初始时间
	void setOriTime() {
		yearWV.setCurrentItem(getNumData(c.get(Calendar.YEAR) + "", yearArrayString));
		monthWV.setCurrentItem(getNumData(c.get(Calendar.MONTH) + 1 + "", monthArrayString) + 0);
		hourWV.setCurrentItem(getNumData(c.get(Calendar.HOUR_OF_DAY) + "", hourArrayString));
		minuteWV.setCurrentItem(getNumData(c.get(Calendar.MINUTE) + "", minuteArrayString));

		dayArrayString = getDayArray(getDay(year, month));
		dayWV.setAdapter(new ArrayWheelAdapter<String>(dayArrayString));
		dayWV.setCurrentItem(getNumData(c.get(Calendar.DAY_OF_MONTH) + "", dayArrayString));

		// 初始化显示的时间
		showDate();
	}

	// 显示时间
	void showDate() {
		createDate(yearArrayString[yearWV.getCurrentItem()], monthArrayString[monthWV.getCurrentItem()],
				dayArrayString[dayWV.getCurrentItem()], hourArrayString[hourWV.getCurrentItem()],
				minuteArrayString[minuteWV.getCurrentItem()]);
	}

	// 生成时间
	void createDate(String year, String month, String day, String hour, String minute) {
		dateStr = year + "年" + month + "月" + day + "日" + hour + "时" + minute + "分";
		time_TV.setText("选择时间为：" + dateStr);
	}

	// 在数组Array[]中找出字符串s的位置
	int getNumData(String s, String[] Array) {
		int num = 0;
		for (int i = 0; i < Array.length; i++) {
			if (s.equals(Array[i])) {
				num = i;
				break;
			}
		}
		return num;
	}

	// 根据当前年份和月份判断这个月的天数
	public int getDay(int year, int month) {
		int day;
		if (year % 4 == 0 && year % 100 != 0) { // 闰年
			if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
				day = 31;
			} else if (month == 2) {
				day = 29;
			} else {
				day = 30;
			}
		} else { // 平年
			if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
				day = 31;
			} else if (month == 2) {
				day = 28;
			} else {
				day = 30;
			}
		}
		return day;
	}

	// 根据数字生成一个字符串数组
	public String[] getDayArray(int day) {
		String[] dayArr = new String[day];
		for (int i = 0; i < day; i++) {
			dayArr[i] = i + 1 + "";
		}
		return dayArr;
	}

	// 根据数字生成一个字符串数组
	public String[] getHMArray(int day) {
		String[] dayArr = new String[day];
		for (int i = 0; i < day; i++) {
			dayArr[i] = i + "";
		}
		return dayArr;
	}

	// 根据初始值start和step得到一个字符数组，自start起至start+step-1
	public String[] getYEARArray(int start, int step) {
		String[] dayArr = new String[step];
		for (int i = 0; i < step; i++) {
			dayArr[i] = start + i + "";
		}
		return dayArr;
	}

	/**
	 * 标题栏隐藏 在Activity.setCurrentView()之前调用此方法
	 */
	private void HideTitle() {
		// TODO TODO TODO TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	/**
	 * 隐藏状态栏（全屏） 在Activity.setCurrentView()之前调用此方法
	 */
	private void HideStatusBar() {
		// TODO TODO TODO TODO Auto-generated method stub
		// 隐藏标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 定义全屏参数
		int flag = WindowManager.LayoutParams.FLAG_FULLSCREEN;
		// 获得窗口对象
		Window myWindow = this.getWindow();
		// 设置 Flag 标识
		myWindow.setFlags(flag, flag);
	}

}