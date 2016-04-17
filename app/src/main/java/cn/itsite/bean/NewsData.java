package cn.itsite.bean;

import java.io.Serializable;

public class NewsData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3518249749127487188L;
	public String title;
	public String link;
	public String pubDate;
	public String description;

	@Override
	public String toString() {
		return "NewsData [title=" + title + ", link=" + link + ", pubDate=" + pubDate + ", description=" + description + "]";
	}

}