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
 * Description: This class houses the network communication and 
 * extends asynchronous class to be able to run in the background
 * without disrupting the main application that the user sees.
 */

public class NetworkHelper extends AsyncTask<String, Void, Boolean> {

    private HttpClient httpClient;
    private final String POST_URI = "http://royrvik.org:8880/users/adduser";
    private static int counter = 0;
    private ArrayList<HttpPost> postmessages;

    public NetworkHelper(){
        httpClient = new DefaultHttpClient();
        postmessages = new ArrayList<HttpPost>();
    }
    
    /*
     * This method is called when the asynchronous task
     * starts to execute.
     * */ 

    protected Boolean doInBackground(String... params) {
        Log.d("APP:", "AsyncTask: Executing POST...");

        for (HttpPost httpPost : postmessages){
            try{
                HttpResponse response = httpClient.execute(httpPost);
                // Write reponse to log
                Log.d("HTTP POST response", response.toString());
            }catch (ClientProtocolException e ){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }

        return null;
    }
    
    /*
     * This method is responsible to post data
     * from the application to the web server.
     * */

    public void makePOSTRequest(List data) {
        Log.d("APP:", "AsyncTask: Generating httpPOsts...");

        counter++;
        HttpPost httpPost = new HttpPost(POST_URI);

        List<NameValuePair> nameValuePairs = data;

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        postmessages.add(httpPost);
    }
}
