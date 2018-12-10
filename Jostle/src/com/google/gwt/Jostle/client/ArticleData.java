package com.google.gwt.Jostle.client;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * Overlay class for http://jsonplaceholder.typicode.com/photos
 * 
 * @author Bonnie
 *
 */
class ArticleData extends JavaScriptObject {
	protected ArticleData() {}
	
	// JSNI Getters
	public final native int getAlbumId() /*-{ return this.albumId; }-*/;
	public final native int getId() /*-{ return this.id; }-*/;
	public final native String getTitle() /*-{ return this.title; }-*/;
	public final native String getUrl() /*-{ return this.url; }-*/;
	public final native String getThumbnailUrl() /*-{ return this.thumbnailUrl; }-*/;

}