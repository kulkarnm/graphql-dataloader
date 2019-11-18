package com.test.graphql.controller;

public class Message {

    private String errorCode;

    private String errorTitle;

    private String errorDetail;

    public Message(String errorCode, String errorTitle, String errorDetail) {
        this.errorCode = errorCode;
        this.errorTitle = errorTitle;
        this.errorDetail = errorDetail;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorTitle() {
        return errorTitle;
    }

    public String getErrorDetail() {
        return errorDetail;
    }
}
