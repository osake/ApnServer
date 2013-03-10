package org.androidpn.server.service.datatypes;

import com.google.protobuf.InvalidProtocolBufferException;
import com.push.datatype.GetUidReq.getUidReq;

public class GetUidRequest 
{
	private String email;
	private String password;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public GetUidRequest(byte[] reqData)
	{
		try 
		{
			getUidReq req = getUidReq.parseFrom(reqData);
			this.setEmail(req.getEmail());
			this.setPassword(req.getPassword());
		} catch (InvalidProtocolBufferException e) 
		{
			e.printStackTrace();
		}
	}
}
