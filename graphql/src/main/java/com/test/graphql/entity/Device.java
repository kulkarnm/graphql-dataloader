package com.test.graphql.entity;

import org.springframework.data.annotation.Id;

public class Device {

    @Id
    private Long id;
    private String deviceNumber;
    private String expiryDate;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getDeviceNumber() {
        return deviceNumber;
    }
    public void setDeviceNumber(String deviceNumber) {
        this.deviceNumber = deviceNumber;
    }
    public String getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
} 
