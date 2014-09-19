package org.rix1.PhishLight;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

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
