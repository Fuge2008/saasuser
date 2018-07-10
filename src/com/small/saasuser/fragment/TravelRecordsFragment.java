package com.small.saasuser.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.small.saasuser.activity.MainActivity;
import com.small.saasuser.activity.TravelRecordDetailActivity;
import com.small.saasuser.activity.R;
import com.small.saasuser.adapter.TravelRecordAdapter;
import com.small.saasuser.entity.TravelRecordEntity;
import com.small.saasuser.entity.TravelRecordEntity.Data;
import com.small.saasuser.global.HttpConstant;
import com.small.saasuser.utils.CacheUtil;
import com.small.saasuser.utils.LogUtil;
import com.small.saasuser.utils.SharePreferenceUtils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TravelRecordsFragment extends BaseFragment {
	protected static final String TAG = MainActivity.class.getSimpleName();
	private List<Data> record_data;
	private TravelRecordAdapter record_adapter;
	private ListView lv_record;

	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_travel_records, null);
		record_data = new ArrayList<Data>();
		lv_record = (ListView) view.findViewById(R.id.lv_travelRecords);

		String resJson;
		try {
			resJson = CacheUtil.getFileCache(mActivity, HttpConstant.SEARCH_RECORED);
			if (resJson != null) {
				processData(resJson);
			}
			sendDataToNet();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return view;
	}

	/**
	 * 封装订单数据包
	 */
	private void sendDataToNet() {
		RequestParams params = new RequestParams();
		params.addBodyParameter("LoginID", SharePreferenceUtils.getString(mActivity, "LoginID", "1122"));// 登录名
		getDataFromNet(HttpConstant.SEARCH_RECORED, params);
	}

	/**
	 * 请求网络
	 * 
	 * @param url
	 *            网络接口
	 * @param params
	 *            提交数据包
	 */
	private void getDataFromNet(String url, final RequestParams params) {
		HttpUtils utils = new HttpUtils();
		// RequestCallBack的泛型表示返回的数据类型, 在此我们只需要json的字符串文本, 所以传递String就可以
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			// 开始请求
			@Override
			public void onStart() {
			}

			// 加载请求
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if (isUploading) {
					Toast.makeText(getActivity(), "提交中..." + current + "/" + total, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "响应中..." + current + "/" + total, Toast.LENGTH_SHORT).show();
				}
			}

			// 请求成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				try {
					CacheUtil.setFileCache(mActivity, HttpConstant.SEARCH_RECORED, result);
				} catch (Exception e) {
					e.printStackTrace();
				}
				processData(result);
			}

			// 请求失败
			@Override
			public void onFailure(HttpException error, String msg) {
				LogUtil.i(TAG, "请求数据失败原因：" + msg);
				error.printStackTrace();
			}
		});

	}

	/**
	 * 解析数据
	 * 
	 * @param result
	 */
	protected void processData(String result) {
		Gson gson = new Gson();
		TravelRecordEntity reData = gson.fromJson(result, TravelRecordEntity.class);
		record_data = reData.Data;
		showData(record_data);

	}

	/**
	 * 显示数据
	 * 
	 * @param record_data
	 */
	private void showData(final List<Data> record_data) {
		record_adapter = new TravelRecordAdapter(mActivity, R.layout.item_travel_records, record_data);
		lv_record.setAdapter(record_adapter);

		lv_record.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Data data = record_data.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("car_user", data.Name); // 用司机替代
				bundle.putString("user_phone", data.PhoneNum);// 用司机号码
				bundle.putInt("order_id", data.ID);
				bundle.putString("start_address", data.Origin);
				bundle.putString("end_address", data.EndSite);
				bundle.putInt("passager_num", data.TotalPersonNum);// 乘车人数
				bundle.putString("user_car_time", data.StartTime);
				bundle.putString("make_order_time", data.OccTime);
				String NOT_STR = "暂  无";
				if (2 == data.OrderState) {
					bundle.putString("end_time", data.EndTime);
					bundle.putString("oil_cost", "¥ " + data.OilCharge);
					bundle.putString("parking_cost", "¥ " + data.ParkingCharge);
					bundle.putString("other_cost", "¥ " + data.OtherCharge);
					bundle.putString("discount", data.ActualDiscount + "折");// 折扣
					bundle.putString("total_cost", "¥ " + data.ActualCost);// 总金额
					bundle.putString("pay_statue", data.SettlementState);// 是否已经支付

				} else if (3 == data.OrderState) {
					bundle.putString("end_time", NOT_STR);
					bundle.putString("oil_cost", NOT_STR);
					bundle.putString("parking_cost", NOT_STR);
					bundle.putString("other_cost", NOT_STR);
					bundle.putString("discount", NOT_STR);// 折扣
					bundle.putString("total_cost", NOT_STR);// 总金额
					bundle.putString("pay_statue", NOT_STR);// 是否已经支付
				}

				Intent intent = new Intent(mActivity, TravelRecordDetailActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
	}

}
