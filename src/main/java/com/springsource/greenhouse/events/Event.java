package com.springsource.greenhouse.events;

import java.util.Date;

import com.springsource.greenhouse.utils.ResourceReference;

public class Event {

	private Long id;

	private String title;

	private Date startDate;

	private Date endDate;

	private String location;

	private String description;
	
	private ResourceReference<String> group;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocation() {
		return location;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ResourceReference<String> getGroup() {
		return group;
	}

	public void setGroup(ResourceReference<String> group) {
		this.group = group;
	}

}