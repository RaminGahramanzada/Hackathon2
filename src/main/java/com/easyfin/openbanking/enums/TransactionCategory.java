package com.easyfin.openbanking.enums;

/**
 * Transaction categories for expense classification
 */
public enum TransactionCategory {
    // Revenue categories
    REVENUE("Revenue", true, false),
    SALES("Sales", true, false),
    
    // Expense categories (tax-deductible)
    FOOD_SUPPLIES("Food Supplies", false, true),
    RENT("Rent", false, true),
    UTILITIES("Utilities", false, true),
    SALARIES("Salaries", false, true),
    OFFICE_SUPPLIES("Office Supplies", false, true),
    MARKETING("Marketing", false, true),
    SOFTWARE("Software & Technology", false, true),
    TRAVEL("Travel", false, true),
    EQUIPMENT("Equipment", false, true),
    MAINTENANCE("Maintenance & Repairs", false, true),
    INSURANCE("Insurance", false, true),
    LEGAL_PROFESSIONAL("Legal & Professional Services", false, true),
    TELECOMMUNICATIONS("Telecommunications", false, true),
    
    // Employee-related expenses
    PAYROLL("Payroll", false, true),
    SSF_CONTRIBUTIONS("SSF Contributions", false, true),
    EMPLOYEE_BENEFITS("Employee Benefits", false, true),
    
    // Other
    OTHER_EXPENSE("Other Expense", false, false),
    UNCATEGORIZED("Uncategorized", false, false);
    
    private final String displayName;
    private final boolean isIncome;
    private final boolean isTaxDeductible;
    
    TransactionCategory(String displayName, boolean isIncome, boolean isTaxDeductible) {
        this.displayName = displayName;
        this.isIncome = isIncome;
        this.isTaxDeductible = isTaxDeductible;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public boolean isIncome() {
        return isIncome;
    }
    
    public boolean isTaxDeductible() {
        return isTaxDeductible;
    }
}

