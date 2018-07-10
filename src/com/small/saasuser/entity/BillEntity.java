package com.small.saasuser.entity;

import java.util.List;

/**
 * 封装账单信息
 *
 */
public class BillEntity {
	public int ErrCode;
	public String ErrMsg;
	public List<Data> Data;

	public static class Data {
		public String ID;
		public String BillType;
		public String OrderID;
		public String SettlementType;
		public String SettlementTime;
		public String Cost;
		public String Discount;
		public String OccTime;
		public String SettlementState;
	}
}
