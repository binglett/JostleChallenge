package com.google.gwt.Jostle.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Provides a complete UI component that allows a user to track 
 * a list of budget categories and a list of expenses/refunds.
 * 
 * @author Bonnie
 *
 */
public class BudgetTrackerVerticalPanel extends VerticalPanel {
	public static NumberFormat nf20;
	
	private FlowPanel budgetTrackerContainer = new FlowPanel();
	private FlowPanel budgetCategoriesContainerPanel = new FlowPanel();
	
	private FlexTable entriesListContainerPanel = new FlexTable();
	private ArrayList<BudgetExpenseEntry> expenseEntriesList = new ArrayList<BudgetExpenseEntry>();
	
	/**
	 * NOTE: This constructor is for prototyping purposes, need to make a generic one
	 * Creates a list of BudgetCategoryUI objects by taking two test arrays 
	 * ASSUMES they're of equal length and are considered a pair based on indices. 
	 *  
	 * @param categoriesList a list of category names ASSUME same length as doublesList
	 * @param doublesList a list of budget amounts ASSUME same length as categoriesList
	 */
	public BudgetTrackerVerticalPanel(String[] categoriesList, double[] doublesList) {
		super();
		
		nf20 = NumberFormat.getFormat("#.##");
		nf20.overrideFractionDigits(2);
		
		// Add styles
		budgetTrackerContainer.addStyleName("budgetTrackerContainer");
		this.entriesListContainerPanel.getRowFormatter().addStyleName(0, "entriesListHeaderRow");
		this.entriesListContainerPanel.addStyleName("entriesListContainer");
		this.budgetCategoriesContainerPanel.addStyleName("budgetCategoriesContainerPanel");
		
		Button addCategoryButton = new Button("+ Category", addCategorClickHandler());
		addCategoryButton.addStyleName("addCategoryButton");
		
		budgetTrackerContainer.add(new HTMLPanel("h1", "Expense Tracker"));
		budgetTrackerContainer.add(addCategoryButton);
		getTestBudgetCategories(categoriesList, doublesList); 
		
		budgetTrackerContainer.add(this.budgetCategoriesContainerPanel);
		
		getEntriesTableHeaderRow();
		budgetTrackerContainer.add(this.entriesListContainerPanel);
		this.add(budgetTrackerContainer);
	}
	
	/**
	 * Helper method for constructor to traverse through the input lists
	 * and propagate the budgetCategoriesContainerPanel
	 * @param categoriesList list of category names
	 * @param doublesList list of category budget amounts
	 */
	private void getTestBudgetCategories(String[] categoriesList, double[] doublesList) {		
		for (int i = 0; i < categoriesList.length; i++) {
			addCategoryToList(categoriesList[i], doublesList[i]);
		}
	}
	
	/** 
	 * Propagates the table of expense entries with hardcoded values 
	 * */
	public void getEntriesTableHeaderRow() {
		this.entriesListContainerPanel.setText(0, 0, "Vendor");
		this.entriesListContainerPanel.setText(0, 1, "Item");
		this.entriesListContainerPanel.setText(0, 2, "Amount");
		this.entriesListContainerPanel.setText(0, 3, "Category");
	}
	
