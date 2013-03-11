package org.androidpn.server.util;

import java.util.ResourceBundle;

import org.androidpn.server.model.User;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class MailUtil {

	public static void sendVerificationMail(User user) 
	{
		try 
		{
			ResourceBundle bundle = ResourceBundle.getBundle("strings");
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.yeah.net");
			String addr = user.getEmail();
			email.addTo(addr, "");
			email.setFrom("cloudpush@yeah.net", bundle.getString("email.setfrom"));
			email.setSubject(bundle.getString("email.subject"));
			email.setAuthentication("cloudpush","apndev123");
			email.setCharset("utf-8");
			email.setHtmlMsg(bundle.getString("email.body"));
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
}
