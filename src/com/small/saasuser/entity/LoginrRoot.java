package com.small.saasuser.entity;

public class LoginrRoot {
	private int ErrCode;

	private String ErrMsg;

	private LoginData Data;

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
	public void setData(LoginData Data){
	this.Data = Data;
	}
	public LoginData getData(){
	return this.Data;
	}

}
