package com.prototype.cruise;

import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AfterDriveFragmentDriveDetails extends Fragment implements
		OnClickListener {

	// Declare logging variable.
	private static final String TAG = "AfterDriveFragmentDriveDetails";

	// Declare parent activity.
	FragmentActivity afterDriveActivity;

	// Declare background and gradient.
	LinearLayout ll;
	GradientDrawable gdBackground;

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

	// Declare TextViews.
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

	// Declare icons.
	ImageView ivSpeed;
	ImageView ivAcc;
	ImageView ivRoute;
	ImageView ivBrake;

	// Declare stars.
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

	// Declare hint.
	TextView tvHint;
	AnimationSet animation;

	BackgroundCalc bc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_after_drive_details,
				container, false);
		init(view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setFonts();
	}

	@Override
	public void onResume() {
		super.onResume();
		setTextViews();
		setStars();
		drawBars();
		drawBackground();
		showHint();
	}

	public void init(View v) {
		// Initialize bars.
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

		// Initialize ImageViews and set OnClickListeners.
		ivSpeed = (ImageView) v.findViewById(R.id.iv_speed);
		ivAcc = (ImageView) v.findViewById(R.id.iv_acc);
		ivRoute = (ImageView) v.findViewById(R.id.iv_route);
		ivBrake = (ImageView) v.findViewById(R.id.iv_brake);
		ivSpeed.setOnClickListener(this);
		ivAcc.setOnClickListener(this);
		ivRoute.setOnClickListener(this);
		ivBrake.setOnClickListener(this);

		// Initialize stars.
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

		// Initialize hint and set background, animation and listener.
		tvHint = (TextView) v.findViewById(R.id.tv_after_drive_hint2);
		tvHint.getBackground().setAlpha(100);
		tvHint.setOnClickListener(this);
		animation = new AnimationSet(false);
	}

	public void setFonts() {
		// Declare & initialize typefaces.
		Typeface tfHelvetica = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/helvetica_bold_oblique.ttf");
		Typeface tfMyriadRegular = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_regular.otf");
		Typeface tfMyriadItalic = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_italic.otf");

		// Set correct fonts to views.
		tvEndingRange.setTypeface(tfHelvetica);
		tvKm.setTypeface(tfHelvetica);
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
		tvHint.setTypeface(tfMyriadRegular);
	}

	public void setTextViews() {
		// Set length of TextViews to avoid font clipping.
		int length = 0;
		int width = 0;
		int height = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_SP, 90, getResources()
						.getDisplayMetrics());
		RelativeLayout.LayoutParams layoutParams;
		length = String.valueOf(AfterDriveActivity.currentRange).length();
		Log.d(TAG, "" + length + "");
		if (length == 3) {
			width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
					130, getResources().getDisplayMetrics());
		} else if (length == 2) {
			width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
					90, getResources().getDisplayMetrics());
		} else if (length == 1) {
			width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
					48, getResources().getDisplayMetrics());
		}
		layoutParams = new RelativeLayout.LayoutParams(width, height);
		tvEndingRange.setLayoutParams(layoutParams);

		// Set TextViews to display correct information.
		tvEndingRange.setText("" + AfterDriveActivity.currentRange + "");
		int startingRange = AfterDriveActivity.currentRange
				+ AfterDriveActivity.rangeUsed
				+ AfterDriveActivity.deficitRange;
		tvStartRange.setText("" + startingRange + " km");
		tvRangeDecrease.setText("" + AfterDriveActivity.rangeUsed + " km");
		tvDistanceTraveled.setText("" + AfterDriveActivity.driveLength + " km");
	}

	public void setStars() {
		// Set speed stars.
		if (AfterDriveActivity.speedScore == 1) {
			ivSpeedStar1.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.speedScore == 2) {
			ivSpeedStar1.setImageResource(R.drawable.starfilled);
			ivSpeedStar2.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.speedScore == 3) {
			ivSpeedStar1.setImageResource(R.drawable.starfilled);
			ivSpeedStar2.setImageResource(R.drawable.starfilled);
			ivSpeedStar3.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.speedScore == 4) {
			ivSpeedStar1.setImageResource(R.drawable.starfilled);
			ivSpeedStar2.setImageResource(R.drawable.starfilled);
			ivSpeedStar3.setImageResource(R.drawable.starfilled);
			ivSpeedStar4.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.speedScore == 5) {
			ivSpeedStar1.setImageResource(R.drawable.starfilled);
			ivSpeedStar2.setImageResource(R.drawable.starfilled);
			ivSpeedStar3.setImageResource(R.drawable.starfilled);
			ivSpeedStar4.setImageResource(R.drawable.starfilled);
			ivSpeedStar5.setImageResource(R.drawable.starfilled);
		}

		// Set acceleration stars
		if (AfterDriveActivity.accScore == 1) {
			ivAccStar1.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.accScore == 2) {
			ivAccStar1.setImageResource(R.drawable.starfilled);
			ivAccStar2.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.accScore == 3) {
			ivAccStar1.setImageResource(R.drawable.starfilled);
			ivAccStar2.setImageResource(R.drawable.starfilled);
			ivAccStar3.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.accScore == 4) {
			ivAccStar1.setImageResource(R.drawable.starfilled);
			ivAccStar2.setImageResource(R.drawable.starfilled);
			ivAccStar3.setImageResource(R.drawable.starfilled);
			ivAccStar4.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.accScore == 5) {
			ivAccStar1.setImageResource(R.drawable.starfilled);
			ivAccStar2.setImageResource(R.drawable.starfilled);
			ivAccStar3.setImageResource(R.drawable.starfilled);
			ivAccStar4.setImageResource(R.drawable.starfilled);
			ivAccStar5.setImageResource(R.drawable.starfilled);
		}

		// Set brake stars
		if (AfterDriveActivity.brakeScore == 1) {
			ivBrakeStar1.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.brakeScore == 2) {
			ivBrakeStar1.setImageResource(R.drawable.starfilled);
			ivBrakeStar2.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.brakeScore == 3) {
			ivBrakeStar1.setImageResource(R.drawable.starfilled);
			ivBrakeStar2.setImageResource(R.drawable.starfilled);
			ivBrakeStar3.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.brakeScore == 4) {
			ivBrakeStar1.setImageResource(R.drawable.starfilled);
			ivBrakeStar2.setImageResource(R.drawable.starfilled);
			ivBrakeStar3.setImageResource(R.drawable.starfilled);
			ivBrakeStar4.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.brakeScore == 5) {
			ivBrakeStar1.setImageResource(R.drawable.starfilled);
			ivBrakeStar2.setImageResource(R.drawable.starfilled);
			ivBrakeStar3.setImageResource(R.drawable.starfilled);
			ivBrakeStar4.setImageResource(R.drawable.starfilled);
			ivBrakeStar5.setImageResource(R.drawable.starfilled);
		}

		// Set route stars
		if (AfterDriveActivity.routeScore == 1) {
			ivRouteStar1.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.routeScore == 2) {
			ivRouteStar1.setImageResource(R.drawable.starfilled);
			ivRouteStar2.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.routeScore == 3) {
			ivRouteStar1.setImageResource(R.drawable.starfilled);
			ivRouteStar2.setImageResource(R.drawable.starfilled);
			ivRouteStar3.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.routeScore == 4) {
			ivRouteStar1.setImageResource(R.drawable.starfilled);
			ivRouteStar2.setImageResource(R.drawable.starfilled);
			ivRouteStar3.setImageResource(R.drawable.starfilled);
			ivRouteStar4.setImageResource(R.drawable.starfilled);
		} else if (AfterDriveActivity.routeScore == 5) {
			ivRouteStar1.setImageResource(R.drawable.starfilled);
			ivRouteStar2.setImageResource(R.drawable.starfilled);
			ivRouteStar3.setImageResource(R.drawable.starfilled);
			ivRouteStar4.setImageResource(R.drawable.starfilled);
			ivRouteStar5.setImageResource(R.drawable.starfilled);
		}
	}

	public void drawBars() {
		// Check relative range and set bars accordingly.
		if (AfterDriveActivity.relativeRange <= 0.9
				&& AfterDriveActivity.relativeRange > 0.8) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (AfterDriveActivity.relativeRange <= 0.8
				&& AfterDriveActivity.relativeRange > 0.7) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (AfterDriveActivity.relativeRange <= 0.7
				&& AfterDriveActivity.relativeRange > 0.6) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (AfterDriveActivity.relativeRange <= 0.6
				&& AfterDriveActivity.relativeRange > 0.5) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (AfterDriveActivity.relativeRange <= 0.5
				&& AfterDriveActivity.relativeRange > 0.4) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (AfterDriveActivity.relativeRange <= 0.4
				&& AfterDriveActivity.relativeRange > 0.3) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (AfterDriveActivity.relativeRange <= 0.3
				&& AfterDriveActivity.relativeRange > 0.2) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (AfterDriveActivity.relativeRange <= 0.2
				&& AfterDriveActivity.relativeRange > 0.1) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
			llBar8.setBackgroundResource(R.drawable.whiteemptybar);
		} else if (AfterDriveActivity.relativeRange <= 0.1
				&& AfterDriveActivity.relativeRange >= 0) {
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

	@SuppressWarnings("deprecation")
	public void drawBackground() {
		bc = new BackgroundCalc(AfterDriveActivity.relativeRange);
		ll.setBackgroundDrawable(bc
				.makeGradient(AfterDriveActivity.relativeRange));
	}

	public void showHint() {
		Log.d(TAG, "" + AfterDriveActivity.firstTime + "");
		if (AfterDriveActivity.firstTime) {
			Animation fadeIn = new AlphaAnimation(0, 1);
			fadeIn.setInterpolator(new DecelerateInterpolator());
			fadeIn.setStartOffset(2000);
			fadeIn.setDuration(1000);

			Animation fadeOut = new AlphaAnimation(1, 0);
			fadeOut.setInterpolator(new AccelerateInterpolator());
			fadeOut.setStartOffset(8000);
			fadeOut.setDuration(1000);

			animation.addAnimation(fadeIn);
			animation.addAnimation(fadeOut);
			animation.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationEnd(Animation animation) {
					tvHint.setVisibility(View.GONE);
				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationStart(Animation animation) {

				}
			});
			tvHint.startAnimation(animation);
		} else {
			tvHint.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View view) {
		Intent i;
		switch (view.getId()) {
		case R.id.tv_after_drive_hint2:
			Log.d(TAG, "lol");
			tvHint.clearAnimation();
			tvHint.setVisibility(View.GONE);
			break;
		case R.id.iv_speed:
			i = new Intent(getActivity().getApplicationContext(),
					HelpActivity.class);
			i.putExtra("helpData", 0);
			startActivity(i);
			break;
		case R.id.iv_acc:
			i = new Intent(getActivity().getApplicationContext(),
					HelpActivity.class);
			i.putExtra("helpData", 1);
			startActivity(i);
			break;
		case R.id.iv_route:
			i = new Intent(getActivity().getApplicationContext(),
					HelpActivity.class);
			i.putExtra("helpData", 2);
			startActivity(i);
			break;
		case R.id.iv_brake:
			i = new Intent(getActivity().getApplicationContext(),
					HelpActivity.class);
			i.putExtra("helpData", 3);
			startActivity(i);
			break;
		}
	}
}
