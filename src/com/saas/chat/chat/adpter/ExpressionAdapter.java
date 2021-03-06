package com.saas.chat.chat.adpter;

import java.util.List;

import com.small.saasuser.activity.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;


public class ExpressionAdapter extends ArrayAdapter<String> {
	Context context;

	public ExpressionAdapter(Context context, int textViewResourceId,
			List<String> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.em_row_expression,
					null);
		}

		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.iv_expression);
		String filename = getItem(position);
		int resId = getContext().getResources().getIdentifier(filename,
				"drawable", getContext().getPackageName());
		imageView.setImageResource(resId);

		return convertView;
	}
}
