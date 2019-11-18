package com.test.graphql.entity;

import org.springframework.data.annotation.Id;

public class Account {

    @Id
    private Long id;
    private String accountNumber;
    private int availableFunds;
    private Long customerId;
    private Long deviceId;

    public Long getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    public int getAvailableFunds() {
        return availableFunds;
    }
    public void setAvailableFunds(int availableFunds) {
        this.availableFunds = availableFunds;
    }
    public Long getCustomerId() {
        return customerId;
    }
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
} 
