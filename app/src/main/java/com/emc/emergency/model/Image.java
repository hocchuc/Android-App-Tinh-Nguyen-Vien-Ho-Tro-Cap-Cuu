package com.emc.emergency.model;



public class Image  {
	public Image() {
	}

	private Long id_image;


	private Accident id_AC;

	private String link;

	private void setId_image(Long value) {
		this.id_image = value;
	}

	public Long getId_image() {
		return id_image;
	}

	public Long getORMID() {
		return getId_image();
	}

	public void setLink(String value) {
		this.link = value;
	}

	public String getLink() {
		return link;
	}

	public void setId_AC(Accident value) {
		this.id_AC = value;
	}

	public Accident getId_AC() {
		return id_AC;
	}

	public String toString() {
		return String.valueOf(getId_image());
	}

}

