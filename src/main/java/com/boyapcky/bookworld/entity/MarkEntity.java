package com.boyapcky.bookworld.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "marks")
public class MarkEntity extends BaseEntity {
    @Column(name = "longitude")
    private double longitude;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "accuracy")
    private double accuracy;

    @Column(name = "time_added")
    private Date timeAdded;

    @Enumerated(EnumType.STRING)
    @Column(name = "road_type")
    private Road road;
}
