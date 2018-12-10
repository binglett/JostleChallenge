package com.google.gwt.Jostle.client;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Provides a labeled and real-time validated textbox input UI component
 * Validation depends on the class that's indicated via a dummy object passed
 * to the constructor.
 * There's probably a much better way to do this
 * 
 * @author Bonnie
 */
public class ValidatedTextBoxPanel extends HTMLPanel {
	/**
	 * Validator should toggle the visibility of the error
	 * based on the input and whether a blank is allowed or not
	 */
	private interface Validator {
		void validate(HTMLPanel errorPanel, String input, boolean allowBlank);
	}
	private static final Map<Class, Validator> dispatch = new HashMap<Class, Validator>();
	
	final HTMLPanel titleHtmlPanel;
	final HTMLPanel errorHTMLPanel = new HTMLPanel("p", "");
	final TextBox inputTextBox = new TextBox();
	
	/**
	 * The instance is a labeled textbox. If the type desired for the textbox
	 * does not have a validator defined in this class there will be no validation done
	 * on the textbox
	 * @param labelText the label for the texbox
	 * @param placeholderText to be displayed as placeholder text in the textbox
	 * @param typeIndicator is so that the appropriate validator can be attached to 
	 * 			this textbox based on its type
	 */
	public ValidatedTextBoxPanel(String labelText, String placeholderText, Object typeIndicator) {
		super("");
		final Object typeObj = typeIndicator;
		
		this.addStyleName("validatedTextBoxPanel");
		
		titleHtmlPanel = new HTMLPanel("h4", labelText);
		
		errorHTMLPanel.addStyleName("formWarning");
		errorHTMLPanel.setVisible(false);
		
		inputTextBox.getElement().setPropertyString("placeholder", placeholderText);
		inputTextBox.setMaxLength(CONSTANT.MAXNAMELENGTH);

		inputTextBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				Validator validator = dispatch.get(typeObj.getClass());
				if (null == validator) {
					// Type not recognized, no validation happens
				} 
				else {
					validator.validate(errorHTMLPanel, inputTextBox.getText(), false);
				}
			}
		});
		
		this.add(titleHtmlPanel);
		this.add(inputTextBox);
		this.add(errorHTMLPanel);
	}
	
	public String getText() {
		return this.inputTextBox.getText();
	}
	
	public boolean errorIsVisible() {
		return this.errorHTMLPanel.isVisible();
	}
	
	/*
	 * Defines class-based validators 
	 * Add new ones here
	 * */
	static {
		dispatch.put(String.class, new Validator() {
			
			@Override
			public void validate(HTMLPanel errorPanel, String input, boolean allowBlank) {
				if (!allowBlank && input.isEmpty()) {
					errorPanel.getElement().setInnerHTML("Please fill in this field");
					errorPanel.setVisible(true);
				} else if (allowBlank && input.isEmpty()) {
					errorPanel.setVisible(false);
				}
				else if (!input.matches("^[a-zA-Z0-9\\.,]+[a-zA-Z0-9\\.,(\\s)*]*$")) {
					errorPanel.getElement().setInnerHTML("Please only use alphanumeric characters, commas and periods");
					errorPanel.setVisible(true);
				} else {
					errorPanel.setVisible(false);
				}
				
			}
		});
		dispatch.put(Double.class, new Validator() {
			
			@Override
			public void validate(HTMLPanel errorPanel, String input, boolean allowBlank) {
				if (input.isEmpty()) {
					errorPanel.getElement().setInnerHTML("Value cannot be empty");
					errorPanel.setVisible(true);
				}
				else if (!(input.matches("^\\s*\\d+\\s*$")
					|| input.matches("^\\s*\\d+\\.\\d?\\d?\\s*$")
					|| input.matches("^\\.\\d\\d?"))) { 
					errorPanel.getElement().setInnerHTML("Value must be xx.xx");
					errorPanel.setVisible(true);
				}
				else if (Double.parseDouble(input) > CONSTANT.MAXBUDGET
							|| Double.parseDouble(input) < CONSTANT.MINBUDGET) {
					errorPanel.getElement().setInnerHTML("Value must be between " 
							+ CONSTANT.DOUBLEZERO + " and " + CONSTANT.MAXBUDGET);
					errorPanel.setVisible(true);
				} else {
					errorPanel.setVisible(false);
				}
				
			}
		});
	}
	
}
