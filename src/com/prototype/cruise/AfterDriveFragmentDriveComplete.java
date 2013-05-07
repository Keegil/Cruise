package com.prototype.cruise;

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
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Animation.AnimationListener;
import android.widget.LinearLayout;
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

	// Declare hint.
	TextView tvHint;
	AnimationSet animation;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(
				R.layout.frag_after_drive_completed, container, false);
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
		tvStars.setTypeface(tfMyriadRegular);
		tvChargeLeft.setTypeface(tfMyriadRegular);
		tvHint.setTypeface(tfMyriadRegular);
	}

	public void setTextViews() {
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

		int accumulatedScore = AfterDriveActivity.accScore
				+ AfterDriveActivity.brakeScore + AfterDriveActivity.speedScore
				+ AfterDriveActivity.routeScore;
		tvStars.setText("" + accumulatedScore + "/20");
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
					msg.obj = setGradient(rr);
					bgHandler.sendMessage(msg);
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					rr = rr - 0.01;
				}
			}
		};
		new Thread(runnable).start();
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

	static final Handler bgHandler = new Handler() {
		public void handleMessage(Message msg) {
			ll.setBackgroundDrawable((Drawable) msg.obj);
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
