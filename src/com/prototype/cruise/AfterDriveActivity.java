package com.prototype.cruise;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;

public class AfterDriveActivity extends FragmentActivity {

	// declare logging variables
	private static final String TAG = "BeforeDriveFragment";

	private MyAdapter mAdapter;
	private ViewPager mPager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);

		mAdapter = new MyAdapter(getSupportFragmentManager());
		mAdapter.addFragment("DriveCompleted", AfterDriveFragment.class);
		mAdapter.addFragment("DriveCompletedDetails", AfterDriveFragment2.class);

		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
	}

	public static class MyAdapter extends FragmentPagerAdapter {

		private FragmentManager fragmentManager;
		private ArrayList<Class<? extends Fragment>> fragments;
		private ArrayList<String> titles;

		public MyAdapter(FragmentManager fm) {
			super(fm);
			fragmentManager = fm;
			fragments = new ArrayList<Class<? extends Fragment>>();
			titles = new ArrayList<String>();
		}

		public void addFragment(String title, Class<? extends Fragment> fragment) {
			titles.add(title);
			fragments.add(fragment);
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			try {
				return fragments.get(position).newInstance();
			} catch (InstantiationException e) {
				Log.wtf(TAG, e);
			} catch (IllegalAccessException e) {
				Log.wtf(TAG, e);
			}
			return null;
		}
	}
}