package com.prototype.cruise;

import java.sql.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DriveActivity extends Activity implements OnClickListener {

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
	
	Date lastTime = null;

	EditText etDistance;
	EditText etAccMistakes;
	EditText etSpeedMistakes;

	TextView tvStartingRange;
	TextView tvDate1;
	TextView tvDate2;
	
	Button bDrive;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drive);
		loadSettings();
		loadData();
		loadDate();
		init();
		calc();
	}

	public void init() {
		etDistance = (EditText) findViewById(R.id.et_distance);
		etAccMistakes = (EditText) findViewById(R.id.et_acc_mistakes);
		etSpeedMistakes = (EditText) findViewById(R.id.et_speed_mistakes);
		tvStartingRange = (TextView) findViewById(R.id.tv_starting_range);
		tvDate1 = (TextView) findViewById(R.id.tv_date1);
		tvDate2 = (TextView) findViewById(R.id.tv_date2);
		bDrive = (Button) findViewById(R.id.b_drive);
		bDrive.setOnClickListener(this);
	}
	
	public void calc() {
		if (lastTime.getTime() == 0) {
			currentRange = defaultRange;
		} else {
			long difference = System.currentTimeMillis() - lastTime.getTime();
			long differenceHours = difference / 3600000;
			int chargedRange = (int) differenceHours;
			chargedRange = chargedRange*10;
			currentRange = currentRange + chargedRange;
		}
		tvStartingRange.setText("" + currentRange + "");
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
	
	public void loadDate() {
		SharedPreferences date = getSharedPreferences(DATE_NAME, 0);
		lastTime = new Date(date.getLong("time", 0));
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

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				defaultRange = Integer.parseInt(input.getText().toString());
				saveSettings();
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

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				defaultAccMistakes = Integer.parseInt(input.getText()
						.toString());
				saveSettings();
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

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				defaultSpeedMistakes = Integer.parseInt(input.getText()
						.toString());
				saveSettings();
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

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				defaultDriveCycle = Integer
						.parseInt(input.getText().toString());
				saveSettings();
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
