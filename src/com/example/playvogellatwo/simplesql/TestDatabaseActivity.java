package com.example.playvogellatwo.simplesql;

import java.util.List;
import java.util.Random;

import com.example.playvogellatwo.R;
import com.example.playvogellatwo.R.layout;
import com.example.playvogellatwo.R.menu;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;

public class TestDatabaseActivity extends ListActivity {
	private CommentsDataSource datasource;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test_database);

		datasource = new CommentsDataSource(this);
		datasource.open();

		List<Comment> values = datasource.getAllComments();

		ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
				android.R.layout.simple_list_item_1, values);
		setListAdapter(adapter);
	}

	public void onClick(View view) {
		ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
		Comment comment = null;
		switch (view.getId()) {
		case R.id.btAdd:
			String[] comments = new String[]{" Eric ", " is ", " sexy "  };
			int nextInt = new Random().nextInt(3);
			
			comment = datasource.createComment(comments[nextInt]);
			adapter.add(comment);
			break;

		case R.id.btDelete:
			if (getListAdapter().getCount() > 0) {
				comment = (Comment) getListAdapter().getItem(0);
				datasource.deleteComment(comment);
				adapter.remove(comment);
			}
			break;
		default:
			break;
		}
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		datasource.open();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		datasource.close();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test_database, menu);
		return true;
	}

}
