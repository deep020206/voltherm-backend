package com.voltherm.service;

import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OtpStorage {
    private final Map<String, OtpData> otpStore = new ConcurrentHashMap<>();

    public void storeOtp(String username, String otp, Instant expirationTime) {
        otpStore.put(username, new OtpData(otp, expirationTime));
    }

    public OtpData getOtp(String username) {
        return otpStore.get(username);
    }

    public void removeOtp(String username) {
        otpStore.remove(username);
    }

    public static class OtpData {
        private final String otp;
        private final Instant expirationTime;

        public OtpData(String otp, Instant expirationTime) {
            this.otp = otp;
            this.expirationTime = expirationTime;
        }

        public String getOtp() {
            return otp;
        }

        public Instant getExpirationTime() {
            return expirationTime;
        }

        public boolean isExpired() {
            return Instant.now().isAfter(expirationTime);
        }
    }
}
