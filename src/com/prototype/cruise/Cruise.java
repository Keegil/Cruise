package com.example.cruiseev;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Cruise extends ListActivity {

	private StatsDataSource statSource;
	private Button simButton;
	private EditText distance;
	private EditText accMist;
	private EditText speedMist;

	private String date, dt, at, st, numcheck;
	private CustomListViewAdapter adapter;
	List<Stats> values;
	private DateFormat dateFormat;
	private Calendar cal;

	/*
	 * 
	 * <EditText android:id="@+id/et_distance"
	 * android:layout_width="fill_parent" android:layout_height="wrap_content"
	 * android:hint="@string/drive_distance" />
	 * 
	 * <EditText android:id="@+id/et_acc_mistakes"
	 * android:layout_width="fill_parent" android:layout_height="wrap_content"
	 * android:hint="@string/acc_mistakes" />
	 * 
	 * <EditText android:id="@+id/et_speed_mistakes"
	 * android:layout_width="fill_parent" android:layout_height="wrap_content"
	 * android:hint="@string/speed_mistakes" />
	 */

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cruise);

		statSource = new StatsDataSource(this);
		statSource.open();

		// formatting date
		dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		// get current date time with Calendar()
		cal = Calendar.getInstance();
		date = dateFormat.format(cal.getTime());

		
		
		values = statSource.getAllStats();
		

		
		adapter = new CustomListViewAdapter(
				this, R.layout.list_item, values);
		setListAdapter(adapter);

		simButton = (Button) findViewById(R.id.b_drive);
		simButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				distance = (EditText) findViewById(R.id.et_distance);
				accMist = (EditText) findViewById(R.id.et_acc_mistakes);
				speedMist = (EditText) findViewById(R.id.et_speed_mistakes);

				dt = distance.getText().toString();
				at = accMist.getText().toString();
				st = speedMist.getText().toString();

				try {
					int numdt = Integer.parseInt(dt);
					int numat = Integer.parseInt(at);
					int numst = Integer.parseInt(st);
					
					cal = Calendar.getInstance();
					date = dateFormat.format(cal.getTime());
					statSource.createStat(numdt, date, numat, numst, 50, 95);
					Stats s = new Stats(adapter.getCount()+1,date, numdt, numat, numst, 50, 95);
					adapter.add(s);
					setListAdapter(adapter);

				} catch (NumberFormatException e) {
					numcheck = "Only numbers allowed";
					Toast toast = Toast.makeText(getApplicationContext(), numcheck,
							Toast.LENGTH_LONG);
					toast.setGravity(Gravity.BOTTOM, 0, 10);
					toast.show();

				}

			}
		});
	}



	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.cruise, menu);
		return true;
	}

}
