package com.skillmentor.models;

import org.json.JSONException;
import org.json.JSONObject;

public class Advert {

	private String id;
	private String title;
	private String description;
	private String location;
	private String reward;
	private String phone;
	private String email;
	private User user;
	
	public void map(JSONObject jsn) throws JSONException {
		this.title = jsn.getString("title");
		this.description = jsn.getString("description");
		this.location = jsn.getString("location");
		this.id = jsn.getString("_id");
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getReward() {
		return reward;
	}
	public void setReward(String reward) {
		this.reward = reward;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
