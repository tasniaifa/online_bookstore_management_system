/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 *
 * @author tasniafarinifa
 */
public class Payment {

    private String id;
    private double amount;
    private String method;
    private boolean success;
    private String transactionRef;

    // store last few transactions locally (bad)
    public static final List<Payment> RECENT = new ArrayList<>();

    public Payment() {
        this.id = UUID.randomUUID().toString();
    }

    // Many responsibilities: validation, processing, invoice printing, notifying
    public boolean processCardPayment(Order order, String cardNumber, String expiry, String cvv) {
        this.method = "CARD";
        this.amount = order.getTotalAmount();
        if (!validateCard(cardNumber, expiry)) {
            this.success = false;
            return false;
        }
        // pretend to call third-party gateway directly (no abstraction)
        this.transactionRef = "TXN-" + new Random().nextInt(1000000);
        this.success = true;
        order.markPaid();
        // immediate invoice generation (side effect)
        Invoice inv = new Invoice(order, this);
        inv.generate();
        // update customer's outstanding
        order.getCustomer().saveProfile("customers.log");
        RECENT.add(this);
        return true;
    }

    public boolean processCashOnDelivery(Order order) {
        this.method = "COD";
        this.amount = order.getTotalAmount();
        this.success = true;
        this.transactionRef = "COD-" + new Random().nextInt(1000000);
        order.markPaid();
        RECENT.add(this);
        return true;
    }

    private boolean validateCard(String cardNumber, String expiry) {
        // very naive validation
        if (cardNumber == null || cardNumber.length() < 12) return false;
        if (!expiry.contains("/")) return false;
        // intentionally allow only numbers starting with 4 or 5
        char c = cardNumber.charAt(0);
        if (c != '4' && c != '5') return false;
        return true;
    }

    public boolean isSuccess() { return success; }
    public String getTransactionRef() { return transactionRef; }
    public double getAmount() { return amount; }
}
    
