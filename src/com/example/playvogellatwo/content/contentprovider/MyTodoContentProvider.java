package com.example.playvogellatwo.content.contentprovider;

import java.util.Arrays;
import java.util.HashSet;

import com.example.playvogellatwo.content.database.TodoDatabaseHelper;
import com.example.playvogellatwo.content.database.TodoTable;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

public class MyTodoContentProvider extends ContentProvider {

	private TodoDatabaseHelper dbHelper;

	private static final int TODOS = 10;
	private static final int TODO_ID = 20;

	private static final String AUTHORITY = "com.example.playvogellatwo.provider";

	private static final String BASE_PATH = "todos";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
			+ "/" + BASE_PATH);
	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/todos";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "todos";

	private static final UriMatcher sURIMatcher = new UriMatcher(
			UriMatcher.NO_MATCH);
	static {
		sURIMatcher.addURI(AUTHORITY, BASE_PATH, TODOS);
		sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", TODO_ID);
	}

	@Override
	public boolean onCreate() {
		dbHelper = new TodoDatabaseHelper(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {

		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		checkColumns(projection);

		queryBuilder.setTables(TodoTable.TABLE_TODO);

		int uriType = sURIMatcher.match(uri);
		switch (uriType) {
		case TODOS:

			break;
		case TODO_ID:
			queryBuilder.appendWhere(TodoTable.COLUMN_ID + " = "
					+ uri.getLastPathSegment());
			break;
		default:
			throw new IllegalArgumentException();
		}

		SQLiteDatabase db = dbHelper.getWritableDatabase();
		Cursor cursor = queryBuilder.query(db, projection, selection,
				selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsDeleted = 0;
		long id = 0;
		switch (uriType) {
		case TODOS:
			id = sqlDB.insert(TodoTable.TABLE_TODO, null, values);
			break;

		default:
			throw new IllegalArgumentException();
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return Uri.parse(BASE_PATH + "/" + id);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

		int rowsDeleted = 0;
		switch (uriType) {
		case TODOS:
			rowsDeleted = sqlDB.delete(TodoTable.TABLE_TODO, selection,
					selectionArgs);
			break;

		case TODO_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsDeleted = sqlDB.delete(TodoTable.TABLE_TODO,
						TodoTable.COLUMN_ID + "=" + id, null);
			} else {
				rowsDeleted = sqlDB.delete(TodoTable.TABLE_TODO,
						TodoTable.COLUMN_ID + "=" + id + " and " + selection,
						null);
			}
			break;
		default:
			throw new IllegalArgumentException();
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {

		int uriType = sURIMatcher.match(uri);
		SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();
		int rowsUpdated = 0;

		switch (uriType) {
		case TODOS:
			rowsUpdated = sqlDB.update(TodoTable.TABLE_TODO, values, selection,
					selectionArgs);
			break;
		case TODO_ID:
			String id = uri.getLastPathSegment();
			if (TextUtils.isEmpty(selection)) {
				rowsUpdated = sqlDB.update(TodoTable.TABLE_TODO, values,
						TodoTable.COLUMN_ID + "= " + id, selectionArgs);
			} else {
				rowsUpdated = sqlDB.update(TodoTable.TABLE_TODO, values,
						TodoTable.COLUMN_ID + "= " + id + " and " + selection,
						selectionArgs);
			}
			break;
		default:
			throw new IllegalArgumentException();
		}
		return rowsUpdated;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	private void checkColumns(String[] projection) {
		String[] available = { TodoTable.COLUMN_ID, TodoTable.COLUMN_SUMMARY,
				TodoTable.COLUMN_CATEGORY, TodoTable.COLUMN_DESCRIPTION };

		if (projection != null) {
			HashSet<String> requestedColumns = new HashSet<String>(
					Arrays.asList(projection));
			HashSet<String> availableColumns = new HashSet<String>(
					Arrays.asList(available));

			if (!availableColumns.containsAll(requestedColumns)) {
				throw new IllegalArgumentException();
			}
		}
	}
}
