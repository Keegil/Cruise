package com.example.cruiseev;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {
	
	//Logging cruise
	public static final String TABLE_CRUISE = "cruise";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DRIVE_LENGTH = "drive_length";
	public static final String COLUMN_DATE = "date";
	public static final String COLUMN_ACC_MISTAKES = "acc_mistakes";
	public static final String COLUMN_SPEED_MISTAKES = "speed_mistakes";
	public static final String COLUMN_POINTS = "points";
	public static final String COLUMN_RANGE = "remaining_range";
	
	private static final String DATABASE_NAME = "cruise.db";
	private static final int DATABASE_VERSION = 1;
	
	// Database creation sql statement table logging
	private static final String DATABASE_CREATE = "create table "
			+ TABLE_CRUISE + "(" + COLUMN_ID
			+ " integer primary key autoincrement, " + COLUMN_DRIVE_LENGTH
			+ " text not null, " + COLUMN_DATE 
			+ " text not null, " + COLUMN_ACC_MISTAKES
			+ " text not null, " + COLUMN_SPEED_MISTAKES
			+ " text not null, " + COLUMN_POINTS
			+ " text not null, " + COLUMN_RANGE +" text not null);";
	

	public MySQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.w(MySQLiteHelper.class.getName(),
				"Upgrading database from version " + oldVersion + " to "
						+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CRUISE);
		onCreate(db);
	}

}