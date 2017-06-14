package com.emc.emergency.model;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Accident implements Parcelable {
	private Long id_AC;
	private Long id_user;
	private String description_AC;
	private String date_AC;
	private Float long_AC;
	private Float lat_AC;
	private String status_AC;
	private List<Chat> chat = new ArrayList<>();
	private List<Image> image = new ArrayList<>();
    private String address;

    public Accident() {
    }

    protected Accident(Parcel in ) {
        this.address = address;
        String[] data = new String[5];

        in.readStringArray(data);

        this.description_AC = data[0];
        this.date_AC = data[1];
        this.status_AC = data[2];
        this.long_AC=Float.valueOf(data[3]);
        this.lat_AC=Float.valueOf(data[4]);
    }


    public static final Creator<Accident> CREATOR = new Creator<Accident>() {
        @Override
        public Accident createFromParcel(Parcel in) {
            return new Accident(in);
        }

        @Override
        public Accident[] newArray(int size) {
            return new Accident[size];
        }
    };

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

    public Float getLong_AC() {
        return long_AC;
    }

    public void setLong_AC(Float long_AC) {
        this.long_AC = long_AC;
    }

    public Float getLat_AC() {
        return lat_AC;
    }

    public void setLat_AC(Float lat_AC) {
        this.lat_AC = lat_AC;
    }

    public String getStatus_AC() {
        return status_AC;
    }

    public void setStatus_AC(String status_AC) {
        this.status_AC = status_AC;
    }

    public List<Chat> getChat() {
        return chat;
    }

    public void setChat(List<Chat> chat) {
        this.chat = chat;
    }

    public List<Image> getImage() {
        return image;
    }

    public void setImage(List<Image> image) {
        this.image = image;
    }

    public Accident(Long id_AC, Long id_user, String description_AC, String date_AC, Float long_AC, Float lat_AC, String status_AC, List<Chat> chat, List<Image> image, String address) {
        this.id_AC = id_AC;
        this.id_user = id_user;
        this.description_AC = description_AC;
        this.date_AC = date_AC;
        this.long_AC = long_AC;
        this.lat_AC = lat_AC;
        this.status_AC = status_AC;
        this.chat = chat;
        this.image = image;
        this.address = address;
    }

    public Accident(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Accident{" +
                "id_AC=" + id_AC +
                ", description_AC='" + description_AC + '\'' +
                ", date_AC='" + date_AC + '\'' +
                ", long_AC=" + long_AC +
                ", lat_AC=" + lat_AC +
                ", status_AC='" + status_AC + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    public Accident(Long id_AC, String description_AC, String date_AC, Float long_AC, Float lat_AC, String status_AC) {
        this.id_AC = id_AC;
        this.description_AC = description_AC;
        this.date_AC = date_AC;
        this.long_AC = long_AC;
        this.lat_AC = lat_AC;
        this.status_AC = status_AC;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(description_AC);
        dest.writeString(date_AC);
        dest.writeString(status_AC);
        dest.writeFloat(long_AC);
        dest.writeFloat(lat_AC);
        dest.writeString(address);

    }
}
