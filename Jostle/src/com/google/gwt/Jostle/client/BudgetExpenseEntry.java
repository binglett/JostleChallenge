package com.google.gwt.Jostle.client;

import java.util.Date;

import com.google.gwt.i18n.client.NumberFormat;

/**
 * Provides an object to represent an expense or refund entry  
 * 
 * NOTE: Date deprecated, need to replace...
 * @author Bonnie
 */
public class BudgetExpenseEntry {
	public static final NumberFormat nf2 = NumberFormat.getFormat("#.##");
	public static final NumberFormat nf20 = NumberFormat.getFormat("#.##");
	
	private Date dateCreated;
	private Date transactionDate;
	private String vendor;
	private double transactionAmount;
	private String budgetCategory;
	private String transactionItem;
	private String entryNote;
	
	public BudgetExpenseEntry(Date date, String vendor, double transAmount, 
			String budgetCategory, String transItem, String note) {
		this.dateCreated = new Date();
		this.transactionDate = date;
		this.vendor = vendor;
		this.transactionAmount = transAmount;
		this.budgetCategory = budgetCategory;
		this.transactionItem = transItem;
		this.entryNote = note;
	}
	
	public Date getDateCreated() {
		return this.dateCreated;
	}
	
	public Date getTransactionDate() {
		return this.transactionDate;
	}
	
	public String getDateCreatedString() {
		return getYYYYMMDDString(this.dateCreated);
	}
	
	public String getTransactionDateString() {
		return getYYYYMMDDString(this.transactionDate);
	}
	
	private String getYYYYMMDDString(Date date) {
		return date.getYear() + " " + date.getMonth() + " " + date.getDay();
	}
	
	public String getVendor() {
		return this.vendor;
	}
	
	public double getTransactionAmount() {
		return this.transactionAmount;
	}
	
	public String getEntryCategory() {
		return this.budgetCategory;
	}
	
	public String getTransactionItem() {
		return this.transactionItem;
	}

	public String getEntryNote() {
		return this.entryNote;
	}
}
