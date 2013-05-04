package com.prototype.cruise;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BeforeDriveActivity extends Activity implements OnClickListener {

	// declare logging variables
	private static final String TAG = "BeforeDriveFragment";

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

	// declare simulation views
	EditText etDistance;
	EditText etAccMistakes;
	EditText etSpeedMistakes;
	Button bDrive;

	// declare textviews
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

	// declare temporary calculcation variables
	long timeDifference;
	boolean firstTime;
	double doubleCurrentRange;
	double doubleDefaultRange;
	double relativeRange;

	// BLUETOOTH CLASS
	private BluetoothBackEnd ba = new BluetoothBackEnd();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_before_drive);
		init();
		setFonts();
		loadSettings();
		loadData();
		loadDate();

		blueTooth();

	}

	@Override
	protected void onResume() {
		super.onResume();
		calc();
		setTextViews();
		drawBars();
	}

	public void blueTooth() {

		/*******
		 * BLUETOOTH STARTS
		 * 
		 * **********/

		// start looking for bt devices
		ba.findBT(true, this);

		try {
			// opens connection to target bluetooth device
			ba.openBT(true, this);
		} catch (IOException ex) {
		}

		// close bluetooth connection
		// ba.closeBT();

		/*********
		 * BLUETOOTH ENDS
		 * 
		 * *********/
	}

	public void init() {
		// initialize bars
		llBar1 = (LinearLayout) findViewById(R.id.bar1);
		llBar2 = (LinearLayout) findViewById(R.id.bar2);
		llBar3 = (LinearLayout) findViewById(R.id.bar3);
		llBar4 = (LinearLayout) findViewById(R.id.bar4);
		llBar5 = (LinearLayout) findViewById(R.id.bar5);
		llBar6 = (LinearLayout) findViewById(R.id.bar6);
		llBar7 = (LinearLayout) findViewById(R.id.bar7);
		llBar8 = (LinearLayout) findViewById(R.id.bar8);
		llBar9 = (LinearLayout) findViewById(R.id.bar9);

		// initialize background and gradient
		ll = (LinearLayout) findViewById(R.id.ll_main);

		// initialize textviews
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

		// initialize edittexts
		etDistance = (EditText) findViewById(R.id.et_distance);
		etAccMistakes = (EditText) findViewById(R.id.et_acc_mistakes);
		etSpeedMistakes = (EditText) findViewById(R.id.et_speed_mistakes);

		// initialize button and set listener
		bDrive = (Button) findViewById(R.id.b_drive);
		bDrive.setOnClickListener(this);

		// initialize database
		gpsDataSource = new GPSDataSource(this);
		gpsDataSource.open();
		statSource = new DrivingStatsDataSource(this);
		statSource.open();
	}

	public GradientDrawable setGradient(double rr) {
		double redStart = 95 + (2013 * rr) - (5311 * rr * rr)
				+ (3204 * rr * rr * rr);
		if (redStart > 255) {
			redStart = 255;
		} else if (redStart < 0) {
			redStart = 0;
		}
		double redStop = 155 + (402 * rr) - (593 * rr * rr)
				+ (290 * rr * rr * rr);
		if (redStop > 255) {
			redStop = 255;
		} else if (redStop < 0) {
			redStop = 0;
		}
		double greenStart = 129 + (294 * rr) - (328 * rr * rr);
		if (greenStart > 255) {
			greenStart = 255;
		} else if (greenStart < 0) {
			greenStart = 0;
		}
		double greenStop = 14 + (164 * rr) + (42 * rr * rr);
		if (greenStop > 255) {
			greenStop = 255;
		} else if (greenStop < 0) {
			greenStop = 0;
		}
		double blueStart = 66 - (472 * rr) + (1191 * rr * rr)
				- (728 * rr * rr * rr);
		if (blueStart > 255) {
			blueStart = 255;
		} else if (blueStart < 0) {
			blueStart = 0;
		}
		double blueStop = 45 + (12 * rr) - (72 * rr * rr) + (37 * rr * rr * rr);
		if (blueStop > 255) {
			blueStop = 255;
		} else if (blueStop < 0) {
			blueStop = 0;
		}
		GradientDrawable gdBackground = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
						Color.rgb((int) redStart, (int) greenStart,
								(int) blueStart),
						Color.rgb((int) redStop, (int) greenStop,
								(int) blueStop) });
		gdBackground.setCornerRadius(0f);
		return gdBackground;
	}

	public void setFonts() {
		// initialize typefaces
		Typeface tfHelvetica = Typeface.createFromAsset(getAssets(),
				"fonts/helvetica_bold_oblique.ttf");
		Typeface tfMyriadRegular = Typeface.createFromAsset(getAssets(),
				"fonts/myriad_regular.otf");
		Typeface tfMyriadItalic = Typeface.createFromAsset(getAssets(),
				"fonts/myriad_italic.otf");

		// set correct fonts to views
		tvLogo.setTypeface(tfHelvetica);
		tvStartingRange.setTypeface(tfHelvetica);
		tvEstRangeRemain.setTypeface(tfMyriadItalic);
		tvKm1.setTypeface(tfMyriadRegular);
		tvKm2.setTypeface(tfMyriadRegular);
		tvKm3.setTypeface(tfMyriadRegular);
		tvHighwayDesc.setTypeface(tfMyriadItalic);
		tvHighwayRange.setTypeface(tfHelvetica);
		tvCityDesc.setTypeface(tfMyriadItalic);
		tvCityRange.setTypeface(tfHelvetica);
		tvTimeParked.setTypeface(tfMyriadRegular);
		tvChargeGained.setTypeface(tfMyriadRegular);
	}

	public void calc() {
		// calculate range and charge time
		timeDifference = System.currentTimeMillis() - lastTime;
		if (lastTime == 0) {
			firstTime = true;
			currentRange = defaultRange;
		} else {
			chargedRange = (int) (double) (timeDifference / 300000);
			if (chargedRange > rangeUsed) {
				chargedRange = rangeUsed;
			}
			if (currentRange + chargedRange >= defaultRange) {
				currentRange = defaultRange;
			} else {
				currentRange = currentRange + chargedRange;
			}
		}
		doubleCurrentRange = (double) currentRange;
		doubleDefaultRange = (double) defaultRange;
		relativeRange = doubleCurrentRange / doubleDefaultRange;
		saveData();
	}

	public String setDate(long l) {
		final long days = TimeUnit.MILLISECONDS.toDays(l);
		final long hrs = TimeUnit.MILLISECONDS.toHours(l
				- TimeUnit.DAYS.toMillis(days));
		final long mins = TimeUnit.MILLISECONDS.toMinutes(l
				- TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hrs));
		return String.format("%02d:%02d:%02d", days, hrs, mins);
	}

	public void setTextViews() {
		if (firstTime) {
			tvTimeParked.setText("Time Parked: 00:00:00");
		} else {
			tvTimeParked
					.setText("Time Parked: " + setDate(timeDifference) + "");
		}
		tvStartingRange.setText("" + currentRange + "");
		tvHighwayRange.setText("" + (int) (currentRange * 0.75) + "");
		tvCityRange.setText("" + (int) (currentRange * 1.2) + "");
		tvChargeGained.setText("Charge Gained: " + chargedRange + " km");
	}

	public void drawBars() {
		// check relative range and set background and bars accordingly
		if (relativeRange <= 1 && relativeRange > 0.9) {
			// ll.setBackgroundResource(R.drawable.gradgreenyellow);
		} else if (relativeRange <= 0.9 && relativeRange > 0.8) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			// ll.setBackgroundResource(R.drawable.gradgreenyellow);
		} else if (relativeRange <= 0.8 && relativeRange > 0.7) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			// ll.setBackgroundResource(R.drawable.gradgreenyellow);
		} else if (relativeRange <= 0.7 && relativeRange > 0.6) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			// ll.setBackgroundResource(R.drawable.gradgreenorange);
		} else if (relativeRange <= 0.6 && relativeRange > 0.5) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			// ll.setBackgroundResource(R.drawable.gradgreenorange);
		} else if (relativeRange <= 0.5 && relativeRange > 0.4) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			// ll.setBackgroundResource(R.drawable.gradyelloworange);
		} else if (relativeRange <= 0.4 && relativeRange > 0.3) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			// ll.setBackgroundResource(R.drawable.gradyelloworange);
		} else if (relativeRange <= 0.3 && relativeRange > 0.2) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
			// ll.setBackgroundResource(R.drawable.gradyelloworange);
		} else if (relativeRange <= 0.2 && relativeRange > 0.1) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
			llBar8.setBackgroundResource(R.drawable.whiteemptybar);
			// ll.setBackgroundResource(R.drawable.gradorangered);
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
			// ll.setBackgroundResource(R.drawable.gradorangered);
		}
		ll.setBackgroundDrawable(setGradient(relativeRange));
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
		rangeUsed = data.getInt("rangeUsed", rangeUsed);
		accMistakes = data.getInt("accMistakes", accMistakes);
		speedMistakes = data.getInt("speedMistakes", speedMistakes);
		brakeMistakes = data.getInt("brakeMistakes", brakeMistakes);
		routeFail = data.getInt("routeFail", routeFail);
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
		editor.putInt("rangeUsed", rangeUsed);
		editor.putInt("accMistakes", accMistakes);
		editor.putInt("speedMistakes", speedMistakes);
		editor.putInt("brakeMistakes", brakeMistakes);
		editor.putInt("routeFail", routeFail);
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
		case R.id.find_bt:
			ba.findBT(true, this);
			return true;
		case R.id.open_bt:
			try {
				ba.openBT(true, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		case R.id.close_bt:
			try {
				ba.closeBT(true, this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	public void setDefaultRange() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Enter default range in km");

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
					}
				});

		alert.show();
	}

	public void setDefaultAcc() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Enter default average amount of acc. mistakes");

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
					}
				});

		alert.show();
	}

	public void setDefaultSpeed() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Enter default average amount of speed mistakes");

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
					}
				});

		alert.show();
	}

	public void setDefaultDriveCycle() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Enter default drive cycle distance in km");

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
					}
				});

		alert.show();
	}

	public void reset() {
		// reset all defaults and restart activity
		defaultRange = 120;
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
	public void onPause() {
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
