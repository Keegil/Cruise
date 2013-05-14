package com.prototype.cruise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.viewpagerindicator.CirclePageIndicator;

public class AfterDriveActivity extends FragmentActivity {

	// Declare & initialize logging variables.
	private static final String TAG = "AfterDriveActivity";

	// Declare fragments.
	AfterDriveFragmentDriveComplete afterDriveFragmentDriveComplete;
	AfterDriveFragmentDriveDetails afterDriveFragmentDriveDetails;

	// Declare ViewPager & Adapter variables.
	private MyAdapter mAdapter;
	private ViewPager mPager;

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

	// Declare score variables.
	public static final String SCORE_NAME = "MyScoreFile";
	public static int accScore = 0;
	public static int brakeScore = 0;
	public static int speedScore = 0;
	public static int routeScore = 0;

	// Declare & initialize date setting.
	public static final String DATE_NAME = "MyDateFile";
	public static long lastTime = 0;

	// Declare database variables.
	public static GPSDataSource gpsDataSource;
	public static DrivingStatsDataSource drivingStatsDataSource;
	public static DateFormat dateFormat;
	public static Calendar cal;
	public static String date;

	// Declare & initialize JSON parsing variables.
	public static String filename = "gpslog30.txt";
	public static final String TAG_DRIVE_DATA = "driveData";
	public static final String TAG_GPS_DATA = "gpsData";
	public static JSONArray driveData = null;

	// Declare temporary calculation variables.
	public static double previousRelativeRange;
	public static double doubleCurrentRange;
	public static double doubleDefaultRange;
	public static double relativeRange;

	// Declare & initialize bluetooth variables.
	String btData;
	private static final String TAG_DRI = "drive1Instants";
	private static final String TAG_ACC = "hardAccels";
	private static final String TAG_BRA = "hardBrakes";
	private static final String TAG_SPE = "speedingCounts";
	private static final String TAG_TOT = "totalCounts";
	private static final String TAG_DIS = "distanceTraveled";
	
