package org.androidpn.server.util;

import org.androidpn.server.model.User;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

public class MailUtil {

	public static void sendVerificationMail(User user) 
	{
		try 
		{
			HtmlEmail email = new HtmlEmail();
			email.setHostName("smtp.yeah.net");
			String addr = user.getEmail();
			email.addTo(addr, "");
			email.setFrom("cloudpush@yeah.net", "云中锦书");
			email.setSubject("欢迎使用云中锦书");
			email.setAuthentication("cloudpush","apndev123");
			email.setCharset("utf-8");
			email.setHtmlMsg("尊敬的用户，<P><P>欢迎使用云中锦书！请点击以下链接，安装浏览器版插件（目前只支持FireFox）。如果发现bug或者有使用上的问题，请回复此邮件。祝您使用愉快！<p><p><b><a href=\"http://www.baidu.com\">邮件测试内容</a></b>");
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		HtmlEmail email = new HtmlEmail();
		String addr = "seekyq@qq.com";
		email.setHostName("smtp.yeah.net");
		try {
			email.addTo(addr, "云中锦书用户");
			email.setFrom("cloudpush@yeah.net", "云中锦书");
			email.setSubject("欢迎使用云中锦书");
			email.setAuthentication("cloudpush","apndev123");
			email.setCharset("utf-8");
			email.setHtmlMsg("尊敬的用户，<P><P>欢迎使用云中锦书！请点击以下链接，安装浏览器版插件（目前只支持FireFox）。如果发现bug或者有使用上的问题，请回复此邮件。祝您使用愉快！<p><p><b><a href=\"http://www.baidu.com\">邮件测试内容</a></b>");
			email.send();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
