package com.voltherm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String productId;
    private String productName;
    private Price price;
    private boolean featured;
    private String category;
    private String subCategory;
    private String batteryChemistry;
    private double capacityAh;
    private double nominalVoltageV;
    private OperatingVoltage operatingVoltageV;
    private double nominalEnergyWh;
    private double usableEnergyWh;
    private ChargeDischargeCurrent chargeDischargeCurrentA;
    private String imageUrl;       // e.g. /images/uuid.jpg
    private String pdfDownloadUrl; // e.g. /api/products/{productId}/pdf

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Price {
        private String currency;
        private double amount;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperatingVoltage {
        private double min;
        private double max;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChargeDischargeCurrent {
        private Current recommended;
        private Current maximum;
        private Current peak;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Current {
            private double charge;
            private double discharge;
        }
    }
}
