package com.easyfin.openbanking.enums;

/**
 * Types of alerts for intelligent monitoring
 */
public enum AlertType {
    UNAUTHORIZED_PAYMENT("Unauthorized Payment", "high"),
    LOW_BALANCE("Low Balance", "high"),
    UNUSUAL_SPENDING("Unusual Spending", "medium"),
    TAX_DEADLINE("Tax Deadline", "high"),
    PAYROLL_REMINDER("Payroll Reminder", "high"),
    CASH_SHORTAGE("Cash Shortage Warning", "high"),
    DUPLICATE_SUBSCRIPTION("Duplicate Subscription", "medium"),
    COST_OPTIMIZATION("Cost Optimization Opportunity", "low");
    
    private final String displayName;
    private final String severity;
    
    AlertType(String displayName, String severity) {
        this.displayName = displayName;
        this.severity = severity;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getSeverity() {
        return severity;
    }
}

