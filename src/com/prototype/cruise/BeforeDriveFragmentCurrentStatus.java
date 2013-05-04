package com.prototype.cruise;

import java.util.concurrent.TimeUnit;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BeforeDriveFragmentCurrentStatus extends Fragment implements
		OnClickListener {

	// Declare & initialize logging variable.
	private static final String TAG = "BeforeDriveFragment";

	// Declare parent activity.
	FragmentActivity beforeDriveActivity;

	// Declare background.
	LinearLayout ll;

	// Declare bars.
	LinearLayout llBar1;
	LinearLayout llBar2;
	LinearLayout llBar3;
	LinearLayout llBar4;
	LinearLayout llBar5;
	LinearLayout llBar6;
	LinearLayout llBar7;
	LinearLayout llBar8;
	LinearLayout llBar9;

	// Declare textviews.
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

	// Declare simulation views.
	EditText etDistance;
	EditText etAccMistakes;
	EditText etBrakeMistakes;
	EditText etSpeedMistakes;
	Button bDrive;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_before_drive_current_status,
						container, false);
		init(view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setFonts();
		setTextViews();
		drawBars();
		drawBackground();
	}

	public void init(View v) {
		// Initialize parent activity.
		beforeDriveActivity = (BeforeDriveActivity) getActivity();

		// Initialize background view.
		ll = (LinearLayout) v.findViewById(R.id.ll_main);

		// Initialize bars.
		llBar1 = (LinearLayout) v.findViewById(R.id.bar1);
		llBar2 = (LinearLayout) v.findViewById(R.id.bar2);
		llBar3 = (LinearLayout) v.findViewById(R.id.bar3);
		llBar4 = (LinearLayout) v.findViewById(R.id.bar4);
		llBar5 = (LinearLayout) v.findViewById(R.id.bar5);
		llBar6 = (LinearLayout) v.findViewById(R.id.bar6);
		llBar7 = (LinearLayout) v.findViewById(R.id.bar7);
		llBar8 = (LinearLayout) v.findViewById(R.id.bar8);
		llBar9 = (LinearLayout) v.findViewById(R.id.bar9);

		// Initialize textviews.
		tvStartingRange = (TextView) v.findViewById(R.id.tv_starting_range);
		tvLogo = (TextView) v.findViewById(R.id.tv_logo);
		tvEstRangeRemain = (TextView) v.findViewById(R.id.tv_est_range_remain);
		tvKm1 = (TextView) v.findViewById(R.id.tv_km);
		tvKm2 = (TextView) v.findViewById(R.id.tv_highway_km);
		tvKm3 = (TextView) v.findViewById(R.id.tv_city_km);
		tvHighwayDesc = (TextView) v.findViewById(R.id.tv_highway_desc);
		tvHighwayRange = (TextView) v.findViewById(R.id.tv_highway_range);
		tvCityDesc = (TextView) v.findViewById(R.id.tv_city_desc);
		tvCityRange = (TextView) v.findViewById(R.id.tv_city_range);
		tvTimeParked = (TextView) v.findViewById(R.id.tv_time_parked);
		tvChargeGained = (TextView) v.findViewById(R.id.tv_charge_gained);

		// Initialize edittexts.
		etDistance = (EditText) v.findViewById(R.id.et_distance);
		etAccMistakes = (EditText) v.findViewById(R.id.et_acc_mistakes);
		etBrakeMistakes = (EditText) v.findViewById(R.id.et_brake_mistakes);
		etSpeedMistakes = (EditText) v.findViewById(R.id.et_speed_mistakes);

		// Initialize button and set listener.
		bDrive = (Button) v.findViewById(R.id.b_drive);
		bDrive.setOnClickListener(this);
	}

	public void setFonts() {
		// Initialize typefaces.
		Typeface tfHelvetica = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/helvetica_bold_oblique.ttf");
		Typeface tfMyriadRegular = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_regular.otf");
		Typeface tfMyriadItalic = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_italic.otf");

		// Set correct fonts to views.
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

	public void setTextViews() {
		// Set textviews to display correct information.
		if (BeforeDriveActivity.firstTime) {
			tvTimeParked.setText("Time Parked: 00:00:00");
		} else {
			tvTimeParked.setText("Time Parked: "
					+ setTime(BeforeDriveActivity.timeDifference) + "");
		}
		tvStartingRange.setText("" + BeforeDriveActivity.currentRange + "");
		tvHighwayRange.setText(""
				+ (int) (BeforeDriveActivity.currentRange * 0.8) + "");
		tvCityRange.setText("" + BeforeDriveActivity.currentRange + "");
		tvChargeGained.setText("Charge Gained: "
				+ BeforeDriveActivity.chargedRange + " km");
	}

	public String setTime(long l) {
		// Returns a string with correct time formatting.
		final long days = TimeUnit.MILLISECONDS.toDays(l);
		final long hrs = TimeUnit.MILLISECONDS.toHours(l
				- TimeUnit.DAYS.toMillis(days));
		final long mins = TimeUnit.MILLISECONDS.toMinutes(l
				- TimeUnit.DAYS.toMillis(days) - TimeUnit.HOURS.toMillis(hrs));
		return String.format("%02d:%02d:%02d", days, hrs, mins);
	}

	public void drawBars() {
		// Check relative range and set bars accordingly.
		if (BeforeDriveActivity.relativeRange <= 0.9
				&& BeforeDriveActivity.relativeRange > 0.8) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (BeforeDriveActivity.relativeRange <= 0.8
				&& BeforeDriveActivity.relativeRange > 0.7) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (BeforeDriveActivity.relativeRange <= 0.7
				&& BeforeDriveActivity.relativeRange > 0.6) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (BeforeDriveActivity.relativeRange <= 0.6
				&& BeforeDriveActivity.relativeRange > 0.5) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (BeforeDriveActivity.relativeRange <= 0.5
				&& BeforeDriveActivity.relativeRange > 0.4) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (BeforeDriveActivity.relativeRange <= 0.4
				&& BeforeDriveActivity.relativeRange > 0.3) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (BeforeDriveActivity.relativeRange <= 0.3
				&& BeforeDriveActivity.relativeRange > 0.2) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (BeforeDriveActivity.relativeRange <= 0.2
				&& BeforeDriveActivity.relativeRange > 0.1) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
			llBar8.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (BeforeDriveActivity.relativeRange <= 0.1
				&& BeforeDriveActivity.relativeRange >= 0) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
			llBar8.setBackgroundResource(R.drawable.whiteemptybar);
			llBar9.setBackgroundResource(R.drawable.whiteemptybar);
		}
	}

	public void drawBackground() {
		// Check relative range and set background accordingly.
		// The transition is animated.
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				Log.d(TAG, "" + BeforeDriveActivity.previousRelativeRange
						+ " | " + BeforeDriveActivity.relativeRange + "");
				double rr = BeforeDriveActivity.previousRelativeRange;
				while (rr <= BeforeDriveActivity.relativeRange) {
					Message msg = new Message();
					msg.obj = setGradient(rr);
					bgHandler.sendMessage(msg);
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					rr = rr + 0.01;
				}
			}
		};
		new Thread(runnable).start();
	}

	public GradientDrawable setGradient(double rr) {
		// Returns a gradient drawable based in relative range.
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

	// UI handler for changing gradient.
	final Handler bgHandler = new Handler() {
		public void handleMessage(Message msg) {
			ll.setBackgroundDrawable((Drawable) msg.obj);
		}
	};

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.b_drive:
			BeforeDriveActivity.driveLength = Integer.parseInt(etDistance
					.getText().toString());
			BeforeDriveActivity.accMistakes = Integer.parseInt(etAccMistakes
					.getText().toString());
			BeforeDriveActivity.brakeMistakes = Integer
					.parseInt(etBrakeMistakes.getText().toString());
			BeforeDriveActivity.speedMistakes = Integer
					.parseInt(etSpeedMistakes.getText().toString());
			((BeforeDriveActivity) beforeDriveActivity).saveData();
			Intent i = new Intent(getActivity().getApplicationContext(),
					AfterDriveActivity.class);
			startActivity(i);
			getActivity().finish();
			break;
		}
	}

}