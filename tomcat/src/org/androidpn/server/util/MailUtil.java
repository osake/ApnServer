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
			String body = bundle.getString("email.body1");
			body += "http://42.96.141.125:8080/getplugin?uid=";
			body += user.getUsername();
			body += bundle.getString("email.body2");
			body += bundle.getString("email.wronglink");
			body += "http://42.96.141.125:8080/getplugin?uid=" + user.getUsername();
			email.setHtmlMsg(body);
			email.send();
			
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])
	{
		try 
		{
			ResourceBundle bundle = ResourceBundle.getBundle("strings");
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.yeah.net");
			String addr = "seekyq@qq.com";
			email.addTo(addr, "");
			email.setFrom("cloudpush@yeah.net", bundle.getString("email.setfrom"));
			email.setSubject(bundle.getString("email.subject"));
			email.setAuthentication("cloudpush","apndev123");
			email.setCharset("utf-8");
			String body = bundle.getString("email.body1");
			body += "http://42.96.141.125:8080/getplugin?uid=";
			body += "123123";
			body += bundle.getString("email.body2");
			body += bundle.getString("email.wronglink");
			body += "http://42.96.141.125:8080/getplugin?uid=" + "123123";
			email.setHtmlMsg(body);
			email.send();
			
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
}
