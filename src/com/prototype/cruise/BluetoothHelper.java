package com.prototype.cruise;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BluetoothHelper extends Activity {

	BluetoothBackEnd btb;

	// Declare & initialize preference variables and set defaults.
	final String PREFS_NAME = "MyPrefsFile";
	int defaultRange = 120;

	// Declare & initialize data variables.
	final String DATA_NAME = "MyDataFile";
	int currentRange = 120;

	double relativeRange;

	// Declare TextViews.
	TextView tvLogo;
	TextView tvBeforeStart;
	TextView tvSettings;

	TextView tvScan;
	TextView tvPin;
	TextView tvStart;

	TextView tvSense;
	TextView tvGoto;
	
	LinearLayout ll;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_helper);
		btb = new BluetoothBackEnd(this);
		init();
	}

	public void openBluetoothSettings(View v) {
		Log.d("bt", "bt settings clicked");
		Intent intentBluetooth = new Intent();
		intentBluetooth
				.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
		startActivity(intentBluetooth);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (btb.isPaired()) {
			finish();
		}

	}

	public void init() {
		ll = (LinearLayout) findViewById(R.id.ll_main);
		tvLogo = (TextView) findViewById(R.id.tv_logo);
		tvBeforeStart = (TextView) findViewById(R.id.tv_before_start);
		tvSettings = (TextView) findViewById(R.id.tv_2_settings);
		tvScan = (TextView) findViewById(R.id.tv_3_scan);
		tvPin = (TextView) findViewById(R.id.tv_4_pin);
		tvStart = (TextView) findViewById(R.id.tv_1_start_engine);
		tvSense = (TextView) findViewById(R.id.tv_senseboard);
		tvGoto = (TextView) findViewById(R.id.tv_goto_settings);

		setFonts();
		drawBackground();
		
	}

	public void setFonts() {
		// Declare & initialize Typefaces.
		Typeface tfHelvetica = Typeface.createFromAsset(this.getAssets(),
				"fonts/helvetica_bold_oblique.ttf");
		Typeface tfMyriadRegular = Typeface.createFromAsset(this.getAssets(),
				"fonts/myriad_regular.otf");

		// Set correct fonts to views.
		tvLogo.setTypeface(tfHelvetica);
		tvBeforeStart.setTypeface(tfMyriadRegular);
		tvSettings.setTypeface(tfMyriadRegular);
		tvScan.setTypeface(tfMyriadRegular);
		tvPin.setTypeface(tfMyriadRegular);
		tvStart.setTypeface(tfMyriadRegular);
		tvSense.setTypeface(tfMyriadRegular);
		tvGoto.setTypeface(tfMyriadRegular);
	}

	public void drawBackground() {
		relativeRange = (double) currentRange / (double) defaultRange;
		ll.setBackgroundDrawable(setGradient(relativeRange));
	}

	public GradientDrawable setGradient(double rr) {
		// Returns a GradientDrawable based in relative range.
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

	public void loadSettings() {
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		defaultRange = settings.getInt("defaultRange", defaultRange);
	}

	public void loadData() {
		SharedPreferences data = getSharedPreferences(DATA_NAME, 0);
		currentRange = data.getInt("currentRange", currentRange);
	}

}
