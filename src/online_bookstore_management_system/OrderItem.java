package online_bookstore_management_system;

import java.util.HashMap;
import java.util.Map;

/**
 * REFACTORED ORDERITEM CLASS
 * --------------------------
 * ✔ SRP: OrderItem ONLY stores order snapshot data.
 * ✔ OCP: Inventory updates moved to separate service.
 * ✔ DIP: OrderItem no longer depends on Book methods directly for stock logic.
 */
public class OrderItem {

    private Book book;
    private int quantity;
    private double priceAtOrder; // snapshot price
    private Map<String, Object> extraProps = new HashMap<>();

    public OrderItem(Book book, int qty) {
        this.book = book;
        this.quantity = qty;
        this.priceAtOrder = book.getPrice();

        // Removed: book.reduceStock(qty);
        // Inventory logic now handled by InventoryService (DIP)
    }

    public double getSubtotal() {
        return priceAtOrder * quantity;
    }

    public Book getBook() {
        return book;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPriceAtOrder() {
        return priceAtOrder;
    }

    public void putExtra(String k, Object v) {
        extraProps.put(k, v);
    }

    public Map<String, Object> getExtras() {
        return Map.copyOf(extraProps);
    }
}

/*
 * ============================================================
 * INVENTORY SERVICE (SRP + DIP)
 * Handles stock updates — previously inside OrderItem (bad SRP)
 * OrderItem depends on interface, not Book implementation.
 * ============================================================
 */

interface InventoryService {
    void reduceStock(Book book, int qty);
}

class DefaultInventoryService implements InventoryService {

    @Override
    public void reduceStock(Book book, int qty) {
        book.reduceStock(qty);
        System.out.println("Stock updated for: " + book.getTitle());
    }
}

/*
 * ============================================================
 * ORDER ITEM ENRICHMENT (OCP)
 * Allows future extensions (e.g., tax calc, bundle items, tags)
 * without modifying OrderItem class.
 * ============================================================
 */

interface OrderItemEnrichment {
    void applyTo(OrderItem item);
}

class TimestampedOrderItemEnrichment implements OrderItemEnrichment {

    @Override
    public void applyTo(OrderItem item) {
        item.putExtra("timestamp", System.currentTimeMillis());
    }
}

class NotesOrderItemEnrichment implements OrderItemEnrichment {
    private final String note;

    public NotesOrderItemEnrichment(String note) {
        this.note = note;
    }

    @Override
    public void applyTo(OrderItem item) {
        item.putExtra("note", note);
    }
}
