package com.prototype.cruise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SplashActivity extends Activity {

	// Declare & initialize preference variables and set defaults.
	int defaultRange = 120;

	// Declare & initialize data variables.
	int currentRange = 120;

	double relativeRange;

	LinearLayout ll;
	ImageView ivLogo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set fullscreen.
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		// getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		//		WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// Set content.
		setContentView(R.layout.activity_splash);

		final SplashActivity sPlashScreen = this;

		ll = (LinearLayout) findViewById(R.id.ll_main);
		ivLogo = (ImageView) findViewById(R.id.iv_logo);
		
		// set and start timer
		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Intent i = new Intent();
					i.setClass(sPlashScreen, BeforeDriveActivity.class);
					startActivity(i);
					finish();
				}
			}
		};
		timer.start();
	}
}