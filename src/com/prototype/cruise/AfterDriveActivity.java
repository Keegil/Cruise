package com.prototype.cruise;

import java.sql.Date;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class AfterDriveActivity extends Activity {

	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String DATA_NAME = "MyDataFile";
	public static final String DATE_NAME = "MyDateFile";

	int defaultRange = 100;
	int defaultAccMistakes = 4;
	int defaultSpeedMistakes = 1;
	int defaultDriveCycle = 11;

	int currentRange = 100;
	int driveLength = 0;
	int accMistakes = 0;
	int speedMistakes = 0;

	double doublePoints = 0;
	int intPoints = 0;
	double doubleUsedRange = 0;
	int intUsedRange = 0;

	Date lastTime = null;

	TextView tvPoints;
	TextView tvRemainingRange;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_drive);
		loadSettings();
		loadData();
		init();
		calc();
	}

	public void init() {
		lastTime = new Date(System.currentTimeMillis());
		tvPoints = (TextView) findViewById(R.id.tv_points);
		tvRemainingRange = (TextView) findViewById(R.id.tv_remaining_range);
	}

	private void calc() {
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
		doublePoints = (1 / (2 / ((0.8 * x) + (0.2 * y)))) * 100;
		intPoints = (int) doublePoints;
		tvPoints.setText("" + intPoints + "");
		doubleUsedRange = d * (1 / (doublePoints / 100));
		intUsedRange = (int) doubleUsedRange;
		currentRange = currentRange - intUsedRange;
		tvRemainingRange.setText("" + currentRange + "");
		saveData();
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
	}

	public void saveSettings() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt("defaultRange", defaultRange);
		editor.putInt("defaultAccMistakes", defaultAccMistakes);
		editor.putInt("defaultSpeedMistakes", defaultSpeedMistakes);
		editor.commit();
	}

	public void saveData() {
		SharedPreferences data = getSharedPreferences(DATA_NAME, 0);
		SharedPreferences.Editor editor = data.edit();
		editor.putInt("currentRange", currentRange);
		editor.putInt("driveLength", driveLength);
		editor.putInt("accMistakes", accMistakes);
		editor.putInt("speedMistakes", speedMistakes);
		editor.commit();
	}

	public void saveDate() {
		SharedPreferences date = getSharedPreferences(DATE_NAME, 0);
		SharedPreferences.Editor editor = date.edit();
		editor.putLong("lastTime", lastTime.getTime());
		editor.commit();
	}

}
