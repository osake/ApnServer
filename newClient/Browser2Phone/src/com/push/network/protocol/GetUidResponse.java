package com.push.network.protocol;

import com.google.protobuf.InvalidProtocolBufferException;
import com.push.datatype.GetUidResp;
import com.push.datatype.GetUidResp.getUidResp;


public class GetUidResponse 
{
	private String status;
	private String uid;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public GetUidResponse(byte[] respData)
	{
		try {
			getUidResp resp = GetUidResp.getUidResp.parseFrom(respData);
			this.setStatus(resp.getStatus());
			this.setUid(resp.getUid());
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
	}
	
}
