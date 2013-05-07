package com.prototype.cruise;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HelpFragmentRoute extends Fragment {

	// Declare & initialize logging variable.
	private static final String TAG = "HelpFragmentSpeed";

	// Declare parent activity.
	FragmentActivity helpActivity;

	// Declare background.
	static LinearLayout ll;

	// Declare Textviews.
	TextView tvLogo;
	TextView tvRouteHead;
	TextView tvRouteInfo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.frag_help_route, container, false);
		init(view);
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setFonts();
		drawBackground();
	}

	public void init(View v) {
		// Initialize parent activity.
		helpActivity = (HelpActivity) getActivity();

		// Initialize background view.
		ll = (LinearLayout) v.findViewById(R.id.ll_help_route);

		// Initialize Textviews.
		tvLogo = (TextView) v.findViewById(R.id.tv_logo_help_route);
		tvRouteHead = (TextView) v.findViewById(R.id.tv_route_head);
		tvRouteInfo = (TextView) v.findViewById(R.id.tv_route_text);
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
		tvRouteHead.setTypeface(tfMyriadRegular);
		tvRouteInfo.setTypeface(tfMyriadRegular);
	}

	public void drawBackground() {
		ll.setBackgroundDrawable(setGradient(HelpActivity.relativeRange));
	}

	public GradientDrawable setGradient(double rr) {
		// Returns a gradient Drawable based in relative range.
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
}
