package com.prototype.cruise;

import java.util.ArrayList;
import java.util.List;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SummaryFragment extends Fragment {

	// Declare & initialize logging variable.
	private static final String TAG = "SummaryFragment";

	// Declare parent activity.
	FragmentActivity fragmentActivity;

	// Declare & initialize preference variables and set defaults.
	final String PREFS_NAME = "MyPrefsFile";
	int defaultRange = 120;

	// Declare & initialize data variables.
	final String DATA_NAME = "MyDataFile";
	int currentRange = 120;

	// Declare background.
	static LinearLayout ll;

	// Declare TextViews.
	TextView tvLogo;
	TextView tvEval;
	TextView tvCompScore;
	TextView tvCompPercent;
	TextView tvCompDesc;
	TextView tvMoneyScore;
	TextView tvMoneyKr;
	TextView tvMoneyDesc;
	TextView tvEffScore;
	TextView tvEffPercent;
	TextView tvEffDesc;
	TextView tvContinue;

	// Declare RelativeLayouts.
	RelativeLayout rlComp;
	RelativeLayout rlMoney;
	RelativeLayout rlEff;

	// Declare database variables.
	DrivingStatsDataSource drivingStatsDataSource;
	DrivingStats drivingStats;

	// Declare candidacy calculation variables.
	List<DrivingStats> drives = new ArrayList<DrivingStats>();

	double failTrain;
	int rating;
	double relativeRange;
	int totalDistance = 0;
	double gasCost = 0;

	BackgroundCalc bc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_summary, container, false);
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
		loadSettings();
		loadData();
		calc();
		setTextViews();
		drawBackground();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void init(View v) {
		// Initialize parent activity.
		fragmentActivity = getActivity();

		// Initialize background view.
		ll = (LinearLayout) v.findViewById(R.id.ll_main_summary);

		// Initialize TextViews.
		tvLogo = (TextView) v.findViewById(R.id.tv_logo_summary);
		tvEval = (TextView) v.findViewById(R.id.tv_eval);
		tvCompScore = (TextView) v.findViewById(R.id.tv_comp_score);
		tvCompPercent = (TextView) v.findViewById(R.id.tv_comp_percent);
		tvCompDesc = (TextView) v.findViewById(R.id.tv_comp_desc);
		tvMoneyScore = (TextView) v.findViewById(R.id.tv_money_score);
		tvMoneyKr = (TextView) v.findViewById(R.id.tv_money_kr);
		tvMoneyDesc = (TextView) v.findViewById(R.id.tv_money_desc);
		tvEffScore = (TextView) v.findViewById(R.id.tv_eff_score);
		tvEffPercent = (TextView) v.findViewById(R.id.tv_eff_percent);
		tvEffDesc = (TextView) v.findViewById(R.id.tv_eff_desc);
		tvContinue = (TextView) v.findViewById(R.id.tv_continue);

		// Initialize RelativeLayouts.
		rlComp = (RelativeLayout) v.findViewById(R.id.rl_comp);
		rlMoney = (RelativeLayout) v.findViewById(R.id.rl_money);
		rlEff = (RelativeLayout) v.findViewById(R.id.rl_eff);

		// Initialize database.
		drivingStatsDataSource = new DrivingStatsDataSource(fragmentActivity);
		drivingStatsDataSource.open();
	}

	public void setFonts() {
		// Initialize fonts.
		Typeface tfHelvetica = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/helvetica_bold_oblique.ttf");
		Typeface tfMyriadRegular = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_regular.otf");
		Typeface tfMyriadItalic = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_italic.otf");

		// Set correct fonts to views.
		tvLogo.setTypeface(tfHelvetica);
		tvEval.setTypeface(tfMyriadRegular);
		tvCompScore.setTypeface(tfHelvetica);
		tvCompPercent.setTypeface(tfHelvetica);
		tvCompDesc.setTypeface(tfMyriadItalic);
		tvMoneyScore.setTypeface(tfHelvetica);
		tvMoneyKr.setTypeface(tfHelvetica);
		tvMoneyDesc.setTypeface(tfMyriadItalic);
		tvEffScore.setTypeface(tfHelvetica);
		tvEffPercent.setTypeface(tfHelvetica);
		tvEffDesc.setTypeface(tfMyriadItalic);
		tvContinue.setTypeface(tfMyriadItalic);
	}

	public void calc() {
		// Reset all variables.
		int i = 0;
		int fails = 0;
		int stars = 0;
		totalDistance = 0;
		gasCost = 0;
		drives = drivingStatsDataSource.getAllDrivingStats();
		if (drives.size() != 0) {
			while (i < drives.size()) {
				drivingStats = drivingStatsDataSource.getDrivingStats(i + 1);
				if (drivingStats.getRangeEnd() == 0) {
					fails++;
				}
				stars = stars + drivingStats.getNumAccEvent()
						+ drivingStats.getNumBrakeEvent()
						+ drivingStats.getNumRouteEvent()
						+ drivingStats.getNumSpeedEvent();
				failTrain = (double) ((double) fails / (double) drives.size());
				double dStars = (double) stars;
				double dDrives = (double) drives.size();
				rating = (int) ((dStars / (dDrives * 20)) * 20);
				totalDistance = totalDistance + drivingStats.getDriveDistance();
				i++;
			}
			gasCost = totalDistance * 0.06 * 14;
		}
	}

	public void setTextViews() {
		if (drives.size() == 0) {
			rlComp.setVisibility(View.GONE);
			rlMoney.setVisibility(View.GONE);
			rlEff.setVisibility(View.GONE);
			tvContinue.setVisibility(View.GONE);
			tvEval.setText(getResources().getString(R.string.eval_none));
		} else {
			// Set length of TextViews to avoid font clipping.

			int length = 0;
			int width = 0;
			RelativeLayout.LayoutParams layoutParams;

			length = String.valueOf((int) ((1 - failTrain) * 100)).length();
			if (length == 3) {
				width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 59, getResources()
								.getDisplayMetrics());
			} else if (length == 2) {
				width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 41, getResources()
								.getDisplayMetrics());
			} else if (length == 1) {
				width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 22, getResources()
								.getDisplayMetrics());
			}
			Log.d(TAG, "Length: " + length + " | Width: " + width + "");
			layoutParams = new RelativeLayout.LayoutParams(width,
					LayoutParams.WRAP_CONTENT);
			tvCompScore.setLayoutParams(layoutParams);

			length = String.valueOf((int) gasCost).length();
			if (length == 4) {
				width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 78, getResources()
								.getDisplayMetrics());
			} else if (length == 3) {
				width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 59, getResources()
								.getDisplayMetrics());
			} else if (length == 2) {
				width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 41, getResources()
								.getDisplayMetrics());
			} else if (length == 1) {
				width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 22, getResources()
								.getDisplayMetrics());
			} else {
				width = 0;
			}
			Log.d(TAG, "Length: " + length + " | Width: " + width + "");
			layoutParams = new RelativeLayout.LayoutParams(width,
					LayoutParams.WRAP_CONTENT);
			tvMoneyScore.setLayoutParams(layoutParams);

			length = String.valueOf(rating).length();
			Log.d(TAG, "" + length + "");
			if (length == 2) {
				width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 41, getResources()
								.getDisplayMetrics());
			} else if (length == 1) {
				width = (int) TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_SP, 22, getResources()
								.getDisplayMetrics());
			} else {
				width = 0;
			}
			Log.d(TAG, "Length: " + length + " | Width: " + width + "");
			layoutParams = new RelativeLayout.LayoutParams(width,
					LayoutParams.WRAP_CONTENT);
			tvEffScore.setLayoutParams(layoutParams);

			// Set TextViews to display correct information.
			final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);
			tvCompScore.setText("" + (int) ((1 - failTrain) * 100) + "");
			tvMoneyScore.setText("" + (int) gasCost + "");
			tvEffScore.setText("" + rating + "");
			if (failTrain < 0.1) {
				final SpannableStringBuilder sb = new SpannableStringBuilder(
						getResources().getString(R.string.eval_strong));
				sb.setSpan(bss, 48, 64, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				tvEval.setText(sb);
				// tvEval.setText(getResources().getString(R.string.eval_strong));
			} else if (failTrain > 0.1 && failTrain < 0.3) {
				final SpannableStringBuilder sb = new SpannableStringBuilder(
						getResources().getString(R.string.eval_good));
				sb.setSpan(bss, 48, 62, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				tvEval.setText(sb);
				// tvEval.setText(getResources().getString(R.string.eval_good));
			} else {
				final SpannableStringBuilder sb = new SpannableStringBuilder(
						getResources().getString(R.string.eval_weak));
				sb.setSpan(bss, 48, 62, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
				tvEval.setText(sb);
				// tvEval.setText(getResources().getString(R.string.eval_weak));
			}
		}
	}

	@SuppressWarnings("deprecation")
	public void drawBackground() {
		relativeRange = (double) currentRange / (double) defaultRange;
		bc = new BackgroundCalc(relativeRange);
		ll.setBackgroundDrawable(bc.makeGradient(relativeRange));
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
		SharedPreferences settings = fragmentActivity.getSharedPreferences(
				PREFS_NAME, 0);
		defaultRange = settings.getInt("defaultRange", defaultRange);
	}

	public void loadData() {
		SharedPreferences data = fragmentActivity.getSharedPreferences(
				DATA_NAME, 0);
		currentRange = data.getInt("currentRange", currentRange);
	}
}
