package com.google.gwt.Jostle.client;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Provides a UI component that contains a list of articles 
 * created using the data in a specific JSON format that is passed to
 * the constructor via a URL when instantiated.
 * via a URL.
 * 
 * @author Bonnie
 */
public class ArticlesVerticalPanel extends VerticalPanel {
	private VerticalPanel articleListContainerPanel = new VerticalPanel();
	
	public ArticlesVerticalPanel(String JSONArticlesDataUrl) {
		super();
		getArticleList(JSONArticlesDataUrl); 
		this.articleListContainerPanel.addStyleName("articleListContainer");
		this.add(this.articleListContainerPanel);
	}
	
	/**
	 * Fetches raw JSON data in a specific form from a URL
	 * and then calls attachArticles with parsed data.
	 * 
	 * @param	JSONArticlesDataUrl	the URL to fetch data from
	 */
	private void getArticleList(String JSONArticlesDataUrl) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, JSONArticlesDataUrl);
		
		try { 
			builder.sendRequest(null, new RequestCallback() { 
				public void onError(Request request, Throwable exception) { displayError("Couldn¡¦t retrieve JSON"); }

			public void onResponseReceived(Request request, Response response) {
			    if (200 == response.getStatusCode()) {
			    	attachArticles(JsonUtils.<JsArray<ArticleData>>safeEval(response.getText()));
			    } 
			    else {
			    	displayError("Couldn't retrieve JSON articles (" + response.getStatusText() + ")");
			    }
			}
		}); } 
		catch (RequestException e) { 
			displayError("Couldn¡¦t retrieve JSON articles"); 
		} 
	}
	
	/**
	 *  Propagates this.articleListContainerPanel with articles
	 *  
	 *  @param	articles the list of articles
	 */
	public void attachArticles(JsArray<ArticleData> articles) {
		for (int i = 0; i < articles.length(); i++) {
			final FlowPanel articleContainer = new FlowPanel();
			articleContainer.addStyleName("articleContainer");
			
			final HTMLPanel dummyArticleDescription = new HTMLPanel("p", "Lorem ipsum dolor sit amet, "
					+ "consectetuer adipiscing elit. Aenean commodo ligula eget dolor. "
					+ "Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, "
					+ "nascetur ridiculus mus. Donec quam felis,");
			dummyArticleDescription.addStyleName("articleDescription");
			
			final int currentIndex = i;
			final String currentTitle = articles.get(i).getTitle();
			
			Image articleThumbnail = new Image();
			articleThumbnail.setUrl(articles.get(i).getThumbnailUrl());
			articleThumbnail.addStyleName("articleImage");
			articleThumbnail.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick( ClickEvent event) {
					HTMLPanel title = new HTMLPanel("p", currentTitle);
					title.addStyleName("articleTitle");
					HTMLPanel indexIndicator = new HTMLPanel("p", " Track Number " + currentIndex);
					HTMLPanel content = new HTMLPanel("");
					content.addStyleName("popupPanel");
					content.add(indexIndicator);
					content.add(title);
					
					final PopupPanel popup = new PopupPanel(true);
					popup.setStyleName("jostlePopup");
					popup.setGlassEnabled(true);
					popup.setWidget(content);
					popup.setPopupPositionAndShow(new PopupPanel.PositionCallback(){
		               public void setPosition(int offsetWidth, int offsetHeight) {
		                  int left = (Window.getClientWidth() - offsetWidth) / 2;
		            	  int top = Window.getScrollTop() + 300;
		                  popup.setPopupPosition(left, top);
		               }
		            });
				}
			});
			
			articleContainer.add(articleThumbnail);
			articleContainer.add(dummyArticleDescription);
			
			articleListContainerPanel.add(articleContainer);
		}
	}
	
	/* 
	 * Display error messages
	 */
	private void displayError(String error) {
	    Window.alert("Error: " + error);	    
	}
}
