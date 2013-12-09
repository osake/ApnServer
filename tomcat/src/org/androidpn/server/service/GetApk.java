package org.androidpn.server.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetApk  extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8504584791351203597L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
		doPost(req, res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException 
	{
		OutputStream os = null;
		InputStream is = null;
		try{
			
			String fileDirectory = Thread.currentThread().getContextClassLoader().getResource("/").toString();
			String fileName = getAPKFilename(fileDirectory.toString());
			if(!fileName.equals(""))
			{
				String header = "attachment;filename=" + fileName;
				res.addHeader("Content-Disposition", header);
				String pluginUri = fileDirectory + fileName;
				os = new BufferedOutputStream(res.getOutputStream());
				URI uri;
				uri = new URI(pluginUri);
				String path = uri.getPath();
				is = new BufferedInputStream(new FileInputStream(path));
				System.out.println(pluginUri);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				
				byte[] buffer = new byte[4 * 1024];
				int read = 0;
				
				while ((read = is.read(buffer)) != -1)
				{
					baos.write(buffer, 0, read);
				}
				
				os.write(baos.toByteArray());
			}
	
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if(os != null)
			{
				os.close();
			}
			if(is != null)
			{
				is.close();
			}
		}
	}
	
	private String getAPKFilename(String path) throws IOException, URISyntaxException 
	{
		URI uri = new URI(path);
		File file = new File(uri);
		File[] files = file.listFiles();
		String fileName = "";
		for (File fl : files) 
		{
			if(fl.getPath().contains(".apk"))
			{
				fileName =  fl.getName();
				break;
			}
		}
		return fileName;
	}

}
