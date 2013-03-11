package com.push.ui;

import java.io.IOException;
import java.util.Properties;
import java.util.regex.Pattern;

import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.push.browser2phone.R;
import com.push.datatype.GetUidReq;
import com.push.network.protocol.GetUidResponse;
import com.push.util.XMPPUtil;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private SharedPreferences prefs = null;
	
    private static final String LOGTAG = LogUtil.makeLogTag(LoginActivity.class);
    
    public static LoginActivity instance = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		prefs = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,MODE_PRIVATE);
		
		if(prefs.getBoolean("isRegisted", false))
		{
			Intent intent  = new Intent();
			intent.setClass(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			this.finish();
		}
		instance = this;
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		
		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		
		findViewById(R.id.register_link).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent  = new Intent();
						intent.setClass(LoginActivity.this, SignupActivity.class);
						startActivity(intent);
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_login, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		//mEmailView.setError(null);
		//mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!XMPPUtil.isValidEmail(mEmail)) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}
		
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			
			Intent intent  = new Intent();
			intent.setClass(LoginActivity.this, MainActivity.class);
			
			Properties props = loadProperties();
			String host = props.getProperty("xmppHost");
			int port = Integer.parseInt(props.getProperty("xmppPort"));
			
			
			try {
				GetUidResponse resp = getUidFromServer(mEmail, mPassword, host);
				String uid = resp.getUid();
				String status = resp.getStatus();
				if(status.equals("0"))
				{
					Editor editor = prefs.edit();
					editor.putString(Constants.XMPP_EMAIL, mEmail);
					editor.putString(Constants.XMPP_PASSWORD, mPassword);
					editor.putString(Constants.XMPP_HOST, host);
					editor.putInt(Constants.XMPP_PORT, port);
					editor.putString(Constants.XMPP_USERNAME, uid);
					editor.putBoolean("isRegisted", true);
					editor.commit();
				}
				else
				{
					return false;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			startActivity(intent);

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
//	public class UserLoginTask {
//		public void login()
//		{
////			Intent intent  = new Intent();
////			intent.setClass(LoginActivity.this, MainActivity.class);
////			prefs.edit().putString("email", mEmail);
////			prefs.edit().putString("password", mPassword);
////			
////			ProfileManager profileManager = ProfileManager.getInstance();
////			profileManager.setEmail(mEmail);
////			profileManager.setPassword(mPassword);
////			Properties props = loadProperties();
////			profileManager.setXmppHost(props.getProperty(Constants.XMPP_HOST));
////			profileManager.setXmppPort(Integer.parseInt(props.getProperty(Constants.XMPP_PORT)));
////			
////			prefs.edit().putString("email", mEmail);
////			prefs.edit().putString("password", mPassword);
////			
////			startActivity(intent);
//			Intent intent  = new Intent();
//			intent.setClass(LoginActivity.this, MainActivity.class);
//			String username = XMPPUtil.newRandomUUID();
//			
//			Properties props = loadProperties();
//			String host = props.getProperty("xmppHost");
//			int port = Integer.parseInt(props.getProperty("xmppPort"));
//			
//			Editor editor = prefs.edit();
//			editor.putString(Constants.XMPP_EMAIL, mEmail);
//			editor.putString(Constants.XMPP_PASSWORD, mPassword);
//			editor.putString(Constants.XMPP_USERNAME, username);
//			editor.putString(Constants.XMPP_HOST, host);
//			editor.putInt(Constants.XMPP_PORT, port);
//			editor.commit();
//			
//			RegistManager.getInstance(prefs).regist();
//			
//			startActivity(intent);
//		}
//	}
	
	public class UserRegistTask {
//		public void regist()
//		{
//			Intent intent  = new Intent();
//			intent.setClass(LoginActivity.this, MainActivity.class);
//			String username = XMPPUtil.newRandomUUID();
//			
//			Editor editor = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,MODE_PRIVATE).edit();
//			editor.putString("email", mEmail);
//			editor.putString("password", mPassword);
//			editor.putString("username", username);
//			editor.commit();
//			
//			
//			startActivity(intent);
//		}
	}
    
    private Properties loadProperties() {
        Properties props = new Properties();
        try {
            int id = this.getResources().getIdentifier("androidpn", "raw",
            		this.getPackageName());
            props.load(this.getResources().openRawResource(id));
        } catch (Exception e) {
            Log.e(LOGTAG, "Could not find the properties file.", e);
            // e.printStackTrace();
        }
        return props;
    }
    
	private GetUidResponse getUidFromServer(String email, String password, String host) throws ClientProtocolException, IOException
	{
		HttpClient client = new DefaultHttpClient();
		HttpPost method = new HttpPost("http://" + host + ":8080/GetUidService");
		GetUidReq.getUidReq.Builder builder = GetUidReq.getUidReq.newBuilder();
		builder.setEmail(email);
		builder.setPassword(password);
		HttpEntity entity = new ByteArrayEntity(builder.build().toByteArray());
		method.setEntity(entity);
		HttpResponse response = client.execute(method);
		byte[] result = EntityUtils.toByteArray(response.getEntity());
		return new GetUidResponse(result);
	}
    
}