	//Viewpager indicator
	static CirclePageIndicator mIndicator;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);

		mAdapter = new MyAdapter(getSupportFragmentManager());

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		
        mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        mIndicator.setViewPager(mPager);

		init();
		loadSettings();
		loadData();
		loadDate();
		getJSON();

		Bundle extras = this.getIntent().getExtras();
		btData = extras.getString("btdata");
		Log.d(TAG, "" + btData + "");

		if (!btData.equalsIgnoreCase("simulation")) {
			btCalc();
		}

		calc();
	}
	
	public static void setBackgroundIndicator(int c){
		
        mIndicator.setBackgroundColor(c);
		
	}

	public void init() {
		// Initialize database.
		drivingStatsDataSource = new DrivingStatsDataSource(this);
		drivingStatsDataSource.open();
		gpsDataSource = new GPSDataSource(this);
		gpsDataSource.open();

		// Format and get date.
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());
	}

	public void getJSON() {
		JSONParser jParser = new JSONParser();
		final JSONObject json = jParser.getJSONFromAsset(this, filename);
		try {
			driveData = json.getJSONArray(TAG_DRIVE_DATA);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < driveData.length(); i++) {
					try {
						JSONObject c = driveData.getJSONObject(i);
						final String sentence = c.getString(TAG_GPS_DATA);
						String[] strValues = sentence.split(",");
						String time = strValues[1];
						float latitude = (float) (Float
								.parseFloat(strValues[3]) * .01);
						if (strValues[4].charAt(0) == 'S') {
							latitude = -latitude;
						}
						float longitude = (float) (Float
								.parseFloat(strValues[5]) * .01);
						if (strValues[6].charAt(0) == 'W') {
							longitude = -longitude;
						}
						gpsDataSource.createGPSData(drivingStatsDataSource
								.getAllDrivingStats().size(), time, latitude,
								longitude);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
		};
		new Thread(runnable).start();
	}

	public void btCalc() {
		try {
			// getting JSON string from URL
			JSONObject json = new JSONObject(btData);
			JSONArray drive = null;

			// Getting Array of Contacts
			drive = json.getJSONArray(TAG_DRI);

			// looping through All Contacts
			for (int i = 0; i < drive.length(); i++) {
				JSONObject c = drive.getJSONObject(i);

				// Storing each json item in variable
				accMistakes = c.getInt(TAG_ACC);
				brakeMistakes = c.getInt(TAG_BRA);
				speedMistakes = c.getInt(TAG_SPE);
				int tot = c.getInt(TAG_TOT);
				driveLength = c.getInt(TAG_DIS);

				Log.d("blue", "Acc: " + accMistakes + " - Bra: "
						+ brakeMistakes + " - Spe: " + speedMistakes
						+ " - Dri: " + driveLength);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		saveData();
	}

	public void calc() {
		chargedRange = 0;
		previousRelativeRange = (double) ((double) currentRange / (double) defaultRange);

		// Calculate acceleration score.
		double accPerKm = accMistakes / driveLength;
		if (accPerKm >= 45) {
			accScore = 0;
		} else if (accPerKm < 45 && accPerKm >= 36) {
			accScore = 1;
		} else if (accPerKm < 36 && accPerKm >= 27) {
			accScore = 2;
		} else if (accPerKm < 27 && accPerKm >= 18) {
			accScore = 3;
		} else if (accPerKm < 18 && accPerKm >= 9) {
			accScore = 4;
		} else if (accPerKm < 9 && accPerKm >= 0) {
			accScore = 5;
		}
		Log.d(TAG, "accPerKm: " + accPerKm + "| accScore: " + accScore + "");

		// Calculate brake score
		double brakePerKm = brakeMistakes / driveLength;
		if (brakePerKm >= 45) {
			brakeScore = 0;
		} else if (brakePerKm < 45 && brakePerKm >= 36) {
			brakeScore = 1;
		} else if (brakePerKm < 36 && brakePerKm >= 27) {
			brakeScore = 2;
		} else if (brakePerKm < 27 && brakePerKm >= 18) {
			brakeScore = 3;
		} else if (brakePerKm < 18 && brakePerKm >= 9) {
			brakeScore = 4;
		} else if (brakePerKm < 9 && brakePerKm >= 0) {
			brakeScore = 5;
		}
		Log.d(TAG, "brakePerKm: " + brakePerKm + "| brakeScore: " + brakeScore
				+ "");

		// Calculate speed score
		double speedPerKm = speedMistakes / driveLength;
		if (speedPerKm >= 45) {
			speedScore = 0;
		} else if (speedPerKm < 45 && speedPerKm >= 36) {
			speedScore = 1;
		} else if (speedPerKm < 36 && speedPerKm >= 27) {
			speedScore = 2;
		} else if (speedPerKm < 27 && speedPerKm >= 18) {
			speedScore = 3;
		} else if (speedPerKm < 18 && speedPerKm >= 9) {
			speedScore = 4;
		} else if (speedPerKm < 9 && speedPerKm >= 0) {
			speedScore = 5;
		}
		speedScore = 5;
		Log.d(TAG, "speedPerKm: " + speedPerKm + "| speedScore: " + speedScore
				+ "");

		// Calculate route score?
		routeScore = 5;
		Log.d(TAG, "routeScore: " + routeScore + "");

		// Calculate and apply modifier
		double modifier = 1 - (((double) accScore + (double) brakeScore + (double) speedScore) / 15);
		double extraRangeInterval = ((double) driveLength * 1.3)
				- ((double) driveLength);
		double extraRange = extraRangeInterval * modifier;
		rangeUsed = driveLength + (int) extraRange;

		// calculate current range
		currentRange = currentRange - rangeUsed;
		if (currentRange < 0) {
			currentRange = 0;
		}

		doubleCurrentRange = (double) currentRange;
		doubleDefaultRange = (double) defaultRange;
		relativeRange = doubleCurrentRange / doubleDefaultRange;

		// get correct time
		if (lastTime == 0) {
			firstTime = true;
			saveSettings();
		}
		lastTime = System.currentTimeMillis();

		// save data and date
		saveData();
		saveDate();
		saveScore();

		// save to database
		cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());
		drivingStatsDataSource.createDrivingStats(date, accScore, speedScore, brakeScore, routeScore, driveLength, 0, 0, 0, 0, 0);
	}

	public static class MyAdapter extends FragmentPagerAdapter {

		public MyAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new AfterDriveFragmentDriveComplete();
			case 1:
				return new AfterDriveFragmentDriveDetails();
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

	public void loadScore() {
		SharedPreferences score = getSharedPreferences(DATA_NAME, 0);
		accScore = score.getInt("accScore", accScore);
		brakeScore = score.getInt("brakeScore", brakeScore);
		speedScore = score.getInt("speedScore", speedScore);
		routeScore = score.getInt("routeScore", routeScore);
	}

	public void loadDate() {
		SharedPreferences date = getSharedPreferences(DATE_NAME, 0);
		lastTime = date.getLong("lastTime", lastTime);
	}

	public void saveSettings() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("defaultRange", defaultRange);
		editor.putBoolean("firstTime", firstTime);
		editor.commit();
	}

	public void saveData() {
		SharedPreferences data = getSharedPreferences(DATA_NAME, 0);
		SharedPreferences.Editor editor = data.edit();
		editor.putInt("currentRange", currentRange);
		editor.putInt("driveLength", driveLength);
		editor.putInt("rangeUsed", rangeUsed);
		editor.putInt("accMistakes", accMistakes);
		editor.putInt("speedMistakes", speedMistakes);
		editor.putInt("brakeMistakes", brakeMistakes);
		editor.putInt("routeFail", routeFail);
		editor.putInt("chargedRange", chargedRange);
		editor.commit();
	}

	public void saveScore() {
		SharedPreferences score = getSharedPreferences(SCORE_NAME, 0);
		SharedPreferences.Editor editor = score.edit();
		editor.putInt("accScore", accScore);
		editor.putInt("brakeScore", brakeScore);
		editor.putInt("speedScore", speedScore);
		editor.putInt("routeScore", routeScore);
		editor.commit();
	}

	public void saveDate() {
		SharedPreferences date = getSharedPreferences(DATE_NAME, 0);
		SharedPreferences.Editor editor = date.edit();
		editor.putLong("lastTime", lastTime);
		editor.commit();
	}
}