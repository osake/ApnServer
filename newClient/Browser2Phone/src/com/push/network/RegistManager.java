package com.push.network;

import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Registration;

import com.push.ui.SignupActivity;

import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;

public class RegistManager {
	
	private boolean isRegisted;
	private static final String LOGTAG = LogUtil.makeLogTag(RegistManager.class);
	private static RegistManager registManger;
	private SharedPreferences prefs;
	
	public boolean isRegisted() {
		return isRegisted;
	}

	public void setRegisted(boolean isRegisted) 
	{
		this.isRegisted = isRegisted;
	}

	private RegistManager(SharedPreferences prefs)
	{
		this.prefs = prefs;
		isRegisted = false;
	}
	
	public void regist(final SignupActivity activity) {
		
		ConnectionManager connectionManager = ConnectionManager.getInstance(prefs);
		final String username = prefs.getString(Constants.XMPP_USERNAME, "");
		final String password = prefs.getString(Constants.XMPP_PASSWORD, "");
		final String email = prefs.getString(Constants.XMPP_EMAIL, "");
		//final String nickname = prefs.getString(Constants.XMPP_NICKNAME, "");
		XMPPConnection connection = connectionManager.getConnection();
		Log.i(LOGTAG, "RegisterTask.run()...");
		if(connection.isConnected())
		{
			if (!isRegisted) {

				Registration registration = new Registration();

				PacketFilter packetFilter = new AndFilter(new PacketIDFilter(
						registration.getPacketID()), new PacketTypeFilter(IQ.class));

				PacketListener packetListener = new PacketListener() {

					public void processPacket(Packet packet) {
						Log.d("RegisterTask.PacketListener", "processPacket().....");
						Log.d("RegisterTask.PacketListener",
								"packet=" + packet.toXML());
						
						Message msg = new Message();
						if (packet instanceof IQ) {
							IQ response = (IQ) packet;
							if (response.getType() == IQ.Type.ERROR) {
								if (!response.getError().toString().contains("409")) {
									Log.e(LOGTAG,
											"Unknown error while registering XMPP account! "
													+ response.getError().getCondition());
									msg.what = 1;
									activity.rHandler.sendMessage(msg);
								}
								else
								{
									Log.e(LOGTAG,
											"Email has been registed! "
													+ response.getError().getCondition());
									msg.what = 2;
									try
									{
										activity.rHandler.sendMessage(msg);
									}
									catch(Exception e)
									{
										e.printStackTrace();
									}
									
								}
							} else if (response.getType() == IQ.Type.RESULT) {
								//Constants.condition = response.getError().getCondition().toString();
								Log.d(LOGTAG, "username=" + username);
								Log.d(LOGTAG,
										"password=" + password);
								isRegisted = true;
								Log.i(LOGTAG, "Account registered successfully");
								msg.what = 3;
								activity.rHandler.sendMessage(msg);
							}
						}
						else
						{
							msg.what = 4;
							activity.rHandler.sendMessage(msg);
						}

					}
				};
				connection.addPacketListener(packetListener, packetFilter);
				
				registration.setType(IQ.Type.SET);
				registration.addAttribute("username", username);
				registration.addAttribute("password", password);
				registration.addAttribute("email", email);
				//registration.addAttribute("name", nickname);
				connection.sendPacket(registration);

			} else {
				Log.i(LOGTAG, "Account registered already");
			}
		}

	}
	
	public static RegistManager getInstance(SharedPreferences prefs)
	{
		if(registManger == null)
		{
			registManger = new RegistManager(prefs);
		}
		return registManger;
	}

}
