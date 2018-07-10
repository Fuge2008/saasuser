
package com.small.saasuser.fragment;

import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.BaiduMap.OnMapStatusChangeListener;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.small.saasuser.activity.ConfirmOrderActivity;
import com.small.saasuser.activity.MainActivity;
import com.small.saasuser.activity.R;
import com.small.saasuser.activity.map.SaasLocationActivity;
import com.small.saasuser.activity.map.SaasLocationActivity.MyLocationListenner;
import com.small.saasuser.adapter.NearAddressAdapter;
import com.small.saasuser.adapter.SearchAddressAdapter;
import com.small.saasuser.application.MyApplication;
import com.small.saasuser.utils.LogUtil;
import com.small.saasuser.utils.StringUtils;
import com.small.saasuser.view.ClearEditText;
import com.small.saasuser.view.dialog.SweetAlertDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 下单fragment
 * 
 * @author admin
 *
 */
public class TravelWantFragment extends BaseFragment
		implements OnGetGeoCoderResultListener, OnGetPoiSearchResultListener, View.OnClickListener {
	protected static final String TAG = MainActivity.class.getSimpleName();
	private View view;
	private ListView near_address_list;
	private ListView search_address_list_view_end;
	private ListView search_address_list_view_start;
	// private TextView search_empty_tv;

	private ClearEditText ce_search_start;
	private ClearEditText ce_search_end;

	private LinearLayout ll_map;

	private ImageButton ib_home_arrow;
	private CheckBox cb_current_traffic;
	private CheckBox cb_weixingtu;

	private MapView mMapView = null;
	private BaiduMap mBaiduMap = null;

	private GeoCoder mSearch = null; // 搜索模块，也可去掉地图模块独立使用
	private MyLocationListenner myListener;
	private LocationClient mLocClient;// 定位相关
	private LocationMode mCurrentMode;
	private boolean isFirstLoc = true;// 是否首次定位
	private PoiSearch mPoiSearch = null;
	private String cityName;
	private boolean EDIT_FOCUS = false;// 判断焦点所在位置
	private boolean SEARCH_FLAG = true;// 是否在搜索选点
	private boolean NEAR_FLAG = true;// 是否在拖动选点

	private NearAddressAdapter nearAddressAdapter = null;
	private SearchAddressAdapter searchAddressAdapter = null;
	private List<PoiInfo> nearAddresses = new ArrayList<PoiInfo>();
	private List<PoiInfo> searchAddresses = new ArrayList<PoiInfo>();
	private String start_address;
	private String end_address;
	private String start_address_detail;
	private String end_address_detail;
	private LatLng startLatLng;
	private LatLng endLatLng;

	public static final int EDIT_FOCUS_START = 1;
	public static final int EDIT_FOCUS_END = 2;

	@Override
	protected View initView() {
		// MyApplication.getInstance().addActivity(this);
		LatLng defaultLatLng = new LatLng(22.555133, 114.066218);
		mMapView = new MapView(mActivity,
				new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(defaultLatLng).zoom(16).build())
						.scaleControlEnabled(true).zoomControlsEnabled(false));
		view = View.inflate(mActivity, R.layout.fragment_travel_want, null);
		initViews();
		setFocus();
		initViewsAndEvents();
		return view;
	}

	public void setFocus() {
		OnFocusChangeListener mFocusChangedListener;
		mFocusChangedListener = new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					EDIT_FOCUS = true;
					LogUtil.i(TAG, "起点输入框获得光标");
				} else {
					LogUtil.i(TAG, "终点输入框获得光标");
					EDIT_FOCUS = false;
				}
			}

		};

		ce_search_start.setOnFocusChangeListener(mFocusChangedListener);
	}

	// 初始化控件
	private void initViews() {
		near_address_list = (ListView) view.findViewById(R.id.near_address_list);
		search_address_list_view_end = (ListView) view.findViewById(R.id.search_address_list_view_end);
		search_address_list_view_start = (ListView) view.findViewById(R.id.search_address_list_view_start);
		ce_search_start = (ClearEditText) view.findViewById(R.id.ce_search_start);
		ce_search_start.setSelection(ce_search_start.getText().length());
		ce_search_end = (ClearEditText) view.findViewById(R.id.ce_search_end);
		ce_search_end.setSelection(ce_search_end.getText().length());
		ib_home_arrow = (ImageButton) view.findViewById(R.id.ib_home_arrow);
		cb_current_traffic = (CheckBox) view.findViewById(R.id.cb_current_traffic);
		cb_weixingtu = (CheckBox) view.findViewById(R.id.cb_weixingtu);
		myListener = new MyLocationListenner();
		ll_map = (LinearLayout) view.findViewById(R.id.ll_map);
		ll_map.addView(mMapView);
		mBaiduMap = mMapView.getMap();

	}

	// 初始化地图
	protected void initViewsAndEvents() {
		cityName = "深圳";// 这个可以通过定位获取
		// 隐藏比例尺和缩放图标
		mMapView.showScaleControl(false);
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

		// 初始化搜索模块，注册事件监听
		mSearch = GeoCoder.newInstance();
		// 初始化搜索模块，注册搜索事件监听
		mPoiSearch = PoiSearch.newInstance();
		mPoiSearch.setOnGetPoiSearchResultListener(this);
		mSearch.setOnGetGeoCodeResultListener(this);

		ib_home_arrow.setOnClickListener(this);
		cb_current_traffic.setOnClickListener(this);

		cb_weixingtu.setOnClickListener(this);
		// 监听地图状态，若地图发生改变，重新获取地图中心位置

		mBaiduMap.setOnMapStatusChangeListener(new OnMapStatusChangeListener() {

			@Override
			public void onMapStatusChangeStart(MapStatus arg0) {

			}

			@Override
			public void onMapStatusChangeFinish(MapStatus arg0) {
				mBaiduMap.clear();
				// 反Geo搜索，arg0则为新的中心
				mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(arg0.target));

			}

			@Override
			public void onMapStatusChange(MapStatus arg0) {

			}
		});
		if (EDIT_FOCUS) {
			ce_search_start.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					search_address_list_view_end.setVisibility(View.GONE);
					search_address_list_view_start.setVisibility(View.VISIBLE);
					Log.i("info2", "输入框信息：" + s + "----->城市:" + cityName);
					if (s == null || s.length() <= 0) {
						search_address_list_view_start.setVisibility(View.GONE);
						return;
					}

					/**
					 * 使用建议搜索服务获取建议列表
					 */

					mPoiSearch.searchInCity((new PoiCitySearchOption()).city(cityName).keyword(s.toString()).pageNum(0)
							.pageCapacity(20));

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					Log.i("info1", "输入框信息：" + s + "------->城市:" + cityName);

				}

				@Override
				public void afterTextChanged(Editable s) {
					Log.i("info3", "输入框信息：" + s + "-------->城市:" + cityName);
				}
			});

		} else {

			ce_search_end.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					search_address_list_view_start.setVisibility(View.GONE);
					search_address_list_view_end.setVisibility(View.VISIBLE);
					Log.i("info22", "输入框信息：" + s + "----->城市:" + cityName);
					if (s == null || s.length() <= 0) {
						return;
					}
					/**
					 * 使用建议搜索服务获取建议列表
					 */
					mPoiSearch.searchInCity((new PoiCitySearchOption()).city(cityName).keyword(s.toString()).pageNum(0)
							.pageCapacity(20));

				}

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					Log.i("info12", "输入框信息：" + s + "------->城市:" + cityName);

				}

				@Override
				public void afterTextChanged(Editable s) {
					Log.i("info33", "输入框信息：" + s + "-------->城市:" + cityName);
				}
			});
		}

		mCurrentMode = LocationMode.Hight_Accuracy;
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(false);

		// 定位初始化
		mLocClient = new LocationClient(mActivity);
		// 设置地图缩放级别为15
		mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(new MapStatus.Builder().zoom(15).build()));
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		// 设置是否返回定位结果 包括地址信息
		option.setIsNeedAddress(true);
		// 设置返回结果是否包含手机机头的方向
		option.setNeedDeviceDirect(true);
		option.setOpenGps(false);// 打开gpss
		option.setCoorType("bd09ll"); // 设置坐标类型 取值有3个： 返回国测局经纬度坐标系：gcj02
		// 返回百度墨卡托坐标系 ：bd09 返回百度经纬度坐标系 ：bd09ll
		option.setScanSpan(1000);// 扫描间隔 单位毫秒
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
		mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
		nearAddressAdapter = new NearAddressAdapter(mActivity, R.layout.item_near_address, nearAddresses);
		near_address_list.setAdapter(nearAddressAdapter);
		// near_address_list.setEmptyView(near_list_empty_ll);

		near_address_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PoiInfo poiInfo = nearAddresses.get(position);
				Bundle bundle = new Bundle();
				bundle.putString("Ing", poiInfo.location.longitude + "");
				bundle.putString("Iat", poiInfo.location.latitude + "");
				bundle.putString("Address", poiInfo.name);
				bundle.putString("DetailedAddress", poiInfo.address);
				// TODO 设置详细地址
				if (EDIT_FOCUS) {

					start_address_detail = poiInfo.address;
					ce_search_start.setText(poiInfo.name);
					ce_search_start.setSelection(ce_search_start.getText().length());
				} else {
					end_address_detail = poiInfo.address;
					ce_search_end.setText(poiInfo.name);
					ce_search_end.setSelection(ce_search_end.getText().length());
				}

			}
		});

		if (EDIT_FOCUS) {
			searchAddressAdapter = new SearchAddressAdapter(mActivity, R.layout.item_search_address, searchAddresses);
			if (searchAddresses != null && searchAddresses.size() > 0) {
				Log.i("info", "起点输入框获得光标,开始显示列表:" + searchAddresses.get(0));
			}
			Log.i("info", "起点输入框获得光标:" + EDIT_FOCUS);
			Log.i("info", "起点输入框获得光标,searchAddresses为空:");

			search_address_list_view_end.setVisibility(View.GONE);
			search_address_list_view_start.setVisibility(View.VISIBLE);
			search_address_list_view_start.setAdapter(searchAddressAdapter);
			search_address_list_view_end.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					PoiInfo poiInfo = searchAddresses.get(position);
					Bundle bundle = new Bundle();
					bundle.putString("Ing", poiInfo.location.longitude + "");
					bundle.putString("Iat", poiInfo.location.latitude + "");
					bundle.putString("Address", poiInfo.name);
					bundle.putString("DetailedAddress", poiInfo.address);
					ce_search_start.setText(poiInfo.name);
					ce_search_start.setSelection(ce_search_start.getText().length());

				}
			});
		} else if (EDIT_FOCUS == false) {
			searchAddressAdapter = new SearchAddressAdapter(mActivity, R.layout.item_search_address, searchAddresses);
			if (searchAddresses != null && searchAddresses.size() > 0) {

				Log.i("info", "终点输入框获得光标,开始显示列表:" + searchAddresses.get(0));
			}
			Log.i("info", "终点输入框获得光标:" + EDIT_FOCUS);
			Log.i("info", "终点输入框获得光标,searchAddresses为空:");

			search_address_list_view_start.setVisibility(View.GONE);
			search_address_list_view_end.setVisibility(View.VISIBLE);
			search_address_list_view_end.setAdapter(searchAddressAdapter);
			// search_address_list_view_end.setEmptyView(search_empty_tv);

			search_address_list_view_end.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					PoiInfo poiInfo = searchAddresses.get(position);
					Bundle bundle = new Bundle();
					bundle.putString("Ing", poiInfo.location.longitude + "");
					bundle.putString("Iat", poiInfo.location.latitude + "");
					bundle.putString("Address", poiInfo.name);
					bundle.putString("DetailedAddress", poiInfo.address);
					// TODO 设置详细地址
					end_address_detail = poiInfo.address;
					ce_search_end.setText(poiInfo.name);
					ce_search_end.setSelection(ce_search_end.getText().length());
					end_address = ce_search_end.getText().toString();
					endLatLng = new LatLng(poiInfo.location.latitude, poiInfo.location.longitude);
					if ((StringUtils.isNotEmpty(ce_search_start.getText().toString(), true))
							&& (StringUtils.isNotEmpty(ce_search_end.getText().toString(), true))) {
						Intent intent = new Intent(mActivity, ConfirmOrderActivity.class);
						intent.putExtra("end_address", end_address);
						intent.putExtra("start_address", start_address);
						intent.putExtra("start_address_detail", start_address_detail);
						intent.putExtra("end_address_detail", end_address_detail);
						intent.putExtra("startLat", startLatLng.latitude);
						intent.putExtra("startLng", startLatLng.longitude);
						intent.putExtra("endLat", endLatLng.latitude);
						intent.putExtra("endLng", endLatLng.longitude);
						startActivity(intent);

					}

				}
			});

		}
	}

	@Override
	public void onPause() {
		mMapView.onPause();
		super.onPause();
	}

	@Override
	public void onResume() {
		mMapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		mMapView.onDestroy();
		super.onDestroy();
		mSearch.destroy();
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		// mBaiduMap.setMyLocationEnabled(false);
		// MyApplication.getInstance().delActivityList(this);
		mMapView = null;
	}

	@Override
	public void onGetGeoCodeResult(GeoCodeResult arg0) {

	}

	// 通过中点的经纬度获取到周围的地点
	@Override
	public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
		if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
			Toast.makeText(mActivity, "抱歉，未能找到结果", Toast.LENGTH_LONG).show();
			return;
		}

		List<PoiInfo> list = result.getPoiList();
		if (list != null && list.size() > 0) {
			nearAddresses.clear();
			nearAddresses.addAll(list);
			nearAddressAdapter.notifyDataSetChanged();
		}
		if (EDIT_FOCUS && nearAddresses.size() > 0) {

			ce_search_start.setText(nearAddresses.get(0).name.toString());
			ce_search_start.setSelection(ce_search_start.getText().length());
			startLatLng = new LatLng(nearAddresses.get(0).location.latitude, nearAddresses.get(0).location.longitude);
			start_address = ce_search_start.getText().toString();
			start_address_detail = nearAddresses.get(0).address.toString();

		} else if (EDIT_FOCUS == false && nearAddresses.size() > 0) {
			ce_search_end.setText(nearAddresses.get(0).name.toString());
			ce_search_end.setSelection(ce_search_end.getText().length());
			endLatLng = new LatLng(nearAddresses.get(0).location.latitude, nearAddresses.get(0).location.longitude);
			end_address = ce_search_end.getText().toString();
			end_address_detail = nearAddresses.get(0).address.toString();
		}
		NEAR_FLAG = true;
	}

	/**
	 * 定位SDK监听函数
	 */

	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// mapview 销毁后不在处理新接收的位置
			if (location == null || mMapView == null) {
				return;
			}
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
			double Latitude = location.getLatitude(); // 获取经度
			double Longitude = location.getLongitude(); // 获取纬度
			String City = location.getCity();
			String CityID = location.getCityCode();
			String time = location.getTime();
			String address = location.getAddrStr();
			if (location.getCity() != null) {
				cityName = location.getCity();

			}

			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
				MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(mapStatusUpdate);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	public void onGetPoiDetailResult(PoiDetailResult arg0) {

	}

	@Override
	public void onGetPoiResult(PoiResult result) {
		if (result == null || result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
			return;
		}
		if (result.error == SearchResult.ERRORNO.NO_ERROR) {
			List<PoiInfo> list = result.getAllPoi();
			// search_ll.setVisibility(View.VISIBLE);
			if (list != null && list.size() > 0) {

				searchAddresses.clear();
				searchAddresses.addAll(list);
				searchAddressAdapter.notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_home_arrow:
			Toast.makeText(mActivity, "正在定位中...", Toast.LENGTH_SHORT).show();
			isFirstLoc = true;
			break;
		case R.id.cb_current_traffic:

			if (mBaiduMap.isTrafficEnabled()) {
				// 开启交通图
				mBaiduMap.setTrafficEnabled(false);
			} else {
				mBaiduMap.setTrafficEnabled(true);
			}
			break;
		case R.id.cb_weixingtu:

			if (mBaiduMap.getMapType() == BaiduMap.MAP_TYPE_NORMAL) {
				// 卫星地图
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
			} else if (mBaiduMap.getMapType() == BaiduMap.MAP_TYPE_SATELLITE) {
				// 普通地图
				mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
			}
			break;

		}

	}

}
