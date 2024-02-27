package com.boyapcky.bookworld.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;

public class MarkRequest {
    private double longitude;
    private double latitude;
    private double accuracy;
    private Long time;
    private String imageFile;

    public MarkRequest(double longitude, double latitude, double accuracy, Long time, String imageFile) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.accuracy = accuracy;
        this.time = time;
        this.imageFile = imageFile;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}
