package org.rix1.PhishLight;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * This class keeps relevant sms details e.g. sender's number
 * Created by Phumlile Sopela on 14/09/2014
 * */

public class SmsDetails {
	
	private String date;
	private Long smsId;
	private BankDetails smsBankDetails;
	private String smsPhoneNumber;
	private Boolean hasBankDetails = false;
	
	public SmsDetails(String date, Long smsId, String content, String phoneNumber) {
		
	    this.date = convertFormat(date);
	    this.smsId = smsId;
	    this.smsBankDetails = getBankSms(content);
	    this.smsPhoneNumber = phoneNumber;
	}

	// Method to convert to date from a string
    private String convertFormat(String date){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(Long.parseLong(date));
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        return dateFormat.format(cal.getTime());
    }

	public Long getSmsId() {
		return this.smsId;
	}

	public String getSmsDate() {
	    return this.date;
	}

	public BankDetails getBankDetails() {
	    return this.smsBankDetails;
	}
	
	public String getSmsPhoneNumber() {
	    return this.smsPhoneNumber;
	}
	
	public Boolean hasBankDetails() {
        Log.d("APP_DETAILS", "Check has bank details...");
		return this.hasBankDetails;
	}
	
	// Method that checks if a sms message has bank details

	public BankDetails getBankSms( String smsContent ) {

        Log.d("APP_DETAILS", "Check has bank details...");
		BankDetails newBankSms = new BankDetails();
		
		if( newBankSms.smsHasBankContents(smsContent) ) {
			// method to filter out bank details accordingly
			newBankSms.setBankDetails(smsContent);
			hasBankDetails = true;
			
			return newBankSms;
		}
		
		return null;
	}
}
