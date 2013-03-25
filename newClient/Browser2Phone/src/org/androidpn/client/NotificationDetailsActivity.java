/*
 * Copyright (C) 2010 Moduad Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.androidpn.client;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

import org.apache.http.client.ClientProtocolException;

import com.push.browser2phone.R;
import com.push.network.protocol.GetUidResponse;
import com.push.ui.LoginActivity;
import com.push.ui.MainActivity;
import com.push.util.XMPPUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for displaying the notification details view.
 * 
 */
public class NotificationDetailsActivity extends Activity {

	private static final String LOGTAG = LogUtil
			.makeLogTag(NotificationDetailsActivity.class);

	private String callbackActivityPackageName;

	private String callbackActivityClassName;
	
	private GetImageTask mGetImageTask = null;
	
	private String mUri;
	
	private Bitmap mBitmap = null;
	
	private View rootView = null;
	
	private ImageView imageView = null;

	public NotificationDetailsActivity() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences sharedPrefs = this.getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
		callbackActivityPackageName = sharedPrefs.getString(
				Constants.CALLBACK_ACTIVITY_PACKAGE_NAME, "");
		callbackActivityClassName = sharedPrefs.getString(
				Constants.CALLBACK_ACTIVITY_CLASS_NAME, "");

		Intent intent = getIntent();
		String notificationId = intent
				.getStringExtra(Constants.NOTIFICATION_ID);
		String notificationApiKey = intent
				.getStringExtra(Constants.NOTIFICATION_API_KEY);
		String notificationTitle = intent
				.getStringExtra(Constants.NOTIFICATION_TITLE);
		String notificationMessage = intent
				.getStringExtra(Constants.NOTIFICATION_MESSAGE);
		String notificationUri = intent
				.getStringExtra(Constants.NOTIFICATION_URI);
		if (notificationUri.equals("")) {
			if (XMPPUtil.IsHandset(notificationMessage)
					|| XMPPUtil.IsTelephone(notificationMessage)) {
				notificationUri = "tel:" + notificationMessage;
			} else if (notificationMessage.startsWith("http://")
					|| notificationMessage.startsWith("https://")) {
				notificationUri = notificationMessage;
			} else if (notificationMessage.startsWith("www.")) {
				notificationUri = "http://" + notificationMessage;
			}
		} else if (notificationUri.contains("http://")) {
			notificationUri = notificationUri.substring(notificationUri
					.indexOf("http://"));
		}

		if (notificationMessage.equals("") && !notificationUri.equals("")) {
			notificationMessage = getString(R.string.notification_detail_url);
		}

		Log.d(LOGTAG, "notificationId=" + notificationId);
		Log.d(LOGTAG, "notificationApiKey=" + notificationApiKey);
		Log.d(LOGTAG, "notificationTitle=" + notificationTitle);
		Log.d(LOGTAG, "notificationMessage=" + notificationMessage);
		Log.d(LOGTAG, "notificationUri=" + notificationUri);
		
		// Display display = getWindowManager().getDefaultDisplay();
		// View rootView;
		// if (display.getWidth() > display.getHeight()) {
		// rootView = null;
		// } else {
		// rootView = null;
		// }

		rootView = createView(notificationTitle, notificationMessage,
				notificationUri);
		setContentView(rootView);
	}

	private View createView(final String title, final String message,
			final String uri) {
		
		ScrollView scrollView = new ScrollView(this);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setBackgroundColor(0xffeeeeee);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		linearLayout.setPadding(5, 5, 5, 5);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT);
		linearLayout.setLayoutParams(layoutParams);

		TextView textTitle = new TextView(this);
		textTitle.setText(title);
		textTitle.setTextSize(18);
		// textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		textTitle.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
		textTitle.setTextColor(0xff000000);
		textTitle.setGravity(Gravity.CENTER);

		layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 30, 30, 0);
		textTitle.setLayoutParams(layoutParams);
		linearLayout.addView(textTitle);

		TextView textDetails = new TextView(this);
		textDetails.setText(message);
		textDetails.setTextSize(14);
		// textTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		textDetails.setTextColor(0xff333333);
		textDetails.setGravity(Gravity.CENTER);

		layoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(30, 10, 30, 20);
		textDetails.setLayoutParams(layoutParams);
		linearLayout.addView(textDetails);
		
		WebView webView = new WebView(this);
		webView.getSettings().setBuiltInZoomControls(true);  
		webView.getSettings().setUseWideViewPort(true);
		webView.loadUrl(uri);
		webView.setLongClickable(true);
		webView.setWebViewClient(new WebViewClient(){       
            public boolean shouldOverrideUrlLoading(WebView view, String url) {       
                view.loadUrl(url);       
                return true;       
            }       
});   
		linearLayout.addView(webView);
		
