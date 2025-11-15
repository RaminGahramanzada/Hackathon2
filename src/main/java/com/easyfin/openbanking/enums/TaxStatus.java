package com.easyfin.openbanking.enums;

/**
 * Tax status types for Azerbaijan businesses
 */
public enum TaxStatus {
    MICRO_ENTREPRENEUR("Micro-Entrepreneur", 0.75, "1-10 employees, <200K AZN income"),
    SMALL_ENTERPRISE("Small Enterprise", 0.0, "11-50 employees, 200K-3M AZN income"),
    MEDIUM_ENTERPRISE("Medium Enterprise", 0.0, "51-250 employees, 3M-30M AZN income"),
    STARTUP("Startup (with certificate)", 1.0, "3-year tax exemption"),
    STANDARD("Standard Tax Payer", 0.0, "Standard corporate tax rates");
    
    private final String displayName;
    private final double taxExemptionRate;
    private final String description;
    
    TaxStatus(String displayName, double taxExemptionRate, String description) {
        this.displayName = displayName;
        this.taxExemptionRate = taxExemptionRate;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public double getTaxExemptionRate() {
        return taxExemptionRate;
    }
    
    public String getDescription() {
        return description;
    }
}

