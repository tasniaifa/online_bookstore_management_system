package online_bookstore_management_system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

// =========================================================
//                    CUSTOMER (DOMAIN CLASS)
//  - Holds only customer-related data & behavior
//  - NO persistence
//  - NO admin auditing
//  - NO business workflow
//  (SRP Applied)
// =========================================================
public class Customer {

    private String name;
    private String email;
    private String address;
    private String phone;
    private List<Order> orders = new ArrayList<>();
    private double outstandingBalance;

    // Domain-only attributes
    private boolean blacklisted;
    private String notes;

    // Delegated responsibility:
    // Preferred payment stored, but logic handled elsewhere
    private String preferredPaymentMethod;

    public void setPreferredPaymentMethod(String preferredPaymentMethod) {
        this.preferredPaymentMethod = preferredPaymentMethod;
    }

    public Customer(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.outstandingBalance = 0.0;
        this.blacklisted = false;
    }

    public static Customer readFromInput(Scanner sc) {
        System.out.print("Customer name: ");
        String name = sc.nextLine();

        System.out.print("Customer email: ");
        String email = sc.nextLine();

        System.out.print("Customer address: ");
        String address = sc.nextLine();

        Customer c = new Customer(name, email, address);

        System.out.print("Phone (optional): ");
        String phone = sc.nextLine();
        if (!phone.isBlank())
            c.phone = phone;

        return c;
    }

    public void addOrder(Order o) {
        orders.add(o);
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    // Delegated behavior
    public void blacklist(String reason, CustomerReviewService reviewer) {
        reviewer.flagCustomer(this, reason);
    }

    // Setters for service classes
    public void setBlacklisted(boolean b) {
        this.blacklisted = b;
    }

    public void setNotes(String n) {
        this.notes = n;
    }

    public boolean isBlacklisted() {
        return blacklisted;
    }

    public String getNotes() {
        return notes;
    }
}

// =========================================================
// PERSISTENCE SERVICE (SRP)
// - Extracted from Customer.saveProfile()
// - Customer no longer handles file IO
// =========================================================
interface CustomerPersistence {
    void save(Customer customer, String file);
}

class FileCustomerPersistence implements CustomerPersistence {
    @Override
    public void save(Customer c, String file) {
        try (java.io.FileWriter fw = new java.io.FileWriter(file, true)) {
            fw.write(c.getName() + "," + c.getEmail() + "," + c.getNotes() + "\n");
        } catch (Exception e) {
        }
    }
}

// =========================================================
// CUSTOMER REVIEW / AUDIT SERVICE (SRP)
// - Extracted from AdminAudit.log() call
// - Customer no longer depends on Admin
// (DIP applied)
// =========================================================
interface CustomerReviewService {
    void flagCustomer(Customer c, String reason);
}

class DefaultCustomerReviewService implements CustomerReviewService {
    @Override
    public void flagCustomer(Customer c, String reason) {
        c.setBlacklisted(true);
        c.setNotes(reason);
        System.out.println("Customer flagged for review: " + c.getName() + " | Reason: " + reason);
    }
}

// =========================================================
// PAYMENT PREFERENCE HANDLING (SRP / OCP)
// - Customer does NOT implement payment logic
// =========================================================

interface PaymentPreferenceService {
    void applyPreference(Customer c, PaymentMethod method);
}

class BasicPaymentPreferenceService implements PaymentPreferenceService {
    @Override
    public void applyPreference(Customer c, PaymentMethod method) {
        c.setPreferredPaymentMethod(method.name());
        System.out.println("Applied preferred payment method: " + method);
    }
}

// =========================================================
// CUSTOMER ANALYTICS SERVICE (Extra SRP)
// =========================================================
class CustomerAnalyticsService {
    public double calculateLifetimeValue(Customer c) {
        return c.getOrders()
                .stream()
                .mapToDouble(Order::getTotalAmount)
                .sum();
    }
}
