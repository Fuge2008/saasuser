package com.small.saasuser.entity;

public class UserCenterRoot {
	private int ErrCode;

	private String ErrMsg;

	private UserCenterData Data;

	public void setErrCode(int ErrCode){
	this.ErrCode = ErrCode;
	}
	public int getErrCode(){
	return this.ErrCode;
	}
	public void setErrMsg(String ErrMsg){
	this.ErrMsg = ErrMsg;
	}
	public String getErrMsg(){
	return this.ErrMsg;
	}
	public void setData(UserCenterData Data){
	this.Data = Data;
	}
	public UserCenterData getData(){
	return this.Data;
	}
}
