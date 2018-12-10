package com.google.gwt.Jostle.client;

import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Provides a UI component that contains a video and its information
 * created using the data in a specific JSON format that is passed
 * to the constructor via a URL with instantiated
 * 
 * @author Bonnie
 */
public class VideoVerticalPanel extends VerticalPanel {
	private Frame videoFrame;
	private FlowPanel videoInfoContainerPanel = new FlowPanel();
	
	public VideoVerticalPanel(String videoUrl, String JSONVideoDataUrl) {
		super();
		this.videoFrame = new Frame(videoUrl);
		this.videoInfoContainerPanel.addStyleName("videoInfoContainer");
		requestVideoInfo(JSONVideoDataUrl);
		this.add(videoInfoContainerPanel);
	}
	
	/**
	 * Fetches raw JSON data in a specific form from a URL
	 * and then calls attachVideoInfo with parsed data.
	 * 
	 * @param	JSONVideoDataUrl the URL to fetch data from
	 */
	private void requestVideoInfo(String JSONVideoDataUrl) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, JSONVideoDataUrl);
		
		try { 
			builder.sendRequest(null, new RequestCallback() { 
				public void onError(Request request, Throwable exception) { displayError("Couldn¡¦t retrieve JSON"); }

			public void onResponseReceived(Request request, Response response) {
			    if (200 == response.getStatusCode()) {
			    	attachVideoInfo(JsonUtils.<VideoInfoData>safeEval(response.getText()));
			    } 
			    else {
			    	displayError("Couldn't retrieve JSON video info (" + response.getStatusText() + ")");
			    }
			}
		}); } 
		catch (RequestException e) { 
			displayError("Couldn¡¦t retrieve JSON video info"); 
		} 
	}
	
	/**
	 * Propagates this.videoInfoContainerPanel with video data
	 * 
	 * @param videoInfos  
	 */
	public void attachVideoInfo(VideoInfoData videoInfos) {
		HTMLPanel videoTitle = new HTMLPanel("h1", videoInfos.getTitle());
		
		HTMLPanel videoDescription = new HTMLPanel("p", videoInfos.getBody());
		
		Image videoPlaceholder = new Image("http://placehold.it/16x9");
		videoPlaceholder.setWidth("100%");		
		HTMLPanel videoHolder = new HTMLPanel("");
		videoHolder.addStyleName("videoHolder");
		videoHolder.add(videoPlaceholder);
		videoHolder.add(videoFrame);
		
		this.videoInfoContainerPanel.add(videoTitle);
		this.videoInfoContainerPanel.add(videoDescription);
		this.videoInfoContainerPanel.add(videoHolder);
	}
	
	/*
	 *  Display error messages
	 */
	private void displayError(String error) {
	    Window.alert("Error: " + error);	    
	}
}
