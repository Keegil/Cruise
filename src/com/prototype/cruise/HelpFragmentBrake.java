package com.prototype.cruise;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpFragmentBrake extends Fragment {

	// Declare & initialize logging variable.
	@SuppressWarnings("unused")
	private static final String TAG = "HelpFragmentSpeed";

	// Declare parent activity.
	FragmentActivity helpActivity;

	// Declare background.
	static LinearLayout ll;

	// Declare TextViews.
	TextView tvLogo;
	TextView tvBrakeHead;
	TextView tvBrakeInfo;

	BackgroundCalc bc;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.frag_help_brake, container, false);
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
		drawBackground();
	}

	public void init(View v) {
		// Initialize parent activity.
		helpActivity = (HelpActivity) getActivity();

		// Initialize background view.
		ll = (LinearLayout) v.findViewById(R.id.ll_help_brake);

		// Initialize TextViews.
		tvLogo = (TextView) v.findViewById(R.id.tv_logo_help_brake);
		tvBrakeHead = (TextView) v.findViewById(R.id.tv_brake_head);
		tvBrakeInfo = (TextView) v.findViewById(R.id.tv_brake_text);
	}

	public void setFonts() {
		// Initialize TypeFaces.
		Typeface tfHelvetica = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/helvetica_bold_oblique.ttf");
		Typeface tfMyriadRegular = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/myriad_regular.otf");

		// Set correct fonts to views.
		tvLogo.setTypeface(tfHelvetica);
		tvBrakeHead.setTypeface(tfMyriadRegular);
		tvBrakeInfo.setTypeface(tfMyriadRegular);
	}

	@SuppressWarnings("deprecation")
	public void drawBackground() {
		bc = new BackgroundCalc(HelpActivity.relativeRange);
		ll.setBackgroundDrawable(bc.makeGradient(HelpActivity.relativeRange));
	}
}
