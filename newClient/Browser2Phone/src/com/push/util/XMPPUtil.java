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

}
