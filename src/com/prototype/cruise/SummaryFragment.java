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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
	TextView tvRating;

	// Declare stars.
	ImageView ivStar1;
	ImageView ivStar2;
	ImageView ivStar3;
	ImageView ivStar4;
	ImageView ivStar5;

	// Declare database variables.
	DrivingStatsDataSource drivingStatsDataSource;
	DrivingStats drivingStats;

	// Declare candidacy calculation variables.
	List<DrivingStats> drives = new ArrayList<DrivingStats>();

	double failTrain;
	int rating;
	double relativeRange;

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
		setStars();
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
		tvRating = (TextView) v.findViewById(R.id.tv_rating);

		// Initialize stars.
		ivStar1 = (ImageView) v.findViewById(R.id.iv_star1);
		ivStar2 = (ImageView) v.findViewById(R.id.iv_star2);
		ivStar3 = (ImageView) v.findViewById(R.id.iv_star3);
		ivStar4 = (ImageView) v.findViewById(R.id.iv_star4);
		ivStar5 = (ImageView) v.findViewById(R.id.iv_star5);

		// Initialize database.
		drivingStatsDataSource = new DrivingStatsDataSource(fragmentActivity);
		drivingStatsDataSource.open();
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
		tvEval.setTypeface(tfMyriadRegular);
	}

	public void calc() {
		// Determine strength of EV candidacy.
		drives = drivingStatsDataSource.getAllDrivingStats();
		int i = 0;
		int fails = 0;
		int stars = 0;
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
			i++;
			Log.d(TAG,
					"Drives: " + drives.size() + " | Fails: " + fails
							+ " | Failtrain: " + failTrain + " | Stars: "
							+ stars + " | Rating: " + rating + " [ accScore = "
							+ drivingStats.getNumAccEvent()
							+ " [ brakeScore = "
							+ drivingStats.getNumBrakeEvent()
							+ " [ routeScore = "
							+ drivingStats.getNumRouteEvent()
							+ " [ speedScore = "
							+ drivingStats.getNumSpeedEvent() + "");
		}

	}

	public void setTextViews() {
		// Set TextViews to display correct information.
		if (failTrain < 0.1) {
			tvEval.setText(getResources().getString(R.string.eval_strong));
		} else if (failTrain > 0.1 && failTrain < 0.3) {
			tvEval.setText(getResources().getString(R.string.eval_good));
		} else {
			tvEval.setText(getResources().getString(R.string.eval_weak));
		}
		tvRating.setText("" + rating + "/20");
	}

	public void setStars() {
		// Set correct amount of stars.
		if (rating == 0) {

		} else if (rating > 0 && rating <= 4) {
			ivStar1.setImageResource(R.drawable.starfilled);
		} else if (rating > 4 && rating <= 8) {
			ivStar1.setImageResource(R.drawable.starfilled);
			ivStar2.setImageResource(R.drawable.starfilled);
		} else if (rating > 8 && rating <= 12) {
			ivStar1.setImageResource(R.drawable.starfilled);
			ivStar2.setImageResource(R.drawable.starfilled);
			ivStar3.setImageResource(R.drawable.starfilled);
		} else if (rating > 12 && rating <= 16) {
			ivStar1.setImageResource(R.drawable.starfilled);
			ivStar2.setImageResource(R.drawable.starfilled);
			ivStar3.setImageResource(R.drawable.starfilled);
			ivStar4.setImageResource(R.drawable.starfilled);
		} else {
			ivStar1.setImageResource(R.drawable.starfilled);
			ivStar2.setImageResource(R.drawable.starfilled);
			ivStar3.setImageResource(R.drawable.starfilled);
			ivStar4.setImageResource(R.drawable.starfilled);
			ivStar5.setImageResource(R.drawable.starfilled);
		}
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