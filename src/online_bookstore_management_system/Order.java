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
import java.util.*;

public class Order {
    private final String id;
    private final Customer customer;
    private final List<OrderItem> items = new ArrayList<>();
    private final Date createdAt;
    private boolean paid;
    private OrderStatus status;
    private ShippingServiceDetails shippingDetails;
    private Discount discount;
    private double totalAmount;

    private final PricingService pricingService;

    public Order(Customer customer, PricingService pricingService) {
        this.id = UUID.randomUUID().toString();
        this.customer = customer;
        this.createdAt = new Date();
        this.status = new OrderStatus.NewStatus();
        this.pricingService = pricingService;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        recalcTotal();
    }

    public void applyDiscount(Discount discount) {
        this.discount = discount;
        recalcTotal();
    }

    private void recalcTotal() {
        this.totalAmount = pricingService.calculateTotal(items, discount);
    }

    public void setShippingDetails(ShippingServiceDetails sd) { this.shippingDetails = sd; }
    public void markPaid() { this.paid = true; this.status = new OrderStatus.PaidStatus(); }
    public void progressStatus() { this.status = status.nextStatus(); }

    public String getId() { return id; }
    public Customer getCustomer() { return customer; }
    public List<OrderItem> getItems() { return Collections.unmodifiableList(items); }
    public double getTotalAmount() { return totalAmount; }
    public boolean isPaid() { return paid; }
    public String getStatus() { return status.getName(); }
    public ShippingServiceDetails getShippingDetails() { return shippingDetails; }
}

    

