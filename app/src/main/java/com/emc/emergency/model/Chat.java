package com.emc.emergency.model;

import java.util.Date;


public class Chat {

	//TODO cau truc lai Chat object
	private boolean self;

	public Chat() {
	}

	public Chat(Long id_chat, Accident id_Ac, User id_user, String comment, Date date_chat, boolean self) {
		this.id_chat = id_chat;
		this.id_AC = id_Ac;
		this.id_user = id_user;
		this.comment = comment;
		this.date_chat = date_chat;
		this.self = self;
	}

	private Long id_chat;


	private Accident id_AC;



	private User id_user;

	private String comment;

	private Date date_chat;

	private void setId_chat(Long value) {
		this.id_chat = value;
	}

	public Long getId_chat() {
		return id_chat;
	}


	public void setComment(String value) {
		this.comment = value;
	}

	public String getComment() {
		return comment;
	}

	public void setDate_chat(Date value) {
		this.date_chat = value;
	}

	public Date getDate_chat() {
		return date_chat;
	}

	public void setId_AC( Accident value) {
		this.id_AC = value;
	}

	public Accident getId_AC() {
		return id_AC;
	}

	public void setId_user(User value) {
		this.id_user = value;
	}

	public User getId_user() {
		return id_user;
	}

	public String toString() {
		return String.valueOf(getId_chat());
	}

	public boolean isSelf() {
		return self;
	}

	public void setSelf(boolean self) {
		this.self = self;
	}
}

