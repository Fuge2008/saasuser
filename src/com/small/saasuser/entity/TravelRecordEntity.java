package com.small.saasuser.entity;

import java.util.List;

public class TravelRecordEntity {
	public int ErrCode;
	public String ErrMsg;
	public List<Data> Data;

//	@Override
//	public String toString() {
//		return "RecordBean [ErrCode=" + ErrCode + ", ErrMsg=" + ErrMsg + ", redata=" + Data + "]";
//	}
	
	public static class Data {
//		// public String recordUrl;
//		public int ID;
//		public int OrderState;
//		public int UseVehicleMode;
//		public String StartTime;
//		public String EndTime;
//		public String OccTime;
//		public String Origin;
//		public String EndSite;
//		public String TollCharge;
//		public String OilCharge;
//		public String ParkingCharge;
//		public String OtherCharge;
//		public String OtherChargeDescription;
//		public int SettlementType;
		
		
		//最新接口字段
		public int ID;
		public String StartTime;
		public String EndTime;
		public String OccTime;
		public String Origin;
		public String EndSite;
		public String TollCharge;
		public String OilCharge;
		public String ParkingCharge;
		public String OtherCharge;
		
		public int OrderState;
		public String Name;
		public String PhoneNum;
		public int TotalPersonNum;
		public String ActualDiscount;
		public String ActualCost;
		public String SettlementState;
		@Override
		public String toString() {
			return "Data [ID=" + ID + ", StartTime=" + StartTime + ", EndTime=" + EndTime + ", OccTime=" + OccTime
					+ ", Origin=" + Origin + ", EndSite=" + EndSite + ", TollCharge=" + TollCharge + ", OilCharge="
					+ OilCharge + ", ParkingCharge=" + ParkingCharge + ", OtherCharge=" + OtherCharge + ", OrderState="
					+ OrderState + ", Name=" + Name + ", PhoneNum=" + PhoneNum + ", TotalPersonNum=" + TotalPersonNum
					+ ", ActualDiscount=" + ActualDiscount + ", ActualCost=" + ActualCost + ", SettlementState="
					+ SettlementState + "]";
		}
		

//		
//		public static class vehicleDriverInfo {
//			public String OrderCode;
//			public String Name;
//			public int Sex;
//			public int Age;
//			public String DriverPhotoPath;
//			public String PLName;
//			public String PhoneNum;
//			public String PrivingLicenseType;
//			public int DrivingAge;
//			public String IsExistDrunkDrivingRecord;
//			public String CompanyInfoName;
//			public String LicenseNum;
//			public int VehicleType;
//			public String Color;
//			public String VehiclePhotoPath;
//			public int SeatingCapacity;
//			public String FuelType;
//			public String FuelCost;
//			public String GasDisplacement;
//			public int ServicedLife;
//			public int Score;
//			public int VehicleLevel;
//			public int VehicleProperty;
//			public String CarLC;
//			public String CarE;
//			public String ECarLC;
//
//			@Override
//			public String toString() {
//				return "vehicleDriverInfo [OrderCode=" + OrderCode + ", Name=" + Name + ", Sex=" + Sex + ", Age=" + Age
//						+ ", DriverPhotoPath=" + DriverPhotoPath + ", PLName=" + PLName + ", PhoneNum=" + PhoneNum
//						+ ", PrivingLicenseType=" + PrivingLicenseType + ", DrivingAge=" + DrivingAge
//						+ ", IsExistDrunkDrivingRecord=" + IsExistDrunkDrivingRecord + ", CompanyInfoName="
//						+ CompanyInfoName + ", LicenseNum=" + LicenseNum + ", VehicleType=" + VehicleType + ", Color="
//						+ Color + ", VehiclePhotoPath=" + VehiclePhotoPath + ", SeatingCapacity=" + SeatingCapacity
//						+ ", FuelType=" + FuelType + ", FuelCost=" + FuelCost + ", GasDisplacement=" + GasDisplacement
//						+ ", ServicedLife=" + ServicedLife + ", Score=" + Score + ", VehicleLevel=" + VehicleLevel
//						+ ", VehicleProperty=" + VehicleProperty + ", CarLC=" + CarLC + ", CarE=" + CarE + ", ECarLC="
//						+ ECarLC + "]";
//			}
//
//		}
//
//		@Override
//		public String toString() {
//			return "Data [ID=" + ID + ", OrderState=" + OrderState + ", UseVehicleMode=" + UseVehicleMode
//					+ ", StartTime=" + StartTime + ", EndTime=" + EndTime + ", OccTime=" + OccTime + ", Origin="
//					+ Origin + ", EndSite=" + EndSite + ", TollCharge=" + TollCharge + ", OilCharge=" + OilCharge
//					+ ", ParkingCharge=" + ParkingCharge + ", OtherCharge=" + OtherCharge + ", OtherChargeDescription="
//					+ OtherChargeDescription + ", SettlementType=" + SettlementType + "]";
		}

	@Override
	public String toString() {
		return "TravelRecordEntity [ErrCode=" + ErrCode + ", ErrMsg=" + ErrMsg + ", Data=" + Data + "]";
	}
	
//	}

}
