package com.prototype.cruise;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DrivingStatsDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_DRIVING_STATS_ID,
			MySQLiteHelper.COLUMN_DRIVING_STATS_DATE,
			MySQLiteHelper.COLUMN_DRIVING_STATS_NUM_ACC_EVENT,
			MySQLiteHelper.COLUMN_DRIVING_STATS_NUM_SPEED_EVENT,
			MySQLiteHelper.COLUMN_DRIVING_STATS_NUM_BRAKE_EVENT,
			MySQLiteHelper.COLUMN_DRIVING_STATS_DRIVE_DISTANCE,
			MySQLiteHelper.COLUMN_DRIVING_STATS_RANGE_START,
			MySQLiteHelper.COLUMN_DRIVING_STATS_RANGE_USED,
			MySQLiteHelper.COLUMN_DRIVING_STATS_RANGE_END,
			MySQLiteHelper.COLUMN_DRIVING_STATS_RANGE_MODIFIER,
			MySQLiteHelper.COLUMN_DRIVING_STATS_FUEL_SAVINGS };

	public DrivingStatsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public DrivingStats createDrivingStats(String date, int numAccEvent,
			int numSpeedEvent, int numBrakeEvent, int driveDistance,
			int rangeStart, int rangeUsed, int rangeEnd, double rangeModifier,
			double fuelSavings) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_DRIVING_STATS_DATE, date);
		values.put(MySQLiteHelper.COLUMN_DRIVING_STATS_NUM_ACC_EVENT,
				numAccEvent);
		values.put(MySQLiteHelper.COLUMN_DRIVING_STATS_NUM_SPEED_EVENT,
				numSpeedEvent);
		values.put(MySQLiteHelper.COLUMN_DRIVING_STATS_NUM_BRAKE_EVENT,
				numAccEvent);
		values.put(MySQLiteHelper.COLUMN_DRIVING_STATS_DRIVE_DISTANCE,
				driveDistance);
		values.put(MySQLiteHelper.COLUMN_DRIVING_STATS_RANGE_START, rangeStart);
		values.put(MySQLiteHelper.COLUMN_DRIVING_STATS_RANGE_USED, rangeUsed);
		values.put(MySQLiteHelper.COLUMN_DRIVING_STATS_RANGE_END, rangeEnd);
		values.put(MySQLiteHelper.COLUMN_DRIVING_STATS_RANGE_MODIFIER,
				rangeModifier);
		values.put(MySQLiteHelper.COLUMN_DRIVING_STATS_FUEL_SAVINGS,
				fuelSavings);
		long insertId = database.insert(MySQLiteHelper.TABLE_DRIVING_STATS,
				null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_DRIVING_STATS,
				allColumns, MySQLiteHelper.COLUMN_DRIVING_STATS_ID + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst();
		DrivingStats drivingStats = cursorToDrivingStats(cursor);
		cursor.close();
		return drivingStats;
	}

	public void deleteDrivingStats(DrivingStats drivingStats) {
		long id = drivingStats.getId();
		database.delete(MySQLiteHelper.TABLE_DRIVING_STATS,
				MySQLiteHelper.COLUMN_DRIVING_STATS_ID + " = " + id, null);
	}
	
	public void deleteAllDrivingStats() {
		String deleteDrivingStats = "DROP TABLE " + MySQLiteHelper.TABLE_DRIVING_STATS + ";";
		database.execSQL(deleteDrivingStats);
	}

	public List<DrivingStats> getAllDrivingStats() {
		List<DrivingStats> drivingStatss = new ArrayList<DrivingStats>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_DRIVING_STATS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			DrivingStats drivingStats = cursorToDrivingStats(cursor);
			drivingStatss.add(drivingStats);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return drivingStatss;
	}

	// Getting single stat
	public DrivingStats getDrivingStats(long id) {

		Cursor cursor = database.query(MySQLiteHelper.TABLE_DRIVING_STATS,
				allColumns, MySQLiteHelper.COLUMN_DRIVING_STATS_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		DrivingStats drivingStats = new DrivingStats(Integer.parseInt(cursor
				.getString(0)), cursor.getString(1), cursor.getInt(2),
				cursor.getInt(3), cursor.getInt(4), cursor.getInt(5),
				cursor.getInt(6), cursor.getInt(7), cursor.getInt(8),
				cursor.getDouble(9), cursor.getDouble(10));
		return drivingStats;
	}

	private DrivingStats cursorToDrivingStats(Cursor cursor) {
		DrivingStats drivingStats = new DrivingStats();
		drivingStats.setId(cursor.getLong(0));
		drivingStats.setDate(cursor.getString(1));
		drivingStats.setNumAccEvent(cursor.getInt(2));
		drivingStats.setNumSpeedEvent(cursor.getInt(3));
		drivingStats.setNumBrakeEvent(cursor.getInt(4));
		drivingStats.setDriveDistance(cursor.getInt(5));
		drivingStats.setRangeStart(cursor.getInt(6));
		drivingStats.setRangeUsed(cursor.getInt(7));
		drivingStats.setRangeEnd(cursor.getInt(8));
		drivingStats.setRangeModifier(cursor.getDouble(9));
		drivingStats.setFuelSavings(cursor.getDouble(10));
		return drivingStats;
	}
}