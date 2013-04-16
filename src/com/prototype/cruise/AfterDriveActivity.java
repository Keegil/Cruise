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
	ImageView ivBatteryFill;
	ImageView ivRangeDecrease;
	LinearLayout llRangeDecrease;
	FrameLayout flRangeDecrease;

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
		tvCompatible = (TextView) findViewById(R.id.tv_compatible);
		tvCharging = (TextView) findViewById(R.id.tv_charging);
		tvCharged = (TextView) findViewById(R.id.tv_charged);
		tvActualDistance = (TextView) findViewById(R.id.tv_actual_distance);
		tvEvDistance = (TextView) findViewById(R.id.tv_ev_distance);
		tvSpeed = (TextView) findViewById(R.id.tv_speed);
		tvAcc = (TextView) findViewById(R.id.tv_acc);
		tvRangeDecrease = (TextView) findViewById(R.id.tv_range_decrease);
		tvRangeLeft = (TextView) findViewById(R.id.tv_ev_range_left);
		ivBatteryFill = (ImageView) findViewById(R.id.iv_battery_fill);
		ivRangeDecrease = (ImageView) findViewById(R.id.iv_range_decrease);
		llRangeDecrease = (LinearLayout) findViewById(R.id.ll_range_decrease);
		flRangeDecrease = (FrameLayout) findViewById(R.id.fl_range_decrease);
		statSource = new DrivingStatsDataSource(this);
		statSource.open();
		gpsDataSource = new GPSDataSource(this);
		gpsDataSource.open();
		// formatting date
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// get current date time with Calendar()
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
		if (modifier <= 1.1) {
			fcs = new ForegroundColorSpan(getResources().getColor(
					R.color.light_green));
			ivRangeDecrease.setImageDrawable(getResources().getDrawable(
					R.drawable.greenstripes));
		} else if ((modifier > 1.1) && (modifier <= 1.4)) {
			fcs = new ForegroundColorSpan(getResources().getColor(
					R.color.mustard));
			ivRangeDecrease.setImageDrawable(getResources().getDrawable(
					R.drawable.yellowstripes));
		} else {
			fcs = new ForegroundColorSpan(getResources().getColor(
					R.color.wine_red));
			ivRangeDecrease.setImageDrawable(getResources().getDrawable(
					R.drawable.redstripes));
		}
		ssb.setSpan(ss, 11, ssb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		ssb.setSpan(fcs, 11, ssb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tvEvDistance.setText(ssb);
		// display mistakes
		if (accMistakes > 0) {
			tvAcc.setVisibility(View.VISIBLE);
			llRangeDecrease.setVisibility(View.VISIBLE);
			flRangeDecrease.setVisibility(View.VISIBLE);
		}
		if (speedMistakes > 0) {
			tvSpeed.setVisibility(View.VISIBLE);
			llRangeDecrease.setVisibility(View.VISIBLE);
			flRangeDecrease.setVisibility(View.VISIBLE);

		}
		// set current range and time
		currentRange = currentRange - iUsedRange;
		if (currentRange < 0) {
			currentRange = 0;
		}
		// set range left
		ssb = new SpannableStringBuilder("" + currentRange + " km range left");
		ssb.setSpan(ss, 0, 5, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
		tvRangeLeft.setText(ssb);
		lastTime = System.currentTimeMillis();
		saveData();
		saveDate();

		cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());
		statSource.createDrivingStats(date, accMistakes, speedMistakes, 0,
				driveLength, 0, iUsedRange, currentRange, iPoints, 0);

		/*
		 * AlertDialog.Builder alert = new AlertDialog.Builder(this);
		 * alert.setTitle("Database data"); final TextView output = new
		 * TextView(this); alert.setView(output); DrivingStats drivingStats =
		 * statSource.getDrivingStats(statSource .getAllDrivingStats().size());
		 * output.setText("ID: " + drivingStats.getId() + "\n" + "Date: " +
		 * drivingStats.getDate() + "\n" + "# acceleration events: " +
		 * drivingStats.getNumAccEvent() + "\n" + "# speed events: " +
		 * drivingStats.getNumSpeedEvent() + "\n" + "# brake events: " +
		 * drivingStats.getNumBrakeEvent() + "\n" + "Drive distance: " +
		 * drivingStats.getDriveDistance() + "\n" + "Starting range: " +
		 * drivingStats.getRangeStart() + "\n" + "Range used: " +
		 * drivingStats.getRangeUsed() + "\n" + "Finishing range: " +
		 * drivingStats.getRangeEnd() + "\n" + "Range modifier: " +
		 * drivingStats.getRangeModifier() + "\n" + "Fuel savings: " +
		 * drivingStats.getFuelSavings()); alert.show();
		 */

	}

	public void draw() {
		if ((((double) currentRange) / ((double) defaultRange)) >= 0.6) {
			ivBatteryFill.setImageResource(R.drawable.green);
			tvCompatible.setText("EV COMPATIBLE DRIVE");
			tvCompatible.setBackgroundColor(getResources().getColor(
					R.color.light_green));
		} else if ((((double) currentRange) / ((double) defaultRange) < 0.6)
				&& (((double) currentRange) / ((double) defaultRange) >= 0.2)) {
			ivBatteryFill.setImageResource(R.drawable.yellow);
			tvCompatible.setText("EV COMPATIBLE DRIVE");
			tvCompatible.setBackgroundColor(getResources().getColor(
					R.color.mustard));
		} else if ((((double) currentRange) / ((double) defaultRange) < 0.2)
				&& (((double) currentRange) / ((double) defaultRange) > 0)) {
			ivBatteryFill.setImageResource(R.drawable.red);
			tvCompatible.setText("EV COMPATIBLE DRIVE");
			tvCompatible.setBackgroundColor(getResources().getColor(
					R.color.wine_red));
			tvCharging.setVisibility(View.VISIBLE);
			tvCharging.setText("CHARGING RECOMMENDED!");
		} else {
			tvCompatible.setText("EV INCOMPATIBLE DRIVE");
			tvCompatible.setBackgroundColor(getResources().getColor(
					R.color.wine_red));
		}
		LayoutParams layoutParams = ivBatteryFill.getLayoutParams();
		int width = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 134, getResources()
						.getDisplayMetrics());
		double batteryWidth = ((double) currentRange) / ((double) defaultRange);
		layoutParams.width = (int) (width * batteryWidth);
		ivBatteryFill.setLayoutParams(layoutParams);
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
