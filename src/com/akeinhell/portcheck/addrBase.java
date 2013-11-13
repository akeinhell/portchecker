package com.akeinhell.portcheck;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class addrBase extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "addr_database.db";
	private static final int DATABASE_VERSION = 1;
	public static final String TABLE_NAME = "addr_table";
	public static final String UID = "_id";
	public static final String IP = "IP";
	public static final String port = "port";

	private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
			+ TABLE_NAME + " (" + UID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ IP + " VARCHAR(255), "+port+" INTEGER);";

	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
			+ TABLE_NAME;

	public addrBase(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}



	public addrBase(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(SQL_CREATE_ENTRIES);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w("LOG_TAG", "ќбновление базы данных с версии " + oldVersion
				+ " до версии " + newVersion + ", которое удалит все старые данные");
		// ”дал€ем предыдущую таблицу при апгрейде
		db.execSQL(SQL_DELETE_ENTRIES);
		// —оздаЄм новый экземпл€р таблицы
		onCreate(db);
	}

}
