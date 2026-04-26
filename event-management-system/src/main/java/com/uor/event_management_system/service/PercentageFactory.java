package com.uor.event_management_system.service;

public class PercentageFactory {

    public static String getStatus(int percent) {

        if (percent == 100) return "full";
        if (percent > 70) return "high";
        if (percent > 40) return "medium";

        return "low";
    }
}
