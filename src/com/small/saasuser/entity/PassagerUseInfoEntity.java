package com.small.saasuser.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.style.ParagraphStyle;


public class PassagerUseInfoEntity implements Parcelable {
	private String startLocation;
	private String endLocation;
	private String passagerName;
	private String passagerPhone;
	private String startTime;
	private String money;
	private String distance;
	private String timeConsuming;

	public String getStartLocation() {
		return startLocation;
	}

	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}

	public String getEndLocation() {
		return endLocation;
	}

	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}

	public String getPassagerName() {
		return passagerName;
	}

	public void setPassagerName(String passagerName) {
		this.passagerName = passagerName;
	}

	public String getPassagerPhone() {
		return passagerPhone;
	}

	public void setPassagerPhone(String passagerPhone) {
		this.passagerPhone = passagerPhone;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getDistance() {
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getTimeConsuming() {
		return timeConsuming;
	}

	public void setTimeConsuming(String timeConsuming) {
		this.timeConsuming = timeConsuming;
	}
	
	@Override
	public String toString() {
		return "OrderInfo [startLocation=" + startLocation + ", endLocation="
				+ endLocation + ", passagerName=" + passagerName
				+ ", passagerPhone=" + passagerPhone + ", startTime="
				+ startTime + ", money=" + money + ", distance=" + distance
				+ ", timeConsuming=" + timeConsuming + "]";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(startLocation);
		dest.writeString(endLocation);
		dest.writeString(passagerName);
		dest.writeString(passagerPhone);
		dest.writeString(startTime);
		dest.writeString(distance);
		dest.writeString(timeConsuming);
		dest.writeString(money);
	}

	public static final Parcelable.Creator<PassagerUseInfoEntity> CREATOR = new Parcelable.Creator<PassagerUseInfoEntity>() {
		@Override
		public PassagerUseInfoEntity createFromParcel(Parcel source) {
			// 根据Parcel容器，创建Person对象
			PassagerUseInfoEntity order = new PassagerUseInfoEntity();
			order.setStartLocation(source.readString());
			order.setEndLocation(source.readString());
			order.setPassagerName(source.readString());
			order.setPassagerPhone(source.readString());
			order.setStartTime(source.readString());
			order.setDistance(source.readString());
			order.setTimeConsuming(source.readString());
			order.setMoney(source.readString());
			return order;
		}

		@Override
		public PassagerUseInfoEntity[] newArray(int size) {
			// 根据size确定数组
			return new PassagerUseInfoEntity[size];
		}
	};

}
