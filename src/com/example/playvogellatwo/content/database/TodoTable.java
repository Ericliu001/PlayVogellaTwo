package com.example.playvogellatwo.content.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class TodoTable implements BaseColumns  {
	
	public static final String TABLE_TODO = "todo";
	public static final String COLUMN_ID = BaseColumns._ID;
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_SUMMARY = "summary";
	public static final String COLUMN_DESCRIPTION = "description";
	
	private static final String DATABASE_CREATE = " create table "
			+ TABLE_TODO 
			+ " ( " + COLUMN_ID + " integer primary key autoincrement,"
			+ COLUMN_CATEGORY + " text not null, "
			+ COLUMN_SUMMARY + " text not null, "
			+ COLUMN_DESCRIPTION + " text not null ); ";
			
	public static void onCreate(SQLiteDatabase db){
		db.execSQL(DATABASE_CREATE);
	}
	
	public static void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
		db.execSQL(" drop table if exists " + TABLE_TODO);
		onCreate(db);
	}
	
}
