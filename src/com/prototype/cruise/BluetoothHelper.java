package com.prototype.cruise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BluetoothHelper extends Activity {

	BluetoothBackEnd btb;

	// Declare & initialize preference variables and set defaults.
	int defaultRange = 120;

	// Declare & initialize data variables.
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

	BackgroundCalc bc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// fullscreen
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		//		WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

	@SuppressWarnings("deprecation")
	public void drawBackground() {
		relativeRange = (double) currentRange / (double) defaultRange;
		bc = new BackgroundCalc(relativeRange);
		ll.setBackgroundDrawable(bc.getGradient());
	}
}
