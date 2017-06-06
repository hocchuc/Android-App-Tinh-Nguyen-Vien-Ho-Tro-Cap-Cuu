
package com.emc.emergency.model;


public class Medical_Info  {
	public Medical_Info() {
	}

	private Long id_MI;

	private Personal_Infomation id_PI;


	private String name_MI;

	private Integer type_MI;

	private String description;

	private void setId_MI(Long value) {
		this.id_MI = value;
	}


	public Long getId_MI() {
		return id_MI;
	}

	public void setName_MI(String value) {
		this.name_MI = value;
	}

	public String getName_MI() {
		return name_MI;
	}

	public void setType_MI(int value) {
		setType_MI(new Integer(value));
	}

	public void setType_MI(Integer value) {
		this.type_MI = value;
	}

	public Integer getType_MI() {
		return type_MI;
	}

	public void setDescription(String value) {
		this.description = value;
	}

	public String getDescription() {
		return description;
	}

	public void setId_PI(Personal_Infomation value) {
		this.id_PI = value;
	}

	public Personal_Infomation getId_PI() {
		return id_PI;
	}

	@Override
	public String toString() {
		return "Medical_Info{" +
				"id_MI=" + id_MI +
				", id_PI=" + id_PI +
				", name_MI='" + name_MI + '\'' +
				", type_MI=" + type_MI +
				", description='" + description + '\'' +
				'}';
	}
}

