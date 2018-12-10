package com.google.gwt.Jostle.client;

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.HTMLPanel;

/** 
 * Provides a UI component that represents a budget for one category.
 * This allows a user to identify what the category is, how much is spent,
 * how much is remaining in the budget and allows them to alter the budget
 * spent by creating an expense or refund entry for the category. 
 * 
 * @author Bonnie
 */
public class BudgetCategoryUI extends BudgetCategory {
	private HTMLPanel budgetCategoryBarPanel;
	private HTMLPanel categoryNamePanel;
	private HTMLPanel budgetRemainingPanel;
	private HTMLPanel budgetCategoryBar;
	private HTMLPanel budgetPercentLabel;

	public static final NumberFormat nf2 = NumberFormat.getFormat("#.##");
	public static NumberFormat nf20;
	
	public static final double GOODPERCENT = 50;
	public static final double OKAYPERCENT = 75;
	public static final double BADPERCENT = 90;
	public static final double OVERPERCENT = 100;
	
	public static final String GOODPERCENTSTYLE = "goodCategoryBarColor";
	public static final String OKAYPERCENTSTYLE = "okayCategoryBarColor";
	public static final String BADPERCENTSTYLE = "badCategoryBarColor";
	public static final String OVERPERCENTSTYLE = "overCategoryBarColor";
	
	public BudgetCategoryUI(String categoryName, double budgetAmount) {
		super(categoryName, budgetAmount);

		nf20 = NumberFormat.getFormat("#.##");
		nf20.overrideFractionDigits(2);
		
		budgetCategoryBar = new HTMLPanel("div", "");
		budgetCategoryBar.setWidth("" + this.getPercentSpent() + "%");
		budgetCategoryBar.addStyleName(GOODPERCENTSTYLE);
		budgetCategoryBar.addStyleName("budgetCategoryBar");
		
		budgetPercentLabel = new HTMLPanel("h3", "" + nf2.format(this.getPercentSpent()) + "%");
		budgetPercentLabel.addStyleName("budgetPercentLabel");
		
		budgetCategoryBarPanel = new HTMLPanel("div", "");
		budgetCategoryBarPanel.addStyleName("budgetCategoryBarPanel");
		budgetCategoryBarPanel.add(budgetCategoryBar);
		budgetCategoryBarPanel.add(budgetPercentLabel);
		
		categoryNamePanel = new HTMLPanel("h2", this.getCategoryName());
		categoryNamePanel.addStyleName("budgetCategoryName");
		
		budgetRemainingPanel = new HTMLPanel("h2", "$" + nf2.format(this.getbudgetRemaining()) + "/$" + nf2.format(this.getBudgetAmount()));
		budgetRemainingPanel.addStyleName("budgetRemaining");
	}
	
	public HTMLPanel getBudgetCategoryBarPanel() {		
		return budgetCategoryBarPanel;
	}
	
	public HTMLPanel getBudgetCategoryNamePanel() {
		return categoryNamePanel;
	}
	
	public HTMLPanel getBudgetRemainingDecimalPanel() {
		return budgetRemainingPanel;
	}
	/**
	 * Updates this UI to reflect the change in budget status
	 * via a transaction
	 * @param transactionAmount is a positive or negative amount 
	 * 			representing an expense or refunded amount
	 */
	public void updateBudgetSpent(double transactionAmount) {
		final double prevPercentSpent = this.getPercentSpent();
		
		if (transactionAmount >= CONSTANT.DOUBLEZERO) {
			this.increasebudgetSpent(transactionAmount);
		}
		else {
			this.decreasebudgetSpent(-transactionAmount);
		}
		
		if (this.getPercentSpent() < 0) {
			this.budgetCategoryBar.setWidth("0");
		}
		else {
			this.budgetCategoryBar.setWidth("" + this.getPercentSpent() + "%");
		}
		this.budgetPercentLabel.getElement().setInnerHTML("" + nf2.format(this.getPercentSpent()) + "%");
		this.budgetRemainingPanel.getElement().setInnerHTML("$" + nf2.format(this.getbudgetRemaining()) + "/$" + nf2.format(this.getBudgetAmount()));
		setBudgetBarStyleColor(prevPercentSpent, this.getPercentSpent(), budgetCategoryBar);
	}
	
	/**
	 * Updates the color according to levels set in this class to visually represent
	 * health of the budget
	 * @param prevPercent the previous percentage of budget used 
	 * @param newPercentSpent the current percentage of budget used
	 * @param budgetCategoryBar the UI element that displays the colors that are 
	 * 			set in this method 
	 * */
	private void setBudgetBarStyleColor(double prevPercent, double newPercentSpent, HTMLPanel budgetCategoryBar) {
		if (GOODPERCENT > prevPercent) {
			budgetCategoryBar.removeStyleName(GOODPERCENTSTYLE);
		} 
		else if (GOODPERCENT <= prevPercent && OKAYPERCENT > prevPercent) {
			budgetCategoryBar.removeStyleName(OKAYPERCENTSTYLE);
		}
		else if (OKAYPERCENT <= prevPercent && BADPERCENT > prevPercent) {
			budgetCategoryBar.removeStyleName(BADPERCENTSTYLE);
		} 
		else {
			budgetCategoryBar.removeStyleName(OVERPERCENTSTYLE);
		}
		budgetCategoryBar.addStyleName(getBarValue(newPercentSpent));
	}
	/**
	 * Helper for setBudgetBarStyleColor that determines the element
	 * style according to the threshold of values defined in this class
	 * @param percentSpent
	 * @return a string that represents the style for the percent range
	 */
	private String getBarValue(double percentSpent) {
		if (GOODPERCENT > percentSpent) {
			return GOODPERCENTSTYLE;
		} 
		else if (GOODPERCENT <= percentSpent && OKAYPERCENT > percentSpent) {
			return OKAYPERCENTSTYLE;
		}
		else if (OKAYPERCENT <= percentSpent && BADPERCENT > percentSpent) {
			return BADPERCENTSTYLE;
		} 
		else {
			return OVERPERCENTSTYLE;
		}
	}
	
	
}
