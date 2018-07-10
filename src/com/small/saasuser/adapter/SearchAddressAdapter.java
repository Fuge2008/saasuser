package com.small.saasuser.adapter;

import java.util.List;

import android.content.Context;

import com.baidu.mapapi.search.core.PoiInfo;
import com.small.saasuser.activity.R;
import com.small.saasuser.adapter.base.BaseViewHolder;
import com.small.saasuser.adapter.base.MyBaseAdapter;

/**
 * 搜索地址列表适配器
 */
public class SearchAddressAdapter extends MyBaseAdapter<PoiInfo> {

	public SearchAddressAdapter(Context context, int resource, List<PoiInfo> list) {
		super(context, resource, list);
	}

	@Override
	public void setConvert(BaseViewHolder viewHolder, PoiInfo info) {
		viewHolder.setTextView(R.id.item_address_name_tv, info.name);
		viewHolder.setTextView(R.id.item_address_detail_tv, info.address);
	}

}
