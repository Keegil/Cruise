package com.prototype.cruise;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AfterDriveFragmentDriveComplete extends Fragment implements
		OnClickListener {

	// declare logging variables
	private static final String TAG = "AfterDriveFragmentDriveComplete";

	// Declare parent activity.
	FragmentActivity afterDriveActivity;

	// Declare background.
	static LinearLayout ll;

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
	TextView tvLogo;
	TextView tvDriveCompleted;
	TextView tvStars;
	TextView tvChargeLeft;
	TextView tvMaxStars;

	// Declare hint.
	TextView tvHint;
	AnimationSet animation;

	static double redStop;
	static double greenStop;
	static double blueStop;

	static BackgroundCalc bc = new BackgroundCalc();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_after_drive_completed,
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
		drawBars();
		drawBackground();
		showHint();
	}

	public void init(View v) {
		// Initialize bars.
		llBar1 = (LinearLayout) v.findViewById(R.id.bar1_after);
		llBar2 = (LinearLayout) v.findViewById(R.id.bar2_after);
		llBar3 = (LinearLayout) v.findViewById(R.id.bar3_after);
		llBar4 = (LinearLayout) v.findViewById(R.id.bar4_after);
		llBar5 = (LinearLayout) v.findViewById(R.id.bar5_after);
		llBar6 = (LinearLayout) v.findViewById(R.id.bar6_after);
		llBar7 = (LinearLayout) v.findViewById(R.id.bar7_after);
		llBar8 = (LinearLayout) v.findViewById(R.id.bar8_after);
		llBar9 = (LinearLayout) v.findViewById(R.id.bar9_after);

		// Initialize background.
		ll = (LinearLayout) v.findViewById(R.id.ll_main_after);

		// Initialize TextViews.
		tvLogo = (TextView) v.findViewById(R.id.tv_logo_after);
		tvStars = (TextView) v.findViewById(R.id.tv_stars);
		tvChargeLeft = (TextView) v.findViewById(R.id.tv_charge_left);
		tvDriveCompleted = (TextView) v.findViewById(R.id.tv_drive_completed);
		tvMaxStars = (TextView) v.findViewById(R.id.tv_stars_max);

		// Initialize hint and set background, animation and listener.
		tvHint = (TextView) v.findViewById(R.id.tv_after_drive_hint1);
		tvHint.getBackground().setAlpha(100);
		tvHint.setOnClickListener(this);
		animation = new AnimationSet(false);
	}

	public void setFonts() {
		// Declare & initialize Typefaces.
		Typeface tfHelvetica = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/helvetica_bold_oblique.ttf");
		Typeface tfMyriadRegular = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_regular.otf");

		// Set correct fonts to views.
		tvLogo.setTypeface(tfHelvetica);
		tvDriveCompleted.setTypeface(tfMyriadRegular);
		tvStars.setTypeface(tfHelvetica);
		tvChargeLeft.setTypeface(tfMyriadRegular);
		tvHint.setTypeface(tfMyriadRegular);
		tvMaxStars.setTypeface(tfHelvetica);
	}

	public void setTextViews() {
		// Calculate accumulated score.
		int accumulatedScore = AfterDriveActivity.accScore
				+ AfterDriveActivity.brakeScore + AfterDriveActivity.speedScore
				+ AfterDriveActivity.routeScore;

		// Set length of TextViews to avoid font clipping.
		int length = 0;
		int width = 0;
		RelativeLayout.LayoutParams layoutParams;
		length = String.valueOf(accumulatedScore).length();
		Log.d(TAG, "" + length + "");
		if (length == 2) {
			width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
					100, getResources().getDisplayMetrics());
		} else if (length == 1) {
			width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
					48, getResources().getDisplayMetrics());
		}
		layoutParams = new RelativeLayout.LayoutParams(width,
				LayoutParams.WRAP_CONTENT);
		// tvStars.setLayoutParams(layoutParams);

		// Set TextViews to display correct information.
		if (AfterDriveActivity.relativeRange >= 0.6) {
			tvChargeLeft
					.setText(getResources().getString(R.string.good_charge));
		} else if (AfterDriveActivity.relativeRange < 0.6
				&& AfterDriveActivity.relativeRange >= 0.15) {
			tvChargeLeft.setText(getResources().getString(R.string.bad_charge));
		} else if (AfterDriveActivity.relativeRange < 0.15) {
			tvChargeLeft.setText(getResources().getString(R.string.no_charge));
		}

		tvStars.setText("" + accumulatedScore + "");
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

	public void drawBackground() {
		// Check relative range and set background accordingly.
		// The transition is animated.
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				double rr = AfterDriveActivity.previousRelativeRange;
				while (rr > AfterDriveActivity.relativeRange) {
					Message msg = new Message();
					// msg.obj = setGradient(rr);
					msg.obj = bc.makeGradient(rr);
					bgHandler.sendMessage(msg);
					try {
						Thread.sleep(2);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					rr = rr - 0.001;
				}
			}
		};
		new Thread(runnable).start();
	}

	static final Handler bgHandler = new Handler() {
		@SuppressWarnings("deprecation")
		public void handleMessage(Message msg) {
			ll.setBackgroundDrawable((Drawable) msg.obj);
			AfterDriveActivity.setBackgroundIndicator(bc.getStopRGB());
		}
	};

	public void showHint() {
		Log.d(TAG, "" + AfterDriveActivity.firstTime + "");
		if (AfterDriveActivity.firstTime) {
			Animation fadeIn = new AlphaAnimation(0, 1);
			fadeIn.setInterpolator(new DecelerateInterpolator());
			fadeIn.setDuration(1000);

			Animation fadeOut = new AlphaAnimation(1, 0);
			fadeOut.setInterpolator(new AccelerateInterpolator());
			fadeOut.setStartOffset(5000);
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
		switch (view.getId()) {
		case R.id.tv_after_drive_hint1:
			Log.d(TAG, "lol");
			tvHint.clearAnimation();
			tvHint.setVisibility(View.GONE);
			break;
		}
	}
}
