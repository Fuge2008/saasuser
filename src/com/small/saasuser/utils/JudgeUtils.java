package com.small.saasuser.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JudgeUtils {
	
	//判断是否为手机号
	public static boolean isMobileNO(String mobiles){     
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");    
		Matcher m = p.matcher(mobiles);    
		return m.matches();
	}
	//判断是否为Email
		public static boolean isEmail(String mobiles){     
			Pattern p = Pattern.compile("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");    
			Matcher m = p.matcher(mobiles);    
			return m.matches();
		}
}
