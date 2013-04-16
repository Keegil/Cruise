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

	public DrivingStats createStat(String date, int numAccEvent,
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
		DrivingStats stat = cursorToStat(cursor);
		cursor.close();
		return stat;
	}

	public void deleteStat(DrivingStats stat) {
		long id = stat.getId();
		System.out.println("Log deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_DRIVING_STATS,
				MySQLiteHelper.COLUMN_DRIVING_STATS_ID + " = " + id, null);
	}

	public List<DrivingStats> getAllStats() {
		List<DrivingStats> stats = new ArrayList<DrivingStats>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_DRIVING_STATS,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			DrivingStats stat = cursorToStat(cursor);
			stats.add(stat);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return stats;
	}

	// Getting single stat
	public DrivingStats getStat(int id) {

		Cursor cursor = database.query(MySQLiteHelper.TABLE_DRIVING_STATS,
				allColumns, MySQLiteHelper.COLUMN_DRIVING_STATS_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		DrivingStats stat = new DrivingStats(Integer.parseInt(cursor
				.getString(0)), cursor.getString(1), cursor.getInt(2),
				cursor.getInt(3), cursor.getInt(4), cursor.getInt(5),
				cursor.getInt(6), cursor.getInt(7), cursor.getInt(8),
				cursor.getDouble(9), cursor.getDouble(10));
		return stat;
	}

	private DrivingStats cursorToStat(Cursor cursor) {
		DrivingStats stat = new DrivingStats();
		stat.setId(cursor.getLong(0));
		stat.setDate(cursor.getString(1));
		stat.setNumAccEvent(cursor.getInt(2));
		stat.setNumSpeedEvent(cursor.getInt(3));
		stat.setNumBrakeEvent(cursor.getInt(4));
		stat.setDriveDistance(cursor.getInt(5));
		stat.setRangeStart(cursor.getInt(6));
		stat.setRangeUsed(cursor.getInt(7));
		stat.setRangeEnd(cursor.getInt(8));
		stat.setRangeModifier(cursor.getDouble(9));
		stat.setFuelSavings(cursor.getDouble(10));
		return stat;
	}
}