package com.springsource.greenhouse.events;

public class EventFavorite {
	
	private final int favoritedCount;
	
	private final EventSession favorite;
	
	public EventFavorite(EventSession favorite, int favoritedCount) {
		this.favorite = favorite;
		this.favoritedCount = favoritedCount;		
	}

	public EventSession getFavorite() {
		return favorite;
	}
	
	public int getFavoritedCount() {
		return favoritedCount;
	}

}
