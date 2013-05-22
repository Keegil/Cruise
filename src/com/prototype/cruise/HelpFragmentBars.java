package com.prototype.cruise;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpFragmentBars extends Fragment implements OnTouchListener {

	// Declare & initialize logging variable.
	@SuppressWarnings("unused")
	private static final String TAG = "HelpFragmentSpeed";

	// Declare parent activity.
	FragmentActivity helpActivity;

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
	LinearLayout llBars;

	// Declare TextViews.
	TextView tvLogo;
	TextView tvBarsHead;
	TextView tvBarsInfo;

	// Declare touch variables.
	float x, y;
	DrawBars drawBars;

	BackgroundCalc bc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_help_bars, container, false);
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
		drawBars(HelpActivity.relativeRange);
		drawBackground();
	}

	public void init(View v) {
		// Initialize parent activity.
		helpActivity = (HelpActivity) getActivity();

		// Initialize background view.
		ll = (LinearLayout) v.findViewById(R.id.ll_help_bars);

		// Initialize bars and set OnClickListener.
		llBar1 = (LinearLayout) v.findViewById(R.id.bar1_help);
		llBar2 = (LinearLayout) v.findViewById(R.id.bar2_help);
		llBar3 = (LinearLayout) v.findViewById(R.id.bar3_help);
		llBar4 = (LinearLayout) v.findViewById(R.id.bar4_help);
		llBar5 = (LinearLayout) v.findViewById(R.id.bar5_help);
		llBar6 = (LinearLayout) v.findViewById(R.id.bar6_help);
		llBar7 = (LinearLayout) v.findViewById(R.id.bar7_help);
		llBar8 = (LinearLayout) v.findViewById(R.id.bar8_help);
		llBar9 = (LinearLayout) v.findViewById(R.id.bar9_help);
		llBars = (LinearLayout) v.findViewById(R.id.ll_bars_help);
		llBars.setOnTouchListener(this);

		// Initialize TextViews.
		tvLogo = (TextView) v.findViewById(R.id.tv_logo_help_bars);
		tvBarsHead = (TextView) v.findViewById(R.id.tv_bars_head);
		tvBarsInfo = (TextView) v.findViewById(R.id.tv_bars_text);
	}

	public void setFonts() {
		// Initialize TypeFaces.
		Typeface tfHelvetica = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/helvetica_bold_oblique.ttf");
		Typeface tfMyriadRegular = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_regular.otf");

		// Set correct fonts to views.
		tvLogo.setTypeface(tfHelvetica);
		tvBarsHead.setTypeface(tfMyriadRegular);
		tvBarsInfo.setTypeface(tfMyriadRegular);
	}

	public void drawBars(double range) {
		// Check relative range and set bars accordingly.
		if (range <= 1 && range > 0.9) {
			llBar1.setBackgroundResource(R.drawable.whitebar);
			llBar2.setBackgroundResource(R.drawable.whitebar);
			llBar3.setBackgroundResource(R.drawable.whitebar);
			llBar4.setBackgroundResource(R.drawable.whitebar);
			llBar5.setBackgroundResource(R.drawable.whitebar);
			llBar6.setBackgroundResource(R.drawable.whitebar);
			llBar7.setBackgroundResource(R.drawable.whitebar);
			llBar8.setBackgroundResource(R.drawable.whitebar);
			llBar9.setBackgroundResource(R.drawable.whitebar);
		} else if (range <= 0.9 && range > 0.8) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whitebar);
			llBar3.setBackgroundResource(R.drawable.whitebar);
			llBar4.setBackgroundResource(R.drawable.whitebar);
			llBar5.setBackgroundResource(R.drawable.whitebar);
			llBar6.setBackgroundResource(R.drawable.whitebar);
			llBar7.setBackgroundResource(R.drawable.whitebar);
			llBar8.setBackgroundResource(R.drawable.whitebar);
			llBar9.setBackgroundResource(R.drawable.whitebar);
		} else if (range <= 0.8 && range > 0.7) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whitebar);
			llBar4.setBackgroundResource(R.drawable.whitebar);
			llBar5.setBackgroundResource(R.drawable.whitebar);
			llBar6.setBackgroundResource(R.drawable.whitebar);
			llBar7.setBackgroundResource(R.drawable.whitebar);
			llBar8.setBackgroundResource(R.drawable.whitebar);
			llBar9.setBackgroundResource(R.drawable.whitebar);
		} else if (range <= 0.7 && range > 0.6) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whitebar);
			llBar5.setBackgroundResource(R.drawable.whitebar);
			llBar6.setBackgroundResource(R.drawable.whitebar);
			llBar7.setBackgroundResource(R.drawable.whitebar);
			llBar8.setBackgroundResource(R.drawable.whitebar);
			llBar9.setBackgroundResource(R.drawable.whitebar);
		} else if (range <= 0.6 && range > 0.5) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whitebar);
			llBar6.setBackgroundResource(R.drawable.whitebar);
			llBar7.setBackgroundResource(R.drawable.whitebar);
			llBar8.setBackgroundResource(R.drawable.whitebar);
			llBar9.setBackgroundResource(R.drawable.whitebar);
		} else if (range <= 0.5 && range > 0.4) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whitebar);
			llBar7.setBackgroundResource(R.drawable.whitebar);
			llBar8.setBackgroundResource(R.drawable.whitebar);
			llBar9.setBackgroundResource(R.drawable.whitebar);
		} else if (range <= 0.4 && range > 0.3) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whitebar);
			llBar8.setBackgroundResource(R.drawable.whitebar);
			llBar9.setBackgroundResource(R.drawable.whitebar);
		} else if (range <= 0.3 && range > 0.2) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
			llBar8.setBackgroundResource(R.drawable.whitebar);
			llBar9.setBackgroundResource(R.drawable.whitebar);
		} else if (range <= 0.2 && range > 0.1) {
			llBar1.setBackgroundResource(R.drawable.whiteemptybar);
			llBar2.setBackgroundResource(R.drawable.whiteemptybar);
			llBar3.setBackgroundResource(R.drawable.whiteemptybar);
			llBar4.setBackgroundResource(R.drawable.whiteemptybar);
			llBar5.setBackgroundResource(R.drawable.whiteemptybar);
			llBar6.setBackgroundResource(R.drawable.whiteemptybar);
			llBar7.setBackgroundResource(R.drawable.whiteemptybar);
			llBar8.setBackgroundResource(R.drawable.whiteemptybar);
			llBar9.setBackgroundResource(R.drawable.whitebar);
		} else if (range <= 0.1 && range >= 0) {
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
		bc = new BackgroundCalc(HelpActivity.relativeRange);
		ll.setBackgroundDrawable(bc.makeGradient(HelpActivity.relativeRange));
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		switch (arg0.getId()) {
		case R.id.ll_bars_help:
			x = arg1.getX();
			y = arg1.getY();
			Log.d(TAG, "(" + x + ", " + y + ")");
			HelpActivity.relativeRange = 1 - (y / 400);
			if (HelpActivity.relativeRange > 1) {
				HelpActivity.relativeRange = 1;
			} else if (HelpActivity.relativeRange < 0) {
				HelpActivity.relativeRange = 0;
			}
			drawBars(HelpActivity.relativeRange);
			ll.setBackgroundDrawable(bc.makeGradient(HelpActivity.relativeRange));
			HelpActivity.setBackgroundIndicator(bc.getStopRGB());
			break;
		}
		return true;
	}
}
