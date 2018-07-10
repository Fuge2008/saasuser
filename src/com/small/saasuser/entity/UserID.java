package com.small.saasuser.entity;

public class UserID {
	private volatile static UserID mUserID;

	private UserID() {
	}

	public static UserID getLoginData() {
		if (mUserID == null) {
			synchronized (LoginData.class) {
				if (mUserID == null) {
					mUserID = new UserID();
				}
			}
		}
		return mUserID;
	}

	private String UserID;

	private String Identify;
	
	private int LoginID;

	public int getLoginID() {
		return LoginID;
	}

	public void setLoginID(int loginID) {
		LoginID = loginID;
	}

	public void setUserID(String UserID) {
		this.UserID = UserID;
	}

	public String getUserID() {
		return this.UserID;
	}

	public void setIdentify(String Identify) {
		this.Identify = Identify;
	}

	public String getIdentify() {
		return this.Identify;
	}
}
