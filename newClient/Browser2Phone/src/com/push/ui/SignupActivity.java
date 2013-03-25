package com.push.ui;

import java.lang.ref.WeakReference;
import java.util.Properties;

import org.androidpn.client.Constants;
import org.androidpn.client.LogUtil;

import com.push.browser2phone.R;
import com.push.network.RegistManager;
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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
public class SignupActivity extends Activity {

	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserSignUpTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	//private String mNickname;
	private String mPassword;
	private String mRePassword;

	// UI references.
	private EditText mEmailView;
	//private EditText mNicknameView;
	private EditText mPasswordView;
	private EditText mRePasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
	private SharedPreferences prefs = null;
	public RegisterHandler rHandler = null;
	
	private static final String LOGTAG = LogUtil.makeLogTag(SignupActivity.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_signup);

		prefs = getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,MODE_PRIVATE);
		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		//mNicknameView = (EditText) findViewById(R.id.nickname);
		
		mPasswordView = (EditText) findViewById(R.id.password);
		mRePasswordView = (EditText) findViewById(R.id.repassword);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptSignUp();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptSignUp();
					}
				});
		rHandler = new RegisterHandler(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_signup, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptSignUp() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		//mNickname = mNicknameView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		mRePassword = mRePasswordView.getText().toString();

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
		} else if (!mPassword.equals(mRePassword)) {
			mPasswordView.setError(getString(R.string.inconformity_password));
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
		
//		if(!XMPPUtil.isValidNickname(mNickname))
//		{
//			mNicknameView.setError(getString(R.string.error_invalid_nickname));
//			focusView = mNicknameView;
//			cancel = true;
//		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_up);
			showProgress(true);
			mAuthTask = new UserSignUpTask();
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
	public class UserSignUpTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			
			String username = XMPPUtil.newRandomUUID();
			
			Properties props = loadProperties();
			String host = props.getProperty("xmppHost");
			int port = Integer.parseInt(props.getProperty("xmppPort"));
			Editor editor = prefs.edit();
			editor.putString(Constants.XMPP_EMAIL, mEmail);
			editor.putString(Constants.XMPP_PASSWORD, mPassword);
			editor.putString(Constants.XMPP_HOST, host);
			editor.putInt(Constants.XMPP_PORT, port);
			editor.putString(Constants.XMPP_USERNAME, username);
			//editor.putString(Constants.XMPP_NICKNAME, mNickname);
			editor.commit();
			
			RegistManager.getInstance(prefs).regist(SignupActivity.this);
			
			return true;
		}

//		@Override
//		protected void onPostExecute(final Boolean success) {
//			mAuthTask = null;
//			showProgress(false);
//
//			if (success) {
//				finish();
//			} else {
//				mPasswordView
//						.setError(getString(R.string.duplicate_register));
//				mPasswordView.requestFocus();
//			}
//		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
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
    
    public static class RegisterHandler extends Handler 
    {
    	WeakReference<SignupActivity> sActivity;
    	public RegisterHandler(SignupActivity sActivity)
    	{
    		this.sActivity =  new WeakReference<SignupActivity>(sActivity);
    	}
    	@Override
    	public void handleMessage(Message msg)
    	{
    		sActivity.get().showProgress(false);
    		switch(msg.what)
    		{
    			case 2:
    				sActivity.get().mEmailView.setError(sActivity.get().getString(R.string.duplicate_register));
    				sActivity.get().mAuthTask = null;
    				break;
    			case 3:
    				SharedPreferences prefs = sActivity.get().getSharedPreferences(Constants.SHARED_PREFERENCE_NAME,MODE_PRIVATE);
    				Editor editor = prefs.edit();
    				editor.putBoolean("isRegisted", true);
    				editor.commit();
    				
    				Intent intent  = new Intent();
    				intent.setClass(sActivity.get(), MainActivity.class);
    				sActivity.get().startActivity(intent);
    				
    				if(LoginActivity.instance != null)
    				{
    					LoginActivity.instance.finish();
    				}
    				sActivity.get().finish();

    				break;
    			default:
    				sActivity.get().mEmailView.setError("Œ¥÷™¥ÌŒÛ£¨«Î…‘∫Û‘Ÿ ‘");
    				sActivity.get().mAuthTask = null;
    				break;
    				
    		}
    	}
    }
    
}
