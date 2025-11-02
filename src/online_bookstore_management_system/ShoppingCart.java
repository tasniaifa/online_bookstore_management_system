/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import java.util.*;

public class ShoppingCart {
    private final Customer owner;
    private final Map<Book, Integer> items = new LinkedHashMap<>();
    private Discount discount;

    public ShoppingCart(Customer owner) { this.owner = owner; }

    public void addItem(Book book, int qty) { items.put(book, items.getOrDefault(book, 0) + qty); }
    public void removeItem(Book book) { items.remove(book); }
    public void applyDiscount(Discount discount) { this.discount = discount; }

    public Order checkout(PricingService pricingService) {
        Order order = new Order(owner, pricingService);
        for (Map.Entry<Book, Integer> e : items.entrySet()) {
            order.addItem(new OrderItem(e.getKey(), e.getValue()));
        }
        if (discount != null) order.applyDiscount(discount);
        items.clear();
        return order;
    }
}

    

