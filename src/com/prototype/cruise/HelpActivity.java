package com.prototype.cruise;

import com.viewpagerindicator.CirclePageIndicator;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

public class HelpActivity extends FragmentActivity {

	// Declare & initialize logging variables.
	private static final String TAG = "HelpActivity";

	// Declare fragments.
	HelpFragmentSpeed helpFragmentSpeed;
	HelpFragmentAcc helpFragmentAcc;
	HelpFragmentRoute helpFragmentRoute;
	HelpFragmentBrake helpFragmentBrake;
	HelpFragmentBars helpFragmentBars;

	// Declare ViewPager & Adapter variables.
	private MyAdapter mAdapter;
	private ViewPager mPager;
	private int whichPage;

	// Declare & initialize preference variables and set defaults.
	public static final String PREFS_NAME = "MyPrefsFile";
	public static int defaultRange = 120;
	public static boolean firstTime = true;

	// Declare & initialize data variables.
	public static final String DATA_NAME = "MyDataFile";
	public static int currentRange = 120;
	public static int driveLength = 0;
	public static int rangeUsed = 0;
	public static int accMistakes = 0;
	public static int speedMistakes = 0;
	public static int brakeMistakes = 0;
	public static int routeFail = 0;
	public static int chargedRange = 0;

	// Declare temporary calculation variables.
	public static double doubleCurrentRange;
	public static double doubleDefaultRange;
	public static double relativeRange;
	public static double relativeY = 0;

	// Declare ViewPager indicator.
	static CirclePageIndicator mIndicator;

	static BackgroundCalc bc = new BackgroundCalc();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(mPager);

		// Set correct page based on data from bundle.
		Bundle extras = this.getIntent().getExtras();
		whichPage = extras.getInt("helpData");
		Log.d(TAG, "" + whichPage + "");
		mPager.setCurrentItem(whichPage);

		loadSettings();
		loadData();

		doubleCurrentRange = (double) currentRange;
		doubleDefaultRange = (double) defaultRange;
		relativeRange = doubleCurrentRange / doubleDefaultRange;
		
		bc.makeGradient(relativeRange);
		setBackgroundIndicator(bc.getStopRGB());
	}

	public static void setBackgroundIndicator(int c) {
		mIndicator.setBackgroundColor(c);
	}

	public static class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new HelpFragmentSpeed();
			case 1:
				return new HelpFragmentAcc();
			case 2:
				return new HelpFragmentRoute();
			case 3:
				return new HelpFragmentBrake();
			case 4:
				return new HelpFragmentBars();
			default:
				return null;
			}
		}
	}

	public void loadSettings() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		defaultRange = settings.getInt("defaultRange", defaultRange);
		firstTime = settings.getBoolean("firstTime", firstTime);
	}

	public void loadData() {
		SharedPreferences data = getSharedPreferences(DATA_NAME, 0);
		currentRange = data.getInt("currentRange", currentRange);
		driveLength = data.getInt("driveLength", driveLength);
		rangeUsed = data.getInt("rangeUsed", rangeUsed);
		accMistakes = data.getInt("accMistakes", accMistakes);
		speedMistakes = data.getInt("speedMistakes", speedMistakes);
		brakeMistakes = data.getInt("brakeMistakes", brakeMistakes);
		routeFail = data.getInt("routeFail", routeFail);
		chargedRange = data.getInt("chargedRange", chargedRange);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.help, menu);
		return true;
	}

}
