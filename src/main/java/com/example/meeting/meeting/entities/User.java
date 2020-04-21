package com.example.meeting.meeting.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.meeting.meeting.entities.entityUtil.UserTable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name = UserTable.TABLE)
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    @Column(name = UserTable.USERID, nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = UserTable.FIRST_NAME, nullable = false)
    private String firstName;
    @Column(name = UserTable.LAST_NAME, nullable = false)
    private String lastName;
    @Column(name = UserTable.ACTIVE, nullable = false)
    private boolean isActive;
    @Column(name = UserTable.DELETED, nullable = false)
    private boolean isDeleted;
    @Column(name = UserTable.COUNTRY, nullable = false)
    private String country;
    @Column(name = UserTable.ZIP)
    private int zip;
    @Column(name = UserTable.MOBILE, nullable = false)
    private String mobile;
    @Column(name = UserTable.EMAIL, nullable = false, unique = true)
    private String email;
    @Column(name = UserTable.BUSY, nullable = false)
    private boolean isBusy;
    @Column(name = UserTable.LATITUDE)
    private double latitude;
    @Column(name = UserTable.LONGITUDE)
    private double longitude;
    @Column(name = UserTable.CREATED)
    private long createdAt;
    @Column(name = UserTable.UPDATED)
    private long updatedAt;

    public long getId() {
        return id;
    }

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public boolean isBusy() {
        return isBusy;
    }

    public void setBusy(boolean busy) {
        isBusy = busy;
    }
    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public long getCreatedAt() {

        return createdAt;
    }

    public void setCreatedAt(long createdAt) {

        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {

        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {

        this.updatedAt = updatedAt;
    }

    public String getMobile() {

        return mobile;
    }

    public void setMobile(String mobile) {

        this.mobile = mobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {

        this.country = country;
    }

    public int getZip() {

        return zip;
    }

    public void setZip(int zip) {

        this.zip = zip;
    }

    public double getLatitude() {

        return latitude;
    }

    public void setLatitude(double latitude) {

        this.latitude = latitude;
    }

    public double getLongitude() {

        return longitude;
    }

    public void setLongitude(double longitude) {

        this.longitude = longitude;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public boolean isDeleted() {
        return this.isDeleted;
    }

    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }
}

