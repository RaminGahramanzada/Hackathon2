package com.easyfin.openbanking.enums;

/**
 * Business types for industry classification
 */
public enum BusinessType {
    RESTAURANT("Restaurant"),
    CAFE("Cafe"),
    RETAIL("Retail Store"),
    SERVICES("Services"),
    TECHNOLOGY("Technology"),
    MANUFACTURING("Manufacturing"),
    CONSULTING("Consulting"),
    CONSTRUCTION("Construction"),
    HEALTHCARE("Healthcare"),
    EDUCATION("Education"),
    OTHER("Other");
    
    private final String displayName;
    
    BusinessType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

