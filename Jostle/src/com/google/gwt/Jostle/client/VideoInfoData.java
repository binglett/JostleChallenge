package com.google.gwt.Jostle.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Overlay class for http://jsonplaceholder.typicode.com/posts/1
 * 
 * @author Bonnie
 *
 */
class VideoInfoData extends JavaScriptObject {
	protected VideoInfoData() {}
	
	// JSNI Getters
	public final native int getUserId() /*-{ return this.userId; }-*/;
	public final native int getId() /*-{ return this.id; }-*/;
	public final native String getTitle() /*-{ return this.title; }-*/;
	public final native String getBody() /*-{ return this.body; }-*/;
}
