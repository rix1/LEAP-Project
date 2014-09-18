package org.rix1.PhishLight;

import android.annotation.SuppressLint;

import java.util.Date;
import java.util.StringTokenizer;

@SuppressLint("UseValueOf")
public class BankDetails {
	
	private String bankName;
	private String accountHolderFullName;
	private String transactionType;
	private String transactionPlace;
	private String transactionTime;
	private String transactionContent; 
	private Long accountNumber;	
	private Date transactionDate;
	private Double availableBalance;
	private Double transactionAmount;
	
	public BankDetails() {
		this.bankName = "ANDROID_bank";
		this.transactionAmount = 1234.89;
		this.accountHolderFullName = "ANDROID_account";
		this.transactionType = "ANDROID_transaction";
		this.transactionPlace = "ANDROID_place";
		this.transactionContent = "ANDROID_content"; 
		this.accountNumber = new Long("..." + "5987");		
		this.transactionTime = "00000";
		this.transactionDate = new Date();
		this.availableBalance = 1234.56;
	}
	
	@SuppressWarnings("deprecation")
	public void setBankDetails(String smsContent){
		
		this.transactionContent = smsContent;
		// look for Bank name, acc no, available balance, type of transaction, amount of transaction, place, time and date.
		
		StringTokenizer stringTK = new StringTokenizer(smsContent, " ");
		
		this.bankName = stringTK.nextToken();
		
		if (this.bankName.equalsIgnoreCase("Standard")) { 
			this.bankName += " " + stringTK.nextToken();
		}
		
		this.transactionAmount = Double.parseDouble( stringTK.nextToken().substring(1) );
		
		this.transactionType = stringTK.nextToken();
		
		if( this.transactionType.equalsIgnoreCase("withdrawn") ) {
			
			stringTK.nextToken(); // ignore "from"
			stringTK.nextToken(); // ignore "Acc".
			
			this.accountNumber = new Long("..." + stringTK.nextToken());
			
			stringTK.nextToken(); // ignore "at"
			
			this.transactionPlace = stringTK.nextToken();
			
			this.transactionTime = stringTK.nextToken();
			
			stringTK.nextToken(); // ignore "number"\
			
			this.availableBalance = Double.parseDouble( stringTK.nextToken().substring(1) );
			
			this.transactionDate = new Date( stringTK.nextToken() );			
		}
		
		else if( this.transactionType.equalsIgnoreCase("purchased") ) {
			stringTK.nextToken(); // ignore "from"
			stringTK.nextToken(); // ignore "Acc".
			
			this.accountNumber = new Long("..." + stringTK.nextToken());
			
			stringTK.nextToken(); // ignore "at"
			
			this.transactionPlace = stringTK.nextToken();
			
			this.transactionTime = stringTK.nextToken();
			
			stringTK.nextToken(); // ignore "number"
			stringTK.nextToken(); // ignore "Acl."
			stringTK.nextToken(); // ignore "Bal".
			
			this.availableBalance = Double.parseDouble( stringTK.nextToken().substring(1) );
			
			this.transactionDate = new Date( stringTK.nextToken() );	
		}
		
		else if( this.transactionType.equalsIgnoreCase("deposited") ) {
			stringTK.nextToken(); // ignore "into"
			stringTK.nextToken(); // ignore "Acc".
			
			this.accountNumber = new Long(stringTK.nextToken());
			
			stringTK.nextToken(); // ignore "from"
			
			this.transactionPlace = stringTK.nextToken();
			
			this.transactionTime = stringTK.nextToken();
			
			stringTK.nextToken(); // ignore "Acl."
			stringTK.nextToken(); // ignore "Bal".
			
			this.availableBalance = Double.parseDouble( stringTK.nextToken().substring(1) );
			
			this.transactionDate = new Date( stringTK.nextToken() );	
		}
		
		else if( this.transactionType.equalsIgnoreCase("transferred") ) {
			stringTK.nextToken(); // ignore "to"
			stringTK.nextToken(); // ignore "Acc".
			
			this.accountNumber = new Long(stringTK.nextToken());
			
			stringTK.nextToken(); // ignore "Acl."
			stringTK.nextToken(); // ignore "Bal".
			
			this.availableBalance = Double.parseDouble( stringTK.nextToken().substring(1) );
			
			this.transactionDate = new Date( stringTK.nextToken() );	
		}
		
	}
	
	public void setAccountHolderFullName( String name ) {
		this.accountHolderFullName = name;
	}
	
	public String getBankName() {
		return this.bankName;
	}
	
	public Double getTransactionAmount() {
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
	
	public Long getAccountNumber() {
		return this.accountNumber;
	}
	
	public String getTransactionTime() {
		return this.transactionTime;
	}	
	
	public Date getTransactionDate() {
		return this.transactionDate;
	}
	
	public Double getAvailableBalance() {
		return this.availableBalance;
	}
	
	public boolean smsHasBankContents( String smsContent ) {
		
		StringTokenizer stringTK = new StringTokenizer(smsContent, " ");
		
		if( stringTK.countTokens() > 0 ) {			
			String bankName  = stringTK.nextToken();
			
			if( bankName.equalsIgnoreCase("FNB") 		||
				bankName.equalsIgnoreCase("Capitac")	||
				bankName.equalsIgnoreCase("Standard")	||
				bankName.equalsIgnoreCase("ABSA")		||
				bankName.equalsIgnoreCase("Nedbank")) {
				return true;
			}				
		}
		return false;
	}
}
