package com.small.saasuser.entity;

import java.util.List;

/**
 * 封装行程记录信息
 *
 */
public class RouteRecordEntity {

	public int	OrderID;//订单编号
	public String	Kilometres;//行程公里数
	public String	StartTime;//开始时间
	public String	EndTime;//结束时间
	public String	ActualEndSite;//实际结束地点
	public String	TollCharge;//路桥费
	public String	OilCharge;//油费
	public String	ParkingCharge;//停车费
	public String	OtherCharge;//其他费用
	public String	OtherChargeDescription;//其他费用描述
	public String	OccTime;//发生时间
	@Override
	public String toString() {
		return "RouteRecordEntity [OrderID=" + OrderID + ", Kilometres="
				+ Kilometres + ", StartTime=" + StartTime + ", EndTime="
				+ EndTime + ", ActualEndSite=" + ActualEndSite
				+ ", TollCharge=" + TollCharge + ", OilCharge=" + OilCharge
				+ ", ParkingCharge=" + ParkingCharge + ", OtherCharge="
				+ OtherCharge + ", OtherChargeDescription="
				+ OtherChargeDescription + ", OccTime=" + OccTime + "]";
	}

	
	

}
