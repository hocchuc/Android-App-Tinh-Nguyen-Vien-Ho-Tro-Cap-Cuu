package com.emc.emergency.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Personal_Infomation implements Serializable  {
	private Long id_PI;
	private String name_PI;
	private Boolean sex__PI;
	private Date birthday;
	private Long personal_id;
	private String work_location;
	private Float long_PI;
	private Float lat_PI;
	private String phone_PI;
	private String address_PI;
	private String email_PI;
	private User id_user;
	private ArrayList<Medical_Info> medical_Info;

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

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Long getPersonal_id() {
		return personal_id;
	}

	public void setPersonal_id(Long personal_id) {
		this.personal_id = personal_id;
	}

	public String getWork_location() {
		return work_location;
	}

	public void setWork_location(String work_location) {
		this.work_location = work_location;
	}

	public Float getLong_PI() {
		return long_PI;
	}

	public void setLong_PI(Float long_PI) {
		this.long_PI = long_PI;
	}

	public Float getLat_PI() {
		return lat_PI;
	}

	public void setLat_PI(Float lat_PI) {
		this.lat_PI = lat_PI;
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

	public ArrayList<Medical_Info> getMedical_Info() {
		return medical_Info;
	}

	public void setMedical_Info(ArrayList<Medical_Info> medical_Info) {
		this.medical_Info = medical_Info;
	}

	public Personal_Infomation() {
	}

	public Personal_Infomation(Long id_PI, String name_PI, Boolean sex__PI, Date birthday, Long personal_id, String work_location, Float long_PI, Float lat_PI, String phone_PI, String address_PI, String email_PI, User id_user, ArrayList<Medical_Info> medical_Info) {
		this.id_PI = id_PI;
		this.name_PI = name_PI;
		this.sex__PI = sex__PI;
		this.birthday = birthday;
		this.personal_id = personal_id;
		this.work_location = work_location;
		this.long_PI = long_PI;
		this.lat_PI = lat_PI;
		this.phone_PI = phone_PI;
		this.address_PI = address_PI;
		this.email_PI = email_PI;
		this.id_user = id_user;
		this.medical_Info = medical_Info;
	}
}
