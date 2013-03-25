package com.push.util;

import java.util.UUID;
import java.util.regex.Pattern;

public class XMPPUtil {
	
	public static String newRandomUUID() {
        String uuidRaw = UUID.randomUUID().toString();
        return uuidRaw.replaceAll("-", "");
    }
	
    public static boolean isValidEmail(String str)
    {
		String pattern= "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		return Pattern.matches(pattern, str);
    			
    }
    
	public static boolean IsTelephone(String str) 
	{
		String regex = "^(\\d{3,4}-)?\\d{6,8}$";
		return Pattern.matches(regex, str);
	}
	
	public static boolean IsHandset(String str)
	{
		String regex = "^[0,1]+[3,5,8]+\\d{9}$";
		return Pattern.matches(regex, str);
	}
	
	public static boolean isValidNickname(String str)
	{
		String regex = "^[a-zA-Z0-9_]+$";
		return Pattern.matches(regex, str);
	}

}
