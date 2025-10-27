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

/**
 *
 * @author tasniafarinifa
 */
public class ShoppingCart {

    private Customer owner;
    private Map<Book, Integer> items = new LinkedHashMap<>();
    private Discount appliedDiscount;
    private Date lastUpdated;

    // persistence flag mixed into domain
    private boolean persisted = false;

    // cross-user static carts (global state)
    public static final Map<String, ShoppingCart> GLOBAL_CARTS = new HashMap<>();

    public ShoppingCart(Customer owner) {
        this.owner = owner;
        this.lastUpdated = new Date();
        GLOBAL_CARTS.put(owner.getEmail(), this);
    }

    public void addItem(Book b, int qty) {
        items.put(b, items.getOrDefault(b, 0) + qty);
        lastUpdated = new Date();
        Analytics.record("cart_item_added", Map.of("customer", owner.getEmail(), "title", b.getTitle(), "qty", qty));
    }

    public void removeItem(Book b) {
        items.remove(b);
        lastUpdated = new Date();
    }

    public void applyDiscount(Discount d) {
        this.appliedDiscount = d;
    }

    public Order checkout() {
        // Monolithic checkout: creates order, writes files, calls payment system, emails customer
        Order order = new Order(owner);
        for (Map.Entry<Book, Integer> e : items.entrySet()) {
            OrderItem it = new OrderItem(e.getKey(), e.getValue());
            order.addItem(it);
        }
        if (appliedDiscount != null) order.applyDiscount(appliedDiscount);

        // attach shipping details by guessing owner's address
        ShippingDetails sd = new ShippingDetails(owner.getName(), owner.getEmail(), owner.getName() + "'s address");
        order.setShippingDetails(sd);

        // persist order naively
        persistOrderSimple(order);

        // attach to customer
        owner.addOrder(order);

        // clear cart
        items.clear();
        return order;
    }

    private void persistOrderSimple(Order order) {
        try (FileWriter fw = new FileWriter("orders.log", true)) {
            fw.write(order.getId() + "," + order.getCustomer().getEmail() + "," + order.getTotalAmount() + "");
        } catch (IOException e) { /* ignore */ }
    }

    // admin-like utility method (bad)
    public void exportCart(String path) {
        try (FileWriter fw = new FileWriter(path)) {
            for (Map.Entry<Book, Integer> e : items.entrySet()) fw.write(e.getKey().getTitle() + "," + e.getValue() + "");
        } catch (IOException ex) { /* ignore */ }
    }
}
    

