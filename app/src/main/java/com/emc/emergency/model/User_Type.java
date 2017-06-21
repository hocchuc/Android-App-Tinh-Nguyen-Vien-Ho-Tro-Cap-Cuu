
package com.emc.emergency.model;


import java.util.List;

public class User_Type   {
	public User_Type() {
	}

	public User_Type(Long id_user_type, String name_user_type) {
		this.id_user_type = id_user_type;
		this.name_user_type = name_user_type;
	}


	private Long id_user_type;

	private String name_user_type;

	private List<User> users ;//= new ArrayList<>();

	public Long getId_user_type() {
		return id_user_type;
	}

	public void setId_user_type(Long id_user_type) {
		this.id_user_type = id_user_type;
	}

	public String getName_user_type() {
		return name_user_type;
	}

	public void setName_user_type(String name_user_type) {
		this.name_user_type = name_user_type;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	@Override
	public String toString() {
		return "User_Type{" +
				"id_user_type=" + id_user_type +
				", name_user_type='" + name_user_type + '\'' +
				'}';
	}
}
