package com.push.network;

import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;

import com.push.service.NotificationService;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class PersistentConnectionManager extends Thread
{
    private static final String LOGTAG = LogUtil.makeLogTag(PersistentConnectionManager.class);
    private NotificationService notification;

    private int waiting;
    private SharedPreferences prefs;
    

    public PersistentConnectionManager(NotificationService notification) {
        this.waiting = 0;
        this.notification = notification;
        this.prefs = notification.getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,Context.MODE_PRIVATE);
    }
    
    public void run() {
        try {
        	boolean isLogin = false;
        	Log.d(LOGTAG, "before the connection!");
            while (!isInterrupted() && !isLogin) {
                Log.d(LOGTAG, "Trying to reconnect in " + waiting() + " seconds");
                Thread.sleep((long) waiting() * 1000L);
                Log.d(LOGTAG, "before the connection login!");
                notification.login();
                waiting++;
                Log.d(LOGTAG, "after the connection login!");
                isLogin = prefs.getBoolean("isLogin", false);
                Log.d(LOGTAG, Boolean.toString(isLogin));
            }
        } catch (final InterruptedException e) {
//            xmppManager.getHandler().post(new Runnable() {
//                public void run() {
//                    xmppManager.getConnectionListener().reconnectionFailed(e);
//                }
//            });
        }
    }

    private int waiting() {
        if (waiting > 20) {
            return 600;
        }
        if (waiting > 13) {
            return 300;
        }
        return waiting <= 7 ? 10 : 60;
    }
}
