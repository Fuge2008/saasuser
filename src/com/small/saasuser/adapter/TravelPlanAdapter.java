package com.small.saasuser.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.small.saasuser.activity.R;
import com.small.saasuser.entity.PlanEntity;
import com.small.saasuser.global.HttpConstant;
import com.small.saasuser.utils.CacheUtil;
import com.small.saasuser.utils.LogUtil;
import com.small.saasuser.view.SlideView;
import com.small.saasuser.view.SlideView.OnSlideListener;
import com.small.saasuser.view.dialog.SweetAlertDialog;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class TravelPlanAdapter extends BaseAdapter implements OnSlideListener {
	private static final String TAG = "TravelPlanAdapter";

	private Context mContext;
	private LayoutInflater mInflater;

	private List<PlanEntity> List = new ArrayList<PlanEntity>();
	private SlideView mLastSlideViewWithStatusOn;

	public TravelPlanAdapter(Context context) {
		mContext = context;
		mInflater = LayoutInflater.from(mContext);
	}

	public TravelPlanAdapter(Context mContext, List<PlanEntity> list) {
		super();
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(mContext);
		List = list;
	}

	public void setList(List<PlanEntity> List) {
		this.List = List;
	}

	@Override
	public int getCount() {
		if (List == null) {
			List = new ArrayList<PlanEntity>();
		}
		return List.size();
	}

	@Override
	public Object getItem(int position) {
		return List.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		SlideView slideView = (SlideView) convertView;
		if (slideView == null) {
			View itemView = mInflater.inflate(R.layout.item_travel_plan, null);

			slideView = new SlideView(mContext);
			slideView.setContentView(itemView);

			holder = new ViewHolder(slideView);
			slideView.setOnSlideListener(this);
			slideView.setTag(holder);
		} else {
			holder = (ViewHolder) slideView.getTag();
		}
		final PlanEntity item = List.get(position);
		item.slideView = slideView;
		item.slideView.shrink();

		holder.tv_start.setText(item.tv_start);
		holder.tv_end.setText(item.tv_end);
		holder.tv_date.setText(item.tv_date);
		holder.tv_statue.setText(item.tv_statue);
		holder.deleteHolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE).setTitleText("确定要取消?")
						.setContentText("取消订单，将不会给你带来任何经济损失！").setCancelText("放弃取消").setConfirmText("确定取消")
						.showCancelButton(true).setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								// reuse previous dialog instance, keep widget
								// user state, reset them if you need
								sDialog.setTitleText("放弃取消!").setContentText("您已经放弃取消该订单").setConfirmText("返回")
										.showCancelButton(false).setCancelClickListener(null)
										.setConfirmClickListener(null).changeAlertType(SweetAlertDialog.ERROR_TYPE);

							}
						}).setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
							@Override
							public void onClick(SweetAlertDialog sDialog) {
								sDialog.setTitleText("取消成功！").setContentText("您已经成功取消该订单!").setConfirmText("返回")
										.showCancelButton(false).setCancelClickListener(null)
										.setConfirmClickListener(null).changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
								List.remove(position);
								notifyDataSetChanged();
								// Toast.makeText(mContext, "删除了" + position,
								// Toast.LENGTH_SHORT).show();
								// TODO 发送网络请求删除数据
								sendRequestToNet(); // 暂时关闭
							}

							private void sendRequestToNet() {
								RequestParams params = new RequestParams();
								// params.addBodyParameter("LoginID", "1122");//
								// 登录名
								params.addBodyParameter("orderId", item.ID + "");// 订单id
								Log.i("info", "请求数据：String.valueOf(item.ID)：" + item.ID);
								HttpUtils utils = new HttpUtils();
								utils.send(HttpMethod.POST, HttpConstant.CANCEL_ORDER, params,
										new RequestCallBack<String>() {

											// 请求成功
											@Override
											public void onSuccess(ResponseInfo<String> responseInfo) {
												Toast.makeText(mContext, "删除成功" + responseInfo.result,
														Toast.LENGTH_SHORT).show();
												LogUtil.i(TAG, "请求数据成功：" + responseInfo.result);
											}

											// 请求失败
											@Override
											public void onFailure(HttpException error, String msg) {
												LogUtil.i(TAG, "请求数据失败原因：" + msg);
												Toast.makeText(mContext, "删除失败" + msg, Toast.LENGTH_SHORT).show();
												error.printStackTrace();
											}
										});

							}

						}).show();

			}

		});

		return slideView;
	}

	private static class ViewHolder {
		public TextView tv_start;
		public TextView tv_end;
		public TextView tv_date;
		public TextView tv_statue;
		public ViewGroup deleteHolder;

		ViewHolder(View view) {
			tv_start = (TextView) view.findViewById(R.id.tv_start);
			tv_end = (TextView) view.findViewById(R.id.tv_end);
			tv_date = (TextView) view.findViewById(R.id.tv_date);
			tv_statue = (TextView) view.findViewById(R.id.tv_statue);
			deleteHolder = (ViewGroup) view.findViewById(R.id.holder);
		}
	}

	@Override
	public void onSlide(View view, int status) {
		if (mLastSlideViewWithStatusOn != null && mLastSlideViewWithStatusOn != view) {
			mLastSlideViewWithStatusOn.shrink();
		}

		if (status == SLIDE_STATUS_ON) {
			mLastSlideViewWithStatusOn = (SlideView) view;
		}
	}
}
