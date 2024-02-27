package com.boyapcky.bookworld.model;

import com.boyapcky.bookworld.entity.MarkEntity;
import com.boyapcky.bookworld.entity.Road;

import java.util.Date;

public class Mark {
    private long id;
    private double longitude;
    private double latitude;
    private double accuracy;
    private Date timeAdded;
    private Road road;

    public Mark(long id, double longitude, double latitude, double accuracy, Date timeAdded, Road road) {
        this.id = id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.accuracy = accuracy;
        this.timeAdded = timeAdded;
        this.road = road;
    }

    public static Mark toModel(MarkEntity markEntity) {
        return new Mark(
                markEntity.getId(),
                markEntity.getLongitude(),
                markEntity.getLatitude(),
                markEntity.getAccuracy(),
                markEntity.getTimeAdded(),
                markEntity.getRoad()
        );
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public Date getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(Date timeAdded) {
        this.timeAdded = timeAdded;
    }

    public Road getRoad() {
        return road;
    }

    public void setRoad(Road road) {
        this.road = road;
    }
}
