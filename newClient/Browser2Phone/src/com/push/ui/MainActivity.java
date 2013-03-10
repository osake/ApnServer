package com.push.ui;

import com.push.browser2phone.R;
import com.push.service.ServiceManager;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        Log.d("MainActivity", "onCreate()...");

        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.activity_main);
        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);
        // Settings
        Button okButton = (Button) findViewById(R.id.btn_settings);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                ServiceManager.viewNotificationSettings(MainActivity.this);
            }
        });

        // Start the service
        
        ServiceManager serviceManager = new ServiceManager(this);
        serviceManager.setNotificationIcon(R.drawable.notification);
        serviceManager.startService();
    }
	   @Override
       public boolean onKeyDown(int keyCode, KeyEvent event) {
           if (keyCode == KeyEvent.KEYCODE_BACK ) { 
           }
           return super.onKeyDown(keyCode, event);
       }	
}
