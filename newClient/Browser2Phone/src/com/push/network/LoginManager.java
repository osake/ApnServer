package com.push.network;

import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;
import org.androidpn.client.NotificationIQ;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;

import com.push.listener.NotificationPacketListener;
import com.push.listener.PersistentConnectionListener;
import com.push.service.NotificationService;

import android.content.SharedPreferences;
import android.util.Log;

public class LoginManager {
	public static LoginManager loginManager;

	private static final String LOGTAG = LogUtil.makeLogTag(LoginManager.class);
	private XMPPConnection connection;
	private NotificationService notification;

	private LoginManager(NotificationService notification) 
	{
		connection = ConnectionManager.getInstance(notification.getSharedPreferences()).getConnection();
		this.notification = notification;
	}

	public static LoginManager getInstance(NotificationService notification) {
		if (loginManager == null) {
			loginManager = new LoginManager(notification);
		}
		return loginManager;
	}

	public void login() {
		SharedPreferences prefs = notification.getSharedPreferences();
		
		String username = prefs.getString(Constants.XMPP_USERNAME, "");
		String password = prefs.getString(Constants.XMPP_PASSWORD, "");

		Log.i(LOGTAG, "LoginTask.run()...");

		if (!isAuthenticated()) {
			Log.d(LOGTAG, "username=" + username);
			Log.d(LOGTAG, "password=" + password);

			try {
				connection.login(username, password, "AndroidpnClient");
				Log.d(LOGTAG, "Loggedn in successfully");
				connection.addConnectionListener(new PersistentConnectionListener(notification));
				
				// packet filter
				PacketListener packetListener = new NotificationPacketListener(this.notification);
				// packet listener
				PacketFilter packetFilter = new PacketTypeFilter(NotificationIQ.class);
				ConnectionManager.getInstance(notification.getSharedPreferences()).getConnection().addPacketListener(packetListener, packetFilter);

			} catch (XMPPException e) {
				Log.e(LOGTAG, "LoginTask.run()... xmpp error");
				Log.e(LOGTAG,
						"Failed to login to xmpp server. Caused by: "
								+ e.getMessage());
				String INVALID_CREDENTIALS_ERROR_CODE = "401";
				String errorMessage = e.getMessage();
				if (errorMessage != null
						&& errorMessage.contains(INVALID_CREDENTIALS_ERROR_CODE)) {
					//RegistManager.getInstance(notification.getSharedPreferences()).regist();
					return;
				}

			} catch (Exception e) {
				Log.e(LOGTAG, "LoginTask.run()... other error");
				Log.e(LOGTAG,
						"Failed to login to xmpp server. Caused by: "
								+ e.getMessage());
				//xmppManager.startReconnectionThread();
			}

		} else {
			Log.i(LOGTAG, "Logged in already");
			//xmppManager.runTask();
		}

	}
	
    public boolean isAuthenticated() {
        return connection != null && connection.isConnected()
                && connection.isAuthenticated();
    }

}
