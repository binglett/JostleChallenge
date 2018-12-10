package com.google.gwt.Jostle.client;

import java.util.logging.Logger;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Jostle implements EntryPoint {		
	/* Default dummy values*/
	private final String[] testValueStrings = {"Groceries", "Eating Out", "Clothing"};
	private final double[] testValueDoubles = {100.00, 300.00, 150.50};
	private final String videoUrl = "https://www.youtube.com/embed/DIZxqQl1QJc";
//	private final String videoUrl = "https://www.sample-videos.com/video123/mp4/720/big_buck_bunny_720p_1mb.mp4";
	private final String JSONVideoUrl = "https://jsonplaceholder.typicode.com/posts/1";
	private final String JSONArticleUrl = "https://jsonplaceholder.typicode.com/photos";
	
	private VerticalPanel videoPanelView = new VideoVerticalPanel(videoUrl, JSONVideoUrl);
	private VerticalPanel articlePanelView = new ArticlesVerticalPanel(JSONArticleUrl);
	private VerticalPanel customPanelView = new BudgetTrackerVerticalPanel(testValueStrings, testValueDoubles);
	private VerticalPanel activePanelView;
	private HorizontalPanel mainPanel = new HorizontalPanel();
	
	Logger logger = Logger.getLogger("NameOfYourLogger");
	
	/*
	 * Entry point method
	 */
	public void onModuleLoad() {
		mainPanel.addStyleName("mainPanel");
		videoPanelView.addStyleName("videoPanel");
		articlePanelView.addStyleName("articlePanel");
		customPanelView.addStyleName("customPanel");

		videoPanelView.setVisible(false);
		articlePanelView.setVisible(false);
		customPanelView.setVisible(false);
		activePanelView = videoPanelView;
		activePanelView.setVisible(true);
		
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mainPanel.add(videoPanelView);
		mainPanel.add(articlePanelView);
		mainPanel.add(customPanelView);
		
		RootPanel.get("mainContainer").add(mainPanel);
		
		Button videoPanelToggle = Button.wrap(Document.get().getElementById("viewToggle-1").getFirstChildElement());
		Button articlePanelToggle = Button.wrap(Document.get().getElementById("viewToggle-2").getFirstChildElement());
		Button customPanelToggle = Button.wrap(Document.get().getElementById("viewToggle-3").getFirstChildElement());
		
		videoPanelToggle.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		toggleView(videoPanelView);
	    	}
	    });
		articlePanelToggle.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		toggleView(articlePanelView);
	    	}
	    });
		customPanelToggle.addClickHandler(new ClickHandler() {
	    	public void onClick(ClickEvent event) {
	    		toggleView(customPanelView);
	    	}
	    });
	}

	/** 
	 * Sets selected view as the active panel
	 * 
	 * @param	selectedView	the view to toggle visible.  
	 * */
	private void toggleView(VerticalPanel selectedView) {
		if (!selectedView.isVisible()) {
			activePanelView.setVisible(false);
			activePanelView = selectedView;
			activePanelView.setVisible(true);
		}
	}
	


}
