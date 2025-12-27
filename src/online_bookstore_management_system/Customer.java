package online_bookstore_management_system;

import java.util.*;

/*
=========================================================
 CUSTOMER (DOMAIN CLASS)
---------------------------------------------------------
✔ SRP: Holds ONLY customer data & core behavior
✔ NO file I/O
✔ NO admin/audit logic
✔ NO business workflows
=========================================================
*/
public class Customer {

    private String name;
    private String email;
    private String address;
    private String phone;

    private List<Order> orders = new ArrayList<>();
    private double outstandingBalance;

    // Domain state
    private boolean blacklisted;
    private String notes;

    // Delegated responsibility
    private String preferredPaymentMethod;

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
        if (!phone.isBlank()) {
            c.phone = phone;
        }
        return c;
    }

    public void addOrder(Order o) {
        orders.add(o);
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    /*
     * -----------------------------------------------------
     * Delegated behavior (DIP)
     * Customer does NOT implement review logic itself
     * -----------------------------------------------------
     */
    public void blacklist(String reason, CustomerReviewService reviewer) {
        reviewer.flagCustomer(this, reason);
    }

    // Setters used by service layer
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

    public void setPreferredPaymentMethod(String method) {
        this.preferredPaymentMethod = method;
    }
}

/*
 * =========================================================
 * CUSTOMER REVIEW / AUDIT SERVICE
 * ---------------------------------------------------------
 * ✔ SRP: Handles customer review only
 * ✔ DIP: Customer depends on abstraction
 * =========================================================
 */
interface CustomerReviewService {
    void flagCustomer(Customer customer, String reason);
}

class DefaultCustomerReviewService implements CustomerReviewService {
    @Override
    public void flagCustomer(Customer customer, String reason) {
        customer.setBlacklisted(true);
        customer.setNotes(reason);
    }
}

/*
 * =========================================================
 * PERSISTENCE SERVICE
 * ---------------------------------------------------------
 * ✔ SRP: File storage extracted from Customer
 * =========================================================
 */
interface CustomerPersistence {
    void save(Customer customer, String file);
}

class FileCustomerPersistence implements CustomerPersistence {
    @Override
    public void save(Customer c, String file) {
        try (java.io.FileWriter fw = new java.io.FileWriter(file, true)) {
            fw.write(c.getName() + "," + c.getEmail() + "," + c.getNotes() + "\n");
        } catch (Exception e) {
            throw new RuntimeException("Failed to save customer", e);
        }
    }
}

/*
 * =========================================================
 * PAYMENT PREFERENCE SERVICE
 * ---------------------------------------------------------
 * ✔ SRP: Payment preference logic separated
 * =========================================================
 */
interface PaymentPreferenceService {
    void setPreferredMethod(Customer customer, String method);
}

class BasicPaymentPreferenceService implements PaymentPreferenceService {
    @Override
    public void setPreferredMethod(Customer customer, String method) {
        customer.setPreferredPaymentMethod(method);
    }
}

/*
 * =========================================================
 * SERVICE FACTORY
 * ---------------------------------------------------------
 * ✔ Centralized object creation
 * ✔ Supports OCP
 * =========================================================
 */
class CustomerServiceFactory {

    public static CustomerReviewService reviewService() {
        return new DefaultCustomerReviewService();
    }

    public static CustomerPersistence persistenceService() {
        return new FileCustomerPersistence();
    }

    public static PaymentPreferenceService paymentPreferenceService() {
        return new BasicPaymentPreferenceService();
    }
}
