package com.sgroup.skara.util;

import com.sgroup.skara.model.UserOption;

public class StrUtil {
   
	/**
	 *  Check String is null or empty
	 * @param check
	 * @return true if string empty,null
	 */
	public static boolean isNullString(String check){
		if(check == null || check.length() <= 0 ) return true;
		return false;
	}
	public static String buildPath(UserOption userOption){
		String builder  = String.valueOf(Constant.DEFAULT_DEVICE);
		if(userOption ==  null) userOption = new UserOption();
		builder  = String.format("%s_%s",UserOption.LIST_DEVICE[userOption.getDevice()],UserOption.LIST_LANGUAGE[userOption.getLanguage()]);
		return builder;
	}
   public static String createKey(UserOption userOption,int a){
	   if(userOption == null ) userOption  = new UserOption();
	   if(a < 1 ) a = 1;
	   int lang  = userOption.getLanguage();
	   int dev   = userOption.getDevice();
	   String result  = String.format("%d-%d--%d", lang,dev,a);
	   return result;
	   
	   
   }
   public static String nvl(final String str) {
       return str != null ? str : "";
   }
}
