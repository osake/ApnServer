package com.push.network.protocol;

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
	
	public GetUidRequest(String email, String password)
	{
		this.email = email;
		this.password = password;
	}
	
}
