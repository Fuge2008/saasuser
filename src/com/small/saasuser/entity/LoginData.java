package com.small.saasuser.entity;

public class LoginData {

	private String userID;

	private int LoginID;

	private String Identify;

	public void setUserID(String userID){
	this.userID = userID;
	}
	public String getUserID(){
	return this.userID;
	}
	public void setLoginID(int LoginID){
	this.LoginID = LoginID;
	}
	public int getLoginID(){
	return this.LoginID;
	}
	public void setIdentify(String Identify){
	this.Identify = Identify;
	}
	public String getIdentify(){
	return this.Identify;
	}
}
