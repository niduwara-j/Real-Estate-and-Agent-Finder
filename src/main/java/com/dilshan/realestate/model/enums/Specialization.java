package com.dilshan.realestate.model.enums;

public enum Specialization {
    RESIDENTIAL("Residential"),
    COMMERCIAL("Commercial"),
    LUXURY("Luxury Estates"),
    INDUSTRIAL("Industrial"),
    RENTAL("Rental & Leasing");

    private final String displayName;

    Specialization(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
