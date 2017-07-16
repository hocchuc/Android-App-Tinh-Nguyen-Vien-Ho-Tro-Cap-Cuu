
package com.emc.emergency.model;


public class Medical_Information {
	public Medical_Information() {
	}
	private Long id_MI;
	private Personal_Infomation id_PI;
	private String name_MI;
	private Integer type_MI;
	private String descriptionMI;

	public Long getId_MI() {
		return id_MI;
	}

	public void setId_MI(Long id_MI) {
		this.id_MI = id_MI;
	}

	public Personal_Infomation getId_PI() {
		return id_PI;
	}

	public void setId_PI(Personal_Infomation id_PI) {
		this.id_PI = id_PI;
	}

	public String getName_MI() {
		return name_MI;
	}

	public void setName_MI(String name_MI) {
		this.name_MI = name_MI;
	}

	public Integer getType_MI() {
		return type_MI;
	}

	public void setType_MI(Integer type_MI) {
		this.type_MI = type_MI;
	}

	public String getDescriptionMI() {
		return descriptionMI;
	}

	public void setDescriptionMI(String descriptionMI) {
		this.descriptionMI = descriptionMI;
	}

	public Medical_Information(String name_MI, Integer type_MI, String descriptionMI) {
		this.name_MI = name_MI;
		this.type_MI = type_MI;
		this.descriptionMI = descriptionMI;
	}

	@Override
	public String toString() {
		return "Medical_Info{" +
				"id_MI=" + id_MI +
				", name_MI='" + name_MI + '\'' +
				", type_MI=" + type_MI +
				", descriptionMI='" + descriptionMI + '\'' +
				'}';
	}
}

