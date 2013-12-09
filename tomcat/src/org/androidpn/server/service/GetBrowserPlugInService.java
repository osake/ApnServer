package org.androidpn.server.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.nio.charset.Charset;

public class GetBrowserPlugInService extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1785221276353429738L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
		doPost(req, res);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
		String uid = req.getParameter("uid");

		String userAgent = req.getHeader("user-agent");
//		OutputStream os = null;
//		InputStream is = null;
		ResourceBundle bundle = ResourceBundle.getBundle("strings");
		if(userAgent.contains("Android") || userAgent.contains("iPhone") || userAgent.contains("iPad"))
		{
			res.setCharacterEncoding("UTF-8");
			res.setContentType("text/html;charset=utf-8");
			PrintWriter out = res.getWriter();
			out.print("<html>" + bundle.getString("error.mobileEdition"));
			out.println("<html>");
		}
		else if (userAgent.contains("Firefox")) 
		{
			try 
			{
//				String header = "attachment;filename=" + "push.xpi";
//				res.addHeader("Content-Disposition", header);
//				String pluginUri = Thread.currentThread().getContextClassLoader().getResource("/") + "//push.xpi";
//				os = new BufferedOutputStream(res.getOutputStream());
//				URI uri = new URI(pluginUri);
//				String path = uri.getPath();
//				is = new BufferedInputStream(new FileInputStream(path));
//				System.out.println(pluginUri);
//				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Cookie cookie = new Cookie("apn_userId", uid);
				cookie.setMaxAge(3600*24*365);
				cookie.setPath("/");
				res.addCookie(cookie);
//				byte[] buffer = new byte[4 * 1024];
//				int read = 0;
//				
//				while ((read = is.read(buffer)) != -1)
//				{
//					baos.write(buffer, 0, read);
//				}
//				
//				os.write(baos.toByteArray());
				res.sendRedirect("https://addons.mozilla.org/zh-CN/firefox/addon/tuitui-assistan/");
				
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
//				os.close();
//				is.close();
			}
		}
		else if(userAgent.contains("Chrome"))
		{
			Cookie cookie = new Cookie("apn_userId", uid);
			cookie.setMaxAge(3600*24*365);
			cookie.setPath("/");
			res.addCookie(cookie);
			res.sendRedirect("https://chrome.google.com/webstore/detail/%E6%8E%A8%E6%8E%A8%E5%8A%A9%E6%89%8B/pbocdnlmdnjjcnnkoljfakjpjifbghkd");
		}
		else
		{
			res.setCharacterEncoding("UTF-8");
			res.setContentType("text/html;charset=utf-8");
			PrintWriter out = res.getWriter();
			out.print("<html>" + bundle.getString("error.unsupportbrowser"));
			out.println("<html>");
		}
		System.out.println(userAgent);

	}

}
