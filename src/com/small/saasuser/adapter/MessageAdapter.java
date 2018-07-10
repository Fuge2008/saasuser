package com.small.saasuser.adapter;

import java.util.List;
import java.util.Map;

import com.small.saasuser.activity.R;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {

	private List<Map<String, Object>> data;
	private LayoutInflater mInflater;
	Context context;

	public MessageAdapter(Context context, List<Map<String, Object>> data) {
		this.data = data;
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return data.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.msg_item, null);

			holder = new ViewHolder();
			holder.tv_msg_title = (TextView) convertView.findViewById(R.id.tv_msg_title);
			holder.tv_msg_content = (TextView) convertView.findViewById(R.id.tv_msg_content);
			holder.tv_msg_date = (TextView) convertView.findViewById(R.id.tv_msg_date);
			holder.iv_msg = (ImageView) convertView.findViewById(R.id.iv_msg);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();

		}

		Map<String, Object> map = data.get(position);
		String content = map.get("content").toString();
		String type = map.get("type").toString();
		String date = map.get("date").toString();
		holder.tv_msg_content.setText(content);
		holder.tv_msg_date.setText(date);

		return convertView;
	}

	class ViewHolder {

		public TextView tv_msg_title, tv_msg_content, tv_msg_date;
		public ImageView iv_msg;

	}

}
