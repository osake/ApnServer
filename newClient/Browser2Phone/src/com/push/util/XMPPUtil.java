package com.push.util;

import android.annotation.SuppressLint;
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
	
	public static boolean isImageURL(String url)
	{
		if(url == null)
		{
			return false;
		}
		url = url.toLowerCase();
		if(url.endsWith(".bmp") || url.endsWith(".jpeg") || url.endsWith(".jpg") || url.endsWith(".gif") || url.endsWith(".png") || url.endsWith(".exif") || url.endsWith(".tiff"))
		{
			return true;
		}
		return false;
	}
	
	public static boolean isVideoURL(String url)
	{
		url = url.toLowerCase();
		
		if (url.contains("youku"))
		{
			return true;
		}
		
		return false;
	}

}
