package com.small.saasuser.fragment;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.small.saasuser.activity.ConfirmOrderActivity;
import com.small.saasuser.activity.MainActivity;
import com.small.saasuser.activity.TravelRecordDetailActivity;
import com.small.saasuser.activity.R;
import com.small.saasuser.activity.TravelNowDetailActivity;
import com.small.saasuser.adapter.TravelNowAdapter;
import com.small.saasuser.entity.TravelNowEntity;
import com.small.saasuser.entity.TravelNowEntity.Data;
import com.small.saasuser.entity.TravelPlanEntity;
import com.small.saasuser.entity.TravelRecordEntity;
import com.small.saasuser.global.HttpConstant;
import com.small.saasuser.utils.CacheUtil;
import com.small.saasuser.utils.LogUtil;
import com.small.saasuser.utils.SharePreferenceUtils;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TravelNowFragment extends BaseFragment {
	protected static final String TAG = MainActivity.class.getSimpleName();

	private List<Data> now_data;
	private ListView lv_now;
	private TravelNowAdapter now_adapter;

	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_travel_now, null);
		lv_now = (ListView) view.findViewById(R.id.lv_travelNow);
		String resJson;
		try {
			resJson = CacheUtil.getFileCache(mActivity, HttpConstant.SEARCH_NOW);
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
		// TODO 发送订单----》下单
		// 封装订单数据
		RequestParams params = new RequestParams();
		params.addBodyParameter("LoginID", SharePreferenceUtils.getString(mActivity, "LoginID", "1122"));// 登录名

		getDataFromNet(HttpConstant.SEARCH_NOW, params);
	}

	private void getDataFromNet(String url, final RequestParams params) {

		HttpUtils utils = new HttpUtils();
		// RequestCallBack的泛型表示返回的数据类型, 在此我们只需要json的字符串文本, 所以传递String就可以
		utils.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
			// 开始请求
			@Override
			public void onStart() {
				LogUtil.i(TAG, "onStart()" + params.toString());
			}

			// 加载请求
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if (isUploading) {
					Toast.makeText(getActivity(), "提交中..." + current + "/" + total, Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(), "响应中..." + current + "/" + total, Toast.LENGTH_SHORT).show();
				}
				LogUtil.i(TAG, "onLoading" + params.toString());
			}

			// 请求成功
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String result = responseInfo.result;
				LogUtil.i(TAG, "请求网络，现在数据结果" + result);
				try {
					CacheUtil.setFileCache(mActivity, HttpConstant.SEARCH_NOW, result);
					LogUtil.i(TAG, "把数据写入缓存：-------");
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

	protected void processData(String result) {
		Gson gson = new Gson();
		TravelNowEntity reData = gson.fromJson(result, TravelNowEntity.class);
		now_data = reData.Data;
		showData(now_data);

	}

	private void showData(final List<Data> now_data) {
		now_adapter = new TravelNowAdapter(mActivity, R.layout.item_travel_now, now_data);
		lv_now.setAdapter(now_adapter);
		lv_now.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Data data = now_data.get(position);
				Bundle bundle = new Bundle();
				bundle.putInt("order_id", data.ID);
				bundle.putString("start_location", data.Origin);
				bundle.putString("start_lat", data.OriginLatitude);
				bundle.putString("start_lng", data.OriginLongitude);
				bundle.putString("end_loacatin", data.Destination);
				bundle.putString("end_lat", data.DestinationLatitude);
				bundle.putString("end_lng", data.DestinationLongitude);
				bundle.putString("driver_name", data.DriverName);
				bundle.putString("car_type", data.CarType);
				bundle.putString("car_color", data.CarColor);
				bundle.putString("car_plate", data.CarLicenseNum);
				bundle.putString("star_level", data.StarLevel);
				bundle.putString("driver_head", data.DriverPhotoPath);
				bundle.putString("tel_number", data.TelNumber);

				Intent intent = new Intent(getActivity(), TravelNowDetailActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
	}

}
