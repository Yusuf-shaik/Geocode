package com.example.geocode;

//class to handle an object that is pulled from the database
public class GeoRecord {
    private int id;
    private String address;
    private String latitude;
    private String longitude;



    //constructor
    public GeoRecord(int id, String address, String latitude, String longitude) {
        this.id=id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
