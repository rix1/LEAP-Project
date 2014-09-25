package org.rix1.PhishLight;

import java.util.StringTokenizer;

/* This class is used to  format contents of an
 * SMS message to capture required and information.
 * Created by Phumlile Sopela on 14/09/2014
 * */

public class BankDetails {
	
	private String bankName;
	private String accountHolderFullName;
	private String transactionType;
	private String transactionPlace;
	private String transactionTime;
	private String transactionContent; 
	private String accountNumber;
	private String transactionDate;
	private String availableBalance;
	private String transactionAmount;
	
	public BankDetails() {
		this.bankName = "ANDROID_bank";
		this.transactionAmount = "" + 1234.89;
		this.accountHolderFullName = "ANDROID_account";
		this.transactionType = "ANDROID_transaction";
		this.transactionPlace = "ANDROID_place";
		this.transactionContent = "ANDROID_content"; 
		this.accountNumber = "..." + "5987";
		this.transactionTime = "00000";
		this.transactionDate = "";
		this.availableBalance = "" + 1234.56;
	}	
	
	/*
	 * This method helps format the a specific message
	 * as not all messages can be formated by this method.
	 * */
	
	public void setBankDetails(String smsContent){
		
		this.transactionContent = smsContent;
		// look for Bank name, acc no, available balance, type of transaction, amount of transaction, place, time and date.
		
		StringTokenizer stringTK = new StringTokenizer(smsContent, " ");
		
		this.bankName = stringTK.nextToken();
		
		if (this.bankName.equalsIgnoreCase("Standard")) { 
			this.bankName += " " + stringTK.nextToken();
		}
		
		this.transactionAmount = stringTK.nextToken().substring(1);
		
		this.transactionType = stringTK.nextToken();
		
		if( this.transactionType.equalsIgnoreCase("withdrawn") ) {
			
			stringTK.nextToken(); // ignore "from"
			stringTK.nextToken(); // ignore "Acc".
			
			this.accountNumber = "..." + stringTK.nextToken();
			
			stringTK.nextToken(); // ignore "at"
			
			this.transactionPlace = stringTK.nextToken();
			
			this.transactionTime = stringTK.nextToken();
			
			stringTK.nextToken(); // ignore "number"
			stringTK.nextToken(); // ignore "Acl"
			stringTK.nextToken(); // ignore "bal"
			
			this.availableBalance = stringTK.nextToken().substring(1);
			
			this.transactionDate = stringTK.nextToken();
		}
		
		else if( this.transactionType.equalsIgnoreCase("purchased") ) {
			stringTK.nextToken(); // ignore "from"
			stringTK.nextToken(); // ignore "Acc".
			
			this.accountNumber = "..." + stringTK.nextToken();
			
			stringTK.nextToken(); // ignore "at"
			
			this.transactionPlace = stringTK.nextToken();
			
			this.transactionTime = stringTK.nextToken();
			
			stringTK.nextToken(); // ignore "number"
			stringTK.nextToken(); // ignore "Acl."
			stringTK.nextToken(); // ignore "Bal".
			
			this.availableBalance = stringTK.nextToken().substring(1);
			
			this.transactionDate = stringTK.nextToken();
		}
		
		else if( this.transactionType.equalsIgnoreCase("deposited") ) {
			stringTK.nextToken(); // ignore "into"
			stringTK.nextToken(); // ignore "Acc".
			
			this.accountNumber = stringTK.nextToken();
			
			stringTK.nextToken(); // ignore "from"
			
			this.transactionPlace = stringTK.nextToken();
			
			this.transactionTime = stringTK.nextToken();
			
			stringTK.nextToken(); // ignore "Acl."
			stringTK.nextToken(); // ignore "Bal".
			
			this.availableBalance = stringTK.nextToken().substring(1);
			
			this.transactionDate = stringTK.nextToken();
		}
		
		else if( this.transactionType.equalsIgnoreCase("transferred") ) {
			stringTK.nextToken(); // ignore "to"
			stringTK.nextToken(); // ignore "Acc".
			
			this.accountNumber = stringTK.nextToken();
			
			stringTK.nextToken(); // ignore "Acl."
			stringTK.nextToken(); // ignore "Bal".
			
			this.availableBalance =stringTK.nextToken().substring(1);
			
			this.transactionDate = stringTK.nextToken();
		}
		
	}
	
	public void setAccountHolderFullName( String name ) {
		this.accountHolderFullName = name;
	}
	
	public String getBankName() {
		return this.bankName;
	}
	
	public String getTransactionAmount() {
		return this.transactionAmount;
	}
	
	public String getAccountHolderFullName() {
		return this.accountHolderFullName;
	}
	
	public String getTransactionType(){
		return this.transactionType;
	}
	
	public String getTransactionPlace() {
		return this.transactionPlace;
	}
	
	public String getTransactionContent() {
		return this.transactionContent;
	} 
	
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	public String getTransactionTime() {
		return this.transactionTime;
	}	
	
	public String getTransactionDate() {
		return this.transactionDate;
	}
	
	public String getAvailableBalance() {
		return this.availableBalance;
	}
	
	
	/*
	 * This function helps with checking if
	 * the contents of an sms contain bank details. 
	 * 
	 * Returns true if it found bank name as a
	 * opening signature of the message.
	 * */	
	
	public boolean smsHasBankContents( String smsContent ) {
		
		StringTokenizer stringTK = new StringTokenizer(smsContent, " ");
		
		if( stringTK.countTokens() > 0 ) {			
			String bankName  = stringTK.nextToken();
			
			if( bankName.equalsIgnoreCase("FNB") 		||
				bankName.equalsIgnoreCase("Capitac")	||
				bankName.equalsIgnoreCase("Standard")	||
				bankName.equalsIgnoreCase("ABSA")		||
				bankName.equalsIgnoreCase("Nedbank")
                    ) {
				return true;
			}				
		}
		return false;
	}
}
