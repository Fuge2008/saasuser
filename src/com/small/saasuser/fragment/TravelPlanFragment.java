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
import com.small.saasuser.activity.MainActivity;
import com.small.saasuser.activity.R;
import com.small.saasuser.activity.TravelPlanDetailActivity;
import com.small.saasuser.entity.PlanEntity;
import com.small.saasuser.adapter.TravelPlanAdapter;
import com.small.saasuser.application.MyApplication;
import com.small.saasuser.entity.TravelPlanEntity;
import com.small.saasuser.entity.TravelPlanEntity.Data;
import com.small.saasuser.global.HttpConstant;
import com.small.saasuser.utils.CacheUtil;
import com.small.saasuser.utils.LogUtil;
import com.small.saasuser.utils.SharePreferenceUtils;
import com.small.saasuser.view.ListViewMoveDelect;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TravelPlanFragment extends BaseFragment {
	protected static final String TAG = MainActivity.class.getSimpleName();
	private ListViewMoveDelect lv_plan;
	private List<Data> plan_data;

	private ListViewMoveDelect list_plan;
	public TravelPlanAdapter plan_adapter;
	public ArrayList<PlanEntity> List;

	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_travel_plan, null);
		list_plan = (ListViewMoveDelect) view.findViewById(R.id.lv_travelPlan);
		List = new ArrayList<PlanEntity>();
		plan_data = new ArrayList<TravelPlanEntity.Data>();
		// String resJson;
		try {
			// resJson = CacheUtil.getFileCache(mActivity,
			// HttpConstant.SEARCH_PLAN);
			// if (resJson != null) {

			// processData(resJson);
			// }
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
		getDataFromNet(HttpConstant.SEARCH_PLAN, params);
	}

	private void getDataFromNet(String url, final RequestParams params) {

		HttpUtils utils = new HttpUtils();
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
					CacheUtil.setFileCache(mActivity, HttpConstant.SEARCH_PLAN, result);
				} catch (Exception e) {
					e.printStackTrace();
				}
				processData(result);
			}

			// 请求失败
			@Override
			public void onFailure(HttpException error, String msg) {
				error.printStackTrace();
			}
		});

	}

	protected void processData(String result) {
		Gson gson = new Gson();
		TravelPlanEntity reData = gson.fromJson(result, TravelPlanEntity.class);
		plan_data = reData.Data;
		LogUtil.i(TAG, "解析数据,未来计划结果" + plan_data);
		showData(plan_data);

	}

	private void showData(final List<Data> plan_data) {

		for (int i = 0; i < plan_data.size(); i++) {
			PlanEntity item = new PlanEntity();
			item.tv_start = plan_data.get(i).Origin;
			item.tv_end = plan_data.get(i).Destination;
			item.tv_date = plan_data.get(i).UseVehicleTime;
			item.ID = plan_data.get(i).ID;
			item.tv_statue = "未开始";// 暂时写死
			List.add(item);

		}
		plan_adapter = new TravelPlanAdapter(mActivity, List);
		plan_adapter.setList(List);
		list_plan.setAdapter(plan_adapter);
		list_plan.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Data data = plan_data.get(position);
				Bundle bundle = new Bundle();
				bundle.putInt("passager_id", data.UserID);
				bundle.putString("passager_name", data.Name);
				bundle.putString("passager_phone", data.PhoneNum);
				bundle.putString("start_address", data.Origin);
				bundle.putString("end_address", data.Destination);
				bundle.putInt("passager_num", data.TotalPersonNum);
				bundle.putString("user_car_time", data.UseVehicleTime);
				bundle.putString("end_time", data.PredictEndTime);
				bundle.putString("make_order_time", data.OccTime);
				bundle.putInt("pay_type", data.SettlementType);
				bundle.putString("other_describe", data.OtherCommand);
				String ORDER_MODEL;
				if (0 == data.OrderMode) {
					ORDER_MODEL = "即    时";
					bundle.putString("car_appointment", ORDER_MODEL);
				} else if (1 == data.OrderMode) {
					ORDER_MODEL = "预    约";
					bundle.putString("car_appointment", ORDER_MODEL);
				} else if (2 == data.OrderMode) {
					ORDER_MODEL = "全    部";
					bundle.putString("car_appointment", ORDER_MODEL);
				} else {
					ORDER_MODEL = "未     知";
					bundle.putString("car_appointment", ORDER_MODEL);
				}

				Intent intent = new Intent(getActivity(), TravelPlanDetailActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});
	}

}
