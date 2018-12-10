package com.google.gwt.Jostle.client;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.ValidationException;


/** 
 * This class provides an object that keeps track of a budget
 * for one category. 
 * 
 * @author Bonnie
 * */
public class BudgetCategory {
	private String categoryName;
	private double budgetAmount;
	private double budgetSpent;
	private double budgetRemaining;

	/**
	 * Instantiates a budget category with its name and budget.
	 * 
	 * @param categoryName 
	 * @param budgetAmount 
	 */
	public BudgetCategory(String categoryName, double budgetAmount) {
		if (budgetAmount < CONSTANT.DOUBLEZERO) {
			throw new ValidationException("Cannot set negative budget");
		}
		checkValidDouble(budgetAmount);
		this.budgetAmount = budgetAmount;
		
		checkValidCategoryName(categoryName);
		this.categoryName = categoryName;
	}
	
	public String getCategoryName() {
		return this.categoryName;
	}
	
	public double getBudgetAmount() {
		return this.budgetAmount;
	}
	
	public double getbudgetSpent() {
		return this.budgetSpent;
	}
	
	public double getbudgetRemaining() {
		return this.budgetRemaining;
	}
	
	public double getPercentSpent() {
		if (this.budgetAmount== 0) {
			return 0;
		}
		// https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
		BigDecimal bd = new BigDecimal(this.budgetSpent / this.budgetAmount);
		bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue() * 100.00;
	}
	
	public double getPercentRemaining() {
		return 100.00 - this.getPercentSpent();
	}
	
	/**
	 * Calculates what percent of this category's budget 
	 * the input amount is
	 * 
	 * @param transactionAmount
	 * @return the percent amount the input is of category budget
	 */
	public double calcPercentOfBudget(double transactionAmount) {
		if (this.budgetAmount == CONSTANT.DOUBLEZERO) {
			return CONSTANT.DOUBLEZERO;
		}
		// https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
		BigDecimal bd = new BigDecimal(transactionAmount / this.budgetAmount);
		bd.setScale(2, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}
	
	public void setCategoryName(String newCategoryName) {
		checkValidCategoryName(newCategoryName);
		this.categoryName = newCategoryName;
	}
	
	/**
	 * Overwrites existing budget amount with a new one
	 * 
	 * @param newBudgetAmount the new budget amount 
	 * @throws ValidationException if a negative input was given
	 */
	public void setBudgetAmount(double newBudgetAmount) {
		if (newBudgetAmount < CONSTANT.MINBUDGET) {
			throw new ValidationException("Cannot set a negative budget");
		}
		else {
			checkValidDouble(newBudgetAmount);
			this.budgetAmount = newBudgetAmount;
			
			double res = this.budgetAmount - this.budgetSpent;
			checkResultInRange(res);
			this.budgetRemaining = res;
		}
	}
	
	public void resetbudgetSpent() {
		this.budgetSpent = CONSTANT.DOUBLEZERO;
		this.budgetRemaining = this.budgetAmount;
	}

	public void resetBudgetCategory() {
		this.budgetAmount = CONSTANT.DOUBLEZERO;
		resetbudgetSpent();
	}
	
	/**
	 * Updates budget used with an expense amount (spend money)
	 * 
	 * @spent is the amount spent
	 * @throws ValidationException if spent amount is negative
	 * 			if a negative amount was "spent" that is 
	 * 			considered a refund
	 */
	public void increasebudgetSpent(double spent) {
		if (spent < CONSTANT.DOUBLEZERO) {
			throw new ValidationException("Cannot spend negative amount");
		} 
		else {
			checkValidDouble(spent);
			double res1 = this.budgetSpent + spent;
			checkValidDouble(res1);
			this.budgetSpent = res1;
			
			double res2 = this.budgetAmount - this.budgetSpent;
			checkResultInRange(res2); 
			this.budgetRemaining = res2;			
		}
		return;
	}
	
	/** 
	 * Updates budget used for when money is refunded; this will increase
	 * the amount that remains in the budget
	 * NOTE: Not intended for use with income amounts
	 * 		 This is to rectify budget remaining in the case of a refund.
	 * 		 Is probably not the proper accounting way to handle this type 
	 * 		 of transaction; will do for now. 
	 * @refund is the amount refunded
	 * @throws ValidationException if refunded amount is negative
	 */
	public void decreasebudgetSpent(double refund) {
		if (refund < CONSTANT.DOUBLEZERO) {
			throw new ValidationException("Cannot refund a positive amount");
		}
		else {
			checkValidDouble(refund);
			double res1 = this.budgetSpent - refund;
			checkResultInRange(refund);
			this.budgetSpent = res1;
			
			double res2 = this.budgetAmount - this.budgetSpent;
			checkResultInRange(res2);
			this.budgetRemaining = res2;
		}
	}
	
	/* Checkers and validators */
	private void checkValidCategoryName(String input) {
		if (input.length() > CONSTANT.MAXNAMELENGTH) {
			throw new ValidationException("Maximum name of category is " + CONSTANT.MAXNAMELENGTH);
		}
	}
	private void checkValidDouble(double input) {
		if (input > CONSTANT.MAXBUDGET || input < CONSTANT.MINBUDGET) {
			throw new ValidationException("Input amount must be between " 
					+ CONSTANT.MINBUDGET + " and " + CONSTANT.MAXBUDGET);
		}
	}
	private void checkResultInRange(double result) {
		if (result > CONSTANT.MAXBUDGET || result < CONSTANT.MINBUDGET) {
			throw new ArithmeticException("Resulting amount exceeds maximum: " 
					+ CONSTANT.MAXBUDGET);
		}
	}
}
