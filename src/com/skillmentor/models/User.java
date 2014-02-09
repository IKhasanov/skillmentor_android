package com.skillmentor.models;

import com.skillmentor.utils.net.ApiSession;

import android.graphics.drawable.Drawable;

public class User {

	private long id;
	private String name;
	private String email;
	private Drawable avatar;
	private String avatarUrl;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Drawable getAvatar() {
		if (avatar == null) {
			avatar = ApiSession.loadImageFromAPI(avatarUrl);
		}
		
		return avatar;
	}
	public void setAvatar(Drawable avatar) {
		this.avatar = avatar;
	}
	public String getAvatarUrl() {
		return avatarUrl;
	}
	public void setAvatarUrl(String avatarUrl) {
		this.avatarUrl = avatarUrl;
	}
	
}