	/** 
	 * Instantiates a budget category UI component with given category name and
	 * budget amount and directly adds it to budgetCategoriesContainerPanel
	 * @param categoryName name of the category
	 * @param categoryAmount budget amount
	 * */
	public void addCategoryToList(String categoryName, Double categoryAmount) {
		final BudgetCategoryUI tmp = new BudgetCategoryUI(categoryName, categoryAmount);	
		
		final Button addEntryButton = new Button("+");
		addEntryButton.addStyleName("addEntryButton");
		
		this.budgetCategoriesContainerPanel.add(tmp.getBudgetCategoryNamePanel());
		this.budgetCategoriesContainerPanel.add(tmp.getBudgetRemainingDecimalPanel());
		this.budgetCategoriesContainerPanel.add(addEntryButton);
		this.budgetCategoriesContainerPanel.add(tmp.getBudgetCategoryBarPanel());
		
		addEntryButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addEntryPanel(tmp);
			}
		});
	}
	
	/**
	 * Adds an expense entry to entriesListContainerPanel and updates the 
	 * corresponding budget category UI component
	 * @param date dummy param that doesn't do anything for now
	 * @param vendor 
	 * @param transAmount
	 * @param budgetCategory
	 * @param transItem
	 * @param note
	 */
	public void addEntryToTable(Date date, String vendor, double transAmount, 
			BudgetCategoryUI budgetCategory, String transItem, String note) {
		String budgetCategoryName = budgetCategory.getCategoryName();
		// *** Date is deprecated... placeholder for now
		BudgetExpenseEntry newEntry = 
				new BudgetExpenseEntry(new Date(), vendor, transAmount, budgetCategoryName, transItem, note);
		expenseEntriesList.add(newEntry);
		int row = expenseEntriesList.size();
		entriesListContainerPanel.setText(row, 0, vendor);
		entriesListContainerPanel.setText(row, 1, transItem);
		if (transAmount >= 0) {
			entriesListContainerPanel.getCellFormatter().addStyleName(row, 2, "negEntryAmount");
			entriesListContainerPanel.setText(row, 2, "-$" + nf20.format(transAmount));
		}
		else {
			entriesListContainerPanel.getCellFormatter().addStyleName(row, 2, "posEntryAmount");
			entriesListContainerPanel.setText(row, 2, "$" + nf20.format(-transAmount));
		}
		entriesListContainerPanel.setText(row, 3, budgetCategoryName);
		
		budgetCategory.updateBudgetSpent(transAmount);
	}
	
	/**
	 * Handler to show a popUp that contains a form for creating a new budget category
	 * @return
	 */
	private ClickHandler addCategorClickHandler() {
		ClickHandler handler = new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				addCategoryPanel();
			}
		};
		return handler;
	}
	
	/**
	 * Displays a popup panel that can create a new budget category
	 */
	private void addCategoryPanel() {
		final PopupPanel popupPanel = new PopupPanel(false);
		
		FormPanel newCategoryForm = new FormPanel();
		newCategoryForm.setStyleName("popupForm"); 
		
		String strObj = new String("");
		Double douObj = new Double(0);
		final ValidatedTextBoxPanel categoryNameTextBox = new ValidatedTextBoxPanel("Category name", "Category Name*", strObj);
		final ValidatedTextBoxPanel categoryAmountTextBox = new ValidatedTextBoxPanel("Budget", "Category budget amount*", douObj);
		
		VerticalPanel newCategoryFormContent = new VerticalPanel();
		newCategoryFormContent.add(new HTMLPanel("h2", "New Category"));
		newCategoryFormContent.add(categoryNameTextBox);
		newCategoryFormContent.add(categoryAmountTextBox);
		
		newCategoryFormContent.add(new Button("Done", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (categoryNameTextBox.getText().isEmpty() 
						|| categoryAmountTextBox.getText().isEmpty()) {
					Window.alert("Please fill in required fields (*)");
				}
				else if (!(categoryNameTextBox.errorIsVisible() || categoryAmountTextBox.errorIsVisible())) {
					double trueAmount = Double.parseDouble(categoryAmountTextBox.getText());
					addCategoryToList(categoryNameTextBox.getText(), trueAmount);
					popupPanel.setVisible(false);
				} 
				else {
					Window.alert("Please resolve the errors and try again");
				}
			}
		}));
		newCategoryFormContent.add(new Button("Cancel", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!(categoryNameTextBox.getText().isEmpty() && categoryAmountTextBox.getText().isEmpty())) {
					if (Window.confirm("Are you sure you want to exit?")) {
						popupPanel.setVisible(false);
					}
				} 
				else {
					popupPanel.setVisible(false);
				}
			}
		}));
		
		newCategoryForm.add(newCategoryFormContent);
		popupPanel.setWidget(newCategoryForm);
		
		popupPanel.addStyleName("customFormPopup");
		popupPanel.setGlassEnabled(true);
		popupPanel.show();
		popupPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback(){
	       public void setPosition(int offsetWidth, int offsetHeight) {
	          int left = (Window.getClientWidth() - offsetWidth) / 2;
	    	  int top = Window.getScrollTop() + 150;
	    	  popupPanel.setPopupPosition(left, top);
	       }
	    });
	}
	
	/**
	 * Displays a popup panel that can create a new budget expense/refund entry
	 */
	private void addEntryPanel(BudgetCategoryUI budgetCategoryUI) {
		final PopupPanel popupPanel = new PopupPanel(false);
		
		final BudgetCategoryUI finalbudgetCategoryUI = budgetCategoryUI;
		
		String strObj = new String("");
		Double douObj = new Double(0);
		final ValidatedTextBoxPanel vendorTextBox = new ValidatedTextBoxPanel("Vendor", "Vendor or company name*", strObj);
		final ValidatedTextBoxPanel itemTextBox = new ValidatedTextBoxPanel("Item", "Transaction item*", strObj);
		final ValidatedTextBoxPanel amountTextBox = new ValidatedTextBoxPanel("Amount", "Transaction amount*", douObj);
		final ValidatedTextBoxPanel noteTextBox = new ValidatedTextBoxPanel("Notes", "Additional notes", strObj);
		
		FormPanel entryForm = new FormPanel();
		entryForm.setStyleName("popupForm"); 
		
		// TODO put entryForm into another class 
		VerticalPanel entryFormContent = new VerticalPanel();
		entryFormContent.add(new HTMLPanel("h2", "New Expense/Refund Entry"));
		
		entryFormContent.add(vendorTextBox);
		entryFormContent.add(itemTextBox);
		entryFormContent.add(amountTextBox);
		entryFormContent.add(noteTextBox);
		
		HorizontalPanel radioButtonsHorizontalPanel = new HorizontalPanel();
		radioButtonsHorizontalPanel.addStyleName("expenseRefundRadioButtonsContainer");
		final RadioButton expenseRadioButton = new RadioButton("ExpOrRef", "Expense");
		expenseRadioButton.setValue(true);
		final RadioButton refundRadioButton = new RadioButton("ExpOrRef", "Refund");
		radioButtonsHorizontalPanel.add(expenseRadioButton);
		radioButtonsHorizontalPanel.add(refundRadioButton);
		
		entryFormContent.add(radioButtonsHorizontalPanel);
		entryFormContent.add(new Button("Done", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (vendorTextBox.getText().isEmpty() || itemTextBox.getText().isEmpty()
						|| amountTextBox.getText().isEmpty()) {
					Window.alert("Please fill in the required fields (*)");
				}
				else if (!(vendorTextBox.errorIsVisible() || itemTextBox.errorIsVisible() 
						|| amountTextBox.errorIsVisible() || noteTextBox.errorIsVisible())) {
					double trueAmount = Double.parseDouble(amountTextBox.getText());
					if (refundRadioButton.getValue()) {
						trueAmount *= -1;
					}
					addEntryToTable(new Date(), vendorTextBox.getText(), trueAmount, 
							finalbudgetCategoryUI, itemTextBox.getText(), noteTextBox.getText());
					popupPanel.setVisible(false);
				} 
				else {
					Window.alert("Please resolve the errors and try again");
				}
			}
		}));
		entryFormContent.add(new Button("Cancel", new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (!(vendorTextBox.getText().isEmpty() && itemTextBox.getText().isEmpty()
						&& amountTextBox.getText().isEmpty())) {
					if (Window.confirm("Are you sure you want to exit?")) {
						popupPanel.setVisible(false);
					}
				} 
				else {
					popupPanel.setVisible(false);
				}
			}
		}));
		
		entryForm.add(entryFormContent);
		popupPanel.setWidget(entryForm);
		popupPanel.addStyleName("customFormPopup");
		popupPanel.setGlassEnabled(true);
		popupPanel.setPopupPositionAndShow(new PopupPanel.PositionCallback(){
	       public void setPosition(int offsetWidth, int offsetHeight) {
	          int left = (Window.getClientWidth() - offsetWidth) / 2;
	    	  int top = Window.getScrollTop() + 100;
	    	  popupPanel.setPopupPosition(left, top);
	       }
	    });
	}
	
}