//		mGetImageTask = new GetImageTask();
//		this.mUri = uri;
//		mGetImageTask.execute();
//		imageView = new ImageView(this);
//		Toast.makeText(NotificationDetailsActivity.this,
//				R.string.notification_detail_download,
//				Toast.LENGTH_LONG).show();
//		int waitTime = 1;
//		while(mBitmap == null)
//		{
//			try {
//				Thread.sleep((waitTime ++) * 1000);
//				if(waitTime > 4)
//				{
//					Toast.makeText(NotificationDetailsActivity.this,
//							R.string.notification_detail_download_fail,
//							Toast.LENGTH_LONG).show();
//					break;
//				}
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}
//		}
//		imageView.setImageBitmap(mBitmap);
//		
//		linearLayout.addView(imageView);

		Button okButton = new Button(this);
		if (uri.startsWith("http:") || uri.startsWith("https:")) {
			okButton.setText(getString(R.string.notification_detail_browser));
			okButton.setWidth(240);
		} else if (uri.startsWith("tel:")) {
			okButton.setText(getString(R.string.notification_detail_call));
			okButton.setWidth(180);
		} else {
			okButton.setText(getString(R.string.notification_detail_return));
			okButton.setWidth(150);
		}

		okButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				Intent intent;
				if (uri != null
						&& uri.length() > 0
						&& (uri.startsWith("http:") || uri.startsWith("https:")
								|| uri.startsWith("tel:") || uri
									.startsWith("geo:"))) {
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				} else {
					intent = new Intent().setClassName(
							callbackActivityPackageName,
							callbackActivityClassName);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					// intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					// intent.setFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
					// intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				}

				NotificationDetailsActivity.this.startActivity(intent);
				NotificationDetailsActivity.this.finish();
			}
		});

		Button copyButton = new Button(this);
		copyButton.setText(getString(R.string.notification_detail_copy));
		copyButton.setWidth(200);

		copyButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				ClipboardManager cmb = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				cmb.setText(message);
				Toast.makeText(NotificationDetailsActivity.this,
						R.string.notification_detail_copy_successful,
						Toast.LENGTH_LONG).show();
			}
		});

		LinearLayout innerLayout = new LinearLayout(this);
		innerLayout.setGravity(Gravity.CENTER);
		innerLayout.addView(okButton);
		innerLayout.addView(copyButton);

		linearLayout.addView(innerLayout);
		scrollView.addView(linearLayout);
		return scrollView;
	}

	// protected void onPause() {
	// super.onPause();
	// finish();
	// }
	//
	// protected void onStop() {
	// super.onStop();
	// finish();
	// }
	//
	// protected void onSaveInstanceState(Bundle outState) {
	// super.onSaveInstanceState(outState);
	// }
	//
	// protected void onNewIntent(Intent intent) {
	// setIntent(intent);
	// }

	
	public class GetImageTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			
			try {
				URL myFileURL;
				InputStream is = null;
				try {
					myFileURL = new URL(mUri);
					HttpURLConnection conn = (HttpURLConnection) myFileURL.openConnection();
					conn.setConnectTimeout(6000);
					conn.setDoInput(true);
					conn.setUseCaches(false);
					is = conn.getInputStream();
					mBitmap = BitmapFactory.decodeStream(is);
				} catch (Exception e) {
					e.printStackTrace();
				}
				finally
				{
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				return true;
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mGetImageTask = null;

			if (success)
			{
				
			} else
			{
			
			}
		}

		@Override
		protected void onCancelled() 
		{
			mGetImageTask = null;
		}
	}
}
