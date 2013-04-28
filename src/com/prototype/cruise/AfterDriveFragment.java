package com.prototype.cruise;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AfterDriveFragment extends Fragment {

	// declare logging variables
	private static final String TAG = "AfterDriveFragment";

	// declare preference variables and set defaults
	public static final String PREFS_NAME = "MyPrefsFile";
	int defaultRange = 120;
	int defaultAccMistakes = 4;
	int defaultSpeedMistakes = 1;
	int defaultDriveCycle = 11;

	// declare data variables
	public static final String DATA_NAME = "MyDataFile";
	int currentRange = 120;
	int driveLength = 0;
	int rangeUsed = 0;
	int accMistakes = 0;
	int speedMistakes = 0;
	int brakeMistakes = 0;
	int routeFail = 0;
	int chargedRange = 0;

	// declare date setting
	public static final String DATE_NAME = "MyDateFile";
	long lastTime = 0;

	// declare database
	private GPSDataSource gpsDataSource;
	private DrivingStatsDataSource statSource;
	private DateFormat dateFormat;
	private Calendar cal;
	private String date;

	// declare JSON parsing variables
	private static String filename = "gpslog20.txt";
	private static final String TAG_DRIVE_DATA = "driveData";
	private static final String TAG_GPS_DATA = "gpsData";
	JSONArray driveData = null;

	int iPoints = 0;
	double dPoints = 0;
	double dUsedRange = 0;
	double modifier = 0;

	long statSize;

	// declare textviews
	TextView tvLogo;
	TextView tvDriveCompleted;
	TextView tvStars;
	TextView tvChargeLeft;

	// declare bars
	LinearLayout llBar1;
	LinearLayout llBar2;
	LinearLayout llBar3;
	LinearLayout llBar4;
	LinearLayout llBar5;
	LinearLayout llBar6;
	LinearLayout llBar7;
	LinearLayout llBar8;
	LinearLayout llBar9;

	// declare background and gradient
	LinearLayout ll;
	GradientDrawable gdBackground;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_after_drive, container,
				false);
		init(view);
		setFonts();
		loadSettings();
		loadData();
		calc();
		getJSON();
		drawBars();
		return view;
	}

	public void init(View v) {
		// initialize bars
		llBar1 = (LinearLayout) v.findViewById(R.id.bar1_after);
		llBar2 = (LinearLayout) v.findViewById(R.id.bar2_after);
		llBar3 = (LinearLayout) v.findViewById(R.id.bar3_after);
		llBar4 = (LinearLayout) v.findViewById(R.id.bar4_after);
		llBar5 = (LinearLayout) v.findViewById(R.id.bar5_after);
		llBar6 = (LinearLayout) v.findViewById(R.id.bar6_after);
		llBar7 = (LinearLayout) v.findViewById(R.id.bar7_after);
		llBar8 = (LinearLayout) v.findViewById(R.id.bar8_after);
		llBar9 = (LinearLayout) v.findViewById(R.id.bar9_after);

		// initialize background
		ll = (LinearLayout) v.findViewById(R.id.ll_main_after);

		// initialize textviews
		tvLogo = (TextView) v.findViewById(R.id.tv_logo_after);
		tvStars = (TextView) v.findViewById(R.id.tv_stars);
		tvChargeLeft = (TextView) v.findViewById(R.id.tv_charge_left);
		tvDriveCompleted = (TextView) v.findViewById(R.id.tv_drive_completed);

		// initialize database
		statSource = new DrivingStatsDataSource(getActivity());
		statSource.open();
		gpsDataSource = new GPSDataSource(getActivity());
		gpsDataSource.open();

		// formatting and get date
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());
	}

	public void setFonts() {
		// initialize typefaces
		Typeface tfHelvetica = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/helvetica_bold_oblique.ttf");
		Typeface tfMyriadRegular = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_regular.otf");

		// set correct fonts to views
		tvLogo.setTypeface(tfHelvetica);
		tvDriveCompleted.setTypeface(tfMyriadRegular);
		tvStars.setTypeface(tfMyriadRegular);
		tvChargeLeft.setTypeface(tfMyriadRegular);
	}

	public void calc() {

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
		rangeUsed = (int) dUsedRange;

		// calculate current range
		currentRange = currentRange - rangeUsed;
		if (currentRange < 0) {
			currentRange = 0;
		}

		// get correct time
		lastTime = System.currentTimeMillis();

		// save data and date
		saveData();
		saveDate();

		// save to database
		cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());
		statSource.createDrivingStats(date, accMistakes, speedMistakes, 0,
				driveLength, 0, rangeUsed, currentRange, iPoints, 0);
	}

	public void drawBars() {
		// check relative range and set background and bars accordingly
		double doubleCurrentRange = (double) currentRange;
		double doubleDefaultRange = (double) defaultRange;
		double relativeRange = doubleCurrentRange / doubleDefaultRange;
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
			ll.setBackgroundResource(R.drawable.gradyelloworange);
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
		final JSONObject json = jParser.getJSONFromAsset(getActivity(),
				filename);
		try {
			driveData = json.getJSONArray(TAG_DRIVE_DATA);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		final ProgressDialog pdParsing = new ProgressDialog(getActivity());
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
						/* gpsDataSource.createGPSData(statSource
								.getAllDrivingStats().size(), time, latitude,
								longitude); */
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
		SharedPreferences settings = getActivity().getSharedPreferences(
				PREFS_NAME, 0);
		defaultRange = settings.getInt("defaultRange", defaultRange);
		defaultAccMistakes = settings.getInt("defaultAccMistakes",
				defaultAccMistakes);
		defaultSpeedMistakes = settings.getInt("defaultSpeedMistakes",
				defaultSpeedMistakes);
		defaultDriveCycle = settings.getInt("defaultDriveCycle",
				defaultDriveCycle);
	}

	public void loadData() {
		SharedPreferences data = getActivity().getSharedPreferences(DATA_NAME,
				0);
		currentRange = data.getInt("currentRange", currentRange);
		driveLength = data.getInt("driveLength", driveLength);
		rangeUsed = data.getInt("rangeUsed", rangeUsed);
		accMistakes = data.getInt("accMistakes", accMistakes);
		speedMistakes = data.getInt("speedMistakes", speedMistakes);
		brakeMistakes = data.getInt("brakeMistakes", brakeMistakes);
		routeFail = data.getInt("routeFail", routeFail);
		chargedRange = data.getInt("chargedRange", chargedRange);
	}

	public void loadDate() {
		SharedPreferences date = getActivity().getSharedPreferences(DATE_NAME,
				0);
		lastTime = date.getLong("lastTime", lastTime);
	}

	public void saveSettings() {
		SharedPreferences settings = getActivity().getSharedPreferences(
				PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("defaultRange", defaultRange);
		editor.putInt("defaultAccMistakes", defaultAccMistakes);
		editor.putInt("defaultSpeedMistakes", defaultSpeedMistakes);
		editor.commit();
	}

	public void saveData() {
		SharedPreferences data = getActivity().getSharedPreferences(DATA_NAME,
				0);
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

	public void saveDate() {
		SharedPreferences date = getActivity().getSharedPreferences(DATE_NAME,
				0);
		SharedPreferences.Editor editor = date.edit();
		editor.putLong("lastTime", lastTime);
		editor.commit();
	}
}
