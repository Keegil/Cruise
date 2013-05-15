package com.prototype.cruise;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

public class BackgroundCalc {

	private double redStart, greenStart, blueStart, redStop, greenStop,
			blueStop;

	public BackgroundCalc() {

	}

	public BackgroundCalc(double rr) {
		calcRGB(rr);

	}

	public GradientDrawable makeGradient(double rr) {
		calcRGB(rr);
		return getGradient();
	}

	public void calcRGB(double rr) {
		redStart = 95 + (2013 * rr) - (5311 * rr * rr) + (3204 * rr * rr * rr);
		if (redStart > 255) {
			redStart = 255;
		} else if (redStart < 0) {
			redStart = 0;
		}
		redStop = 155 + (402 * rr) - (593 * rr * rr) + (290 * rr * rr * rr);
		if (redStop > 255) {
			redStop = 255;
		} else if (redStop < 0) {
			redStop = 0;
		}
		greenStart = 129 + (294 * rr) - (328 * rr * rr);
		if (greenStart > 255) {
			greenStart = 255;
		} else if (greenStart < 0) {
			greenStart = 0;
		}
		greenStop = 14 + (164 * rr) + (42 * rr * rr);
		if (greenStop > 255) {
			greenStop = 255;
		} else if (greenStop < 0) {
			greenStop = 0;
		}
		blueStart = 66 - (472 * rr) + (1191 * rr * rr) - (728 * rr * rr * rr);
		if (blueStart > 255) {
			blueStart = 255;
		} else if (blueStart < 0) {
			blueStart = 0;
		}
		blueStop = 45 + (12 * rr) - (72 * rr * rr) + (37 * rr * rr * rr);
		if (blueStop > 255) {
			blueStop = 255;
		} else if (blueStop < 0) {
			blueStop = 0;
		}
	}

	public GradientDrawable getGradient() {
		// Returns a gradient drawable based in relative range.

		GradientDrawable gdBackground = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
						Color.rgb((int) redStart, (int) greenStart,
								(int) blueStart),
						Color.rgb((int) redStop, (int) greenStop,
								(int) blueStop) });
		gdBackground.setCornerRadius(0f);
		return gdBackground;
	}

	public double getRedStart() {
		return redStart;
	}

	public double getGreenStart() {
		return greenStart;
	}

	public double getBlueStart() {
		return blueStart;
	}

	public double getRedStop() {
		return redStop;
	}

	public double getGreenStop() {
		return greenStop;
	}

	public double getBlueStop() {
		return blueStop;
	}

	public int getStartRGB() {
		return Color.rgb((int) redStart, (int) greenStart, (int) blueStart);
	}

	public int getStopRGB() {
		return Color.rgb((int) redStop, (int) greenStop, (int) blueStop);
	}

}
