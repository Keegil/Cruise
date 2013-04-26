package com.prototype.cruise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AfterDriveActivity extends Activity {

	// JSON parsing
	private static String filename = "gpslog30.txt";
	private static final String TAG_DRIVE_DATA = "driveData";
	private static final String TAG_GPS_DATA = "gpsData";
	JSONArray driveData = null;

	// logging
	private static final String TAG = "AfterDriveActivity";

	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String DATA_NAME = "MyDataFile";
	public static final String DATE_NAME = "MyDateFile";

	private DrivingStatsDataSource statSource;
	private GPSDataSource gpsDataSource;
	private DateFormat dateFormat;
	private Calendar cal;
	private String date;

	SpannableStringBuilder ssb;
	StyleSpan ss;
	ForegroundColorSpan fcs;

	int defaultRange = 100;
	int defaultAccMistakes = 4;
	int defaultSpeedMistakes = 1;
	int defaultDriveCycle = 11;

	int currentRange = 100;
	int driveLength = 0;
	int accMistakes = 0;
	int speedMistakes = 0;
	int chargedRange = 0;
	
	double doubleCurrentRange;
	double doubleDefaultRange;
	double relativeRange;

	int iPoints = 0;
	double dPoints = 0;
	int iUsedRange = 0;
	double dUsedRange = 0;
	double modifier = 0;

	long lastTime;

	long statSize;

	TextView tvCompatible;
	TextView tvCharging;
	TextView tvCharged;
	TextView tvActualDistance;
	TextView tvEvDistance;
	TextView tvAcc;
	TextView tvSpeed;
	TextView tvRangeDecrease;
	TextView tvRangeLeft;

	LinearLayout llBar1;
	LinearLayout llBar2;
	LinearLayout llBar3;
	LinearLayout llBar4;
	LinearLayout llBar5;
	LinearLayout llBar6;
	LinearLayout llBar7;
	LinearLayout llBar8;
	LinearLayout llBar9;
	LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_drive);
		loadSettings();
		loadData();
		init();
		calc();
		draw();
		getJSON();
	}

	public void init() {
		// initialize bars
		llBar1 = (LinearLayout) findViewById(R.id.bar1_after);
		llBar2 = (LinearLayout) findViewById(R.id.bar2_after);
		llBar3 = (LinearLayout) findViewById(R.id.bar3_after);
		llBar4 = (LinearLayout) findViewById(R.id.bar4_after);
		llBar5 = (LinearLayout) findViewById(R.id.bar5_after);
		llBar6 = (LinearLayout) findViewById(R.id.bar6_after);
		llBar7 = (LinearLayout) findViewById(R.id.bar7_after);
		llBar8 = (LinearLayout) findViewById(R.id.bar8_after);
		llBar9 = (LinearLayout) findViewById(R.id.bar9_after);
		
		// initialize background
		ll = (LinearLayout) findViewById(R.id.ll_main_after);
		
		// initialize textviews
		tvCompatible = (TextView) findViewById(R.id.tv_compatible);
		tvCharging = (TextView) findViewById(R.id.tv_charging);
		tvCharged = (TextView) findViewById(R.id.tv_charged);
		tvActualDistance = (TextView) findViewById(R.id.tv_actual_distance);
		tvEvDistance = (TextView) findViewById(R.id.tv_ev_distance);
		tvSpeed = (TextView) findViewById(R.id.tv_speed);
		tvAcc = (TextView) findViewById(R.id.tv_acc);
		tvRangeDecrease = (TextView) findViewById(R.id.tv_range_decrease);
		tvRangeLeft = (TextView) findViewById(R.id.tv_ev_range_left);
		
		// initialize database
		statSource = new DrivingStatsDataSource(this);
		statSource.open();
		gpsDataSource = new GPSDataSource(this);
		gpsDataSource.open();
		
		// formatting and get date
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());
	}

	public void calc() {
		// set distance charged
		ssb = new SpannableStringBuilder("Charged " + chargedRange
				+ " km before drive");
		ss = new StyleSpan(android.graphics.Typeface.BOLD);
		ssb.setSpan(ss, 8, 12, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tvCharged.setText(ssb);
		// set distance driven
		ssb = new SpannableStringBuilder("Drove " + driveLength + " km");
		ssb.setSpan(ss, 6, ssb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tvActualDistance.setText(ssb);
		// calculate points
		double aa = (double) defaultAccMistakes;
		double a = (double) accMistakes;
		double da = (double) defaultDriveCycle;
		double d = (double) driveLength;
		double sa = (double) defaultSpeedMistakes;
		double s = (double) speedMistakes;
		double x = ((aa * (d / da)) / ((aa * (d / da)) + a));
		x = 1 + Math.pow(x, 1.1);
		double y = ((sa * (d / da)) / ((sa * (d / da)) + s));
		y = 1 + Math.pow(y, 1.1);
		dPoints = (1 / (2 / ((0.8 * x) + (0.2 * y)))) * 100;
		iPoints = (int) dPoints;
		// calculate and apply modifier
		modifier = (1 / (dPoints / 100));
		dUsedRange = d * modifier;
		iUsedRange = (int) dUsedRange;
		// set range down
		ssb = new SpannableStringBuilder("Range down " + iUsedRange + " km");
		tvEvDistance.setText(ssb);
		// set current range and time
		currentRange = currentRange - iUsedRange;
		if (currentRange < 0) {
			currentRange = 0;
		}
		// set range left
		tvRangeLeft.setText("" + currentRange + " km range left");
		lastTime = System.currentTimeMillis();
		saveData();
		saveDate();

		cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());
		statSource.createDrivingStats(date, accMistakes, speedMistakes, 0,
				driveLength, 0, iUsedRange, currentRange, iPoints, 0);
	}

	public void draw() {
		doubleCurrentRange = (double) currentRange;
		doubleDefaultRange = (double) defaultRange;
		relativeRange = doubleCurrentRange / doubleDefaultRange;
		if (relativeRange <= 1 && relativeRange > 0.9) {
			ll.setBackgroundResource(R.drawable.gradgreenyellow);
		} else if (relativeRange <= 0.9 && relativeRange > 0.8) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			ll.setBackgroundResource(R.drawable.gradgreenyellow);
		} else if (relativeRange <= 0.8 && relativeRange > 0.7) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			ll.setBackgroundResource(R.drawable.gradgreenyellow);
		} else if (relativeRange <= 0.7 && relativeRange > 0.6) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			ll.setBackgroundResource(R.drawable.gradgreenorange);
		} else if (relativeRange <= 0.6 && relativeRange > 0.5) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			ll.setBackgroundResource(R.drawable.gradgreenorange);
		} else if (relativeRange <= 0.5 && relativeRange > 0.4) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			ll.setBackgroundResource(R.drawable.gradgreenorange);
		} else if (relativeRange <= 0.4 && relativeRange > 0.3) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			ll.setBackgroundResource(R.drawable.gradyelloworange);
		} else if (relativeRange <= 0.3 && relativeRange > 0.2) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
			ll.setBackgroundResource(R.drawable.gradyelloworange);
		} else if (relativeRange <= 0.2 && relativeRange > 0.1) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
			llBar8.setBackgroundResource(R.drawable.whiteemptybar);
			ll.setBackgroundResource(R.drawable.gradorangered);
		} else if (relativeRange <= 0.1 && relativeRange >= 0) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
			llBar8.setBackgroundResource(R.drawable.whiteemptybar);
			llBar9.setBackgroundResource(R.drawable.whiteemptybar);
			ll.setBackgroundResource(R.drawable.gradorangered);
		}
	}

	public void getJSON() {
		JSONParser jParser = new JSONParser();
		final JSONObject json = jParser.getJSONFromAsset(this, filename);
		try {
			driveData = json.getJSONArray(TAG_DRIVE_DATA);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		final ProgressDialog pdParsing = new ProgressDialog(this);
		pdParsing.setTitle("Parsing " + filename + "...");
		pdParsing.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pdParsing.setProgress(0);
		pdParsing.setMax(driveData.length());
		pdParsing.setCancelable(false);
		pdParsing.show();
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
						gpsDataSource.createGPSData(statSource
								.getAllDrivingStats().size(), time, latitude,
								longitude);
						pdParsing.incrementProgressBy(1);
						if (pdParsing.getProgress() >= pdParsing.getMax()) {
							pdParsing.dismiss();
							GPSData gpsData = gpsDataSource
									.getGPSData(statSource.getAllDrivingStats()
											.size());
							Log.d(TAG,
									"ID: " + gpsData.getId() + "\n" + "Time: "
											+ gpsData.getTime() + "\n"
											+ "Latitude: "
											+ gpsData.getLatitude() + "\n"
											+ "Longitude: "
											+ gpsData.getLongitude());
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			}
		};
		new Thread(runnable).start();
	}

	public void loadSettings() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		defaultRange = settings.getInt("defaultRange", defaultRange);
		defaultAccMistakes = settings.getInt("defaultAccMistakes",
				defaultAccMistakes);
		defaultSpeedMistakes = settings.getInt("defaultSpeedMistakes",
				defaultSpeedMistakes);
		defaultDriveCycle = settings.getInt("defaultDriveCycle",
				defaultDriveCycle);
	}

	public void loadData() {
		SharedPreferences data = getSharedPreferences(DATA_NAME, 0);
		currentRange = data.getInt("currentRange", currentRange);
		driveLength = data.getInt("driveLength", driveLength);
		accMistakes = data.getInt("accMistakes", accMistakes);
		speedMistakes = data.getInt("speedMistakes", speedMistakes);
		chargedRange = data.getInt("chargedRange", chargedRange);
	}

	public void saveData() {
		SharedPreferences data = getSharedPreferences(DATA_NAME, 0);
		SharedPreferences.Editor editor = data.edit();
		editor.putInt("currentRange", currentRange);
		editor.putInt("driveLength", driveLength);
		editor.putInt("accMistakes", accMistakes);
		editor.putInt("speedMistakes", speedMistakes);
		editor.putInt("chargedRange", chargedRange);
		editor.commit();
	}

	public void saveDate() {
		SharedPreferences date = getSharedPreferences(DATE_NAME, 0);
		SharedPreferences.Editor editor = date.edit();
		editor.putLong("lastTime", lastTime);
		editor.commit();
	}
}
