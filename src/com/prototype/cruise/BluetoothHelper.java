package com.prototype.cruise;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class BluetoothHelper extends Activity {

	BluetoothBackEnd btb;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_helper);
		btb = new BluetoothBackEnd(this);
	}

	public void openBluetoothSettings(View v) {
		Log.d("bt", "bt settings clicked");
		Intent intentBluetooth = new Intent();
		intentBluetooth
				.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
		startActivity(intentBluetooth);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (btb.isPaired()) {
			finish();
		}

	}

}
