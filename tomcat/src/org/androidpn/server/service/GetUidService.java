package org.androidpn.server.service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.androidpn.server.model.User;
import org.androidpn.server.service.datatypes.GetUidRequest;
import com.push.datatype.GetUidResp.getUidResp;;

public class GetUidService extends HttpServlet 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2367668275834015946L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		doPost(req, res);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException
	{
		byte[] requestData =  getRequestBodyBytes(req);
		GetUidRequest request = new GetUidRequest(requestData);
		String email = request.getEmail();
		String password = request.getPassword();
		UserService userService = ServiceLocator.getUserService();
		User user = userService.getUserByEmail(email);
		getUidResp.Builder builder = getUidResp.newBuilder();
		OutputStream os = null;

		if(user != null && user.getPassword().equals(password))
		{
			builder.setStatus("0");
			builder.setUid(user.getUsername());
			
		}
		else
		{
			builder.setStatus("-1");
			builder.setUid("");
		}
		
		os = res.getOutputStream();
		os.write(builder.build().toByteArray());
		
	}
	
    /**
     * @param req
     * @return request entity from HTTP request
     */
    private byte[] getRequestBodyBytes(HttpServletRequest req)
    {
    	BufferedInputStream in = null;
		byte[] requestBodyBytes = null;
		InputStream input = null;
		try{
			int length = req.getContentLength();
			if (length == -1)
			{
				return null;
			}
			input =  req.getInputStream();
			in = new BufferedInputStream(input);
			requestBodyBytes = new byte[length];
			if(length != in.read(requestBodyBytes, 0, length))
			{
				return null;
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (in != null)
				{
					in.close();
				}
				if (input != null)
				{
					input.close();
				}
			}
			catch (IOException ie)
			{
				ie.printStackTrace();
			}
		}
		
		return requestBodyBytes;
    }

}
