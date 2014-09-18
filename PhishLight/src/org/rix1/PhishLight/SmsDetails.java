package org.rix1.PhishLight;

import java.util.Date;

public class SmsDetails {
	
	private Date date;
	private Long smsId;
	private BankDetails smsBankDetails;
	private String smsPhoneNumber;
	private Boolean hasBankDetails = false;
	
	public SmsDetails(Date date, Long smsId, String content, String phoneNumber) {
		
	    this.date = date;
	    this.smsId = smsId;
	    this.smsBankDetails = getBankSms(content);
	    this.smsPhoneNumber = phoneNumber;
	}
	
	public Long getSmsId() {
		return this.smsId;
	}

	public Date getSmsDate() {
	    return this.date;
	}

	public BankDetails getBankDetails() {
	    return this.smsBankDetails;
	}
	
	public String getSmsPhoneNumber() {
	    return this.smsPhoneNumber;
	}
	
	public Boolean hasBankDetails() { 
		return this.hasBankDetails;
	}
	
	public BankDetails getBankSms( String smsContent ) {
		
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
