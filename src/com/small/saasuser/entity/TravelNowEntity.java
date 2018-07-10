package com.small.saasuser.entity;

import java.util.List;

public class TravelNowEntity {
	public int ErrCode;
	public String ErrMsg;
	public List<Data> Data;

	public static class Data {
		// public int ID;// 订单编号
		// public int UseVehicleMode;
		// public String Origin;
		// public String Destination;
		// public String UseVehicleTime;
		// public int UseVehicleType;
		// public int UseVehicleLevel;
		// public int UseVehicleSeatingCapacity;
		// public int SettlementType;
		// public int CustomCycleLength;
		// public String OtherCommand;
		// public int OrderMode;
		// public int TotalPersonNum;

		public int ID;
		public String Origin;
		public String OriginLongitude;
		public String OriginLatitude;
		public String Destination;
		public String DestinationLongitude;
		public String DestinationLatitude;
		public String RemindTime;
		public String CurrentLocationName;
		public String CurrentLocationLat;
		public String CurrentLocationLon;
		public String DriverName;
		public String CarLicenseNum;
		public String TelNumber;
		public String DriverPhotoPath;
		public String CarType;
		public String CarColor;
		public String StarLevel;

		@Override
		public String toString() {
			return "Data [ID=" + ID + ", Origin=" + Origin + ", OriginLongitude=" + OriginLongitude
					+ ", OriginLatitude=" + OriginLatitude + ", Destination=" + Destination + ", DestinationLongitude="
					+ DestinationLongitude + ", DestinationLatitude=" + DestinationLatitude + ", RemindTime="
					+ RemindTime + ", CurrentLocationName=" + CurrentLocationName + ", CurrentLocationLat="
					+ CurrentLocationLat + ", CurrentLocationLon=" + CurrentLocationLon + ", DriverName=" + DriverName
					+ ", CarLicenseNum=" + CarLicenseNum + ", TelNumber=" + TelNumber + ", DriverPhotoPath="
					+ DriverPhotoPath + ", CarType=" + CarType + ", CarColor=" + CarColor + ", StarLevel=" + StarLevel
					+ "]";
		}

	}

	@Override
	public String toString() {
		return "TravelNowEntity [ErrCode=" + ErrCode + ", ErrMsg=" + ErrMsg + ", Data=" + Data + "]";
	}

}
