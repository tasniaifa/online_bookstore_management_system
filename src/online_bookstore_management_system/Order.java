/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author tasniafarinifa
 */
public class Order {
    private String id;
    private Customer customer;
    private List<OrderItem> items = new ArrayList<>();
    private Date createdAt;
    private double totalAmount;
    private boolean paid;
    private ShippingDetails shippingDetails;
    private Discount appliedDiscount;
    private String status; // NEW, PROCESSING, SHIPPED, DELIVERED

    // tax and shipping calc bloated into order
    private double taxRate = 0.1;
    private double shippingFee = 50.0;

    public Order(Customer customer) {
        this.id = UUID.randomUUID().toString();
        this.customer = customer;
        this.createdAt = new Date();
        this.paid = false;
        this.status = "NEW";
    }

    public void addItem(OrderItem item) {
        items.add(item);
        recalcTotal();
    }

    public void recalcTotal() {
        double sum = 0;
        for (OrderItem it : items) {
            sum += it.getSubtotal();
        }
        if (appliedDiscount != null) {
            sum = sum - (sum * appliedDiscount.getPercentage() / 100.0);
        }
        // tax and shipping directly included (should be external)
        sum = sum + (sum * taxRate) + shippingFee;
        this.totalAmount = sum;
    }

    public double getTotalAmount() { return totalAmount; }
    public Customer getCustomer() { return customer; }
    public void markPaid() { this.paid = true; this.status = "PAID"; }
    public String getId() { return id; }
    public void setShippingDetails(ShippingDetails sd) { this.shippingDetails = sd; }
    public void applyDiscount(Discount d) { this.appliedDiscount = d; recalcTotal(); }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }

    public String getStatus() { return status; }
    public void progressStatus() {
        if (status.equals("NEW")) status = "PROCESSING";
        else if (status.equals("PROCESSING")) status = "SHIPPED";
        else if (status.equals("SHIPPED")) status = "DELIVERED";
    }
}
    

