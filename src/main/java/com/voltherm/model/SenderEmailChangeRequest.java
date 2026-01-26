package com.voltherm.model;

public class SenderEmailChangeRequest {
    private String senderEmail;
    private String appPassword;

    public SenderEmailChangeRequest() {}

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getAppPassword() {
        return appPassword;
    }

    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }
}
