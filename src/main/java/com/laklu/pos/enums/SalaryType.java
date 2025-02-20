package com.laklu.pos.enums;

public enum SalaryType {
    MONTHLY("Monthly"),
    HOURLY("Hourly"),
    SHIFTLY("Shiftly");

    private final String label;

    SalaryType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}