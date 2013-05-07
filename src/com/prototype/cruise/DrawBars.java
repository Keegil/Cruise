package com.prototype.cruise;

import android.content.Context;
import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class DrawBars extends SurfaceView implements Runnable {

	// Declare variables.
	SurfaceHolder ourHolder;
	Thread ourThread = null;
	boolean isRunning = false;

	public DrawBars(Context context) {
		super(context);
		// define holder variable
		ourHolder = getHolder();
	}

	public void pause() {
		// stop the run while-loop when paused
		isRunning = false;
		while (true) {
			// try to let thread join
			try {
				ourThread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		}
		// set thread to null again
		ourThread = null;
	}

	// this runs when the activity starts
	public void resume() {
		// start the run while-loop when resumed
		isRunning = true;
		// create a new thread, use THIS run-method further below
		ourThread = new Thread(this);
		// start thread
		ourThread.start();
	}

	public void run() {
		while (isRunning) {
			// check if surface is valid
			if (!ourHolder.getSurface().isValid()) {
				// keep checking if-statement until valid
				continue;
			}
			// set up canvas and lock it for painting
			Canvas canvas = ourHolder.lockCanvas();

			// unlock and display canvas
			ourHolder.unlockCanvasAndPost(canvas);
		}
	}
}
