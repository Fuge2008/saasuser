package com.small.saasuser.fragment;

import java.util.ArrayList;
import java.util.List;

import com.small.saasuser.activity.R;
import com.small.saasuser.view.NoScrollViewPager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class HomePageFragment extends BaseFragment implements OnCheckedChangeListener {

	private RadioGroup radioGroup_travel;
	private RadioButton rb_travel_want, rb_travel_now, rb_travel_plan, rb_travel_records;
	private NoScrollViewPager vp_order;
	List<Fragment> list = null;

	@Override
	protected View initView() {
		View view = View.inflate(mActivity, R.layout.fragment_home_page, null);
		vp_order = (NoScrollViewPager) view.findViewById(R.id.vp_order);
		radioGroup_travel = (RadioGroup) view.findViewById(R.id.radioGroup_travel);
		rb_travel_want = (RadioButton) view.findViewById(R.id.rb_travel_want);
		rb_travel_now = (RadioButton) view.findViewById(R.id.rb_travel_now);
		rb_travel_plan = (RadioButton) view.findViewById(R.id.rb_travel_plan);
		rb_travel_records = (RadioButton) view.findViewById(R.id.rb_travel_records);

		list = new ArrayList<Fragment>();
		TravelWantFragment want = new TravelWantFragment();
		TravelNowFragment now = new TravelNowFragment();
		TravelPlanFragment plan = new TravelPlanFragment();
		TravelRecordsFragment records = new TravelRecordsFragment();
		list.add(want);
		list.add(now);
		list.add(plan);
		list.add(records);

		TravelAdapter adapter = new TravelAdapter(getFragmentManager(), list);
		vp_order.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		radioGroup_travel.setOnCheckedChangeListener(this);
		rb_travel_want.setChecked(true);

		// // 滑动切换
		// vp_order.addOnPageChangeListener(new OnPageChangeListener() {
		// @Override
		// public void onPageSelected(int arg0) {
		// switch (arg0) {
		// case 0:
		// rb_travel_want.setChecked(true);
		// break;
		// case 1:
		// rb_travel_now.setChecked(true);
		// break;
		// case 2:
		// rb_travel_plan.setChecked(true);
		// break;
		// case 3:
		// rb_travel_records.setChecked(true);
		// break;
		//
		// }
		//
		// }
		//
		// @Override
		// public void onPageScrolled(int arg0, float arg1, int arg2) {
		//
		// }
		//
		// @Override
		// public void onPageScrollStateChanged(int arg0) {
		//
		// }
		// });

		return view;
	}

	class TravelAdapter extends FragmentStatePagerAdapter {

		List<Fragment> list;

		public TravelAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			this.list = list;
		}

		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}

		@Override
		public int getCount() {

			return list.size();
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup arg0, int checkedId) {
		if (checkedId == rb_travel_want.getId()) {
			vp_order.setCurrentItem(0);
		} else if (checkedId == rb_travel_now.getId()) {
			vp_order.setCurrentItem(1);
		} else if (checkedId == rb_travel_plan.getId()) {
			vp_order.setCurrentItem(2);
		} else if (checkedId == rb_travel_records.getId()) {
			vp_order.setCurrentItem(3);
		}
	}

}
