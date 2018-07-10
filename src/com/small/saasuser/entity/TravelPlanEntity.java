package com.small.saasuser.entity;

import java.util.List;

import com.small.saasuser.view.SlideView;

/**
 * 封装未来计划信息
 */
public class TravelPlanEntity {
	public int ErrCode;
	public String ErrMsg;
	public List<Data> Data;

	@Override
	public String toString() {
		return "TravelPlanEntity [ErrCode=" + ErrCode + ", ErrMsg=" + ErrMsg + ", Data=" + Data + "]";
	}

	public static class Data {
		public int ID;
		public int UseVehicleMode;
		public String Origin;
		public String Destination;
		public String UseVehicleTime;
		// public int UseVehicleType;
		// public int UseVehicleSeatingCapacity;
		public int SettlementType;
		public int CustomCycleLength;
		public String OtherCommand;
		public String OccTime;
		public int OrderMode;
		public int TotalPersonNum;

		public int UserID;
		public String Name;
		public String PhoneNum;
		public String PredictEndTime;

		@Override
		public String toString() {
			return "Data [ID=" + ID + ", UseVehicleMode=" + UseVehicleMode + ", Origin=" + Origin + ", Destination="
					+ Destination + ", UseVehicleTime=" + UseVehicleTime + ", SettlementType=" + SettlementType
					+ ", CustomCycleLength=" + CustomCycleLength + ", OtherCommand=" + OtherCommand + ", OccTime="
					+ OccTime + ", OrderMode=" + OrderMode + ", TotalPersonNum=" + TotalPersonNum + ", UserID=" + UserID
					+ ", Name=" + Name + ", PhoneNum=" + PhoneNum + ", PredictEndTime=" + PredictEndTime + "]";
		}

	}
}
