package com.saas.chat.adpter;

import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.small.saasuser.activity.R;
import com.saas.chat.bean.User;
import com.saas.chat.common.PingYinUtil;
import com.saas.chat.common.PinyinComparator;
import com.saas.chat.common.ViewHolder;

public class ContactAdapter extends BaseAdapter implements SectionIndexer {
	private Context mContext;
	private List<User> UserInfos;// 好友信息

	public ContactAdapter(Context mContext, List<User> UserInfos) {
		this.mContext = mContext;
		this.UserInfos = UserInfos;
		// 排序(实现了中英文混排)
		Collections.sort(UserInfos, new PinyinComparator());
	}

	@Override
	public int getCount() {
		return UserInfos.size();
	}

	@Override
	public Object getItem(int position) {
		return UserInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		User user = UserInfos.get(position);
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.em_contact_item, null);

		}
		ImageView ivAvatar = ViewHolder.get(convertView,
				R.id.contactitem_avatar_iv);
		TextView tvCatalog = ViewHolder.get(convertView,
				R.id.contactitem_catalog);
		TextView tvNick = ViewHolder.get(convertView, R.id.contactitem_nick);
		String catalog = "";
		if (TextUtils.isEmpty(user.getUserName()))
			catalog = "#";
		else
			catalog = PingYinUtil.converterToFirstSpell(user.getUserName())
					.substring(0, 1);
		if (position == 0) {
			tvCatalog.setVisibility(View.VISIBLE);
			tvCatalog.setText(catalog);
		} else {
			User Nextuser = UserInfos.get(position - 1);
			String lastCatalog = "";
			if (TextUtils.isEmpty(Nextuser.getUserName()))
				lastCatalog = "#";
			else
				lastCatalog = PingYinUtil.converterToFirstSpell(
						Nextuser.getUserName()).substring(0, 1);
			if (catalog.equals(lastCatalog)) {
				tvCatalog.setVisibility(View.GONE);
			} else {
				tvCatalog.setVisibility(View.VISIBLE);
				tvCatalog.setText(catalog);
			}
		}

		ivAvatar.setImageResource(R.drawable.em_head);
		tvNick.setText(user.getUserName());
		return convertView;
	}

	@Override
	public int getPositionForSection(int section) {
		for (int i = 0; i < UserInfos.size(); i++) {
			User user = UserInfos.get(i);
			String catalog = "";
			if (TextUtils.isEmpty(user.getUserName()))
				catalog = "#";
			else
				catalog = PingYinUtil.converterToFirstSpell(user.getUserName())
						.substring(0, 1);
			char firstChar = catalog.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		return 0;
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}
