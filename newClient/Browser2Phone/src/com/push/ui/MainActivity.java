package com.push.ui;

import com.push.browser2phone.R;
import com.push.service.ServiceManager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends Activity
{
	
	private ServiceManager serviceManager = null;
	// private ViewPager mPager;
	// private List<View> listViews;
	// private ImageView cursor;
	// private TextView main_page, history_page, setting_page;
	// private int offset = 0;
	// private int currIndex = 0;
	// private int bmpW;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		Log.d("MainActivity", "onCreate()...");

		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		// setContentView(R.layout.activity_main);
		// getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		// R.layout.title);
		// Settings
		// InitImageView();
		// InitTextView();
		// InitViewPager();

		View view = PageCreator.createHistoryPage(this);
		setContentView(view);
		
		serviceManager = new ServiceManager(this);
		serviceManager.setNotificationIcon(R.drawable.notification);
		serviceManager.startService();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
		case R.id.menu_settings:
			Intent intent = new Intent();
			intent.setClass(this, NotificationSettingsActivity.class);
			this.startActivity(intent);
			break;
		case R.id.menu_exit:
			AlertDialog.Builder builder = new Builder(MainActivity.this);
			builder.setMessage(R.string.quite_alert);
			builder.setTitle(R.string.quite_title);

			builder.setPositiveButton(R.string.quite_positive,
					new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							MainActivity.this.finish();
							MainActivity.this.serviceManager.stopService();

						}
					});

			builder.setNegativeButton(R.string.quite_negative,
					new OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
						}
					});

			builder.create().show();

		}
		return true;
	}
	// @Override
	// public boolean onKeyDown(int keyCode, KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK ) {
	// }
	// return super.onKeyDown(keyCode, event);
	// }
	//
	// private void InitTextView() {
	// main_page = (TextView) findViewById(R.id.main_page);
	// history_page = (TextView) findViewById(R.id.history_page);
	// setting_page = (TextView) findViewById(R.id.setting_page);
	//
	// main_page.setOnClickListener(new MyOnClickListener(0));
	// history_page.setOnClickListener(new MyOnClickListener(1));
	// setting_page.setOnClickListener(new MyOnClickListener(2));
	// }
	//
	// private void InitViewPager() {
	// mPager = (ViewPager) findViewById(R.id.vPager);
	// listViews = new ArrayList<View>();
	// LayoutInflater mInflater = getLayoutInflater();
	// listViews.add(mInflater.inflate(R.layout.main_page, null));
	// listViews.add(PageCreator.createHistoryPage(this));
	// listViews.add(mInflater.inflate(R.layout.setting_page, null));
	// mPager.setAdapter(new MyPagerAdapter(listViews));
	// mPager.setCurrentItem(0);
	// mPager.setOnPageChangeListener(new MyOnPageChangeListener());
	// }
	//
	// private void InitImageView() {
	// cursor = (ImageView) findViewById(R.id.cursor);
	// bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor)
	// .getWidth();
	// DisplayMetrics dm = new DisplayMetrics();
	// getWindowManager().getDefaultDisplay().getMetrics(dm);
	// int screenW = dm.widthPixels;
	// offset = (screenW / 3 - bmpW) / 2;
	// Matrix matrix = new Matrix();
	// matrix.postTranslate(offset, 0);
	// cursor.setImageMatrix(matrix);
	// }
	//
	// public class MyPagerAdapter extends PagerAdapter {
	// public List<View> mListViews;
	//
	// public MyPagerAdapter(List<View> mListViews) {
	// this.mListViews = mListViews;
	// }
	//
	// @Override
	// public void destroyItem(View arg0, int arg1, Object arg2) {
	// ((ViewPager) arg0).removeView(mListViews.get(arg1));
	// }
	//
	// @Override
	// public void finishUpdate(View arg0) {
	// }
	//
	// @Override
	// public int getCount() {
	// return mListViews.size();
	// }
	//
	// @Override
	// public Object instantiateItem(View arg0, int arg1) {
	// ((ViewPager) arg0).addView(mListViews.get(arg1), 0);
	// return mListViews.get(arg1);
	// }
	//
	// @Override
	// public boolean isViewFromObject(View arg0, Object arg1) {
	// return arg0 == (arg1);
	// }
	//
	// @Override
	// public void restoreState(Parcelable arg0, ClassLoader arg1) {
	// }
	//
	// @Override
	// public Parcelable saveState() {
	// return null;
	// }
	//
	// @Override
	// public void startUpdate(View arg0) {
	// }
	// }
	//
	// public class MyOnClickListener implements View.OnClickListener {
	// private int index = 0;
	//
	// public MyOnClickListener(int i) {
	// index = i;
	// }
	//
	// @Override
	// public void onClick(View v) {
	// mPager.setCurrentItem(index);
	// }
	// };
	//
	// public class MyOnPageChangeListener implements OnPageChangeListener {
	//
	// int one = offset * 2 + bmpW;
	// @Override
	// public void onPageSelected(int arg0) {
	// // Animation animation = null;
	// // switch (arg0) {
	// // case 0:
	// // if (currIndex == 1) {
	// // animation = new TranslateAnimation(one, 0, 0, 0);
	// // } else if (currIndex == 2) {
	// // animation = new TranslateAnimation(two, 0, 0, 0);
	// // }
	// // break;
	// // case 1:
	// // if (currIndex == 0) {
	// // animation = new TranslateAnimation(offset, one, 0, 0);
	// // } else if (currIndex == 2) {
	// // animation = new TranslateAnimation(two, one, 0, 0);
	// // }
	// // break;
	// // case 2:
	// // if (currIndex == 0) {
	// // animation = new TranslateAnimation(offset, two, 0, 0);
	// // } else if (currIndex == 1) {
	// // animation = new TranslateAnimation(one, two, 0, 0);
	// // }
	// // break;
	// // }
	// Animation animation = new TranslateAnimation(one*currIndex, one*arg0, 0,
	// 0);
	// currIndex = arg0;
	// animation.setFillAfter(true);
	// animation.setDuration(300);
	// cursor.startAnimation(animation);
	// }
	//
	// @Override
	// public void onPageScrolled(int arg0, float arg1, int arg2) {
	// }
	//
	// @Override
	// public void onPageScrollStateChanged(int arg0) {
	// }
	// }
}
