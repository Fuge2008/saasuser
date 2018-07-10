package com.saas.chat;

public interface Constants {

	// 聊天
	public static final String NEW_FRIENDS_USERNAME = "item_new_friends";
	String isFriend = "isFriend";
	String LoginState = "LoginState";
	String UserInfo = "UserInfo";
	String URL = "URL";
	String Title = "Title";
	String ID = "id";
	String NAME = "NAME";
	String AccessToken = "AccessToken";
	String PWD = "PWD";
	String ContactMsg = "ContactMsg";
	String VersionInfo = "VersionInfo";
	String SeeMe = "SeeMe";
	String LokeMe = "LokeMe";
	String IsMsg = "IsMsg";
	String IsVideo = "IsVideo";
	String IsZhen = "IsZhen";
	String User_ID = "User_ID";
	String GROUP_ID = "GROUP_ID";
	String TYPE = "TYPE";
	// JSON status
	String Info = "info";
	String Value = "data";
	String Result = "status";
	String DB_NAME = "WeChat.db";
	String NET_ERROR = "网络错误，请稍后再试！";
	String BaiduPullKey = "Uvw5AMP15i9v1cUoS5aY7GR1";

	// 主机地址
	// public static String IP = "http://wechatjuns.sinaapp.com/";
	// String MAIN_ENGINE = "http://10.16.16.79/wechat/index.php/mobile/";
	//String MAIN_ENGINE = "http://wechatjuns.sinaapp.com/index.php/";

	// 发送验证码 codeType 1注册 2修改密码
	String SendCodeURL = "";
	// 用户注册
	// String RegistURL = MAIN_ENGINE + "user/regigter";
	String RegistURL = "http://192.168.1.103:8080/webeasy/_samples/app/json/register.json";
	// 用户登录
	// String Login_URL = MAIN_ENGINE + "user/login";
	String Login_URL = "http://192.168.1.103:8080/webeasy/_samples/app/json/login.json";
	// 更新用户信息
	// String UpdateInfoURL = MAIN_ENGINE + "user/update_userinfo";
	String UpdateInfoURL = "http://192.168.1.103:8080/webeasy/_samples/app/json/updateUserinfo.json";
	// 获取用户信息
	//String getUserInfoURL = MAIN_ENGINE + "user/get_user_list";
	String getUserInfoURL_All = "http://192.168.1.103:8080/webeasy/_samples/app/json/getUserAllList.json";
	String getUserInfoURL_id_pho = "http://192.168.1.103:8080/webeasy/_samples/app/json/getUserList.json";
	String getUserInfoURL_pho = "http://192.168.1.103:8080/webeasy/_samples/app/json/getUserList.json";
	// 检查版本
	//String getVersionURL = MAIN_ENGINE + "020";
	// 更新用户信息
	//String updateUserInfoURL = MAIN_ENGINE + "004";
	// 我的群组
	// String getGroupListURL = MAIN_ENGINE + "group/get_group_list";
	String getGroupListURL = "http://192.168.1.103:8080/webeasy/_samples/app/json/getGrouAllList.json";
	// 反馈
	//String FeedbackURL = MAIN_ENGINE + "019";
	// 获取群组信息
	//String getGroupInfoURL = MAIN_ENGINE + "027";

	// 查询所有好友033
	// String getContactFriendURL = MAIN_ENGINE + "user/get_contact_list";
	String getContactFriendURL = "http://192.168.1.103:8080/webeasy/_samples/app/json/getContactList.json";
	// 举报018
	//String JuBaoURL = MAIN_ENGINE + "018";
	// 加入群组
	//String addGroupURL = MAIN_ENGINE + "031";
	// 退出群组
	//String exitGroupURL = MAIN_ENGINE + "029";
	// 新建群组
	// String newGroupURL = MAIN_ENGINE + "group/add_group";
	String newGroupURL = "http://192.168.1.103:8080/webeasy/_samples/app/json/addGroup.json";
}
