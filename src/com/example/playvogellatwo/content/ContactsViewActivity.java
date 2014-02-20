package com.example.playvogellatwo.content;

import com.example.playvogellatwo.R;
import com.example.playvogellatwo.R.layout;
import com.example.playvogellatwo.R.menu;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.widget.TextView;

public class ContactsViewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contants_view);
		
		TextView contactView = (TextView) findViewById(R.id.contactView);
		
		Cursor cursor = getContacts();
		
		while (cursor.moveToNext()) {
			String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			contactView.append("Name: ");
			contactView.append(displayName);
			contactView.append("\n");
		}
		cursor.close();
	}

	private Cursor getContacts() {

		Uri uri = ContactsContract.Contacts.CONTENT_URI;
		String[] projection = new String[] { ContactsContract.Contacts._ID,
				ContactsContract.Contacts.DISPLAY_NAME };
		String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '" + "1" + "'"; 
		String[] selectionArgs = null;
		String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC ";
		return getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.contants_view, menu);
		return true;
	}

}
