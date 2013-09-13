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
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class LoginManager
{
	public static LoginManager loginManager;

	private static final String LOGTAG = LogUtil.makeLogTag(LoginManager.class);
	private XMPPConnection connection;
	private NotificationService notification;

	private LoginManager(NotificationService notification)
	{
		this.notification = notification;
	}

	public static LoginManager getInstance(NotificationService notification)
	{
		if (loginManager == null)
		{
			loginManager = new LoginManager(notification);
		}
		return loginManager;
	}

	public synchronized void login()
	{
		SharedPreferences prefs = notification.getSharedPreferences();

		String username = prefs.getString(Constants.XMPP_USERNAME, "");
		String password = prefs.getString(Constants.XMPP_PASSWORD, "");
		boolean isAuthenticated = isAuthenticated();
		Log.i(LOGTAG, "LoginTask.run()...");

		if (!isAuthenticated)
		{
			connection = ConnectionManager.getInstance(
					notification.getSharedPreferences()).getConnection();
			Log.d(LOGTAG, "username=" + username);
			Log.d(LOGTAG, "password=" + password);

			try
			{
				if (connection == null || !connection.isConnected())
				{
					connection.connect();
				}
				connection.login(username, password, "AndroidpnClient");
				prefs.edit().putBoolean("isLogin", true);
				prefs.edit().commit();
				Log.d(LOGTAG, "Loggedn in successfully");

				if (!isAuthenticated)
				{
					connection
							.addConnectionListener(new PersistentConnectionListener(
									notification));

					// packet filter
					PacketListener packetListener = new NotificationPacketListener(
							this.notification);
					// packet listener
					PacketFilter packetFilter = new PacketTypeFilter(
							NotificationIQ.class);
					ConnectionManager
							.getInstance(notification.getSharedPreferences())
							.getConnection()
							.addPacketListener(packetListener, packetFilter);

				}

			} catch (XMPPException e)
			{
				Log.e(LOGTAG, "LoginTask.run()... xmpp error");
				Log.e(LOGTAG,
						"Failed to login to xmpp server. Caused by: "
								+ e.getMessage());
				String INVALID_CREDENTIALS_ERROR_CODE = "401";
				String errorMessage = e.getMessage();
				Editor editor = prefs.edit();
				editor.putBoolean("isLogin", false);
				editor.commit();
				if (errorMessage != null && errorMessage.contains(INVALID_CREDENTIALS_ERROR_CODE))
				{
					return;
				}
				if (!PersistentConnectionManager.isRunning)
				{
					new PersistentConnectionManager(notification).start();
				}

			} 
			catch (Exception e)
			{
				Log.e(LOGTAG, "LoginTask.run()... other error");
				Log.e(LOGTAG,
						"Failed to login to xmpp server. Caused by: "
								+ e.getMessage());
				Editor editor = prefs.edit();
				editor.putBoolean("isLogin", false);
				editor.commit();
				connection.disconnect();
				if (!PersistentConnectionManager.isRunning)
				{
					new PersistentConnectionManager(notification).start();
				}
			}

		} else
		{
			Editor editor = prefs.edit();
			editor.putBoolean("isLogin", true);
			editor.commit();
			Log.i(LOGTAG, "Logged in already");
			// xmppManager.runTask();
		}

	}

	public boolean isAuthenticated()
	{
		return connection != null && connection.isConnected()
				&& connection.isAuthenticated();
	}

}
