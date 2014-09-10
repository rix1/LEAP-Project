package org.rix1.PhishLight;


import android.os.AsyncTask;
import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rikard Eide on 10/09/14.
 * Description:
 */
public class NetworkHelper extends AsyncTask<String, Void, Boolean> {

    private HttpClient httpClient;
    private HttpPost httpPost;
    private final String POST_URI = "http://royrvik.org:8880/users/adduser";
    private static int counter = 0;

    public NetworkHelper(){
        httpClient = new DefaultHttpClient();
    }

    protected Boolean doInBackground(String... params) {
        Log.d("APP:", "AsyncTask: Executing POST...");
        try{
            HttpResponse response = httpClient.execute(httpPost);
            // Write reponse to log
            Log.d("HTTP POST response", response.toString());
        }catch (ClientProtocolException e ){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return null;
    }

    public void makePOSTRequest() {
        counter++;
        httpPost = new HttpPost(POST_URI);

        // Post data: 'username=USERNAME & email=EMAIL & fullname=FULLNAME & age=AGE & location=LOCATION & gender=GENDER'
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("username", "ANDROID_user " + counter));
        nameValuePairs.add(new BasicNameValuePair("email", "ANDROID_email"));
        nameValuePairs.add(new BasicNameValuePair("fullname", "ANDROID_fullname"));
        nameValuePairs.add(new BasicNameValuePair("age", "ANDROID_age"));
        nameValuePairs.add(new BasicNameValuePair("location", "ANDROID_location"));
        nameValuePairs.add(new BasicNameValuePair("gender", "ANDROID_gender"));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }
}
