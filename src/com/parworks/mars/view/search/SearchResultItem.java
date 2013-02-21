package com.parworks.mars.view.search;

public class SearchResultItem {
	
	public String siteId;	  	
	public String posterImageUrl;
  	
	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getPosterImageUrl() {
		return posterImageUrl;
	}

	public void setPosterImageUrl(String posterImageUrl) {
		this.posterImageUrl = posterImageUrl;
	}

	public SearchResultItem(String siteId, String posterImageUrl) {	  	
	    this.siteId = siteId;
	    this.posterImageUrl = posterImageUrl;
	} 	
}
