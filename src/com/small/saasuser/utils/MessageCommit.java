package com.small.saasuser.utils;

public class MessageCommit {
	
	/**
     * Data : null
     * ErrCode : 1
     * ErrMsg : 提交成功
     */
	
	private Object Data;
    private  int ErrCode;
    private   String ErrMsg;

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    public   int getErrCode() {
        return ErrCode;
    }

    public void setErrCode(int ErrCode) {
        this.ErrCode = ErrCode;
    }

    public   String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String ErrMsg) {
        this.ErrMsg = ErrMsg;
    }
	

}
