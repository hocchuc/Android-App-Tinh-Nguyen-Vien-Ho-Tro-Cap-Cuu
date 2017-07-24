package com.emc.emergency.Helper.Model;

public class Accident {
	private Long id_AC;
	private Long id_user;
	private String description_AC;
	private String date_AC;
	private Double long_AC;
	private Double lat_AC;
	private String status_AC;
    private String address;
    private String firebaseKey;
    private Long id_admin_active;
    private Boolean request_AC;

    public Accident() {
    }

    public Boolean getRequest_AC() {
        return request_AC;
    }

    public void setRequest_AC(Boolean request_AC) {
        this.request_AC = request_AC;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getId_AC() {
        return id_AC;
    }

    public void setId_AC(Long id_AC) {
        this.id_AC = id_AC;
    }

    public Long getId_user() {
        return id_user;
    }

    public void setId_user(Long id_user) {
        this.id_user = id_user;
    }

    public String getDescription_AC() {
        return description_AC;
    }

    public void setDescription_AC(String description_AC) {
        this.description_AC = description_AC;
    }

    public String getDate_AC() {
        return date_AC;
    }

    public void setDate_AC(String date_AC) {
        this.date_AC = date_AC;
    }

    public Double getLong_AC() {
        return long_AC;
    }

    public void setLong_AC(Double long_AC) {
        this.long_AC = long_AC;
    }

    public Double getLat_AC() {
        return lat_AC;
    }

    public void setLat_AC(Double lat_AC) {
        this.lat_AC = lat_AC;
    }

    public String getStatus_AC() {
        return status_AC;
    }

    public void setStatus_AC(String status_AC) {
        this.status_AC = status_AC;
    }

    public String getFirebaseKey() {
        return firebaseKey;
    }

    public void setFirebaseKey(String firebaseKey) {
        this.firebaseKey = firebaseKey;
    }

    public Long getId_admin_active() {
        return id_admin_active;
    }

    public void setId_admin_active(Long id_admin_active) {
        this.id_admin_active = id_admin_active;
    }

    public Accident(Long id_AC, Long id_user, String description_AC, String date_AC, Double long_AC, Double lat_AC, String status_AC, String address, String firebaseKey, Long id_admin_active, Boolean request_AC) {
        this.id_AC = id_AC;
        this.id_user = id_user;
        this.description_AC = description_AC;
        this.date_AC = date_AC;
        this.long_AC = long_AC;
        this.lat_AC = lat_AC;
        this.status_AC = status_AC;
        this.address = address;
        this.firebaseKey = firebaseKey;
        this.id_admin_active = id_admin_active;
        this.request_AC = request_AC;
    }

    @Override
    public String toString() {
        return "Accident{" +
                "id_AC=" + id_AC +
                ", id_user=" + id_user +
                ", description_AC='" + description_AC + '\'' +
                ", date_AC='" + date_AC + '\'' +
                ", long_AC=" + long_AC +
                ", lat_AC=" + lat_AC +
                ", status_AC='" + status_AC + '\'' +
                ", address='" + address + '\'' +
                ", firebaseKey='" + firebaseKey + '\'' +
                ", id_admin_active=" + id_admin_active +
                ", request_AC=" + request_AC +
                '}';
    }

    public Accident(Long id_AC, String description_AC, String date_AC, Double long_AC, Double lat_AC, String status_AC) {
        this.id_AC = id_AC;
        this.description_AC = description_AC;
        this.date_AC = date_AC;
        this.long_AC = long_AC;
        this.lat_AC = lat_AC;
        this.status_AC = status_AC;
    }
}
