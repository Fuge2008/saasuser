package com.small.saasuser.entity;


/**
 * 订单可选项类实体
 * 
 * @author admin
 *
 */
public class OderOption {
	public int ErrCode;

	public String ErrMsg;

	public Data Data;

	public class Data {
		public  String OrderModeList;// 订单类型列表

		public String UseVehicleModeList;// 用车方式列表

		public String UseVehicleTypeList;// 用车类型列表

		public String UseVehicleLevelList;// 用车等级列表

		public String UseVehicleSeatingCapacityList;// 用车座位数列表

		public String SettlementTypeList;// 结算类型列表

		public String CustomCycleLengthMax;// 自定义周期时长
		
		public String getOrderModeList() {
			return OrderModeList;
		}

		public void setOrderModeList(String orderModeList) {
			OrderModeList = orderModeList;
		}

		public String getUseVehicleModeList() {
			return UseVehicleModeList;
		}

		public void setUseVehicleModeList(String useVehicleModeList) {
			UseVehicleModeList = useVehicleModeList;
		}

		public String getUseVehicleTypeList() {
			return UseVehicleTypeList;
		}

		public void setUseVehicleTypeList(String useVehicleTypeList) {
			UseVehicleTypeList = useVehicleTypeList;
		}

		public String getUseVehicleLevelList() {
			return UseVehicleLevelList;
		}

		public void setUseVehicleLevelList(String useVehicleLevelList) {
			UseVehicleLevelList = useVehicleLevelList;
		}

		public String getUseVehicleSeatingCapacityList() {
			return UseVehicleSeatingCapacityList;
		}

		public void setUseVehicleSeatingCapacityList(String useVehicleSeatingCapacityList) {
			UseVehicleSeatingCapacityList = useVehicleSeatingCapacityList;
		}

		public String getSettlementTypeList() {
			return SettlementTypeList;
		}

		public void setSettlementTypeList(String settlementTypeList) {
			SettlementTypeList = settlementTypeList;
		}

		public String getCustomCycleLengthMax() {
			return CustomCycleLengthMax;
		}

		public void setCustomCycleLengthMax(String customCycleLengthMax) {
			CustomCycleLengthMax = customCycleLengthMax;
		}

		@Override
		public String toString() {
			return "Data [OrderModeList=" + OrderModeList + ", UseVehicleModeList=" + UseVehicleModeList
					+ ", UseVehicleTypeList=" + UseVehicleTypeList + ", UseVehicleLevelList=" + UseVehicleLevelList
					+ ", UseVehicleSeatingCapacityList=" + UseVehicleSeatingCapacityList + ", SettlementTypeList="
					+ SettlementTypeList + ", CustomCycleLengthMax=" + CustomCycleLengthMax + "]";
		}
		
	}

	@Override
	public String toString() {
		return "OderOption [ErrCode=" + ErrCode + ", ErrMsg=" + ErrMsg + ", Data=" + Data + "]";
	}
	
}
