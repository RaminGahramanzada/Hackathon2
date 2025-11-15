package com.easyfin.openbanking.service;

import com.easyfin.openbanking.enums.AlertType;
import com.easyfin.openbanking.enums.BusinessType;
import com.easyfin.openbanking.enums.TaxStatus;
import com.easyfin.openbanking.enums.TransactionCategory;
import com.easyfin.openbanking.model.*;
import com.easyfin.openbanking.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Service to initialize mock data for the restaurant use case
 */
@Service
@RequiredArgsConstructor
public class DataInitializationService {
    
    private final BusinessRepository businessRepository;
    private final EmployeeRepository employeeRepository;
    private final TransactionRepository transactionRepository;
    private final PayrollRepository payrollRepository;
    private final AlertRepository alertRepository;
    private final RecommendationRepository recommendationRepository;
    private final CategorizationService categorizationService;
    
    private final Random random = new Random();
    
    @PostConstruct
    @Transactional
    public void initializeData() {
        // Check if data already exists
        if (businessRepository.count() > 0) {
            System.out.println("Data already initialized. Skipping...");
            return;
        }
        
        System.out.println("Initializing mock data for restaurant use case...");
        
        // 1. Create Business
        Business business = createBusiness();
        
        // 2. Create Employees
        List<Employee> employees = createEmployees(business);
        
        // 3. Create Transactions
        List<Transaction> transactions = createTransactions(business);
        
        // 4. Create Payroll Records
        createPayrollRecords(business, employees);
        
        // 5. Create Alerts
        createAlerts(business, transactions);
        
        // 6. Create Recommendations
        createRecommendations(business);
        
        System.out.println("Mock data initialization completed!");
        System.out.println("Business: " + business.getBusinessName());
        System.out.println("Employees: " + employees.size());
        System.out.println("Transactions: " + transactions.size());
    }
    
    private Business createBusiness() {
        Business business = new Business();
        business.setBusinessName("Nizami Restaurant");
        business.setBusinessType(BusinessType.RESTAURANT);
        business.setEmployeeCount(7);
        business.setAnnualIncome(new BigDecimal("185000.00"));
        business.setCurrency("AZN");
        business.setTaxStatus(TaxStatus.MICRO_ENTREPRENEUR);
        business.setTaxExemption(0.75);
        business.setOwnerName("Murad Aliyev");
        business.setEmail("murad@nizamirestaurant.az");
        business.setPhone("+994501234567");
        business.setAddress("28 May küç., Baku, Azerbaijan");
        business.setTaxId("1234567890");
        business.setRegistrationDate(LocalDate.now().minusYears(2));
        business.setIsActive(true);
        
        return businessRepository.save(business);
    }
    
    private List<Employee> createEmployees(Business business) {
        List<Employee> employees = new ArrayList<>();
        
        // Head Chef
        employees.add(createEmployee(business, "Rashad", "Mammadov", "Head Chef", "1500.00"));
        
        // Sous Chef
        employees.add(createEmployee(business, "Aysel", "Huseynova", "Sous Chef", "1200.00"));
        
        // Waiters
        employees.add(createEmployee(business, "Elvin", "Aliyev", "Waiter", "800.00"));
        employees.add(createEmployee(business, "Leyla", "Hasanova", "Waiter", "800.00"));
        employees.add(createEmployee(business, "Nigar", "Qasimova", "Waiter", "800.00"));
        
        // Dishwasher
        employees.add(createEmployee(business, "Orkhan", "Ibrahimov", "Dishwasher", "600.00"));
        
        // Manager
        employees.add(createEmployee(business, "Sabina", "Mammadova", "Restaurant Manager", "1800.00"));
        
        return employees;
    }
    
    private Employee createEmployee(Business business, String firstName, String lastName, 
                                   String position, String salary) {
        Employee employee = new Employee();
        employee.setBusiness(business);
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setPosition(position);
        employee.setMonthlySalary(new BigDecimal(salary));
        employee.setHireDate(LocalDate.now().minusMonths(random.nextInt(24) + 1));
        employee.setEmail(firstName.toLowerCase() + "@nizamirestaurant.az");
        employee.setPhone("+99450" + (1000000 + random.nextInt(9000000)));
        employee.setIsActive(true);
        
        return employeeRepository.save(employee);
    }
    
