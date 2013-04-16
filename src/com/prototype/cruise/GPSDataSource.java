package com.prototype.cruise;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class GPSDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_GPS_DATA_ID,
			MySQLiteHelper.COLUMN_GPS_DATA_DRIVING_STATS_ID,
			MySQLiteHelper.COLUMN_GPS_DATA_TIME,
			MySQLiteHelper.COLUMN_GPS_DATA_LATITUDE,
			MySQLiteHelper.COLUMN_GPS_DATA_LONGITUDE };

	public GPSDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public GPSData createGPSData(long drivingStatsId, String time,
			float latitude, float longitude) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_GPS_DATA_DRIVING_STATS_ID, drivingStatsId);
		values.put(MySQLiteHelper.COLUMN_GPS_DATA_TIME,
				time);
		values.put(MySQLiteHelper.COLUMN_EVENT_DATA_LATITUDE,
				latitude);
		values.put(MySQLiteHelper.COLUMN_GPS_DATA_LONGITUDE,
				longitude);
		long insertId = database.insert(MySQLiteHelper.TABLE_GPS_DATA,
				null, values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_GPS_DATA,
				allColumns, MySQLiteHelper.COLUMN_GPS_DATA_ID + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst();
		GPSData gps = cursorToGPSData(cursor);
		cursor.close();
		return gps;
	}

	public void deleteGPSData(GPSData gps) {
		long id = gps.getId();
		database.delete(MySQLiteHelper.TABLE_GPS_DATA,
				MySQLiteHelper.COLUMN_DRIVING_STATS_ID + " = " + id, null);
	}
	
	public void deleteAllGPSData() {
		String deleteGPS = "DROP TABLE " + MySQLiteHelper.TABLE_GPS_DATA + ";";
		database.execSQL(deleteGPS);
	}

	public List<GPSData> getAllGPSData() {
		List<GPSData> gpss = new ArrayList<GPSData>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_GPS_DATA,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			GPSData gps = cursorToGPSData(cursor);
			gpss.add(gps);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return gpss;
	}

	// Getting single stat
	public GPSData getGPSData(long id) {

		Cursor cursor = database.query(MySQLiteHelper.TABLE_GPS_DATA,
				allColumns, MySQLiteHelper.COLUMN_GPS_DATA_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		GPSData gps = new GPSData(Integer.parseInt(cursor
				.getString(0)), cursor.getLong(1), cursor.getString(2),
				cursor.getFloat(3), cursor.getFloat(4));
		return gps;
	}

	private GPSData cursorToGPSData(Cursor cursor) {
		GPSData gps = new GPSData();
		gps.setId(cursor.getLong(0));
		gps.setDrivingStatsId(cursor.getLong(1));
		gps.setTime(cursor.getString(2));
		gps.setLatitude(cursor.getFloat(3));
		gps.setLongitude(cursor.getFloat(4));

		return gps;
	}
}