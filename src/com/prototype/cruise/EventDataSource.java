package com.prototype.cruise;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class EventDataSource {

	// Database fields
	private SQLiteDatabase database;
	private MySQLiteHelper dbHelper;
	private String[] allColumns = { MySQLiteHelper.COLUMN_EVENT_DATA_ID,
			MySQLiteHelper.COLUMN_EVENT_DATA_DRIVING_STATS_ID,
			MySQLiteHelper.COLUMN_EVENT_DATA_TIME,
			MySQLiteHelper.COLUMN_EVENT_DATA_LATITUDE,
			MySQLiteHelper.COLUMN_EVENT_DATA_LONGITUDE,
			MySQLiteHelper.COLUMN_EVENT_DATA_ACC_EVENT,
			MySQLiteHelper.COLUMN_EVENT_DATA_SPEED_EVENT,
			MySQLiteHelper.COLUMN_EVENT_DATA_BRAKE_EVENT };

	public EventDataSource(Context context) {
		dbHelper = new MySQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public EventData createEventData(long drivingStatsId, String time,
			float latitude, float longitude, int accEvent, int speedEvent,
			int brakeEvent) {
		ContentValues values = new ContentValues();
		values.put(MySQLiteHelper.COLUMN_EVENT_DATA_DRIVING_STATS_ID,
				drivingStatsId);
		values.put(MySQLiteHelper.COLUMN_EVENT_DATA_TIME, time);
		values.put(MySQLiteHelper.COLUMN_EVENT_DATA_LATITUDE, latitude);
		values.put(MySQLiteHelper.COLUMN_EVENT_DATA_LONGITUDE, longitude);
		values.put(MySQLiteHelper.COLUMN_EVENT_DATA_ACC_EVENT, accEvent);
		values.put(MySQLiteHelper.COLUMN_EVENT_DATA_SPEED_EVENT, speedEvent);
		values.put(MySQLiteHelper.COLUMN_EVENT_DATA_BRAKE_EVENT, brakeEvent);
		long insertId = database.insert(MySQLiteHelper.TABLE_EVENT_DATA, null,
				values);
		Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENT_DATA,
				allColumns, MySQLiteHelper.COLUMN_EVENT_DATA_ID + " = "
						+ insertId, null, null, null, null);
		cursor.moveToFirst();
		EventData eventData = cursorToEventData(cursor);
		cursor.close();
		return eventData;
	}

	public void deleteStat(EventData eventData) {
		long id = eventData.getId();
		System.out.println("Log deleted with id: " + id);
		database.delete(MySQLiteHelper.TABLE_EVENT_DATA,
				MySQLiteHelper.COLUMN_EVENT_DATA_ID + " = " + id, null);
	}

	public List<EventData> getAllEventData() {
		List<EventData> eventDatas = new ArrayList<EventData>();

		Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENT_DATA,
				allColumns, null, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			EventData eventData = cursorToEventData(cursor);
			eventDatas.add(eventData);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return eventDatas;
	}

	// Getting single stat
	public EventData getEventData(long id) {

		Cursor cursor = database.query(MySQLiteHelper.TABLE_EVENT_DATA,
				allColumns, MySQLiteHelper.COLUMN_EVENT_DATA_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();
		EventData eventData = new EventData(Integer.parseInt(cursor.getString(0)),
				Integer.parseInt(cursor.getString(1)), cursor.getString(2),
				cursor.getFloat(3), cursor.getFloat(4), cursor.getInt(5),
				cursor.getInt(6), cursor.getInt(7));
		return eventData;
	}

	private EventData cursorToEventData(Cursor cursor) {
		EventData eventData = new EventData();
		eventData.setId(cursor.getLong(0));
		eventData.setDrivingStatsId(cursor.getLong(1));
		eventData.setDate(cursor.getString(2));
		eventData.setLatitude(cursor.getInt(3));
		eventData.setLongitude(cursor.getInt(4));
		eventData.setAccEvent(cursor.getInt(5));
		eventData.setSpeedEvent(cursor.getInt(6));
		eventData.setBrakeEvent(cursor.getInt(7));
		return eventData;
	}
}
