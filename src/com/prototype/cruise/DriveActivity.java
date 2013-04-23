package com.prototype.cruise;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DriveActivity extends Activity implements OnClickListener {

	private static final String TAG = "DriveActivity";

	public static final String PREFS_NAME = "MyPrefsFile";
	public static final String DATA_NAME = "MyDataFile";
	public static final String DATE_NAME = "MyDateFile";

	private GPSDataSource gpsDataSource;
	private DrivingStatsDataSource statSource;

	int defaultRange = 100;
	double doubleDefaultRange;
	int defaultAccMistakes = 4;
	int defaultSpeedMistakes = 1;
	int defaultDriveCycle = 11;

	int currentRange = 100;
	double doubleCurrentRange;
	double relativeRange;

	int driveLength = 0;
	int accMistakes = 0;
	int speedMistakes = 0;
	int chargedRange = 0;

	long lastTime = 0;

	EditText etDistance;
	EditText etAccMistakes;
	EditText etSpeedMistakes;

	TextView tvStartingRange;
	TextView tvLogo;
	TextView tvEstRangeRemain;
	TextView tvKm1;
	TextView tvKm2;
	TextView tvKm3;
	TextView tvHighwayDesc;
	TextView tvHighwayRange;
	TextView tvCityDesc;
	TextView tvCityRange;
	TextView tvTimeParked;
	TextView tvChargeGained;
	
	LinearLayout llBar1;
	LinearLayout llBar2;
	LinearLayout llBar3;
	LinearLayout llBar4;
	LinearLayout llBar5;
	LinearLayout llBar6;
	LinearLayout llBar7;
	LinearLayout llBar8;
	LinearLayout llBar9;

	Button bDrive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drive);

		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
						Color.rgb(2, 104, 56), Color.rgb(0, 147, 69) });
		gd.setCornerRadius(0f);
		loadSettings();
		loadData();
		loadDate();
		init();
		calc();
		// draw();
	}

	public void init() {
		llBar1  = (LinearLayout) findViewById(R.id.bar1);
		
		etDistance = (EditText) findViewById(R.id.et_distance);
		etAccMistakes = (EditText) findViewById(R.id.et_acc_mistakes);
		etSpeedMistakes = (EditText) findViewById(R.id.et_speed_mistakes);
		tvStartingRange = (TextView) findViewById(R.id.tv_starting_range);
		tvLogo = (TextView) findViewById(R.id.tv_logo);
		tvEstRangeRemain = (TextView) findViewById(R.id.tv_est_range_remain);
		tvKm1 = (TextView) findViewById(R.id.tv_km);
		tvKm2 = (TextView) findViewById(R.id.tv_highway_km);
		tvKm3 = (TextView) findViewById(R.id.tv_city_km);
		tvHighwayDesc = (TextView) findViewById(R.id.tv_highway_desc);
		tvHighwayRange = (TextView) findViewById(R.id.tv_highway_range);
		tvCityDesc = (TextView) findViewById(R.id.tv_city_desc);
		tvCityRange = (TextView) findViewById(R.id.tv_city_range);
		tvTimeParked = (TextView) findViewById(R.id.tv_time_parked);
		tvChargeGained = (TextView) findViewById(R.id.tv_charge_gained);
		bDrive = (Button) findViewById(R.id.b_drive);
		bDrive.setOnClickListener(this);
		// ivPreBatteryFill = (ImageView)
		// findViewById(R.id.iv_pre_battery_fill);
		gpsDataSource = new GPSDataSource(this);
		gpsDataSource.open();
		statSource = new DrivingStatsDataSource(this);
		statSource.open();
		Typeface tfHelvetica = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_bold_oblique.ttf");
		Typeface tfGeosansLightOblique = Typeface.createFromAsset(getAssets(),
				"fonts/geosans_light_oblique.ttf");
		Typeface tfGeosansLight = Typeface.createFromAsset(getAssets(),
				"fonts/geosans_light.ttf");
		tvLogo.setTypeface(tfHelvetica);
		tvStartingRange.setTypeface(tfHelvetica);
		tvEstRangeRemain.setTypeface(tfGeosansLightOblique);
		tvKm1.setTypeface(tfGeosansLight);
		tvKm2.setTypeface(tfGeosansLight);
		tvKm3.setTypeface(tfGeosansLight);
		tvHighwayDesc.setTypeface(tfGeosansLightOblique);
		tvHighwayRange.setTypeface(tfHelvetica);
		tvCityDesc.setTypeface(tfGeosansLightOblique);
		tvCityRange.setTypeface(tfHelvetica);
		tvTimeParked.setTypeface(tfGeosansLight);
		tvChargeGained.setTypeface(tfGeosansLight);
		LinearLayout ll = (LinearLayout) findViewById(R.id.ll_main);
		ll.setBackgroundResource(R.drawable.gradgreenyellow);
	}

	public void calc() {
		if (lastTime == 0) {
			currentRange = defaultRange;
		} else {
			chargedRange = (int) (double) ((System.currentTimeMillis() - lastTime) / 300000);
			if (currentRange + chargedRange >= defaultRange) {
				currentRange = defaultRange;
			} else {
				currentRange = currentRange + chargedRange;
			}
		}
		tvStartingRange.setText("" + currentRange + "");
		Log.d(TAG, ""
				+ ((double) ((System.currentTimeMillis() - lastTime) / 60000))
				+ "");
		saveData();
	}

	public void draw() {
		doubleCurrentRange = (double) currentRange;
		doubleDefaultRange = (double) defaultRange;
		relativeRange = doubleCurrentRange / doubleDefaultRange;
		if (relativeRange <= 1 && relativeRange > 0.9) {
			
		} else if (relativeRange <= 0.9 & relativeRange > 0.8) {
			ivPreBatteryFill.setImageResource(R.drawable.yellow);
			tvStartingRange.setBackgroundColor(getResources().getColor(
					R.color.mustard));
		} else if ((doubleCurrentRange / doubleDefaultRange < 0.2)
				&& (doubleCurrentRange / doubleDefaultRange > 0)) {
			ivPreBatteryFill.setImageResource(R.drawable.red);
			tvStartingRange.setBackgroundColor(getResources().getColor(
					R.color.wine_red));
		} else {
			ivPreBatteryFill.setImageResource(R.drawable.red);
			tvStartingRange.setBackgroundColor(getResources().getColor(
					R.color.wine_red));
		}
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

	public void loadDate() {
		SharedPreferences date = getSharedPreferences(DATE_NAME, 0);
		lastTime = date.getLong("lastTime", lastTime);
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
		editor.putInt("chargedRange", chargedRange);
		editor.commit();
	}

	public void saveDate() {
		SharedPreferences date = getSharedPreferences(DATE_NAME, 0);
		SharedPreferences.Editor editor = date.edit();
		editor.putLong("lastTime", lastTime);
		editor.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.drive, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.action_defaultrange:
			setDefaultRange();
			return true;
		case R.id.action_defaultacc:
			setDefaultAcc();
			return true;
		case R.id.action_defaultspeed:
			setDefaultSpeed();
			return true;
		case R.id.action_defaultdrivecycle:
			setDefaultDriveCycle();
			return true;
		case R.id.action_reset:
			reset();
			return true;
		case R.id.action_reset_db:
			resetDb();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setDefaultRange() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Enter default range in km");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);
		input.setText("" + defaultRange + "");
		input.setInputType(InputType.TYPE_CLASS_PHONE);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				defaultRange = Integer.parseInt(input.getText().toString());
				saveSettings();
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}

	public void setDefaultAcc() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Enter default average amount of acc. mistakes");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);
		input.setText("" + defaultAccMistakes + "");
		input.setInputType(InputType.TYPE_CLASS_PHONE);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				defaultAccMistakes = Integer.parseInt(input.getText()
						.toString());
				saveSettings();
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}

	public void setDefaultSpeed() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Enter default average amount of speed mistakes");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);
		input.setText("" + defaultSpeedMistakes + "");
		input.setInputType(InputType.TYPE_CLASS_PHONE);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				defaultSpeedMistakes = Integer.parseInt(input.getText()
						.toString());
				saveSettings();
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}

	public void setDefaultDriveCycle() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Enter default drive cycle distance in km");

		// Set an EditText view to get user input
		final EditText input = new EditText(this);
		alert.setView(input);
		input.setText("" + defaultDriveCycle + "");
		input.setInputType(InputType.TYPE_CLASS_PHONE);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				defaultDriveCycle = Integer
						.parseInt(input.getText().toString());
				saveSettings();
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		});

		alert.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
					}
				});

		alert.show();
	}

	public void reset() {
		defaultRange = 100;
		defaultAccMistakes = 4;
		defaultSpeedMistakes = 1;
		defaultDriveCycle = 11;
		lastTime = 0;
		currentRange = defaultRange;
		chargedRange = 0;
		saveSettings();
		saveData();
		saveDate();
		Intent intent = getIntent();
		finish();
		startActivity(intent);
	}

	public void resetDb() {
		gpsDataSource.deleteAllGPSData();
		statSource.deleteAllDrivingStats();
	}

	@Override
	protected void onPause() {
		super.onPause();
		saveSettings();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.b_drive:
			driveLength = Integer.parseInt(etDistance.getText().toString());
			accMistakes = Integer.parseInt(etAccMistakes.getText().toString());
			speedMistakes = Integer.parseInt(etSpeedMistakes.getText()
					.toString());
			saveData();
			Intent i = new Intent(getApplicationContext(),
					AfterDriveActivity.class);
			startActivity(i);
			finish();
			break;
		}
	}

}
