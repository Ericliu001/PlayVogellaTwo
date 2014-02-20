package com.example.playvogellatwo.content;

import android.app.ListActivity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.playvogellatwo.R;
import com.example.playvogellatwo.content.contentprovider.MyTodoContentProvider;
import com.example.playvogellatwo.content.database.TodoTable;

public class TodosOverviewActivity extends ListActivity implements LoaderCallbacks<Cursor> {
	
	private static final int ACTIVITY_CREATE = 0;
	private static final int ACTIVITY_EDIT = 1;
	private static final int DELETE_ID = Menu.FIRST + 1;
	
	private SimpleCursorAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todos_overview);
		
		this.getListView().setDividerHeight(2);
		fillData();
		registerForContextMenu(getListView());
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void fillData() {
		String[] from = new String[]{TodoTable.COLUMN_SUMMARY};
		int[]      to = new int[]{R.id.label};
		
		
		getLoaderManager().initLoader(0, null, this); // DON'T MISS THIS LINE !!!
		adapter = new SimpleCursorAdapter(this, R.layout.todo_row, null, from, to, 0);
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.todos_overview, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.insert:
			createTodo();
			return true;
		}
		
		return super.onOptionsItemSelected(item);
	}
	
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case DELETE_ID:
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
			Uri uri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/" + info.id);
			getContentResolver().delete(uri, null, null);
//			fillData();
			return true;
		}
		
		
		return super.onContextItemSelected(item);
	}

	private void createTodo() {
		Intent i = new Intent(this, TodoDetailActivity.class);
		startActivityForResult(i, ACTIVITY_CREATE);
	}
	
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, DELETE_ID, 0, "Delete");
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {

		Intent i = new Intent(this, TodoDetailActivity.class);
		Uri todoUri = Uri.parse(MyTodoContentProvider.CONTENT_URI + "/" + id);
		i.putExtra(MyTodoContentProvider.CONTENT_ITEM_TYPE, todoUri);
		
		startActivityForResult(i, ACTIVITY_EDIT);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = {TodoTable.COLUMN_ID, TodoTable.COLUMN_SUMMARY};
		CursorLoader cursorLoader = new CursorLoader(this, MyTodoContentProvider.CONTENT_URI, projection, null, null, null);
		
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		adapter.swapCursor(data);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		adapter.swapCursor(null);
	}

}
