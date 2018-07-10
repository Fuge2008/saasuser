package com.small.saasuser.adapter;

import java.util.List;

import com.small.saasuser.activity.R;
import com.small.saasuser.adapter.base.BaseViewHolder;
import com.small.saasuser.adapter.base.MyBaseAdapter;
import com.small.saasuser.entity.TravelNowEntity;
import com.small.saasuser.entity.TravelNowEntity.Data;

import android.content.Context;

public class TravelNowAdapter extends MyBaseAdapter<Data> {

	public TravelNowAdapter(Context context, int resource, List<Data> list) {
		super(context, resource, list);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setConvert(BaseViewHolder viewHolder, Data data) {
		viewHolder.setTextView(R.id.tv_end, data.Destination);
		viewHolder.setTextView(R.id.tv_start, data.Origin);
		viewHolder.setTextView(R.id.tv_statue, "正在进行");//statue   数据表无此数据，暂时写死

	}

}
