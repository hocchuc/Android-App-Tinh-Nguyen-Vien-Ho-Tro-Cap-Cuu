package com.emc.emergency.Helper.Model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Model cho quản lý thông tin cá nhân
 *
 */
public class Personal_Infomation implements Serializable  {
	private Long id_PI;
	private String name_PI;
	private Boolean sex__PI;
	private String birthday;
	private String personal_id;
	private String work_location;
	private String phone_PI;
	private String address_PI;
	private String email_PI;
	private User id_user;
	private String avatar;
	private ArrayList<Medical_Information> medical_Information;

	public Long getId_PI() {
		return id_PI;
	}

	public void setId_PI(Long id_PI) {
		this.id_PI = id_PI;
	}

	public String getName_PI() {
		return name_PI;
	}

	public void setName_PI(String name_PI) {
		this.name_PI = name_PI;
	}

	public Boolean getSex__PI() {
		return sex__PI;
	}

	public void setSex__PI(Boolean sex__PI) {
		this.sex__PI = sex__PI;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getPersonal_id() {
		return personal_id;
	}

	public void setPersonal_id(String personal_id) {
		this.personal_id = personal_id;
	}

	public String getWork_location() {
		return work_location;
	}

	public void setWork_location(String work_location) {
		this.work_location = work_location;
	}

	public String getPhone_PI() {
		return phone_PI;
	}

	public void setPhone_PI(String phone_PI) {
		this.phone_PI = phone_PI;
	}

	public String getAddress_PI() {
		return address_PI;
	}

	public void setAddress_PI(String address_PI) {
		this.address_PI = address_PI;
	}

	public String getEmail_PI() {
		return email_PI;
	}

	public void setEmail_PI(String email_PI) {
		this.email_PI = email_PI;
	}

	public User getId_user() {
		return id_user;
	}

	public void setId_user(User id_user) {
		this.id_user = id_user;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public ArrayList<Medical_Information> getMedical_Information() {
		return medical_Information;
	}

	public void setMedical_Information(ArrayList<Medical_Information> medical_Information) {
		this.medical_Information = medical_Information;
	}

	public Personal_Infomation() {
	}

	public Personal_Infomation(Long id_PI, String name_PI, Boolean sex__PI, String birthday, String personal_id, String work_location, String phone_PI, String address_PI, String email_PI, User id_user, String avatar) {
		this.id_PI = id_PI;
		this.name_PI = name_PI;
		this.sex__PI = sex__PI;
		this.birthday = birthday;
		this.personal_id = personal_id;
		this.work_location = work_location;
		this.phone_PI = phone_PI;
		this.address_PI = address_PI;
		this.email_PI = email_PI;
		this.id_user = id_user;
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "Personal_Infomation{" +
				"id_PI=" + id_PI +
				", name_PI='" + name_PI + '\'' +
				", sex__PI=" + sex__PI +
				", birthday='" + birthday + '\'' +
				", personal_id='" + personal_id + '\'' +
				", work_location='" + work_location + '\'' +
				", phone_PI='" + phone_PI + '\'' +
				", address_PI='" + address_PI + '\'' +
				", email_PI='" + email_PI + '\'' +
				", id_user=" + id_user +
				", avatar='" + avatar + '\'' +
				'}';
	}
}
