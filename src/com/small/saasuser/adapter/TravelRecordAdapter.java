package com.small.saasuser.adapter;

import java.util.List;

import android.content.Context;

import com.small.saasuser.activity.R;
import com.small.saasuser.adapter.base.BaseViewHolder;
import com.small.saasuser.adapter.base.MyBaseAdapter;
import com.small.saasuser.entity.TravelRecordEntity.Data;
import com.small.saasuser.utils.LogUtil;

/**
 * 订单记录适配器
 */
public class TravelRecordAdapter extends MyBaseAdapter<Data> {

	public TravelRecordAdapter(Context context, int resource, List<Data> list) {
		super(context, resource, list);
	}

	@Override
	public void setConvert(BaseViewHolder viewHolder, Data data) {
		int order_statue = data.OrderState;
		String STATUE;
		if (2 == order_statue) {
			STATUE = "已完成";
		} else if (3 == order_statue) {
			STATUE = "已取消";
		} else {
			STATUE = "未知";
		}
		viewHolder.setTextView(R.id.tv_end, data.EndSite);
		viewHolder.setTextView(R.id.tv_date, data.EndTime);
		viewHolder.setTextView(R.id.tv_start, data.Origin);
		viewHolder.setTextView(R.id.tv_money, data.ActualCost + "¥");
		viewHolder.setTextView(R.id.tv_statue, STATUE);
		LogUtil.i("info",
				"adapter数据：" + data.EndSite + "---" + data.EndTime + "---" + data.Origin + "---" + data.ActualCost);
	}

}