package com.prototype.cruise;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AfterDriveFragment2 extends Fragment {

	// declare logging variables
	private static final String TAG = "DriveActivity";

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

	// declare score variables
	public static final String SCORE_NAME = "MyScoreFile";
	int accScore = 0;
	int brakeScore = 0;
	int speedScore = 0;
	int routeScore = 0;

	// declare date setting
	public static final String DATE_NAME = "MyDateFile";
	long lastTime = 0;

	// declare database
	private GPSDataSource gpsDataSource;
	private DrivingStatsDataSource statSource;

	// declare textviews
	TextView tvEndingRange;
	TextView tvKm;
	TextView tvEstRangeRemain;
	TextView tvSpeed;
	TextView tvAcc;
	TextView tvBrake;
	TextView tvRoute;
	TextView tvStartRangeDesc;
	TextView tvRangeDecreaseDesc;
	TextView tvDistanceTraveledDesc;
	TextView tvStartRange;
	TextView tvRangeDecrease;
	TextView tvDistanceTraveled;

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

	// declare stars
	ImageView ivSpeedStar1;
	ImageView ivSpeedStar2;
	ImageView ivSpeedStar3;
	ImageView ivSpeedStar4;
	ImageView ivSpeedStar5;
	ImageView ivAccStar1;
	ImageView ivAccStar2;
	ImageView ivAccStar3;
	ImageView ivAccStar4;
	ImageView ivAccStar5;
	ImageView ivBrakeStar1;
	ImageView ivBrakeStar2;
	ImageView ivBrakeStar3;
	ImageView ivBrakeStar4;
	ImageView ivBrakeStar5;
	ImageView ivRouteStar1;
	ImageView ivRouteStar2;
	ImageView ivRouteStar3;
	ImageView ivRouteStar4;
	ImageView ivRouteStar5;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_after_drive_2,
				container, false);
		init(view);
		setFonts();
		loadSettings();
		loadData();
		loadScore();
		drawBars();
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setTextViews();
		setStars();
	}

	public void init(View v) {
		// initialize bars
		llBar1 = (LinearLayout) v.findViewById(R.id.bar1_after2);
		llBar2 = (LinearLayout) v.findViewById(R.id.bar2_after2);
		llBar3 = (LinearLayout) v.findViewById(R.id.bar3_after2);
		llBar4 = (LinearLayout) v.findViewById(R.id.bar4_after2);
		llBar5 = (LinearLayout) v.findViewById(R.id.bar5_after2);
		llBar6 = (LinearLayout) v.findViewById(R.id.bar6_after2);
		llBar7 = (LinearLayout) v.findViewById(R.id.bar7_after2);
		llBar8 = (LinearLayout) v.findViewById(R.id.bar8_after2);
		llBar9 = (LinearLayout) v.findViewById(R.id.bar9_after2);

		// initialize background
		ll = (LinearLayout) v.findViewById(R.id.ll_main_after2);

		// initialize textviews
		tvEndingRange = (TextView) v.findViewById(R.id.tv_ending_range);
		tvKm = (TextView) v.findViewById(R.id.tv_km_after);
		tvEstRangeRemain = (TextView) v
				.findViewById(R.id.tv_est_range_remain_after);
		tvSpeed = (TextView) v.findViewById(R.id.tv_speed_desc);
		tvAcc = (TextView) v.findViewById(R.id.tv_acc_desc);
		tvBrake = (TextView) v.findViewById(R.id.tv_brake_desc);
		tvRoute = (TextView) v.findViewById(R.id.tv_route_desc);
		tvStartRangeDesc = (TextView) v.findViewById(R.id.tv_start_range_desc);
		tvRangeDecreaseDesc = (TextView) v
				.findViewById(R.id.tv_range_decrease_desc);
		tvDistanceTraveledDesc = (TextView) v
				.findViewById(R.id.tv_distance_traveled_desc);
		tvStartRange = (TextView) v.findViewById(R.id.tv_start_range);
		tvRangeDecrease = (TextView) v.findViewById(R.id.tv_range_decrease);
		tvDistanceTraveled = (TextView) v
				.findViewById(R.id.tv_distance_traveled);

		// initialize stars
		ivSpeedStar1 = (ImageView) v.findViewById(R.id.iv_speed_star1);
		ivSpeedStar2 = (ImageView) v.findViewById(R.id.iv_speed_star2);
		ivSpeedStar3 = (ImageView) v.findViewById(R.id.iv_speed_star3);
		ivSpeedStar4 = (ImageView) v.findViewById(R.id.iv_speed_star4);
		ivSpeedStar5 = (ImageView) v.findViewById(R.id.iv_speed_star5);
		ivAccStar1 = (ImageView) v.findViewById(R.id.iv_acc_star1);
		ivAccStar2 = (ImageView) v.findViewById(R.id.iv_acc_star2);
		ivAccStar3 = (ImageView) v.findViewById(R.id.iv_acc_star3);
		ivAccStar4 = (ImageView) v.findViewById(R.id.iv_acc_star4);
		ivAccStar5 = (ImageView) v.findViewById(R.id.iv_acc_star5);
		ivBrakeStar1 = (ImageView) v.findViewById(R.id.iv_brake_star1);
		ivBrakeStar2 = (ImageView) v.findViewById(R.id.iv_brake_star2);
		ivBrakeStar3 = (ImageView) v.findViewById(R.id.iv_brake_star3);
		ivBrakeStar4 = (ImageView) v.findViewById(R.id.iv_brake_star4);
		ivBrakeStar5 = (ImageView) v.findViewById(R.id.iv_brake_star5);
		ivRouteStar1 = (ImageView) v.findViewById(R.id.iv_route_star1);
		ivRouteStar2 = (ImageView) v.findViewById(R.id.iv_route_star2);
		ivRouteStar3 = (ImageView) v.findViewById(R.id.iv_route_star3);
		ivRouteStar4 = (ImageView) v.findViewById(R.id.iv_route_star4);
		ivRouteStar5 = (ImageView) v.findViewById(R.id.iv_route_star5);
	}

	public void setTextViews() {
		tvEndingRange.setText("" + currentRange + "");
		int startingRange = currentRange + rangeUsed;
		tvStartRange.setText("" + startingRange + " km");
		tvRangeDecrease.setText("" + rangeUsed + " km");
		tvDistanceTraveled.setText("" + driveLength + " km");
	}

	public void setStars() {

		// Set speed stars
		if (speedScore == 1) {
			ivSpeedStar1.setImageResource(R.drawable.starfilled);
		} else if (speedScore == 2) {
			ivSpeedStar1.setImageResource(R.drawable.starfilled);
			ivSpeedStar2.setImageResource(R.drawable.starfilled);
		} else if (speedScore == 3) {
			ivSpeedStar1.setImageResource(R.drawable.starfilled);
			ivSpeedStar2.setImageResource(R.drawable.starfilled);
			ivSpeedStar3.setImageResource(R.drawable.starfilled);
		} else if (speedScore == 4) {
			ivSpeedStar1.setImageResource(R.drawable.starfilled);
			ivSpeedStar2.setImageResource(R.drawable.starfilled);
			ivSpeedStar3.setImageResource(R.drawable.starfilled);
			ivSpeedStar4.setImageResource(R.drawable.starfilled);
		} else if (speedScore == 5) {
			ivSpeedStar1.setImageResource(R.drawable.starfilled);
			ivSpeedStar2.setImageResource(R.drawable.starfilled);
			ivSpeedStar3.setImageResource(R.drawable.starfilled);
			ivSpeedStar4.setImageResource(R.drawable.starfilled);
			ivSpeedStar5.setImageResource(R.drawable.starfilled);
		}

		// Set acceleration stars
		if (accScore == 1) {
			ivAccStar1.setImageResource(R.drawable.starfilled);
		} else if (accScore == 2) {
			ivAccStar1.setImageResource(R.drawable.starfilled);
			ivAccStar2.setImageResource(R.drawable.starfilled);
		} else if (accScore == 3) {
			ivAccStar1.setImageResource(R.drawable.starfilled);
			ivAccStar2.setImageResource(R.drawable.starfilled);
			ivAccStar3.setImageResource(R.drawable.starfilled);
		} else if (accScore == 4) {
			ivAccStar1.setImageResource(R.drawable.starfilled);
			ivAccStar2.setImageResource(R.drawable.starfilled);
			ivAccStar3.setImageResource(R.drawable.starfilled);
			ivAccStar4.setImageResource(R.drawable.starfilled);
		} else if (accScore == 5) {
			ivAccStar1.setImageResource(R.drawable.starfilled);
			ivAccStar2.setImageResource(R.drawable.starfilled);
			ivAccStar3.setImageResource(R.drawable.starfilled);
			ivAccStar4.setImageResource(R.drawable.starfilled);
			ivAccStar5.setImageResource(R.drawable.starfilled);
		}

		// Set brake stars
		if (brakeScore == 1) {
			ivBrakeStar1.setImageResource(R.drawable.starfilled);
		} else if (brakeScore == 2) {
			ivBrakeStar1.setImageResource(R.drawable.starfilled);
			ivBrakeStar2.setImageResource(R.drawable.starfilled);
		} else if (brakeScore == 3) {
			ivBrakeStar1.setImageResource(R.drawable.starfilled);
			ivBrakeStar2.setImageResource(R.drawable.starfilled);
			ivBrakeStar3.setImageResource(R.drawable.starfilled);
		} else if (brakeScore == 4) {
			ivBrakeStar1.setImageResource(R.drawable.starfilled);
			ivBrakeStar2.setImageResource(R.drawable.starfilled);
			ivBrakeStar3.setImageResource(R.drawable.starfilled);
			ivBrakeStar4.setImageResource(R.drawable.starfilled);
		} else if (brakeScore == 5) {
			ivBrakeStar1.setImageResource(R.drawable.starfilled);
			ivBrakeStar2.setImageResource(R.drawable.starfilled);
			ivBrakeStar3.setImageResource(R.drawable.starfilled);
			ivBrakeStar4.setImageResource(R.drawable.starfilled);
			ivBrakeStar5.setImageResource(R.drawable.starfilled);
		}

		// Set route stars
		if (routeScore == 1) {
			ivRouteStar1.setImageResource(R.drawable.starfilled);
		} else if (routeScore == 2) {
			ivRouteStar1.setImageResource(R.drawable.starfilled);
			ivRouteStar2.setImageResource(R.drawable.starfilled);
		} else if (routeScore == 3) {
			ivRouteStar1.setImageResource(R.drawable.starfilled);
			ivRouteStar2.setImageResource(R.drawable.starfilled);
			ivRouteStar3.setImageResource(R.drawable.starfilled);
		} else if (routeScore == 4) {
			ivRouteStar1.setImageResource(R.drawable.starfilled);
			ivRouteStar2.setImageResource(R.drawable.starfilled);
			ivRouteStar3.setImageResource(R.drawable.starfilled);
			ivRouteStar4.setImageResource(R.drawable.starfilled);
		} else if (routeScore == 5) {
			ivRouteStar1.setImageResource(R.drawable.starfilled);
			ivRouteStar2.setImageResource(R.drawable.starfilled);
			ivRouteStar3.setImageResource(R.drawable.starfilled);
			ivRouteStar4.setImageResource(R.drawable.starfilled);
			ivRouteStar5.setImageResource(R.drawable.starfilled);
		}
	}

	public void setFonts() {
		// initialize typefaces
		Typeface tfHelvetica = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/helvetica_bold_oblique.ttf");
		Typeface tfMyriadRegular = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_regular.otf");
		Typeface tfMyriadItalic = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_italic.otf");

		// set correct fonts to views
		tvEndingRange.setTypeface(tfHelvetica);
		tvKm.setTypeface(tfMyriadRegular);
		tvEstRangeRemain.setTypeface(tfMyriadItalic);
		tvSpeed.setTypeface(tfMyriadItalic);
		tvAcc.setTypeface(tfMyriadItalic);
		tvBrake.setTypeface(tfMyriadItalic);
		tvRoute.setTypeface(tfMyriadItalic);
		tvStartRangeDesc.setTypeface(tfMyriadRegular);
		tvRangeDecreaseDesc.setTypeface(tfMyriadRegular);
		tvDistanceTraveledDesc.setTypeface(tfMyriadRegular);
		tvStartRange.setTypeface(tfMyriadRegular);
		tvRangeDecrease.setTypeface(tfMyriadRegular);
		tvDistanceTraveled.setTypeface(tfMyriadRegular);
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

	public void drawBars() {
		// check relative range and set background and bars accordingly
		double doubleCurrentRange = (double) currentRange;
		double doubleDefaultRange = (double) defaultRange;
		double relativeRange = doubleCurrentRange / doubleDefaultRange;
		if (relativeRange <= 1 && relativeRange > 0.9) {

		} else if (relativeRange <= 0.9 && relativeRange > 0.8) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (relativeRange <= 0.8 && relativeRange > 0.7) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (relativeRange <= 0.7 && relativeRange > 0.6) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (relativeRange <= 0.6 && relativeRange > 0.5) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (relativeRange <= 0.5 && relativeRange > 0.4) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (relativeRange <= 0.4 && relativeRange > 0.3) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (relativeRange <= 0.3 && relativeRange > 0.2) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (relativeRange <= 0.2 && relativeRange > 0.1) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
			llBar8.setBackgroundResource(R.drawable.whiteemptybar);
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
		}
		ll.setBackgroundDrawable(setGradient(relativeRange));
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

	public void loadScore() {
		SharedPreferences data = getActivity().getSharedPreferences(SCORE_NAME,
				0);
		accScore = data.getInt("accScore", accScore);
		brakeScore = data.getInt("brakeScore", brakeScore);
		speedScore = data.getInt("speedScore", speedScore);
		routeScore = data.getInt("routeScore", routeScore);
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
