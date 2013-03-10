package com.push.util;

import java.util.UUID;
import java.util.regex.Pattern;

public class XMPPUtil {
	
	public static String newRandomUUID() {
        String uuidRaw = UUID.randomUUID().toString();
        return uuidRaw.replaceAll("-", "");
    }
	
    public static boolean isValidEmailOrPhoneNumber(String str)
    {
    	if(!str.contains("@"))
    	{
    		if(str.length() != 11)
    		{
    			return false;
    		}
        	if(str.startsWith("13") || str.startsWith("18") || str.startsWith("15"))
        	{
        		for (int i = str.length();--i>=0;)
        		{   
        			   if (!Character.isDigit(str.charAt(i)))
        			   {
        				   return false;
        			   }
        		}
        	}
        	else
        	{
        		return false;
        	}
        	
        	return true;
    	}
    	else
    	{
    		String pattern= "^([\\w-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([\\w-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
    		return Pattern.matches(pattern, str);
    			
    	}
    }

}
