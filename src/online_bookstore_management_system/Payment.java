/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.*;

public class Payment {

    private final String id;
    private double amount;
    private String method;
    private boolean success;
    private String transactionRef;

    
    private static final List<Payment> RECENT = new ArrayList<>();

    private final InvoiceService invoiceService;   
    private final PricingService pricingService;   

    public Payment(InvoiceService invoiceService, PricingService pricingService) {
        this.id = UUID.randomUUID().toString();
        this.invoiceService = invoiceService;
        this.pricingService = pricingService;
    }

    
    public boolean processCardPayment(Order order, String cardNumber, String expiry, String cvv) {
        this.method = "CARD";
        this.amount = pricingService.calculateTotal(order.getItems(), getDiscount(order));

        if (!validateCard(cardNumber, expiry)) {
            this.success = false;
            return false;
        }

        this.transactionRef = "TXN-" + new Random().nextInt(1000000);
        this.success = true;
        order.markPaid();

        invoiceService.generateInvoice(order);
        order.getCustomer().saveProfile("customers.log");
        RECENT.add(this);
        return true;
    }

    public boolean processCashOnDelivery(Order order) {
        this.method = "COD";
        this.amount = pricingService.calculateTotal(order.getItems(), getDiscount(order));

        this.transactionRef = "COD-" + new Random().nextInt(1000000);
        this.success = true;
        order.markPaid();

        RECENT.add(this);
        return true;
    }

    private boolean validateCard(String cardNumber, String expiry) {
        return cardNumber != null && cardNumber.length() >= 12
                && expiry.contains("/") && (cardNumber.charAt(0) == '4' || cardNumber.charAt(0) == '5');
    }

    private Discount getDiscount(Order order) {
        try {
            var field = Order.class.getDeclaredField("discount");
            field.setAccessible(true);
            return (Discount) field.get(order);
        } catch (Exception e) {
            return null;
        }
    }

    public String getId() { return id; }
    public boolean isSuccess() { return success; }
    public String getTransactionRef() { return transactionRef; }
    public double getAmount() { return amount; }
}

    
