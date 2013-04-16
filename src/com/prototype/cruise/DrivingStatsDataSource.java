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
	private String[] allColumns = { MySQLiteHelper.COLUMN_ID,
			MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_DRIVE_LENGTH,
			MySQLiteHelper.COLUMN_ACC_MISTAKES,
			MySQLiteHelper.COLUMN_SPEED_MISTAKES, MySQLiteHelper.COLUMN_POINTS,
			MySQLiteHelper.COLUMN_RANGE };

	public DrivingStatsDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public Stats createStat(int driveLength, String date, int accMistakes,
			int speedMistakes, int point, int remainingRange) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_DRIVE_LENGTH, driveLength);
		values.put(MySQLiteHelper.COLUMN_DATE, date);
		values.put(MySQLiteHelper.COLUMN_ACC_MISTAKES, accMistakes);
		values.put(MySQLiteHelper.COLUMN_SPEED_MISTAKES, speedMistakes);
		values.put(MySQLiteHelper.COLUMN_POINTS, point);
		values.put(MySQLiteHelper.COLUMN_RANGE, remainingRange);
		long insertId = database.insert(MySQLiteHelper.TABLE_DRIVING_STATS, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_DRIVING_STATS, allColumns,
				MySQLiteHelper.COLUMN_ID + " = " + insertId, null, null, null,
				null);
		cursor.moveToFirst();
		Stats stat = cursorToStat(cursor);
		cursor.close();
		return stat;
	}

	public void deleteStat(Stats stat) {
		long id = stat.getId();
		System.out.println("Log deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_DRIVING_STATS, MySQLiteHelper.COLUMN_ID
				+ " = " + id, null);
	}

	public List<Stats> getAllStats() {
		List<Stats> stats = new ArrayList<Stats>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_DRIVING_STATS, allColumns,
				null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			Stats stat = cursorToStat(cursor);
			stats.add(stat);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return stats;
	}

	// Getting single stat
	public Stats getStat(int id) {

		Cursor cursor = database.query(MySQLiteHelper.TABLE_DRIVING_STATS, allColumns,
				MySQLiteHelper.COLUMN_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		Stats stat = new Stats(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getInt(2), cursor.getInt(3),
				cursor.getInt(4), cursor.getInt(5), cursor.getInt(6));
		return stat;
	}

	private Stats cursorToStat(Cursor cursor) {
		Stats stat = new Stats();
		stat.setId(cursor.getLong(0));
		stat.setDriveLength(cursor.getInt(1));
		stat.setDate(cursor.getString(2));
		stat.setAccMistakes(cursor.getInt(3));
		stat.setSpeedMistakes(cursor.getInt(4));
		stat.setPoint(cursor.getInt(5));
		stat.setRemainingRange(cursor.getInt(6));
		return stat;
	}
}