package com.prototype.cruise;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "cruise20.db";
	private static final int DATABASE_VERSION = 1;

	// driving stats table
	public static final String TABLE_DRIVING_STATS = "driving_stats";
	public static final String COLUMN_DRIVING_STATS_ID = "_id";
	public static final String COLUMN_DRIVING_STATS_DATE = "date";
	public static final String COLUMN_DRIVING_STATS_NUM_ACC_EVENT = "num_acc_event";
	public static final String COLUMN_DRIVING_STATS_NUM_SPEED_EVENT = "num_speed_event";
	public static final String COLUMN_DRIVING_STATS_NUM_BRAKE_EVENT = "num_brake_event";
	public static final String COLUMN_DRIVING_STATS_NUM_ROUTE_EVENT = "num_route_event";
	public static final String COLUMN_DRIVING_STATS_DRIVE_DISTANCE = "drive_distance";
	public static final String COLUMN_DRIVING_STATS_RANGE_START = "range_start";
	public static final String COLUMN_DRIVING_STATS_RANGE_USED = "range_used";
	public static final String COLUMN_DRIVING_STATS_RANGE_END = "range_end";
	public static final String COLUMN_DRIVING_STATS_RANGE_MODIFIER = "range_modifier";
	public static final String COLUMN_DRIVING_STATS_FUEL_SAVINGS = "fuel_savings";

	// event data table
	public static final String TABLE_EVENT_DATA = "event_data";
	public static final String COLUMN_EVENT_DATA_ID = "_id";
	public static final String COLUMN_EVENT_DATA_DRIVING_STATS_ID = "driving_stats_id";
	public static final String COLUMN_EVENT_DATA_TIME = "time";
	public static final String COLUMN_EVENT_DATA_LATITUDE = "latitude";
	public static final String COLUMN_EVENT_DATA_LONGITUDE = "longitude";
	public static final String COLUMN_EVENT_DATA_ACC_EVENT = "acc_event";
	public static final String COLUMN_EVENT_DATA_SPEED_EVENT = "speed_event";
	public static final String COLUMN_EVENT_DATA_BRAKE_EVENT = "brake_event";

	// gps data table
	public static final String TABLE_GPS_DATA = "gps_data";
	public static final String COLUMN_GPS_DATA_ID = "_id";
	public static final String COLUMN_GPS_DATA_DRIVING_STATS_ID = "driving_stats_id";
	public static final String COLUMN_GPS_DATA_TIME = "time";
	public static final String COLUMN_GPS_DATA_LATITUDE = "latitude";
	public static final String COLUMN_GPS_DATA_LONGITUDE = "longitude";

	// tollpass data table
	public static final String TABLE_TOLLPASS_DATA = "tollpass_data";
	public static final String COLUMN_TOLLPASS_DATA_ID = "_id";
	public static final String COLUMN_TOLLPASS_DATA_DRIVING_STATS_ID = "driving_stats_id";
	public static final String COLUMN_TOLLPASS_DATA_SAVINGS = "savings";

	// tollbooth data table
	public static final String TABLE_TOLLBOOTH_DATA = "tollbooth_data";
	public static final String COLUMN_TOLLBOOTH_DATA_ID = "_id";
	public static final String COLUMN_TOLLBOOTH_DATA_LATITUDE = "latitude";
	public static final String COLUMN_TOLLBOOTH_DATA_LONGITUDE = "longitude";
	public static final String COLUMN_TOLLBOOTH_DATA_PRICE = "price";

	// Database creation sql statement table logging
	private static final String DATABASE_CREATE_TABLE_DRIVING_STATS = "create table "
			+ TABLE_DRIVING_STATS
			+ "("
			+ COLUMN_DRIVING_STATS_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_DRIVING_STATS_DATE
			+ " text not null, "
			+ COLUMN_DRIVING_STATS_NUM_ACC_EVENT
			+ " text not null, "
			+ COLUMN_DRIVING_STATS_NUM_SPEED_EVENT
			+ " text not null, "
			+ COLUMN_DRIVING_STATS_NUM_BRAKE_EVENT
			+ " text not null, "
			+ COLUMN_DRIVING_STATS_NUM_ROUTE_EVENT
			+ " text not null, "
			+ COLUMN_DRIVING_STATS_DRIVE_DISTANCE
			+ " text not null, "
			+ COLUMN_DRIVING_STATS_RANGE_START
			+ " text not null, "
			+ COLUMN_DRIVING_STATS_RANGE_USED
			+ " text not null, "
			+ COLUMN_DRIVING_STATS_RANGE_END
			+ " text not null, "
			+ COLUMN_DRIVING_STATS_RANGE_MODIFIER
			+ " text not null, "
			+ COLUMN_DRIVING_STATS_FUEL_SAVINGS + " text not null);";

	private static final String DATABASE_CREATE_TABLE_EVENT_DATA = "create table "
			+ TABLE_EVENT_DATA
			+ "("
			+ COLUMN_EVENT_DATA_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_EVENT_DATA_DRIVING_STATS_ID
			+ " text not null, "
			+ COLUMN_EVENT_DATA_TIME
			+ " text not null, "
			+ COLUMN_EVENT_DATA_LATITUDE
			+ " text not null, "
			+ COLUMN_EVENT_DATA_LONGITUDE
			+ " text not null, "
			+ COLUMN_EVENT_DATA_ACC_EVENT
			+ " text not null, "
			+ COLUMN_EVENT_DATA_SPEED_EVENT
			+ " text not null, "
			+ COLUMN_EVENT_DATA_BRAKE_EVENT + " text not null);";

	private static final String DATABASE_CREATE_TABLE_GPS_DATA = "create table "
			+ TABLE_GPS_DATA
			+ "("
			+ COLUMN_GPS_DATA_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_GPS_DATA_DRIVING_STATS_ID
			+ " text not null, "
			+ COLUMN_GPS_DATA_TIME
			+ " text not null, "
			+ COLUMN_GPS_DATA_LATITUDE
			+ " text not null, "
			+ COLUMN_GPS_DATA_LONGITUDE + " text not null);";

	private static final String DATABASE_CREATE_TABLE_TOLLPASS_DATA = "create table "
			+ TABLE_TOLLPASS_DATA
			+ "("
			+ COLUMN_TOLLPASS_DATA_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_TOLLPASS_DATA_DRIVING_STATS_ID
			+ " text not null, "
			+ COLUMN_TOLLPASS_DATA_SAVINGS + " text not null);";

	private static final String DATABASE_CREATE_TABLE_TOLLBOOTH_DATA = "create table "
			+ TABLE_TOLLBOOTH_DATA
			+ "("
			+ COLUMN_TOLLBOOTH_DATA_ID
			+ " integer primary key autoincrement, "
			+ COLUMN_TOLLBOOTH_DATA_LATITUDE
			+ " text not null, "
			+ COLUMN_TOLLBOOTH_DATA_LONGITUDE
			+ " text not null, "
			+ COLUMN_TOLLBOOTH_DATA_PRICE + " text not null);";

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE_TABLE_DRIVING_STATS);
		database.execSQL(DATABASE_CREATE_TABLE_EVENT_DATA);
		database.execSQL(DATABASE_CREATE_TABLE_GPS_DATA);
		database.execSQL(DATABASE_CREATE_TABLE_TOLLPASS_DATA);
		database.execSQL(DATABASE_CREATE_TABLE_TOLLBOOTH_DATA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRIVING_STATS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_EVENT_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_GPS_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOLLPASS_DATA);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOLLBOOTH_DATA);
		onCreate(db);
	}

}