package com.push.network;

import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;
import org.androidpn.client.NotificationIQProvider;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.provider.ProviderManager;

import com.push.service.NotificationService;

import android.content.SharedPreferences;
import android.util.Log;

public class ConnectionManager implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4565129231056574381L;

	private static final String LOGTAG = LogUtil
			.makeLogTag(ConnectionManager.class);

	private static final String XMPP_RESOURCE_NAME = "AndroidpnClient";

	private NotificationService.TaskSubmitter taskSubmitter;

	private NotificationService.TaskTracker taskTracker;

	private XMPPConnection connection;
	
	private SharedPreferences prefs;

	private static ConnectionManager connectionManager;

	public NotificationService.TaskSubmitter getTaskSubmitter() {
		return taskSubmitter;
	}

	public void setTaskSubmitter(NotificationService.TaskSubmitter taskSubmitter) {
		this.taskSubmitter = taskSubmitter;
	}

	public NotificationService.TaskTracker getTaskTracker() {
		return taskTracker;
	}

	public void setTaskTracker(NotificationService.TaskTracker taskTracker) {
		this.taskTracker = taskTracker;
	}

	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	public static String getLogtag() {
		return LOGTAG;
	}

	public static String getXmppResourceName() {
		return XMPP_RESOURCE_NAME;
	}

	private ConnectionManager(SharedPreferences prefs)
	{
		this.prefs = prefs;
	}

	private boolean isConnected() {
		return connection != null && connection.isConnected();
	}

	public XMPPConnection getConnection() {
		if (!isConnected()) {
			connection = this.connect();
		}
		return connection;
	}

	private XMPPConnection connect() {
		connectionManager = ConnectionManager.this;
		Log.i(LOGTAG, "ConnectTask.run()...");

		// Create the configuration for this new connection
		String host = prefs.getString(Constants.XMPP_HOST, "");
		int port = prefs.getInt(Constants.XMPP_PORT, 5222);
		ConnectionConfiguration connConfig = new ConnectionConfiguration(host, port);
		connConfig.setSecurityMode(SecurityMode.disabled);
		connConfig.setSecurityMode(SecurityMode.required);
		connConfig.setSASLAuthenticationEnabled(false);
		connConfig.setCompressionEnabled(false);

		XMPPConnection connection = new XMPPConnection(connConfig);

		try {
			// Connect to the server
			connection.connect();
			Log.i(LOGTAG, "XMPP connected successfully");

			// packet provider
			ProviderManager.getInstance().addIQProvider("notification",
					"androidpn:iq:notification", new NotificationIQProvider());

		} catch (Exception e) {
			Log.e(LOGTAG, "XMPP connection failed", e);
		}
		return connection;
	}

	// private boolean isRegistered() {
	// return connectionManager.getUsername() != null &&
	// !connectionManager.getUsername().equals("");
	// }
	//
	public static ConnectionManager getInstance(SharedPreferences prefs) {
		if (connectionManager == null) {
			connectionManager = new ConnectionManager(prefs);
		}
		return connectionManager;
	}
	
	public boolean hasAliableConnection()
	{
		return isConnected();
	}

}
