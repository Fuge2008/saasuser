package com.small.saasuser.global;

public class HttpConstant {
	/**
	 * 测试接口
	 */
	// Tomcat服务器
	// public static String HISTORY_RECORED =
	// "http://192.168.1.59:4001/Order/GetOrderListByLoginName/?loginName=13612858466";
	// public static String COMMIT_ORDER =
	// "http://192.168.1.59:4001/Order/SendOrderInfo/";
	 public static String TOM_URL = "http://192.168.1.103:8080/webeasy/_samples/app/zhbj/";
	 public static String SEARCH_RECORED="http://192.168.1.103:8080/webeasy/_samples/app/zhbj/record.json";
	 public static String SEARCH_PLAN = TOM_URL+"plan.json";
	 public static String SEARCH_NOW = TOM_URL+"now.json";
	// 另外一個TomCat
	// public static String TOM_URL =
	// "http://192.168.155.1:8080/webeasy/_samples/app/zhbj/";
	// public static String SEARCH_RECORED
	// ="http://192.168.155.1:8080/webeasy/_samples/app/zhbj/record.json";
	// public static String SEARCH_PLAN = TOM_URL+"plan.json";
	// public static String SEARCH_NOW = TOM_URL+"now.json";

	/**
	 * 正式接口
	 */

	// base URL
	public static String BASEURL = "http://192.168.1.126:4002/";

	/**
	 * ************ 订单模块***************
	 */

	// 提交订单
	public static String USER_ROUTE_REPLY = BASEURL + "Order/SendOrderInfo/";
	// 未来计划
	//public static String SEARCH_PLAN = BASEURL + "Order/GetFuturePlansOrderByLoginName/";
	// 历史订单
	//public static String SEARCH_RECORED = BASEURL + "Order/GetOrderListByLoginName/";
	// 当前订单
	//public static String SEARCH_NOW = BASEURL + "Order/GetUnderWayOrder/";
	// 取消订单
	public static String CANCEL_ORDER = BASEURL + "Order/CancelOrder/";
	// 获取账单
	// public static String GET_BILL =BASEURL+"Bill/GetBillListByLoginName/";
	// 加载订单可选项
	public static String LOAD_ORDER_OPTIONS = BASEURL + "Order/LoadOrderOptionalOptions/";
	// 用户确认行程回复超时
	// public static String USER_ROUTE_REPLY
	// =BASEURL+"EStaffUseInfoCell/Overtime/";
	/** ================== 登录注册的URL接口 ================== */
	/** 登录*/
    public final static String USER_LOGIN=BASEURL+"/User/GetLoginResult/";
	/** 修改密码的验证码*/
    public final static String USER_LOGIN_VERIFICATION=BASEURL+"/User/GetForgetCode/";
	/** 注册的验证码*/
    public final static String USER_RESIGITER_VERIFICATION=BASEURL+"/User/GetRegisterCode/";
    /** 修改密码*/
    public final static String USER_CHANGE_PASSWORD=BASEURL+"/User/UpdatePassword/";
    /** 注册用户*/
    public final static String USER_RESIGITER=BASEURL+"/User/Registered/";
	/** ================== 用户中心 ================== */
	/** 获取用户个人信息的*/
    public final static String USER_CENTER=BASEURL+"/User/GetUserModelByLoginName/";

}
