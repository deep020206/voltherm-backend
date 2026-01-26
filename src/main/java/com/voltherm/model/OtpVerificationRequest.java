package com.voltherm.model;

public class OtpVerificationRequest {
    private String otp;
    private String newPassword;

    public OtpVerificationRequest() {}

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
