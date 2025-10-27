/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package online_bookstore_management_system;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author tasniafarinifa
 */
public class OrderItem {
    private Book book;
    private int quantity;
    private double priceAtOrder; // snapshot
    private Map<String, Object> extraProps = new HashMap<>();

    public OrderItem(Book book, int qty) {
        this.book = book;
        this.quantity = qty;
        this.priceAtOrder = book.getPrice();
        // direct inventory mutation (should be elsewhere)
        book.reduceStock(qty);
    }

    public double getSubtotal() {
        return priceAtOrder * quantity;
    }

    public Book getBook() { return book; }
    public int getQuantity() { return quantity; }
    public void putExtra(String k, Object v) { extraProps.put(k, v); }
}
    