    private List<Transaction> createTransactions(Business business) {
        List<Transaction> transactions = new ArrayList<>();
        
        // Generate transactions for the last 60 days
        LocalDateTime now = LocalDateTime.now();
        
        // Daily revenue (income) - restaurant sales
        for (int i = 0; i < 60; i++) {
            LocalDateTime date = now.minusDays(i);
            
            // Daily sales (2-3 transactions per day)
            int dailySales = random.nextInt(2) + 2;
            for (int j = 0; j < dailySales; j++) {
                transactions.add(createTransaction(business, date, 
                        "Daily Sales - " + date.toLocalDate(), 
                        randomAmount(400, 800), true, TransactionCategory.REVENUE));
            }
        }
        
        // Monthly rent (recurring)
        for (int i = 0; i < 2; i++) {
            transactions.add(createTransaction(business, now.minusMonths(i).withDayOfMonth(1), 
                    "Monthly Rent - Baku Center", new BigDecimal("3000.00"), 
                    false, TransactionCategory.RENT));
        }
        
        // Food supplies (frequent)
        String[] foodSuppliers = {"Taze Bazar", "Yasil Bazar", "Metro Market", "Meat Supplier LLC"};
        for (int i = 0; i < 30; i++) {
            transactions.add(createTransaction(business, now.minusDays(i * 2), 
                    foodSuppliers[random.nextInt(foodSuppliers.length)], 
                    randomAmount(150, 300), false, TransactionCategory.FOOD_SUPPLIES));
        }
        
        // Utilities
        transactions.add(createTransaction(business, now.minusDays(5), 
                "Azercell - Business Plan", new BigDecimal("89.90"), 
                false, TransactionCategory.TELECOMMUNICATIONS));
        transactions.add(createTransaction(business, now.minusDays(10), 
                "Azersu - Water Bill", new BigDecimal("120.50"), 
                false, TransactionCategory.UTILITIES));
        transactions.add(createTransaction(business, now.minusDays(12), 
                "Azerishiq - Electricity", new BigDecimal("380.00"), 
                false, TransactionCategory.UTILITIES));
        transactions.add(createTransaction(business, now.minusDays(15), 
                "Azergas - Gas Bill", new BigDecimal("210.00"), 
                false, TransactionCategory.UTILITIES));
        
        // Equipment and maintenance
        transactions.add(createTransaction(business, now.minusDays(20), 
                "Kitchen Equipment Repair", new BigDecimal("450.00"), 
                false, TransactionCategory.MAINTENANCE));
        transactions.add(createTransaction(business, now.minusDays(35), 
                "New Refrigerator - Baku Appliances", new BigDecimal("1200.00"), 
                false, TransactionCategory.EQUIPMENT));
        
        // Office supplies
        transactions.add(createTransaction(business, now.minusDays(25), 
                "Office Supplies - Ali&Nino", new BigDecimal("85.00"), 
                false, TransactionCategory.OFFICE_SUPPLIES));
        
        // Marketing
        transactions.add(createTransaction(business, now.minusDays(18), 
                "Instagram Advertising", new BigDecimal("150.00"), 
                false, TransactionCategory.MARKETING));
        transactions.add(createTransaction(business, now.minusDays(40), 
                "Facebook Ads Campaign", new BigDecimal("200.00"), 
                false, TransactionCategory.MARKETING));
        
        // Transport
        transactions.add(createTransaction(business, now.minusDays(8), 
                "Bolt - Supply Delivery", new BigDecimal("25.00"), 
                false, TransactionCategory.TRAVEL));
        
        return transactions;
    }
    
    private Transaction createTransaction(Business business, LocalDateTime date, 
                                         String merchant, BigDecimal amount, 
                                         boolean isIncome, TransactionCategory category) {
        Transaction transaction = new Transaction();
        transaction.setBusiness(business);
        transaction.setTransactionDate(date);
        transaction.setMerchantName(merchant);
        transaction.setDescription(merchant + " - " + category.getDisplayName());
        transaction.setAmount(amount);
        transaction.setCurrency("AZN");
        transaction.setIsIncome(isIncome);
        transaction.setCategory(category);
        transaction.setIsTaxDeductible(category.isTaxDeductible());
        transaction.setCategorizationConfidence(0.85);
        
        return transactionRepository.save(transaction);
    }
    
    private BigDecimal randomAmount(int min, int max) {
        double amount = min + (max - min) * random.nextDouble();
        return BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    private void createPayrollRecords(Business business, List<Employee> employees) {
        LocalDate currentMonth = LocalDate.now().withDayOfMonth(1);
        
        for (Employee employee : employees) {
            Payroll payroll = new Payroll();
            payroll.setBusiness(business);
            payroll.setEmployee(employee);
            payroll.setPayrollMonth(currentMonth);
            payroll.setGrossSalary(employee.getMonthlySalary());
            
            // Calculate SSF and taxes
            BigDecimal employeeSsf = employee.getMonthlySalary().multiply(new BigDecimal("0.03"));
            BigDecimal employerSsf = employee.getMonthlySalary().multiply(new BigDecimal("0.22"));
            BigDecimal incomeTax = employee.getMonthlySalary().subtract(employeeSsf)
                    .multiply(new BigDecimal("0.14"));
            BigDecimal netSalary = employee.getMonthlySalary().subtract(employeeSsf).subtract(incomeTax);
            BigDecimal totalCost = employee.getMonthlySalary().add(employerSsf);
            
            payroll.setEmployeeSsfContribution(employeeSsf);
            payroll.setEmployerSsfContribution(employerSsf);
            payroll.setIncomeTax(incomeTax);
            payroll.setNetSalary(netSalary);
            payroll.setTotalEmployerCost(totalCost);
            payroll.setIsPaid(false);
            
            payrollRepository.save(payroll);
        }
    }
    
    private void createAlerts(Business business, List<Transaction> transactions) {
        // Low balance alert
        Alert lowBalance = new Alert();
        lowBalance.setBusiness(business);
        lowBalance.setAlertType(AlertType.LOW_BALANCE);
        lowBalance.setTitle("Low Balance Warning");
        lowBalance.setMessage("Your account balance is running low. Current projected balance: 2,500 AZN. Consider reviewing upcoming expenses.");
        lowBalance.setSeverity("high");
        lowBalance.setIsDismissed(false);
        alertRepository.save(lowBalance);
        
        // Tax deadline alert - "Tax Deadline Approaching - VAT Payment in 5 days"
        Alert taxDeadline = new Alert();
        taxDeadline.setBusiness(business);
        taxDeadline.setAlertType(AlertType.TAX_DEADLINE);
        taxDeadline.setTitle("Tax Deadline Approaching");
        taxDeadline.setMessage("VAT Payment in 5 days");
        taxDeadline.setSeverity("high");
        taxDeadline.setIsDismissed(false);
        
        // Add metadata for iOS
        Map<String, Object> metadata = new HashMap<>();
        LocalDate deadlineDate = LocalDate.now().plusDays(5);
        metadata.put("deadlineDate", deadlineDate.toString());
        metadata.put("daysRemaining", 5);
        metadata.put("actionUrl", "/taxes/declarations");
        taxDeadline.setMetadata(metadata);
        
        alertRepository.save(taxDeadline);
        
        // Additional tax deadline for quarterly declaration
        Alert quarterlyTax = new Alert();
        quarterlyTax.setBusiness(business);
        quarterlyTax.setAlertType(AlertType.TAX_DEADLINE);
        quarterlyTax.setTitle("Quarterly Tax Declaration Due");
        quarterlyTax.setMessage("Submit your Q4 declaration in 12 days");
        quarterlyTax.setSeverity("medium");
        quarterlyTax.setIsDismissed(false);
        
        Map<String, Object> quarterlyMetadata = new HashMap<>();
        LocalDate quarterlyDeadline = LocalDate.now().plusDays(12);
        quarterlyMetadata.put("deadlineDate", quarterlyDeadline.toString());
        quarterlyMetadata.put("daysRemaining", 12);
        quarterlyMetadata.put("actionUrl", "/taxes/quarterly");
        quarterlyTax.setMetadata(quarterlyMetadata);
        
        alertRepository.save(quarterlyTax);
        
        // Unusual spending alert
        Alert unusualSpending = new Alert();
        unusualSpending.setBusiness(business);
        unusualSpending.setAlertType(AlertType.UNUSUAL_SPENDING);
        unusualSpending.setTitle("Unusual Spending Detected");
        unusualSpending.setMessage("Food supply costs increased by 25% this month compared to last month. Review your suppliers.");
        unusualSpending.setSeverity("medium");
        unusualSpending.setIsDismissed(false);
        alertRepository.save(unusualSpending);
    }
    
    private void createRecommendations(Business business) {
        // Tax savings recommendation
        Recommendation taxRec = new Recommendation();
        taxRec.setBusiness(business);
        taxRec.setTitle("You qualify for 75% tax exemption!");
        taxRec.setDescription("As a micro-entrepreneur with 7 employees and income under 200,000 AZN, " +
                "you qualify for 75% income tax exemption (sadələşdirilmiş vergi). " +
                "Estimated annual tax savings: 27,750 AZN");
        taxRec.setCategory("tax-savings");
        taxRec.setPotentialSavings(new BigDecimal("27750.00"));
        taxRec.setPriority("high");
        taxRec.setIsActedUpon(false);
        recommendationRepository.save(taxRec);
        
        // Cost reduction
        Recommendation costRec = new Recommendation();
        costRec.setBusiness(business);
        costRec.setTitle("Review Food Supplier Pricing");
        costRec.setDescription("Your food supply costs have increased 25% in the last month. " +
                "Consider comparing prices from alternative suppliers or negotiating better rates.");
        costRec.setCategory("cost-reduction");
        costRec.setPotentialSavings(new BigDecimal("500.00"));
        costRec.setPriority("medium");
        costRec.setIsActedUpon(false);
        recommendationRepository.save(costRec);
        
        // Cash flow
        Recommendation cashRec = new Recommendation();
        cashRec.setBusiness(business);
        cashRec.setTitle("Cash Flow Optimization");
        cashRec.setDescription("Based on your spending patterns, consider maintaining a cash reserve of " +
                "at least 15,000 AZN to cover 2 months of operating expenses.");
        cashRec.setCategory("cash-flow");
        cashRec.setPotentialSavings(new BigDecimal("0.00"));
        cashRec.setPriority("medium");
        cashRec.setIsActedUpon(false);
        recommendationRepository.save(cashRec);
    }
}

